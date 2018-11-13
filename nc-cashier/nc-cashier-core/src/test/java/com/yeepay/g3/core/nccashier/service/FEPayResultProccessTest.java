package com.yeepay.g3.core.nccashier.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.gateway.service.FrontEndService;
import com.yeepay.g3.core.nccashier.utils.FastJsonMessageConverter;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;

public class FEPayResultProccessTest extends BaseTest {

	@Resource
	private FrontEndPayMessageReceiver receiver;
	
	@Resource
	private FrontEndService frontEndService;

	@Test
	public void testOnMessage() {
//		PayResultMessage message = new PayResultMessage();
		String orderId = "411609073892691200";
//		FrontendQueryResponseDTO response = frontEndService.queryPaymentOrder(orderId);
//		response.setOutTradeNo("wap_auto55126127");
//		response.setBankSuccessTime(new Date());
//		response.setPayStatus(PayStatusEnum.SUCCESS);
		FastJsonMessageConverter jsonMessageConverter = new FastJsonMessageConverter();
//		Message msg = jsonMessageConverter.toMessage(response, new MessageProperties());
//		receiver.onMessage(msg);
	}


}
