package com.yeepay.g3.core.nccashier.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;

public class NewOrderHandleServiceTest extends BaseTest{
	
	@Resource
	private NewOrderHandleService newOrderHandleService;
	
	@Test
	public void testOrderReferCheck() {
		OrderSysConfigDTO orderSysConfigDTO = CommonUtil.getBizSysCnfigParams("TEST");
		OrderDetailInfoModel orderDetailInfoModel = new OrderDetailInfoModel();
		orderDetailInfoModel.setMerchantAccountCode("10040007800");
		orderDetailInfoModel.setMerchantOrderId("test123222");
		orderDetailInfoModel.setUniqueOrderNo("12311212113441");
		orderDetailInfoModel.setReffer("www.yeepay.com");
		try {
			newOrderHandleService.orderReferCheck(orderSysConfigDTO, orderDetailInfoModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
