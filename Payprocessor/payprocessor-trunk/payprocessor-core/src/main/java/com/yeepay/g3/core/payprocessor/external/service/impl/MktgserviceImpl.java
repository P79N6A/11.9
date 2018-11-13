/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.external.service.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.dao.CombPayRecordDao;
import com.yeepay.g3.core.payprocessor.dao.PayRecordDao;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.external.service.MktgService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.mktg.dto.DepositRequestDTO;
import com.yeepay.g3.facade.mktg.dto.DepositResponseDTO;
import com.yeepay.g3.facade.mktg.dto.PaymentRequestDTO;
import com.yeepay.g3.facade.mktg.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.BasicRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.CombTrxStatusEnum;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类名称: MktgserviceImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/19 上午10:17
 * @version: 1.0.0
 */
@Service
public class MktgserviceImpl implements MktgService {

    public static final Logger logger = PayLoggerFactory.getLogger(MktgserviceImpl.class);

    @Autowired
    private PayRecordDao payRecordDao;

    @Autowired
    private CombPayRecordDao combPayRecordDao;

    private com.yeepay.g3.facade.mktg.facade.PaymentFacade marketingPaymentFacade = RemoteFacadeProxyFactory.getService(com.yeepay.g3.facade.mktg.facade.PaymentFacade.class, ExternalSystem.MKTG);

//    protected com.yeepay.g3.facade.mktg.facade.PaymentFacade marketingPaymentFacade = RemoteServiceFactory.getService("http://10.151.32.27:30104/mktg-hessian/hessian/PaymentFacade",
//            RemotingProtocol.HESSIAN, com.yeepay.g3.facade.mktg.facade.PaymentFacade.class);

    @Override
    public void deposit(BasicRequestDTO requestDTO, PayRecord payRecord, CombPayRecord combPayRecord, PaymentRequest paymentRequest) {
        if(!payRecord.isCombinedPay()) {
            return;
        }
        DepositRequestDTO depositRequestDTO = buildDepostiRequest(requestDTO, payRecord, combPayRecord, paymentRequest);
        DepositResponseDTO depositResponseDTO = null;
        try {
            depositResponseDTO = marketingPaymentFacade.deposit(depositRequestDTO);
        }catch (Throwable t) {
            logger.error("调用营销系统预冻结异常，recordNo=" + payRecord.getRecordNo(), t);
            PayBizException payBizException = new PayBizException(ErrorCode.P9003051);
            bulidCombPayRecordError(payRecord, combPayRecord, payBizException);
            throw payBizException;
        }
        if(depositRequestDTO == null) {
            logger.error("调用营销系统预冻结结果为空，recordNo=" + payRecord.getRecordNo());
            PayBizException payBizException = new PayBizException(ErrorCode.P9003051);
            bulidCombPayRecordError(payRecord, combPayRecord, payBizException);
            throw payBizException;
        }
        if("MK000000".equals(depositResponseDTO.getCode())) {
            combPayRecord.setStatus(CombTrxStatusEnum.DEPOSIT.name());
            combPayRecord.setPayOrderNo(depositResponseDTO.getMarketingOrderNo());
            // 精度设置成2位，营销系统也是2位，精确到分
            combPayRecord.setAmount(depositResponseDTO.getDiscountAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            combPayRecord.setUpdateTime(depositResponseDTO.getSuccessDate());
            combPayRecordDao.updateToDeposit(combPayRecord);
            // 精度精确到分后做运算
            payRecord.setFirstPayAmount(payRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).subtract(combPayRecord.getAmount()));
            payRecordDao.updateFirstAmount(payRecord);
        }else {
            PayBizException payBizException = ErrorCodeUtil.mapErrorCode(ErrorCodeSource.MKTG.getSysCode(), depositResponseDTO.getCode(),
                    depositResponseDTO.getMessage(), ErrorCode.P9003051);
            bulidCombPayRecordError(payRecord, combPayRecord, payBizException);
            logger.error("调用营销系统预冻结失败， recordNo=" + payRecord.getRecordNo() + "errorCode=" + payRecord.getErrorCode()
                    + "errorMsg" + payRecord.getErrorMsg());
            throw payBizException;
        }
    }

    /**
     * 构建预冻结参数
     * @param basicRequestDTO
     * @param payRecord
     * @param combPayRecord
     * @return
     */
    private DepositRequestDTO buildDepostiRequest(BasicRequestDTO basicRequestDTO, PayRecord payRecord,
                                                  CombPayRecord combPayRecord, PaymentRequest paymentRequest) {
        DepositRequestDTO depositRequest = new DepositRequestDTO();
        depositRequest.setPaymentBizOrderNo(payRecord.getRecordNo());
        depositRequest.setMerchantOrderNo(paymentRequest.getOutTradeNo());
        depositRequest.setMarketingNo(combPayRecord.getMarketingNo());
        depositRequest.setMerchantNo(paymentRequest.getCustomerNo());
        depositRequest.setMerchantName(paymentRequest.getCustomerName());
        depositRequest.setTradeSysNo(paymentRequest.getOrderSystem());
        depositRequest.setBizSysNo(ConstantUtils.BIZ_SYS_NO);
        depositRequest.setAccountingProductCode(payRecord.getBasicProductCode());
        depositRequest.setSalesProductCode(payRecord.getRetailProductCode());
        // 备注：此处需注意，营销系统入参的金额是2位小数，如果不是，会报金额校验错误
        depositRequest.setOrderAmount(payRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        depositRequest.setPaymentProduct(payRecord.getPayProduct());
        if(basicRequestDTO instanceof NcPayOrderRequestDTO) {
            NcPayOrderRequestDTO ncPayOrderRequestDTO = (NcPayOrderRequestDTO) basicRequestDTO;
            if(ncPayOrderRequestDTO.getBankCardInfoDTO() != null) {
//                depositRequest.setPaymentType(ncPayOrderRequestDTO.getBankCardInfoDTO().getCardType());
                depositRequest.setBankCode(ncPayOrderRequestDTO.getBankCardInfoDTO().getBankCode());
            }
        }
        if(basicRequestDTO instanceof NetPayRequestDTO) {
            NetPayRequestDTO netPayRequestDTO = (NetPayRequestDTO) basicRequestDTO;
//            depositRequest.setPaymentType(netPayRequestDTO.getBankAccountType().name());
            depositRequest.setBankCode(netPayRequestDTO.getBankId());
        }
        if(basicRequestDTO.getCombRequestDTO() != null) {
            depositRequest.setPaymentType(basicRequestDTO.getCombRequestDTO().getPaymentType());
        }
        depositRequest.setRequestDate(new Date());
        return depositRequest;
    }


    @Override
    public void payment(PayRecord payRecord, CombPayRecord combPayRecord, PaymentRequest paymentRequest) {
        PaymentRequestDTO paymentRequestDTO = buildPaymentRequest(payRecord, combPayRecord, paymentRequest);
        PaymentResponseDTO paymentResponseDTO = null;
        try {
            paymentResponseDTO = marketingPaymentFacade.payment(paymentRequestDTO);
        }catch (Throwable t) {
            logger.error("调用营销系统支付异常，recordNo=" + payRecord.getRecordNo(), t);
            throw new PayBizException(ErrorCode.P9003052);
        }
        if(paymentRequest == null) {
            logger.error("调用营销系统支付结果为空，recordNo=" + payRecord.getRecordNo());
            throw new PayBizException(ErrorCode.P9003052);
        }
        if("MK000000".equals(paymentResponseDTO.getCode())) {
            combPayRecord.setStatus(CombTrxStatusEnum.SUCCESS.name());
            combPayRecord.setBankOrderNo(paymentResponseDTO.getMarketingSubSysNo());
            combPayRecord.setFrpCode(paymentResponseDTO.getChannelSign());
            combPayRecord.setPayTime(paymentResponseDTO.getSuccessDate());
        } else {
            PayBizException payBizException = ErrorCodeUtil.mapErrorCode(ErrorCodeSource.MKTG.getSysCode(), paymentResponseDTO.getCode(),
                    paymentResponseDTO.getMessage(), ErrorCode.P9003052);
            combPayRecord.setStatus(CombTrxStatusEnum.FAILURE.name());
            combPayRecord.setErrorCode(payBizException.getDefineCode());
            combPayRecord.setErrorMsg(payBizException.getMessage());
        }
    }


    /**
     * 构建预冻结参数
     * @param payRecord
     * @param combPayRecord
     * @return
     */
    private PaymentRequestDTO buildPaymentRequest(PayRecord payRecord, CombPayRecord combPayRecord, PaymentRequest paymentRequest) {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setMarketingOrderNo(combPayRecord.getPayOrderNo());
        paymentRequestDTO.setPaymentBizOrderNo(payRecord.getRecordNo());
        paymentRequestDTO.setMerchantOrderNo(paymentRequest.getOutTradeNo());
        paymentRequestDTO.setMarketingNo(combPayRecord.getMarketingNo());
        paymentRequestDTO.setMerchantNo(paymentRequest.getCustomerNo());
        paymentRequestDTO.setTradeSysNo(paymentRequest.getOrderSystem());
        paymentRequestDTO.setBizSysNo(ConstantUtils.BIZ_SYS_NO);
        // 备注：此处需注意，营销系统入参的金额是2位小数，如果不是，会报金额校验错误
        paymentRequestDTO.setPaymentAmount(payRecord.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        paymentRequestDTO.setPaymentDate(new Date());
        return paymentRequestDTO;
    }

    /**
     * 更新子单和第二支付单失败和错误信息
     * @param payRecord
     * @param combPayRecord
     * @param paybizException
     */
    private void bulidCombPayRecordError(PayRecord payRecord, CombPayRecord combPayRecord, PayBizException paybizException) {
        combPayRecord.setStatus(CombTrxStatusEnum.FAILURE.name());
        combPayRecord.setErrorCode(paybizException.getDefineCode());
        combPayRecord.setErrorMsg(paybizException.getMessage());
        combPayRecordDao.updateToFail(combPayRecord);
        payRecordDao.updateFailWithComb(payRecord);
    }

}