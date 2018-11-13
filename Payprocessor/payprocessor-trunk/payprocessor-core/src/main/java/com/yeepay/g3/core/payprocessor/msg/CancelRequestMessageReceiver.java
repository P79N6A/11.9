
package com.yeepay.g3.core.payprocessor.msg;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.biz.CancelBiz;
import com.yeepay.g3.core.payprocessor.param.CancelRequestParam;
import com.yeepay.g3.core.payprocessor.param.CancelResponse;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.utils.common.json.JSONUtils;

/**
 * Description:接受订单处理器撤销订单mq
 * 
 * @author peile.fan
 * @since:2017年2月6日 下午1:03:11
 */
public class CancelRequestMessageReceiver implements MessageListener {
	private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(CancelRequestMessageReceiver.class);

	@Autowired
	private CancelBiz cancelBiz;

	@Override
	public void onMessage(Message msg) {
		String jsonMessage = "";
		try {
			jsonMessage = new String(msg.getBody(), "UTF-8");
			logger.info("接受到opr撤销订单mq消息" + jsonMessage);
			Map<String, String> mapMsg = JSONUtils.jsonToBean(jsonMessage, HashMap.class);
			if (mapMsg != null) {
				CancelRequestParam param = new CancelRequestParam();
				BeanUtils.populate(param, mapMsg);
				PayLoggerFactory.TAG_LOCAL.set("[撤销订单请求]-[DealUniqueSerialNo = " + param.getDealUniqueSerialNo() + "]");
				CancelResponse response = cancelBiz.cancelRequest(param);
				logger.info("撤销结果：" + response);
			} else {
				logger.error("收到的消息为空");
			}
		} catch (PayBizException pe) {
			logger.error("PayBizException: " + pe.getDefineCode() + ", " + pe.getMessage());
			return;
		} catch (Throwable e) {
			logger.error("[MQ消息接收] 系统异常", e);
			return;
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
	}

}
