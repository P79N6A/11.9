package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.service.PayOrderService;
import com.yeepay.g3.core.frontend.util.MailSendHelper;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.RefundStatusEnum;
import com.yeepay.g3.facade.refund.RefundResultNotifyFacade;
import com.yeepay.g3.facade.refund.dto.RefundResultNotifyDTO;
import com.yeepay.g3.facade.refund.enums.RefundResultNotifyStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 退款中心完成回调接口
 * @author chronos.
 * @createDate 16/8/5.
 */
@Service("refundResultNotifyFacade")
public class RefundResultNotifyFacadeImpl implements RefundResultNotifyFacade {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(RefundResultNotifyFacadeImpl.class);

    @Autowired
    private PayOrderService payOrderService;

    @Override
    public void refundResultNotify(RefundResultNotifyDTO refundResultNotifyDTO) {
        logger.info("refund center notify:"+refundResultNotifyDTO.toString());
        PayOrder order = payOrderService.queryByOrderNo(refundResultNotifyDTO.getRequestNo());
        if (order==null){
            logger.error("原订单不存在!orderNo:"+refundResultNotifyDTO.getRequestNo());
            return;
        }

        if (RefundResultNotifyStatusEnum.REFUND_SUCCESS.equals(refundResultNotifyDTO.getStatus())){
            //退款成功,更新退款状态
            order.setRefundStatus(RefundStatusEnum.SUCCESS.name());
            payOrderService.singleUpdate(order);
        } else {
            //退款失败,邮件通知发起方
            Map<String,Object> errorMail = new HashMap<String, Object>();
            errorMail.put("orderNo",refundResultNotifyDTO.getOrderNo());
            errorMail.put("refundWay",refundResultNotifyDTO.getRefundWay());
            errorMail.put("failReason",refundResultNotifyDTO.getFailReason());
            errorMail.put("refundStatus",refundResultNotifyDTO.getStatus().name());
            errorMail.put("initiator",refundResultNotifyDTO.getInitiator());
            errorMail.put("refundAmount",refundResultNotifyDTO.getRefundAmount().doubleValue());
            errorMail.put("orderAmount",order.getTotalAmount());
            MailSendHelper.sendEmail(errorMail,MailSendHelper.REFUND_ERROR_RULE,MailSendHelper.recipients);
        }
    }
}
