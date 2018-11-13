package com.yeepay.g3.core.nccashier.service;

import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.NcPayService;
import com.yeepay.g3.core.nccashier.utils.FastJsonMessageConverter;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;

public class NcPayResultProccessTest extends BaseTest {
	@Autowired
	private NcPayResultProccess proccess;

	@Autowired
	private PayMessageReceiver receiver;

	@Autowired
	private NcPayService ncPayService;

	@Autowired
	private PaymentRequestService paymentRequestService;

	@Autowired
	private PaymentProcessService paymentProcessService;

	@Test
	public void testOnMessage() {
		PaymentResultMessage message = new PaymentResultMessage();
		message.setBizType(7l);
		message.setBizOrderNum("DD1465789449246");
		message.setPayOrderNum("101606134239225758");
		message.setAccountNo("11115515120721069451");
		message.setBankCode("ICBC");
		message.setBankOrderNo("111333");
		message.setCost(new Amount("5.88"));
		message.setPayCompleteDate(System.currentTimeMillis());
		// message.setErrorCode(resultMessage.getErrorCode());
		// message.setErrorMsg(resultMessage.getErrorMsg());
		message.setFrpCode("ICBC_KUAIPAY_DEBIT_GHDS8013");
		message.setTradeSerialNo("16061311004907998");

		message.setPayStatus(PaymentOrderStatusEnum.FAILURE);
		message.setErrorCode("E411199");
		message.setErrorMsg("预留手机号与开户行归属地不一致");
		FastJsonMessageConverter jsonMessageConverter = new FastJsonMessageConverter();
		Message msg = jsonMessageConverter.toMessage(message, new MessageProperties());
		receiver.onMessage(msg);
		// Boolean result = proccess.processForCallBack(message);
		// System.out.println(result);
	}

	@Test
	public void testForQuery() {
		String orderOrderId = "411611033808088400";
		String orderSysNo = "18";
		PaymentRecord paymentRecord = new PaymentRecord();
		PaymentRequest paymentRequest =
				paymentRequestService.findPayRequestByOrderOrderId(orderOrderId, orderSysNo);
		if (null == paymentRequest) {
			throw CashierBusinessException.PAY_REQUEST_NULL;
		}
		
		
		JSONObject info = JSONObject.parseObject(paymentRequest.getRemark());
		
		String[] arrinfo = info.getObject("payTool", String[].class);
		
		if (CashierVersionEnum.PC.name().equals(paymentRequest.getCashierVersion()) && arrinfo != null
				&& arrinfo.length == 1) {
			paymentRecord.setPayTool(arrinfo[0]);
		}
		
		System.out.println(JSON.toJSONString(paymentRecord));
		
	}

	@Test
	public void testconfirmPayCallBack() {


	}

}
