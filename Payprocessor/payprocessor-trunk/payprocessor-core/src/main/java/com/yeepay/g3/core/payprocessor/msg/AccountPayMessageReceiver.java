package com.yeepay.g3.core.payprocessor.msg;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.PayRecordService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.dto.AccountPayResultMessage;
import com.yeepay.g3.core.payprocessor.service.AccountPayResultProccess;
import com.yeepay.g3.core.payprocessor.service.FeResultProccess;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.PayResultMessage;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 
 * @author peile.fan
 *
 */
public class AccountPayMessageReceiver implements MessageListener {

	private static final Logger logger = PayLoggerFactory.getLogger(AccountPayMessageReceiver.class);

	@Autowired
	private AccountPayResultProccess accountPayResultProccess;

	@Autowired
	private PayRecordService payRecordService;

	@Override
	public void onMessage(Message msg) {
		String jsonMessage = "";
		AccountPayResultMessage resultMessage = null;
		try {
			jsonMessage = new String(msg.getBody(), "UTF-8");
			logger.info("接受到account mq消息" + jsonMessage);
			resultMessage = JSONUtils.jsonToBean(jsonMessage, AccountPayResultMessage.class);
			if (resultMessage != null) {
				PayLoggerFactory.TAG_LOCAL.set("[接收account支付消息|onMessage] - [tradeSysOrderId=" + resultMessage.getOrderNo());
				logger.info("接受到accountPay mq消息" + resultMessage);
				PayRecord payRecord = payRecordService.queryRecordById(resultMessage.getOrderNo());
				if (payRecord == null) {
					logger.error("payRecord not exist");
					throw new PayBizException(ErrorCode.P9002006);
				}
				if(payRecord.isCombinedPay()) {
					accountPayResultProccess.processForAccountPayMsgComb(resultMessage, payRecord);
				}else {
					accountPayResultProccess.processForAccountPayMsg(resultMessage, payRecord);
				}
			} else {
				logger.error("收到的消息为空");
			}
		} catch (PayBizException pe) {
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
