package com.yeepay.g3.core.nccashier.biz;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityResponseDTO;

public class MarketActivityBizTest extends BaseTest{

	@Resource
	private MarketActivityBiz marketActivityBiz;
	
	@Test
	public void testGetMarketActivityInfo(){
		MarketActivityRequestDTO requestDTO = new MarketActivityRequestDTO();
		requestDTO.setRequestId(46380l);
		requestDTO.setTokenId("236ea38f-605f-4829-b9ea-6576c3f62f48");
		MarketActivityResponseDTO response = marketActivityBiz.getMarketActivityInfo(requestDTO);
		System.out.println(response);
	}
}
