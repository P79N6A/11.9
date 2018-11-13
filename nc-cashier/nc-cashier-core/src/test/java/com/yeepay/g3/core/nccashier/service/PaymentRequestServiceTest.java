package com.yeepay.g3.core.nccashier.service;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;

public class PaymentRequestServiceTest extends BaseTest {
	@Autowired
	private PaymentRequestService paymentRequestService;

	@Test
	public void testExpiredTime() {
		PaymentRequest request = paymentRequestService.findPayRequestById(2835L);
		paymentRequestService.isRequestExpired(request);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateRequestBaseOnStatus() {
		PaymentRequest request = paymentRequestService.findPayRequestById(2835L);
		request.setState("SUCCESS");
		System.out.println(JSON.toJSONString(request));
		paymentRequestService.updateRequestBaseOnStatus(request, Arrays.asList("INIT"));
	}
}
