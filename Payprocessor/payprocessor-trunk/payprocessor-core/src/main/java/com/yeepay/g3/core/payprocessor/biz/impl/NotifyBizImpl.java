package com.yeepay.g3.core.payprocessor.biz.impl;

import java.util.Date;
import java.util.List;

import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.biz.NotifyBiz;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.service.NotifyService;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;
import com.yeepay.g3.utils.common.DateUtils;

/**
 * @author chronos.
 * @createDate 2016/11/16.
 */
@Service
public class NotifyBizImpl extends BaseBizImpl implements NotifyBiz {

    private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(NotifyBizImpl.class);

    @Autowired
    private NotifyService notifyService;

    @Override
    public void reNotify() {
        PayLoggerFactory.TAG_LOCAL.set("[定时补发通知]");
        int startHours = ConstantUtils.getReNotifyOrderStartTimeOffset();
        int endHours = ConstantUtils.getReNotifyOrderEndTimeOffset();
        Date d = new Date();
        Date start = DateUtils.addHour(d, startHours);
        Date end = DateUtils.addHour(d, endHours);

        List<PaymentRequest> unNotifyList = null;
        try {
            unNotifyList = paymentRequestService.queryUnNotifyPayment(start, end);
        } catch (Throwable th) {
            logger.error("[查询异常]", th);
        }
        if (unNotifyList == null || unNotifyList.size() < 1) {
            logger.info("[暂无订单需要查询补单]");
            PayLoggerFactory.TAG_LOCAL.remove();
            return;
        }
        logger.info("[准备通知] - [数量" + unNotifyList.size() + "]");
        for (PaymentRequest payment : unNotifyList) {
            try {
                PayRecord record = payRecordService.queryRecordById(payment.getRecordNo());
                if(PayOrderType.PREAUTH_RE.name().equals(record.getPayOrderType()) ||
                        PayOrderType.PREAUTH_CL.name().equals(record.getPayOrderType()) ||
                        PayOrderType.PREAUTH_CM.name().equals(record.getPayOrderType()) ||
                        PayOrderType.PREAUTH_CC.name().equals(record.getPayOrderType())) {
                    notifyService.notifyForPreAuth(record);
                }else {
                    notifyService.notify(record);
                }
            } catch (Throwable th) {
                logger.error("[通知处理失败] - [orderNo = " + payment.getOrderNo() + "]", th);
            }
        }
        logger.info("[通知完成]");
        PayLoggerFactory.TAG_LOCAL.remove();
    }

    @Override
    public OperationResponseDTO batchReNotify(OperationRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[批量补发通知] - [size = " + requestDTO.getRecordList().size() + "]");
        OperationResponseDTO responseDTO = new OperationResponseDTO();
        for (String recordNo : requestDTO.getRecordList()){
            try {
                PayRecord record = payRecordService.queryRecordById(recordNo);
                if(PayOrderType.PREAUTH_RE.name().equals(record.getPayOrderType()) ||
                        PayOrderType.PREAUTH_CL.name().equals(record.getPayOrderType()) ||
                        PayOrderType.PREAUTH_CM.name().equals(record.getPayOrderType()) ||
                        PayOrderType.PREAUTH_CC.name().equals(record.getPayOrderType())) {
                    notifyService.notifyForPreAuth(record);
                }else {
                    notifyService.notify(record);
                }
                responseDTO.setSuccess(responseDTO.getSuccess() + 1);
            } catch (PayBizException e){
                logger.warn("[通知处理失败] [recordNo="+ recordNo +"]- [code = " +e.getDefineCode() + "] - [message = " + e.getMessage() + "]");
                responseDTO.getErrorList().add("[" + recordNo + "] - ["+ e.getDefineCode() +"]" + e.getMessage());
            } catch (Throwable th){
                logger.error("[通知处理失败] - [recordNo = " + recordNo + "]", th);
                responseDTO.getErrorList().add("[" + recordNo +"]" + th.getMessage());
            }
        }
        PayLoggerFactory.TAG_LOCAL.remove();
        return responseDTO;
    }
}
