package com.yeepay.g3.core.nccashier.gatewayservice;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.gateway.service.UserCenterService;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.ncconfig.param.ConfigCashierParam;
import com.yeepay.g3.facade.ncconfig.result.ConfigCashierDTO;
import com.yeepay.g3.facade.ncmember.dto.GetUsableRespDTO;
import com.yeepay.g3.facade.ncmember.dto.MerchantUserDTO;

public class UserCenterServiceTest extends BaseTest{
	
	@Autowired
	private UserCenterService userCenterService;
	
	@Test
	public void testQueryUseableBindBankList(){
		MerchantUserDTO merchantUserDTO = new MerchantUserDTO();
		merchantUserDTO.setIdentityId("FYB000000000001413");
		merchantUserDTO.setIdentityType(Constant.YIBAO);
		GetUsableRespDTO response = userCenterService.queryUseableBindBankList(merchantUserDTO);
		System.out.println(JSON.toJSONString(response));
	}

}
