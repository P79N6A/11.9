/**
 * 
 */
package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.frontend.service.SendMqService;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.PayResultMessage;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * @author TML
 */
@Service
public class SendMqServiceImpl extends AbstractService implements SendMqService{

	private static final Logger logger = FeLoggerFactory.getLogger(SendMqServiceImpl.class);
	
	@Override
	public void sendPayResultMessageMq(PayResultMessage msg, String mqId) {
		try {
			if(mqId.equalsIgnoreCase("PAYPROCESSOR")) {
				String routingKey = PayResultMessage.FRONTEND_NOTIFY_QUEUE + "_" + mqId;
				delayAmqpTemplate.convertAndSend(routingKey, msg);
				logger.info("业务系统发送MQ消息(支持延迟)OK, routingKey:"+ routingKey + ", payResultMessage:" + msg.toString());
			}else {
				String routingKey = PayResultMessage.FRONTEND_NOTIFY_QUEUE + "_" + mqId;
				amqpTemplate.convertAndSend(routingKey, msg);
				logger.info("业务系统发送MQ消息OK, routingKey:"+ routingKey + ", payResultMessage:" + msg.toString());
			}
		} catch (Throwable th){
			logger.error("业务系统发送MQ消息失败, orderNo:" + msg.getOrderNo(), th);
			throw new FrontendBizException(ErrorCode.F0003004,"业务系统发送MQ消息失败, orderNo:" + msg.getOrderNo());
		}
	}

}
