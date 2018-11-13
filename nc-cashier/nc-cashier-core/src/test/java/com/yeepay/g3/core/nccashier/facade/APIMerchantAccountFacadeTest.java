package com.yeepay.g3.core.nccashier.facade;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.service.APIMerchantAccountFacade;

public class APIMerchantAccountFacadeTest extends BaseTest {
	
	@Resource
	private APIMerchantAccountFacade apiMerchantAccountFacade;
	
	public APIMerchantAccountPayRequestDTO buildAPIMerchantAccountPayRequestDTO(){
		APIMerchantAccountPayRequestDTO requestDTO = new APIMerchantAccountPayRequestDTO();
//		requestDTO.setMerchantNo("10040007800");
		requestDTO.setMerchantNo("10040041171");
		requestDTO.setToken("CC87E27A5A1AC91A854E5F5B25B184B1A815C45FD478DDFC70586C7125CF53C0");
		requestDTO.setVersion("1.0");
//		requestDTO.setVersion("1");
		requestDTO.setUserIp("127.0.0.1");
		return requestDTO;
	}
	
	@Test
	public void testAccountPay(){
		APIMerchantAccountPayRequestDTO requestDTO = buildAPIMerchantAccountPayRequestDTO();
		APIBasicResponseDTO responseDTO = apiMerchantAccountFacade.pay(requestDTO);
		System.out.println("返回值=" + JSON.toJSONString(responseDTO));
	}
	

}
