package com.yeepay.g3.core.nccashier.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.PayResultMessage;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;


public class FrontEndPayMessageReceiver implements MessageListener {

	private static final Logger logger = NcCashierLoggerFactory.getLogger(FrontEndPayMessageReceiver.class);

	@Autowired
	private NcPayResultProccess ncPayResultProccess;

	@Override
	public void onMessage(Message msg) {
		String jsonMessage = "";
		PayResultMessage resultMessage = null;
		try {
			jsonMessage = new String(msg.getBody(), "UTF-8");
			logger.info("接受到FE mq消息" + jsonMessage);
			
			resultMessage = JSONUtils.jsonToBean(jsonMessage, PayResultMessage.class);
			if (resultMessage != null) {
				NcCashierLoggerFactory.TAG_LOCAL.set("[接收FE消息|onMessage] - [tradeSysOrderId=" + resultMessage.getRequestId());
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
