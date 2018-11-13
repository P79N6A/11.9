package com.yeepay.g3.core.payprocessor.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.ncpay.dto.GuaranteeCflResultMessage;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 担保分期 结果处理器
 * 只处理成功和失败，其他的结果不处理。
 * 返回的结果包括 INIT、SUCCESS、FAILURE、DOING
 * 
 * @author：tao.liu
 * @since：2018年2月5日 下午5:43:21
 * @version:
 */
@Service
public class GuaranteeCflResultProccess extends AbstractService {

    private static final Logger logger = PayLoggerFactory.getLogger(GuaranteeCflResultProccess.class);
    
    @Autowired
    private ResultProcessService resultProcessService;

    @Autowired
    private NotifyService notifyService;

    /**
     * 处理mq消息
     * 
     * @param message
     */
    public void processForMQ(GuaranteeCflResultMessage message) {

        if (!"SUCCESS".equals(message.getState()) && !"FAILURE".equals(message.getState())) {
            logger.info("该state:" + message.getState() + "不进行处理！！！");
            return;
        }

        PayRecord payRecord = payRecordDao.selectByPrimaryKey(message.getBizOrder());// 查询支付子表
        String recordStatus = payRecord.getStatus();
        
        checkPayRecord(payRecord);// 校验订单状态

        if ("FAILURE".equals(message.getState())) {
            
            if (TrxStatusEnum.REVERSE.name().equals(recordStatus)){
                logger.error("该订单已冲正，不允许更新为失败");
                return;
            }
            
            updatePayRecordToFail(payRecord, message);
            setPayResultToRedis(message.getBizOrder());
        } else if ("SUCCESS".equals(message.getState())) {
           
            processPayRecord(payRecord, message);

            if (TrxStatusEnum.REVERSE.name().equals(recordStatus)){
                
                if (reverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
                    payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
                    createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
                }
            } else if (TrxStatusEnum.DOING.name().equals(recordStatus)) {
                
                resultProcessService.updatePaymentToSuccess(payRecord);
                notifyService.notify(payRecord);
                setPayResultToRedis(message.getBizOrder());
            }
        }
    }
    
    /**
     * 修改支付子表状态，并将ncpay的错误码映射为支付处理器的错误码
     * 
     * @param payRecord
     * @param message
     */
    private void updatePayRecordToFail(PayRecord payRecord, GuaranteeCflResultMessage message) {
        
        ErrorMeta errorMeta = ErrorCodeUtil.translateCode(ErrorCodeSource.NCPAY.getSysCode(), message.getErrorCode(), message.getErrorMsg(),
                ErrorCode.P9001000);

        if (payRecordDao.updatePaymentToFail(payRecord.getRecordNo(), errorMeta.getErrorCode(), errorMeta.getErrorMsg()) == 0){
            logger.error("该订单更新失败失败，订单状态已改变");
            throw new PayBizException(ErrorCode.P9002005);
        }
    }
    
    private void processPayRecord(PayRecord payRecord, GuaranteeCflResultMessage message) {
        payRecord.setBankId(message.getBankCode());
        payRecord.setBankOrderNo(message.getBankOrderNo());
        payRecord.setBankSeq(message.getBankSeq());
        payRecord.setBankTrxId(message.getTradeSerialNo());
        payRecord.setCost(message.getCost() == null ? null : message.getCost().getValue());
        payRecord.setPayTime(new Date(message.getConfirmTime()));
        payRecord.setCardId(message.getCardId());
        payRecord.setCardType(message.getCardType());
        payRecord.setFrpCode(message.getFrpCode());
        payRecord.setUpdateTime(new Date());
        payRecord.setCflRate(message.getPayerInterestRate());
        payRecord.setBasicProductCode(StringUtils.isBlank(payRecord.getBasicProductCode()) ? message.getBasicProductCode() : payRecord.getBasicProductCode());
    }

}
