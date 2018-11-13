package com.yeepay.g3.app.nccashier.wap.service;

import java.util.Map;

import com.yeepay.g3.app.nccashier.wap.vo.MarketingInfoVO;

public interface MarketActivityManageService {

	/**
	 * 获取营销活动信息，构造返回前端响应数据
	 * 
	 * @param requestId
	 * @param tokenId
	 * @return 给前端的响应数据
	 */
	MarketingInfoVO getMarketActivityInfo(long requestId, String tokenId);

}
