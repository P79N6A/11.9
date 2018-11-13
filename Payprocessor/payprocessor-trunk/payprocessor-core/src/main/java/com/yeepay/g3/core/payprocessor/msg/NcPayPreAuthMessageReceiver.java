package com.yeepay.g3.core.payprocessor.msg;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.external.service.NcPayService;
import com.yeepay.g3.core.payprocessor.service.NcPayResultPreAuthProccess;
import com.yeepay.g3.core.payprocessor.service.NcPayResultProccess;
import com.yeepay.g3.core.payprocessor.service.PayRecordService;
import com.yeepay.g3.core.payprocessor.service.PreAuthReverseRecordService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author peile.fan
 *
 */
public class NcPayPreAuthMessageReceiver implements MessageListener {

	protected static final Logger logger = PayLoggerFactory.getLogger(NcPayPreAuthMessageReceiver.class);

	@Autowired
	private NcPayResultPreAuthProccess ncPayResultPreAuthProccess;

	@Autowired
	private NcPayService ncPayService;

	@Autowired
	private PayRecordService payRecordService;

	@Autowired
	private PreAuthReverseRecordService preAuthReverseRecordService;

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
				PayLoggerFactory.TAG_LOCAL.set("[接收ncpay预授权消息|onMessage] - [orderOrderId=" + resultMessage.getBizOrderNum()
						+ " orderSysNo=" + resultMessage.getBizType() + "]");
				logger.info("接受到ncpay预授权mq消息" + resultMessage);
				PayRecord payRecord = payRecordService.queryRecordByRecord(resultMessage.getBizOrderNum());
				// 正常业务
				if (payRecord != null) {
					ncPayResultPreAuthProccess.processForNcPayPreAuthMsg(resultMessage, payRecord);
				// 冲正业务
				}else {
					PreAuthReverseRecord reverseRecord = preAuthReverseRecordService.queryByReverseNo(resultMessage.getBizOrderNum());
					ncPayResultPreAuthProccess.processForNcPayReversePreAuthMsg(resultMessage, reverseRecord);
				}
				ncPayService.updateTaskStatus(resultMessage.getPayOrderNum(), resultMessage.getBizOrderNum());
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
