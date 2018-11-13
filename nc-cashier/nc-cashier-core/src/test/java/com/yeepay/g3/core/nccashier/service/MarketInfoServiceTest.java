package com.yeepay.g3.core.nccashier.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.gateway.service.MarketInfoService;
import com.yeepay.g3.core.nccashier.vo.ActivityInfoOfPayProduct;
import com.yeepay.g3.core.nccashier.vo.MarketReductionActivitiesRequestParam;

public class MarketInfoServiceTest extends BaseTest{

	
	@Resource
	private MarketInfoService marketInfoService;
	
	@Test
	public void testQueryMarketingReductionActivities(){
		MarketReductionActivitiesRequestParam requestParam = new MarketReductionActivitiesRequestParam();
		requestParam.setMerchantNo("10040020578");
		requestParam.setMerchantOrderNo(UUID.randomUUID()+"");
		requestParam.setOrderAmount(new BigDecimal("400"));
		Map<String, ActivityInfoOfPayProduct> result = marketInfoService.queryMarketingReductionActivities(requestParam);
		System.out.println(result);
	}
}
