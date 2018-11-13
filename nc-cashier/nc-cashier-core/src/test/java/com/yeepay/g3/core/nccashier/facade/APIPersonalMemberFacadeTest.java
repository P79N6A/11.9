package com.yeepay.g3.core.nccashier.facade;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMemberBalancePayRequestDTO;
import com.yeepay.g3.facade.nccashier.service.APIPersonalMemberFacade;

public class APIPersonalMemberFacadeTest extends BaseTest {
	
	@Resource
	private APIPersonalMemberFacade apiPersonalMemberFacade;

	private APIMemberBalancePayRequestDTO buildRequestDTONormal(){
		APIMemberBalancePayRequestDTO requestDTO = new APIMemberBalancePayRequestDTO();
/*		requestDTO.setBizType("QM");*/
		requestDTO.setMerchantUserNo("C21123456789012112321"); // 10040020578的memberNo 212468328424这个会员没有余额
		requestDTO.setMerchantNo("10040020667");
		requestDTO.setToken("5B30BE1E3BB3EE2DD5469A324858847A9FF69BBEF7F77F71DD2E413140C1C0AF");
		requestDTO.setUserIp("127.0.0.1");
		requestDTO.setVersion("1.0");
		return requestDTO;
	}
	
	@Test
	public void balancePayTest(){
		APIMemberBalancePayRequestDTO requestDTO = buildRequestDTONormal();
        System.out.println("会员余额支付请求入参是===" + requestDTO);
		APIBasicResponseDTO response = apiPersonalMemberFacade.balancePay(requestDTO);
		System.out.println("会员余额支付的结果是===" + response);
	}
}
