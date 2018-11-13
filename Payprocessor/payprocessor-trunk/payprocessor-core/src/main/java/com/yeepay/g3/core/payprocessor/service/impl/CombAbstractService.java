/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.external.service.MktgService;
import com.yeepay.g3.core.payprocessor.service.NotifyService;
import com.yeepay.g3.core.payprocessor.service.ResultProcessService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.CombTrxStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 类名称: CombAbstractService <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/21 下午1:47
 * @version: 1.0.0
 */
@Service
public class CombAbstractService extends AbstractService {

    private static final Logger logger = PayLoggerFactory.getLogger(CombAbstractService.class);

    @Autowired
    private MktgService mktgService;

    @Autowired
    private ResultProcessService resultProcessService;

    @Autowired
    private NotifyService notifyService;

    public void processCombResult(PayRecord payRecord, CombPayRecord combPayRecord) {
        if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
            // 更新信息，然后发起退款
            // 判断是否发起过退款
            if (reverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
                payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
                createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
            }
        } else if (TrxStatusEnum.DOING.name().equals(payRecord.getStatus())) {
            int payRecord_result = payRecordDao.updateRecordToSuccess(payRecord);
            if (payRecord_result == 0) {
                throw new PayBizException(ErrorCode.P9002002);
            }
            payRecord.setStatus(TrxStatusEnum.SUCCESS.name());
            // 调营销系统支付
            PaymentRequest paymentRequest = paymentRequestDao.selectByPrimaryKey(payRecord.getRequestId());
            mktgService.payment(payRecord, combPayRecord, paymentRequest);
            // 扣减成功，更新第二支付子单和主单
            if(CombTrxStatusEnum.SUCCESS.name().equals(combPayRecord.getStatus())) {
                resultProcessService.updateCombPayRecordAndPaymentSuccess(payRecord, combPayRecord);
                // 扣减失败，更新第二支付子单失败、冲正第一支付子单
            }else {
                resultProcessService.updateCombPayRecordFail(payRecord, combPayRecord);
            }
            notifyService.notify(payRecord);
            setPayResultToRedis(payRecord.getRecordNo());
        } else if(TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
            // 第一支付单已成功，第二支付单如果还是deposit，则调营销系统处理
            if(CombTrxStatusEnum.DEPOSIT.name().equals(combPayRecord.getStatus())) {
                // 调营销系统支付
                PaymentRequest paymentRequest = paymentRequestDao.selectByPrimaryKey(payRecord.getRequestId());
                mktgService.payment(payRecord, combPayRecord, paymentRequest);
                // 扣减成功，更新第二支付子单和主单
                if(CombTrxStatusEnum.SUCCESS.name().equals(combPayRecord.getStatus())) {
                    resultProcessService.updateCombPayRecordAndPaymentSuccess(payRecord, combPayRecord);
                    // 扣减失败，更新第二支付子单失败、冲正第一支付子单
                }else {
                    resultProcessService.updateCombPayRecordFail(payRecord, combPayRecord);
                }
                notifyService.notify(payRecord);
                setPayResultToRedis(payRecord.getRecordNo());
            }
        }
    }


    /**
     * 组合支付转返回实体
     * @param recordNo
     * @return
     */
    public CombResponseDTO bulidCombResponse(String recordNo) {
        CombResponseDTO combResponseDTO = new CombResponseDTO();
        CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(recordNo);
        if(combPayRecord != null) {
            combResponseDTO.setPayOrderType(combPayRecord.getPayOrderType());
            combResponseDTO.setPayOrderNo(combPayRecord.getPayOrderNo());
            combResponseDTO.setBankOrderNo(combPayRecord.getBankOrderNo());
            combResponseDTO.setAmount(combPayRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            combResponseDTO.setPaySuccDate(combPayRecord.getPayTime());
            combResponseDTO.setChannelId(combPayRecord.getFrpCode());
            combResponseDTO.setPayProduct(combPayRecord.getPayProduct());
            combResponseDTO.setStatus(combPayRecord.getStatus());
        }
        return combResponseDTO;
    }


    /**
     * 更新第二支付子单为失败
     */
    public void updateFailByRecordNo(PayRecord payRecord) {
        if(!payRecord.isCombinedPay()) {
            return;
        }
        CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
        if(combPayRecord == null) {
            return;
        }
        try {
            combPayRecordDao.updateToFail(combPayRecord);
        }catch (Throwable t) {
            logger.error("更新第二支付子单失败状态失败，recordNo=" + payRecord.getRecordNo(), t);
        }

    }
}