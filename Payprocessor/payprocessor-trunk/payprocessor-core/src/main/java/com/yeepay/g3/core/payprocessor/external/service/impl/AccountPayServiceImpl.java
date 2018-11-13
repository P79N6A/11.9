package com.yeepay.g3.core.payprocessor.external.service.impl;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.facade.account.pay.params.EnterprisePayReqParam;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.dto.AccountPayResultMessage;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.external.service.AccountPayService;
import com.yeepay.g3.core.payprocessor.service.AccountPayResultProccess;
import com.yeepay.g3.core.payprocessor.service.SendMqService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.account.pay.dto.PayOrderQueryDTO;
import com.yeepay.g3.facade.account.pay.dto.PayResultDTO;
import com.yeepay.g3.facade.account.pay.enums.FeeTypeEnum;
import com.yeepay.g3.facade.account.pay.params.PayRequestParam;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 
 * @author：zhangxh
 * @since：2017年5月16日 上午11:24:45
 * @version:
 */
@Service
public class AccountPayServiceImpl extends AbstractService implements AccountPayService {

    private static final Logger logger = PayLoggerFactory.getLogger(AccountPayServiceImpl.class);

    @Autowired
    private SendMqService sendMqService;
    
    @Autowired
    private AccountPayResultProccess accountPayResultProccess;

    @Autowired
    private CombAbstractService combAbstractService;

    /**
     * 异步支付
     */
    @Override
    public AccountPayResponseDTO pay(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException {
        try {
            PayRequestParam payRequestParam = buildPayRequestParam(requestDTO, payRecord);
            PayResultDTO payResultDTO = accountPayFacade.pay(payRequestParam);
            // 后台处理通知订单处理器、回写redis等操作
            // 防止hang住本线程，保证本线程快速返回
            handleResultBackground(requestDTO, payResultDTO , payRecord);
            return buildAccountPayResponseDTO(requestDTO, payRecord, payResultDTO);
        } catch (PayBizException e) {
            throw e;
        } catch (Throwable th) {
            logger.error("[请求ACCOUNT-PAY下单失败]", th);
            throw new PayBizException(ErrorCode.P9001000);
        }

    }


    /**
     * 异步支付（新）
     * added by zhijun.wang 20180426
     */
    @Override
    public AccountPayResponseDTO enterprisePay(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException {
        try {
            EnterprisePayReqParam reqParam = buildEnterpriseReqParam(requestDTO, payRecord);
            PayResultDTO payResultDTO = accountPayFacade.enterprisePay(reqParam);
            // 后台处理通知订单处理器、回写redis等操作
            // 防止hang住本线程，保证本线程快速返回
            handleResultBackground(requestDTO, payResultDTO , payRecord);
            return buildAccountPayResponseDTO(requestDTO, payRecord, payResultDTO);
        } catch (PayBizException e) {
            throw e;
        } catch (Throwable th) {
            logger.error("[请求ACCOUNT-PAY下单失败]", th);
            throw new PayBizException(ErrorCode.P9001000);
        }

    }

    /**
     * 同步支付
     */
    @Override
    public AccountSyncPayResponseDTO paySync(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException {
        try {
            //发起账户支付请求
            PayResultDTO payResultDTO = accountPayFacade.pay(buildPayRequestParam(requestDTO, payRecord));
            //同步处理支付结果
            if(payRecord.isCombinedPay()) {
                accountPayResultProccess.processSyncResponseComb(payResultDTO, payRecord);
            }else {
                accountPayResultProccess.processSyncResponse(payResultDTO, payRecord);
            }
            return buildAccountSyncPayResponseDTO(requestDTO, payRecord, payResultDTO);
        } catch (PayBizException e) {
            throw e;
        } catch (Throwable th) {
            logger.error("[请求ACCOUNT-PAY下单失败]", th);
            throw new PayBizException(ErrorCode.P9001000);
        }
    }

    /**
     * 通步支付（新）
     * added by zhijun.wang 20180426
     */
    @Override
    public AccountSyncPayResponseDTO enterprisePaySync(AccountPayRequestDTO requestDTO, PayRecord payRecord) throws SocketTimeoutException {
        try {
            //发起账户支付请求
            PayResultDTO payResultDTO = accountPayFacade.enterprisePay(buildEnterpriseReqParam(requestDTO, payRecord));
            //同步处理支付结果
            if(payRecord.isCombinedPay()) {
                accountPayResultProccess.processSyncResponseComb(payResultDTO, payRecord);
            }else {
                accountPayResultProccess.processSyncResponse(payResultDTO, payRecord);
            }
            return buildAccountSyncPayResponseDTO(requestDTO, payRecord, payResultDTO);
        } catch (PayBizException e) {
            throw e;
        } catch (Throwable th) {
            logger.error("[请求ACCOUNT-PAY下单失败]", th);
            throw new PayBizException(ErrorCode.P9001000);
        }
    }

    /**
     * 查询订单
     */
    @Override
    public PayOrderQueryDTO queryPayInfoByOrderNo(String orderNo, Date trxTime) {
        try {
            return accountPayQueryFacade.queryPayInfoByOrderNo(orderNo, trxTime);
        } catch (Throwable t) {
            logger.error("[请求ACCOUNT-PAY查单失败]", t);
        }
        return null;
    }

    private void handleResultBackground(AccountPayRequestDTO requestDTO, PayResultDTO payResultDTO, PayRecord payRecord) {
        try {
            AccountPayResultMessage accountPayResultMessage = buildAccountPayResultMessage(payResultDTO , payRecord);
            sendMqService.sendAccountPayResultMessageMq(accountPayResultMessage);
        } catch (Throwable t) {
            logger.error("handle account result background error.", t);
        }

    }
    
    /**
     * 构建异步响应结果
     */
    private AccountPayResponseDTO buildAccountPayResponseDTO(AccountPayRequestDTO requestDTO, PayRecord payRecord, PayResultDTO payResultDTO) {
        AccountPayResponseDTO responseDTO = new AccountPayResponseDTO();
        responseDTO.setProcessStatus(ProcessStatus.SUCCESS);
        responseDTO.setCustomerNumber(payResultDTO.getCreditCustomerNo());
        responseDTO.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
        responseDTO.setDebitAccountNo(payResultDTO.getDebitAccountNo());
        responseDTO.setDebitCustomerNo(payResultDTO.getDebitCustomerNo());
        responseDTO.setOrderNo(payResultDTO.getOrderNo());
        responseDTO.setRecordNo(payRecord.getRecordNo());
        responseDTO.setOutTradeNo(requestDTO.getOutTradeNo());
        responseDTO.setExtendInfo(payResultDTO.getExtInfo());
        return responseDTO;
    }
    
    /**
     * 构建同步响应结果
     */
    private AccountSyncPayResponseDTO buildAccountSyncPayResponseDTO(AccountPayRequestDTO requestDTO, PayRecord payRecord, PayResultDTO payResultDTO) {
        AccountSyncPayResponseDTO responseDTO = new AccountSyncPayResponseDTO();
        responseDTO.setProcessStatus(ProcessStatus.SUCCESS);
        responseDTO.setDebitAccountNo(payResultDTO.getDebitAccountNo());
        responseDTO.setDebitCustomerNo(payResultDTO.getDebitCustomerNo());
        responseDTO.setRecordNo(payRecord.getRecordNo());
        responseDTO.setExtendInfo(payResultDTO.getExtInfo());
        responseDTO.setFrpCode(payResultDTO.getPayInterfaceCode());
        responseDTO.setBasicProductCode(payRecord.getBasicProductCode());
        responseDTO.setResponseCode(payRecord.getErrorCode());
        responseDTO.setResponseMsg(payRecord.getErrorMsg());
        if (!TrxStatusEnum.DOING.name().equals(payRecord.getStatus())){
            responseDTO.setTrxStatus(TrxStatusEnum.valueOf(payRecord.getStatus())); 
        }
        if(payRecord.isCombinedPay()) {
            CombResponseDTO combResponseDTO = combAbstractService.bulidCombResponse(payRecord.getRecordNo());
            responseDTO.setCombResponseDTO(combResponseDTO);
            responseDTO.setFirstPayAmount(payRecord.getFirstPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return responseDTO;
    }

    private AccountPayResultMessage buildAccountPayResultMessage(PayResultDTO payResultDTO, PayRecord payRecord) {
        AccountPayResultMessage accountPayResultMessage = new AccountPayResultMessage();
        accountPayResultMessage.setStatus(payResultDTO.getStatus());
        accountPayResultMessage.setAmount(payResultDTO.getAmount());
        accountPayResultMessage.setCreateTime(payResultDTO.getCreateTime());
        accountPayResultMessage.setCreditCustomerNo(payResultDTO.getCreditCustomerNo());
        accountPayResultMessage.setDebitAccountNo(payResultDTO.getDebitAccountNo());
        accountPayResultMessage.setDebitCustomerNo(payResultDTO.getDebitCustomerNo());
        accountPayResultMessage.setExceptionCode(payResultDTO.getExceptionCode());
        accountPayResultMessage.setExtInfo(payResultDTO.getExtInfo());
        accountPayResultMessage.setFailureReason(payResultDTO.getFailureReason());
        accountPayResultMessage.setFee(payResultDTO.getFee());
        accountPayResultMessage.setFeeType(payResultDTO.getFeeType());
        accountPayResultMessage.setOperator(payResultDTO.getOperator());
        accountPayResultMessage.setOrderNo(payResultDTO.getOrderNo());
        accountPayResultMessage.setPayInterfaceCode(payResultDTO.getPayInterfaceCode());
        accountPayResultMessage.setTrxTime(payResultDTO.getTrxTime());
        //TODO 等账户支付返回值有了，就用账户支付的返回dto的
        accountPayResultMessage.setBasicProductCode(payRecord.getBasicProductCode());
        // added by zhijun.wang 20180417
        if(!"CLOSE".equals(ConstantUtils.getNewAccountPay())) {
            accountPayResultMessage.setAccountPayOrderNo(payResultDTO.getInnerOrderNo());
        }
        return accountPayResultMessage;
    }


    private PayRequestParam buildPayRequestParam(AccountPayRequestDTO requestDTO, PayRecord payRecord) {
        PayRequestParam payRequestParam = new PayRequestParam();
        // 组合支付，第一支付单金额不等于订单金额
        if(payRecord.isCombinedPay()) {
            payRequestParam.setAmount(payRecord.getFirstPayAmount());
        }else {
            payRequestParam.setAmount(requestDTO.getAmount());
        }
        payRequestParam.setCreditCustomerNo(requestDTO.getCustomerNumber());
        payRequestParam.setDebitAccountNo(requestDTO.getDebitAccountNo());
        payRequestParam.setDebitCustomerNo(requestDTO.getDebitCustomerNo());
        payRequestParam.setExtInfo(requestDTO.getExtInfo());
        if(requestDTO.getUserFee() == null) {
            //账户支付子系统，fee字段不可为null，故置为zero
            payRequestParam.setFee(BigDecimal.ZERO);
        } else {
            payRequestParam.setFee(requestDTO.getUserFee());
        }
        payRequestParam.setOrderNo(payRecord.getRecordNo());
        payRequestParam.setTrxTime(payRecord.getCreateTime());
        payRequestParam.setFeeType(FeeTypeEnum.INNER);
        payRequestParam.setOperator(ConstantUtils.getAccountPayOperator());
        payRequestParam.setPayInterfaceCode(ConstantUtils.getAccountPayInterface());
        payRequestParam.setBasicProductCode(payRecord.getBasicProductCode());
        payRequestParam.setSalesProductCode(payRecord.getRetailProductCode());
        return payRequestParam;
    }


    /**
     * 组装入参
     */
    private EnterprisePayReqParam buildEnterpriseReqParam(AccountPayRequestDTO requestDTO, PayRecord payRecord) {
        EnterprisePayReqParam reqParam = new EnterprisePayReqParam();
        reqParam.setOrderNo(payRecord.getRecordNo());
        reqParam.setOperator(ConstantUtils.getAccountPayOperator());
        reqParam.setDebitCustomerNo(requestDTO.getDebitCustomerNo());
        reqParam.setDebitAccountNo(requestDTO.getDebitAccountNo());
        reqParam.setCreditCustomerNo(requestDTO.getCustomerNumber());
        // 组合支付，第一支付单金额不等于订单金额
        if(payRecord.isCombinedPay()) {
            reqParam.setAmount(payRecord.getFirstPayAmount());
        }else {
            reqParam.setAmount(requestDTO.getAmount());
        }
        reqParam.setTrxTime(payRecord.getCreateTime());
        reqParam.setFeeType(FeeTypeEnum.INNER);
        reqParam.setPayInterfaceCode(ConstantUtils.getAccountPayInterface());
        if(requestDTO.getUserFee() == null) {
            //账户支付子系统，fee字段不可为null，故置为zero
            reqParam.setFee(BigDecimal.ZERO);
        } else {
            reqParam.setFee(requestDTO.getUserFee());
        }
        reqParam.setExtInfo(requestDTO.getExtInfo());
        reqParam.setDebitCustomerLoginName(requestDTO.getDebitCustomerLoginName());
        reqParam.setDebitCustomerPwd(requestDTO.getDebitCustomerPwd());
        reqParam.setBasicProductCode(payRecord.getBasicProductCode());
        reqParam.setSalesProductCode(payRecord.getRetailProductCode());
        return reqParam;
    }

}
