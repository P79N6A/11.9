package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.dto.AccountPayResultMessage;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;

/**
 * 发送mq消息接口
 * @author TML
 * @author zhangxh
 * 
 *
 */
public interface SendMqService {
	
	
	/**
	 * 支付处理器发送账户支付的mq消息，并处理该mq的消息
	 * @param accountPayResultMessage
	 */
	void sendAccountPayResultMessageMq(AccountPayResultMessage accountPayResultMessage);

	/**
	 * 支付处理器发送支付结果的mq消息给收银台
	 * @param responseDTO
	 */
	void sendPayRecordResultMessageMq(PayRecordResponseDTO responseDTO);

}
