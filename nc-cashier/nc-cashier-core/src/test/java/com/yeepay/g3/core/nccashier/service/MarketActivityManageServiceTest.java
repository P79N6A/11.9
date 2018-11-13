package com.yeepay.g3.core.nccashier.service;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.ActivityInfoOfPayProduct;

public class MarketActivityManageServiceTest extends BaseTest{
	
	@Resource
	private MarketInfoManageService marketInfoManageService;
	
	@Resource
	private PaymentRequestService paymentRequestService;
	
	@Test
	public void testQueryMarketingReductionActivities(){
		try{
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(46088l);
			Map<String, ActivityInfoOfPayProduct> activities = marketInfoManageService.getMarketActivity("a6339cef-0dc0-4339-91cb-bee309c7238d", paymentRequest);
			System.out.println("获取到的营销信息为:" + activities);
		}catch(Throwable t){
			t.printStackTrace();
		}
		
	}

}
