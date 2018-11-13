package com.yeepay.g3.core.payprocessor.external.service.impl;

import com.alibaba.dubbo.common.json.JSON;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.external.service.CsProcessService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.csprocess.dto.ErrorRefundDTO;
import com.yeepay.g3.facade.csprocess.dto.ErrorRefundResponseDTO;
import com.yeepay.g3.facade.csprocess.enumtype.CallbackProtocalEnum;
import com.yeepay.g3.facade.csprocess.enumtype.ErrorRefundStatusEnum;
import com.yeepay.g3.facade.csprocess.enumtype.ErrorRefundTypeEnum;
import com.yeepay.g3.facade.csprocess.enumtype.FeeTypeEnum;
import com.yeepay.g3.facade.csprocess.exception.CsHessionException;
import com.yeepay.g3.facade.csprocess.facade.RefundFacade;
import com.yeepay.g3.utils.common.json.JSONUtils;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author chronos.
 * @createDate 2016/11/14.
 */
@Service("csProcessService")
public class CsProcessServiceImpl extends AbstractService implements CsProcessService {

    private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(CsProcessServiceImpl.class);

    private RefundFacade refundFacade = RemoteFacadeProxyFactory.getService(RefundFacade.class, ExternalSystem.CS);

    @Override
    public boolean processCsRefund(PaymentRequest payment, PayRecord payRecord) {
        logger.info("[recordNo = " + payRecord.getRecordNo() + "]发送到清算中心");
        boolean result = false;
        try {
            ErrorRefundDTO refundDTO = buildRefundDto(payment, payRecord);
            logger.info("[refundDTO = " + JSONUtils.toJsonString(refundDTO) + "]");
            ErrorRefundResponseDTO responseDTO = refundFacade.errorRefund(refundDTO);
            if (responseDTO != null){
                result = true;
            }
        } catch (CsHessionException th){
            logger.error("[recordNo = " + payRecord.getRecordNo() + "]发送到清算中心异常", th);
            if ("100000".equals(th.getDefineCode())){//系统异常,需要查询退款状态
                result = isCsReceive(payRecord.getRecordNo());
            }
            if ("100509".equals(th.getDefineCode())) {//退款请求号重复返回成功
                result = true;
            }
        } catch (Throwable th){
            logger.error("[recordNo = " + payRecord.getRecordNo() + "]发送到清算中心异常", th);
            result = isCsReceive(payRecord.getRecordNo());
        }
        return result;
    }

    @Override
    public boolean queryCsRecord(String recordNo) {
        boolean result = false;
        try {
            ErrorRefundResponseDTO responseDTO = query(recordNo);
            if (ErrorRefundStatusEnum.REFUND_SUCCESS.equals(responseDTO.getStatus())){
                result = true;
            }
        } catch (Throwable th){
            logger.error("[查询失败] - [recordNo = " + recordNo + "]" , th);
        }
        return result;
    }

    private ErrorRefundDTO buildRefundDto(PaymentRequest payment, PayRecord record){
        ErrorRefundDTO refundDTO = new ErrorRefundDTO();
        refundDTO.setBizSystem(ConstantUtils.SYS_NO);
        refundDTO.setMerchantNo(payment.getCustomerNo());
        refundDTO.setBizRefundRequestNo(record.getRecordNo());
        refundDTO.setRelatedTransId(payment.getOrderNo());
        refundDTO.setBankSuccessDate(record.getPayTime());
        refundDTO.setAmount(record.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        refundDTO.setBankOrderNo(record.getBankOrderNo());
        refundDTO.setBankInterface(record.getFrpCode());
		if (record.getUserFee() != null && record.getUserFee().compareTo(new BigDecimal("0")) > 0) {
			refundDTO.setSrcFee(record.getUserFee().setScale(2, BigDecimal.ROUND_HALF_UP));
			refundDTO.setSrcFeeType(FeeTypeEnum.INNER);
		}
        refundDTO.setErrorRefundType(ErrorRefundTypeEnum.ERROR_REFUND);
        refundDTO.setPayFlowNo(record.getBankTrxId());//银行子系统流水号
        refundDTO.setMerchantRequestNo(payment.getOutTradeNo());
        refundDTO.setCallbackUrl(ConstantUtils.CS_CALL_BACK_URL);
        refundDTO.setCallProtocol(CallbackProtocalEnum.HESSIAN);
        return refundDTO;
    }

    /**
     * 查询清算中心处理状态
     * @param recordNo
     * @return
     */
    private ErrorRefundResponseDTO query(String recordNo){
        ErrorRefundDTO refundDTO = new ErrorRefundDTO();
        refundDTO.setBizRefundRequestNo(recordNo);
        ErrorRefundResponseDTO responseDTO = refundFacade.queryErrorRefund(refundDTO);
        if (responseDTO == null)
            throw new PayBizException(ErrorCode.P9003007);
        return responseDTO;
    }

    /**
     * 判断是否接收退款
     * 查询清算中心处理状态
     * @param recordNo
     */
    private boolean isCsReceive(String recordNo){
        boolean received = false;
        try {
            query(recordNo);
            received = true;
        } catch (Throwable th){
            logger.error("[查询失败] - [recordNo = " + recordNo + "]" , th);
        } finally {
            return received;
        }
    }

}
