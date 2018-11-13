package com.yeepay.g3.core.nccashier.facade;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.service.CashierRouteFacade;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CashierRouteFacadeTest extends BaseTest {
	@Autowired
	private CashierRouteFacade cashierRouteFacade;
	@Ignore
	@Test
	public void testCreatePaymentRequest() {
		CashierRequestDTO cashierRequestDTO = new CashierRequestDTO();
		cashierRequestDTO.setMerchantNo("10040018287");
		cashierRequestDTO.setTradeSysNo("7");
		cashierRequestDTO.setTradeSysOrderId("223607125569957406111");
		cashierRequestDTO.setIdentityId("1001");
		cashierRequestDTO.setIdentityType(IdentityType.ID_CARD);
		cashierRequestDTO.setMerchantName("测试");
		cashierRequestDTO.setMerchantOrderId("SH1461234512668");
		cashierRequestDTO.setProductName("话费充值");
		cashierRequestDTO.setCashierVersion(CashierVersionEnum.WAP);
		cashierRequestDTO.setGoodsCategoryCode("7993");
		cashierRequestDTO.setOrderAmount(BigDecimal.valueOf(10.00));
		cashierRequestDTO.setOrderCreateDate(new Date());
//		cashierRequestDTO.setCardNo("6258091653650874");
//		cashierRequestDTO.setIdcard("522401199012035554");
		CashierResultDTO cashierResultDTO = cashierRouteFacade.receiptRequest(cashierRequestDTO);
		System.out.println(cashierResultDTO.toString());
	}
	
	
	
	@Test
	public void testRoutePayWay(){
		long requestId = 11920l;
		BussinessTypeResponseDTO BussinessTypeResponseDTO = cashierRouteFacade.routerPayWay(requestId,Constant.BNAK_RULE_CUSTYPE_SALE);
		System.out.println(BussinessTypeResponseDTO.toString());
	}
	@Ignore
	@Test
	public void testScanpayCreatePaymentRequest() {
		CashierRequestDTO cashierRequestDTO = new CashierRequestDTO();
		cashierRequestDTO.setMerchantNo("10040018287");
		cashierRequestDTO.setTradeSysNo("NCTRADE");
		cashierRequestDTO.setTradeSysOrderId("SCCANPAY:" + System.currentTimeMillis());
		// cashierRequestDTO.setIdentityId("");
		// cashierRequestDTO.setIdentityType(IdentityType.ID_CARD);
		cashierRequestDTO.setMerchantName("fanpl扫码支付测试");
		cashierRequestDTO.setMerchantOrderId("ORDER_ID_" + System.currentTimeMillis());
		cashierRequestDTO.setOrderOrderId(System.currentTimeMillis() + "");
		cashierRequestDTO.setOrderSysNo("18");
		cashierRequestDTO.setProductName("商品名称");
		cashierRequestDTO.setCashierVersion(CashierVersionEnum.PC);
		cashierRequestDTO.setGoodsCategoryCode("7993");
		cashierRequestDTO.setOrderAmount(BigDecimal.valueOf(10.00));
		cashierRequestDTO.setOrderCreateDate(new Date());
		cashierRequestDTO.setRemark(getRemark());
		cashierRequestDTO.setUserIp("223.223.193.194");
		CashierResultDTO cashierResultDTO = cashierRouteFacade.receiptRequest(cashierRequestDTO);
		System.out.println(cashierResultDTO.toString());
	}


	private String getRemark() {
		Map<String, Object> tradeAdditionInfo = new HashMap<String, Object>();
		tradeAdditionInfo.put("directPayType", 0);
		// 设置支付工具
		String[] paytool = {"SCCANPAY"};
		tradeAdditionInfo.put("payTool", paytool);
		//设置支付方式
		String[] payType = {"21", "23", "28"};
		tradeAdditionInfo.put("PayType", payType);
		return JSON.toJSONString(tradeAdditionInfo);
	}

	@Test
	public void testNewOrderRequest(){
		String orderNo = "10012016000000000046";
		NewOrderRequestResponseDTO response = cashierRouteFacade.newOrderRequest(orderNo,1);
		Assert.assertTrue(response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		
	} 
	
	@Test
	public void test(){
		OrderProcessorRequestDTO orderProcessorRequestDTO = new OrderProcessorRequestDTO();
		orderProcessorRequestDTO.setToken("F920FC277E1E9C51B3E81345B613498A834D21753DECE929517A7A59E3C05EFB");
		orderProcessorRequestDTO.setCashierVersion(CashierVersionEnum.PC);
		orderProcessorRequestDTO.setMerchantNo("10040020578");
		orderProcessorRequestDTO.setUserIp("172.17.104.19");
		cashierRouteFacade.orderProcessorRequest(orderProcessorRequestDTO);
	}
}
