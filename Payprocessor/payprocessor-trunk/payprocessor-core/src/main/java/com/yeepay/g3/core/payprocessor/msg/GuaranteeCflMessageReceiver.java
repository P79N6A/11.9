package com.yeepay.g3.core.payprocessor.msg;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.service.GuaranteeCflResultProccess;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.ncpay.dto.GuaranteeCflResultMessage;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 担保分期 结果消息接收处理
 * 
 * @author：tao.liu
 * @since：2018年2月5日 下午5:40:00
 * @version:
 */
public class GuaranteeCflMessageReceiver implements MessageListener {

    private static final Logger logger = PayLoggerFactory.getLogger(GuaranteeCflMessageReceiver.class);

    @Autowired
    private GuaranteeCflResultProccess guaranteeCflResultProccess;

    @Override
    public void onMessage(Message msg) {

        try {
            GuaranteeCflResultMessage resultMessage = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(new String(msg.getBody(), "UTF-8"), GuaranteeCflResultMessage.class);

            if (resultMessage == null) {
                logger.error("收到消息为空");
                return;
            }

            PayLoggerFactory.TAG_LOCAL.set("[接收-担保分期-消息] - [orderOrderId=" + resultMessage.getBizOrder() + " orderSysNo=" + resultMessage.getBiz() + "]");
            logger.info("接收到担保分期mq消息" + resultMessage);

            guaranteeCflResultProccess.processForMQ(resultMessage);

        } catch (PayBizException pe) {
            logger.error("PayBizException: " + pe.getDefineCode() + ", " + pe.getMessage());
        } catch (Throwable e) {
            logger.error("[MQ消息接收] 系统异常:", e);
            return;
        } finally {
            PayLoggerFactory.TAG_LOCAL.set(null);
        }
    }

}
