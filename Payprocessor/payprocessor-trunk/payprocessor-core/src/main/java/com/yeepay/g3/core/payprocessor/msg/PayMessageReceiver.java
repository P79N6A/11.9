package com.yeepay.g3.core.payprocessor.msg;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.PayRecordService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.service.NcPayResultProccess;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.utils.common.log.Logger;
/**
 * 
 * @author peile.fan
 *
 */
public class PayMessageReceiver implements MessageListener {

	protected static final Logger logger = PayLoggerFactory.getLogger(PayMessageReceiver.class);

	@Autowired
	private NcPayResultProccess ncPayResultProccess;

	@Autowired
	private PayRecordService payRecordService;

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
				PayLoggerFactory.TAG_LOCAL.set("[接收ncpay消息|onMessage] - [orderOrderId=" + resultMessage.getBizOrderNum()
						+ " orderSysNo=" + resultMessage.getBizType() + "]");
				logger.info("接受到ncpay mq消息" + resultMessage);
				PayRecord payRecord = payRecordService.queryRecordById(resultMessage.getBizOrderNum());
				if (payRecord == null) {
					logger.error("payRecord not exist");
					throw new PayBizException(ErrorCode.P9002006);
				}
				if(payRecord.isCombinedPay()) {
					ncPayResultProccess.processForNcPayMsgComb(resultMessage, payRecord);
				}else {
					ncPayResultProccess.processForNcPayMsg(resultMessage, payRecord);
				}
			} else {
				logger.error("收到消息为空");
			}
		} catch (PayBizException pe) {
			logger.error("PayBizException: " + pe.getDefineCode() + ", " + pe.getMessage());
		}  catch (Throwable e) {
			logger.error("[MQ消息接收] 系统异常:", e);
			return;
		} finally {
			PayLoggerFactory.TAG_LOCAL.set(null);
		}
	}
}
