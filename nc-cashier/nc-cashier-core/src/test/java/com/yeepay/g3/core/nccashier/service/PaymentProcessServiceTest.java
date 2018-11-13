package com.yeepay.g3.core.nccashier.service;

import java.util.Arrays;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;

public class PaymentProcessServiceTest extends BaseTest {
	@Resource
	private PaymentProcessService paymentProcessService;

	@Test
	public void testUpdateRecordState() {
		Long paymentRecordId = 5659l;
		PaymentRecord record =
				paymentProcessService.findRecordByPaymentRecordId(String.valueOf(paymentRecordId));
//		paymentProcessService.updateRecordBaseOnOriginalStatus(record, Arrays.asList(
//				new PayRecordStatusEnum[] {PayRecordStatusEnum.INIT, PayRecordStatusEnum.PAYING}));

	}

	@Test
	public void testUpdateRecordStatusAndPaymentExt() {
		Long paymentRecordId = 5659l;
		String payOrderId= "";
		PayRecordStatusEnum ordered= PayRecordStatusEnum.ORDERED;
		JSONObject paymentExt = new JSONObject();
		paymentExt.put(Constant.GUARANTEE_INSTALLMENT_SERVICE_CHARGE_RATE, "0.1");
		paymentProcessService.updateRecordStatusAndPaymentExt(paymentRecordId,payOrderId,ordered,paymentExt.toJSONString());

	}
	
}
