package com.yeepay.g3.core.payprocessor.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;

public class PayRecordServiceTest extends BaseTest {
	@Autowired
	private PayRecordService payRecordService;

	@Test
	public void testQueryNcPaymentNo() {
		String recordNo = "SALE14809227854467179111";
		String paymentNO = payRecordService.queryNcPaymentNo(recordNo);
		System.out.println("***********paymentNo:" + paymentNO);
	}

	@Test
	public void testQueryRecordById() {
		String recordNo = "SALE1806201343589621654";
		PayRecord payRecord = payRecordService.queryRecordById(recordNo);
		System.out.println(JSON.toJSONString(payRecord));
	}


	@Test
	public void testQueryCompleteCancelCount() {
		int count = payRecordService.queryCompleteCancelCount(27466L);
		System.out.println(count);
	}
}
