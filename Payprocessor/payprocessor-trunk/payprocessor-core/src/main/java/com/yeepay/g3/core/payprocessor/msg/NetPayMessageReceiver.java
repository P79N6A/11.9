package com.yeepay.g3.core.payprocessor.msg;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.NetPayResultProcess;
import com.yeepay.g3.core.payprocessor.service.PayRecordService;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.NetPayResultMessage;
import com.yeepay.g3.utils.common.json.JSONUtils;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chronos.
 * @createDate 2016/12/16.
 */
public class NetPayMessageReceiver implements MessageListener {

    private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(PayLogger.class);

    @Autowired
    private NetPayResultProcess netPayResultProcess;

    @Autowired
    private PayRecordService payRecordService;

    @Override
    public void onMessage(Message msg) {
        String jsonMessage = "";
        NetPayResultMessage resultMessage = null;
        try {
            jsonMessage = new String(msg.getBody(), "UTF-8");
            logger.info("接受到FE mq消息" + jsonMessage);
            resultMessage = JSONUtils.jsonToBean(jsonMessage, NetPayResultMessage.class);
            if (resultMessage != null) {
                PayLoggerFactory.TAG_LOCAL.set("[接收FE-NET消息|onMessage] - [tradeSysOrderId=" + resultMessage.getRequestId());
                PayRecord payRecord = payRecordService.queryRecordById(resultMessage.getRequestId());
                if (payRecord == null) {
                    logger.error("payRecord not exist");
                    throw new PayBizException(ErrorCode.P9002006);
                }
                // 组合支付
                if(payRecord.isCombinedPay()) {
                    netPayResultProcess.processForFePayMsgComb(resultMessage, payRecord);
                }else {
                    netPayResultProcess.processForFePayMsg(resultMessage, payRecord);
                }
            } else {
                logger.error("收到的消息为空");
            }
        } catch(PayBizException pe){
            logger.error("PayBizException: " + pe.getDefineCode() + ", " + pe.getMessage());
            return;
        } catch (Throwable e) {
            logger.error("[MQ消息接收] 系统异常", e);
            throw new AmqpRejectAndDontRequeueException("[MQ消息接收] 系统异常" , e);
        } finally {
            PayLoggerFactory.TAG_LOCAL.set(null);
        }
    }
}
