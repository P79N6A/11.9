package com.yeepay.g3.core.nccashier.gatewayservice;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.facade.cwh.enumtype.UserType;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;

public class CwhServiceTest extends BaseTest{
	
	@Autowired
	private CwhService cwhService;
	
	@Test
	public void testGetBindCardsByExternalId(){
		
		 List<BindCardDTO> bindcards = cwhService.getBindCardsByExternalId("", UserType.LOGIN);
		System.out.println(JSON.toJSONString(bindcards));
	}

}
