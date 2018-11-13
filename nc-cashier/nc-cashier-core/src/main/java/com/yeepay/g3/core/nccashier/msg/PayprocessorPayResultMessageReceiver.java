package com.yeepay.g3.core.nccashier.msg;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.vo.responseDto.PpPayResultResponseDTO;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;


public class PayprocessorPayResultMessageReceiver implements MessageListener {

	private static final Logger logger = NcCashierLoggerFactory.getLogger(PayprocessorPayResultMessageReceiver.class);

	@Autowired
	private PayprocessorResultProccess payprocessorResultProccess;

	@Override
	public void onMessage(Message msg) {
		String jsonMessage = "";
		PpPayResultResponseDTO resultMessage = null;
		try {
			jsonMessage = new String(msg.getBody(), "UTF-8");
			logger.info("接受到PP mq消息" + jsonMessage);

			resultMessage = JSONUtils.jsonToBean(jsonMessage, PpPayResultResponseDTO.class);
			if (resultMessage != null) {
				NcCashierLoggerFactory.TAG_LOCAL.set("[接收PP消息|onMessage] - [tradeSysOrderId=" + resultMessage.getRecordNo());
				payprocessorResultProccess.processForCallBack(resultMessage);
			} else {
				logger.error("收银台接收到的PP的MQ消息为空");
			}
		} catch (Throwable e) {
			logger.error("[MQ消息接收] 系统异常  jsonMessage:" + jsonMessage, e);
			return;
		} finally {
			NcCashierLoggerFactory.TAG_LOCAL.set(null);
		}
	}
}
