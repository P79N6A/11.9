package com.yeepay.g3.core.nccashier.facade;

import org.jgroups.util.UUID;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.service.APIMerchantScanPayFacade;

public class APIMerchantScanPayFacadeFacadeTest extends BaseTest {
	@Autowired
	private APIMerchantScanPayFacade apiMerchantScanPayFacade;
	@Test
	public void testPay(){
		APIMerchantScanPayDTO request =new APIMerchantScanPayDTO();
//		request.setCode("285980296262300401");
//		request.setCodeType("ALIPAY_SCAN");
		request.setCode("130474835530614386");
		request.setCodeType("WECHAT_SCAN");
		request.setStoreCode("10040007800");
		request.setDeviceSn("000000000001"); 
		request.setMerchantNo("10040007800");
		//a7c3681e-5
		request.setToken("fba61b6d-e");
//		request.setToken(UUID.randomUUID().toString().substring(0,10));
		BasicResponseDTO response = apiMerchantScanPayFacade.pay(request);
		System.out.println(response.toString());
	}

}
