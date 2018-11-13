package com.yeepay.g3.core.nccashier.gateway.service;

import java.util.Map;

import com.yeepay.g3.core.nccashier.vo.ActivityInfoOfPayProduct;
import com.yeepay.g3.core.nccashier.vo.MarketReductionActivitiesRequestParam;

public interface MarketInfoService {

	Map<String, ActivityInfoOfPayProduct> queryMarketingReductionActivities(
			MarketReductionActivitiesRequestParam requestParam);

}
