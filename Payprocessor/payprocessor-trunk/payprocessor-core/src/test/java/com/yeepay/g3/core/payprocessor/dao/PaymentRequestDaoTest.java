package com.yeepay.g3.core.payprocessor.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;
import com.yeepay.g3.utils.common.DateUtils;

/**
 * @author chronos.
 * @createDate 2016/11/9.
 */
public class PaymentRequestDaoTest extends BaseTest {

	@Autowired
	private PaymentRequestDao paymentRequestDao;

	@Test
	public void deleteByPrimaryKey() throws Exception {

	}

	@Test
	public void insert() throws Exception {
		PaymentRequest payment = new PaymentRequest();
		payment.setOrderSystem("orderProcessor");
		payment.setOrderSystemStatus(OrderSystemStatusEnum.SUCCESS.name());
		payment.setAmount(new BigDecimal("0.01"));
		payment.setCustomerNo("10040028946");
		payment.setCustomerName("测试商户");
		payment.setOrderNo("OR" + RandomUtils.nextInt());
		payment.setDealUniqueSerialNo("10040007708" + payment.getOrderNo());
		payment.setPayStatus(PaymentStatusEnum.DOING.name());
		payment.setOutTradeNo("YZFtestZTT5566264418");
		payment.setCreateTime(new Date());
		payment.setUpdateTime(new Date());
		payment.setExpireDate(DateUtils.addHour(new Date(), 1));
		int rows = paymentRequestDao.insert(payment);

		


		System.out.println("[插入测试]" + ToStringBuilder.reflectionToString(payment));
		System.out.println("[更新行数] - [rows=" + rows + "]");
	}

	@Test
	public void selectByPrimaryKey() throws Exception {
		PaymentRequest payment = paymentRequestDao.selectByPrimaryKey(4l);
		System.out.println("[查询测试]" + ToStringBuilder.reflectionToString(payment));
	}

	@Test
	public void updateByPrimaryKey() throws Exception {
		PaymentRequest payment = paymentRequestDao.selectByPrimaryKey(4l);
		payment.setRecordNo("REC" + RandomUtils.nextInt());
		payment.setConfirmTime(new Date());
		payment.setUpdateTime(new Date());
		payment.setPayStatus(PaymentStatusEnum.SUCCESS.name());
		int rows = paymentRequestDao.updateByPrimaryKey(payment);
		System.out.println("[通过主键更新] - [rows = " + rows + "]");
	}

	@Test
	public void updateRequestToReverse() throws Exception {
		PaymentRequest payment = paymentRequestDao.selectByPrimaryKey(4l);
		int rows = paymentRequestDao.updateRequestToReverse(payment.getId(), PaymentStatusEnum.DOING.name());
		System.out.println("[更新为退款状态] - [rows = " + rows + "]");
	}

	@Test
	public void testQueryBySystemAndOrderNo() throws Exception {
		String requestSystem = "nccashier";
		String orderNo = "1479193844279";
		PaymentRequest payment = paymentRequestDao.queryBySystemAndOrderNo(requestSystem, orderNo);
		System.out.println(JSON.toJSONString(payment));
	}

	@Test
	public void testUpdatePaymentOrderSystemStatus() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "13999");
		map.put("orderSystemStatus", "NONE");
		paymentRequestDao.updatePaymentOrderSystemStatus(map);
	}

}