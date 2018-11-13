package com.yeepay.g3.core.nccashier.facade;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferRequestDTO;
import com.yeepay.g3.facade.nccashier.service.RiskControlFacade;

public class RiskControlFacadeTest extends BaseTest {
	
	@Autowired
	private RiskControlFacade riskControlFacade;


	@Test
	public void testCheckReffer(){
		CheckRefferRequestDTO requestDTO = new CheckRefferRequestDTO();
		requestDTO.setBizOrderId("411123423232322");
		requestDTO.setMerchantAccountCode("10040007800");
		requestDTO.setReffer("http://www.yeepay.com");
		CheckRefferReponseDTO response  = riskControlFacade.checkReffer(requestDTO);
		System.out.println(response.getResultCode());
		
	}
	
	
}
