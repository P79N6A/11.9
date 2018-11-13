package com.yeepay.g3.core.nccashier.gatewayservice;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.facade.ncconfig.param.ConfigCashierParam;
import com.yeepay.g3.facade.ncconfig.result.ConfigCashierDTO;

public class NcConfigServiceTest extends BaseTest{
	
	@Autowired
	private NcConfigService ncConfigService;
	
	@Test
	public void queryCashierTemplate(){
		ConfigCashierParam param = new ConfigCashierParam();
		param.setClientCode("YJZF-WAP-USUAL");
		param.setGoodsCode("7993");
		param.setMerchantCode("10040041016");
		param.setSysId(21l);//来客
		param.setToolCode("NCCASHIER");
		ConfigCashierDTO response = ncConfigService.queryConfigCashier(param);
		System.out.println("收银台模板=" + JSON.toJSONString(response));
	}

}
