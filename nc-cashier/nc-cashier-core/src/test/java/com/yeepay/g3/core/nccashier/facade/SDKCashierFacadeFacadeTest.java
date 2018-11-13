package com.yeepay.g3.core.nccashier.facade;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayResponseDTO;
import com.yeepay.g3.facade.nccashier.service.SDKCashierFacade;

public class SDKCashierFacadeFacadeTest extends BaseTest {
	
	private static final String NCCASHIER_CORE_QA = "http://10.151.30.8:8009/nc-cashier-hessian/hessian/";
	
	@Autowired
	private SDKCashierFacade sdkCashierFacade;
	
	@Test
	public void testPayRequest(){
		SDKCreateOrderRequestDTO createOrderRequest = buildSDKCreateOrderRequestDTO();
		SDKCreateOrderResponseDTO response = sdkCashierFacade.payRequest(createOrderRequest);
		System.out.println(JSON.toJSONString(response));
	}
	
	@Test
	public void testPay(){
		SDKPayRequestDTO pay = new SDKPayRequestDTO();
		pay.setMerchantNo("10040007800");
		pay.setRequestId(16934l);
		pay.setPayType("ALIPAY");
		pay.setUserIp("192.168.0.1");
		SDKPayResponseDTO response = sdkCashierFacade.pay(pay);
		System.out.println(response.toString());
		
	}
	
	private static SDKCreateOrderRequestDTO buildSDKCreateOrderRequestDTO(){
		SDKCreateOrderRequestDTO createOrderRequest = new  SDKCreateOrderRequestDTO();
		createOrderRequest.setMerchantNo("10040040286");
//		createOrderRequest.setUserNo(userNo);
//		createOrderRequest.setAppId("wx26f6a9008c72e75b");
		createOrderRequest.setToken("5B9DD957A0B4287D376A2574AF18049B43C0E4C757147F0180426DEFFB7C7C35");
		long timeStamp = System.currentTimeMillis()/1000;
		createOrderRequest.setTimeStamp(timeStamp+"");
		return createOrderRequest;
	}
	
	public static void main(String[] args) {
		HessianProxyFactory bean = new HessianProxyFactory();
		try {
			SDKCashierFacade sdkCashierFacade = (SDKCashierFacade) bean.create(SDKCashierFacade.class,
					NCCASHIER_CORE_QA + "SDKCashierFacade");
			SDKCreateOrderRequestDTO sdkCreateOrderRequestDTO = buildSDKCreateOrderRequestDTO();
			SDKCreateOrderResponseDTO responseDTO = sdkCashierFacade.payRequest(sdkCreateOrderRequestDTO);
			System.out.println(JSON.toJSONString(responseDTO));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
