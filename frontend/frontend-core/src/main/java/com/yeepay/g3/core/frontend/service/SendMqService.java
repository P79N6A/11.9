package com.yeepay.g3.core.frontend.service;

import com.yeepay.g3.facade.frontend.dto.PayResultMessage;

/**
 * 发送mq消息接口
 * @author TML
 *
 */
public interface SendMqService {
	
	/**
	 * 发送
	 * @param msg
	 * @param mqId
	 */
	void sendPayResultMessageMq(PayResultMessage msg, String mqId);

}
