package com.yeepay.g3.core.nccashier.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.utils.common.log.Logger;


public class PayMessageReceiver implements MessageListener {

	private static final Logger logger = NcCashierLoggerFactory.getLogger(PayMessageReceiver.class);

	@Autowired
	private NcPayResultProccess ncPayResultProccess;

	@Override
	public void onMessage(Message msg) {
		String jsonMessage = "";
		PaymentResultMessage resultMessage = null;
		try {
			jsonMessage = new String(msg.getBody(), "UTF-8");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			resultMessage = objectMapper.readValue(jsonMessage, PaymentResultMessage.class);
			if (resultMessage != null) {
				NcCashierLoggerFactory.TAG_LOCAL.set(
						"[接收ncpay消息|onMessage] - [orderOrderId=" + resultMessage.getBizOrderNum()
								+ " orderSysNo=" + resultMessage.getBizType() + "]");
				logger.info("接受到ncpay mq消息" + resultMessage);
				ncPayResultProccess.processForCallBack(resultMessage);
			} else {
				logger.error("wap收银台接收到的消息为空");
			}
		} catch (Throwable e) {
			logger.error("[MQ消息接收] 系统异常  jsonMessage:" + jsonMessage, e);
			return;
		} finally {
			NcCashierLoggerFactory.TAG_LOCAL.set(null);
		}
	}
}
