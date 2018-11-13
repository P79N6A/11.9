
package com.yeepay.g3.core.payprocessor.msg;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.PayRecordService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.service.CflPayResultProcess;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.utils.common.json.JSONUtils;

/**
 * @author peile.fan
 *
 */
public class CflPayMessageReceiver implements MessageListener {

	private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(PayLogger.class);

	@Autowired
	private CflPayResultProcess cflPayResultProcess;

	@Autowired
	private PayRecordService payRecordService;

	@Override
	public void onMessage(Message msg) {
		String jsonMessage = "";
		InstallmentResultMessage resultMessage = null;
		try {
			jsonMessage = new String(msg.getBody(), "UTF-8");
			logger.info("接受到CFL mq消息" + jsonMessage);
			resultMessage = JSONUtils.jsonToBean(jsonMessage, InstallmentResultMessage.class);
			if (resultMessage != null) {
				PayLoggerFactory.TAG_LOCAL
						.set("[接收CFL消息|onMessage] - [tradeSysOrderId=" + resultMessage.getRequestId());
				logger.info("接受到FE-installment mq消息：" + resultMessage.toString());
				PayRecord payRecord = payRecordService.queryRecordById(resultMessage.getRequestId());
				if (payRecord == null) {
					logger.error("payRecord not exist");
					throw new PayBizException(ErrorCode.P9002006);
				}
				if(payRecord.isCombinedPay()) {
					cflPayResultProcess.processForCflPayMsgComb(resultMessage, payRecord);
				}else {
					cflPayResultProcess.processForCflPayMsg(resultMessage, payRecord);
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
			PayLoggerFactory.TAG_LOCAL.remove();
		}
	}

}
