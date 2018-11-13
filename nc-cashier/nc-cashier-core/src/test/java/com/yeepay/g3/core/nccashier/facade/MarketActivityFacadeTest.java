package com.yeepay.g3.core.nccashier.facade;


import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.MarketActivityResponseDTO;
import com.yeepay.g3.facade.nccashier.service.MarketActivityFacade;

public class MarketActivityFacadeTest extends BaseTest{
	
	@Resource
	private MarketActivityFacade marketActivityFacade;
	
	@Test
	public void testGetMarketActivityInfo(){
		try{
			MarketActivityRequestDTO requestDTO = new MarketActivityRequestDTO();
//			requestDTO.setRequestId(46088l);
//			requestDTO.setTokenId("a6339cef-0dc0-4339-91cb-bee309c7238d");
			requestDTO.setRequestId(46117l);
			requestDTO.setTokenId("7c981546-53b4-4c57-8f94-55c62af01303");
			MarketActivityResponseDTO response = marketActivityFacade.getMarketActivityInfo(requestDTO);
			System.out.println(response);
		}catch(Throwable t){
			t.printStackTrace();
		}
		
	}

}
