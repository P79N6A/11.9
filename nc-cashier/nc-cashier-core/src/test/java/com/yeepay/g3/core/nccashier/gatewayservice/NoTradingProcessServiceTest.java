package com.yeepay.g3.core.nccashier.gatewayservice;

import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.gateway.service.NoTradingProcessorService;
import com.yeepay.g3.facade.nop.dto.AuthBindCardConfirmRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardConfirmResponseDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardResponseDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardSmsRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardSmsResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryCardBinRequestDTO;
import com.yeepay.g3.facade.nop.dto.QueryCardbinResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryOrderRequestDTO;
import com.yeepay.g3.facade.nop.dto.QueryOrderResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryProductStatusReponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryProductStatusRequestDTO;

public class NoTradingProcessServiceTest extends BaseTest {
	@Resource
	private NoTradingProcessorService service;

	@Test
	public void testAuthBindCardRequest() {
		AuthBindCardRequestDTO nopAuthBindCardRequest = new AuthBindCardRequestDTO();
		nopAuthBindCardRequest.setParentMerchantNo("");
		nopAuthBindCardRequest.setMerchantNo("");
		// 业务方，默认DS
		nopAuthBindCardRequest.setRequestSystem("DS");
		// TODO 待补充
		// nopAuthBindCardRequest.setRequestSystemCode("20");
		nopAuthBindCardRequest.setRequestFlowId("");
		//调用方
		nopAuthBindCardRequest.setUserNo("");
		nopAuthBindCardRequest.setUserType("");
		nopAuthBindCardRequest.setBankCardNo("");
		nopAuthBindCardRequest.setUserName("");
		nopAuthBindCardRequest.setIdCardNo("");
		nopAuthBindCardRequest.setPhone("");
		if (StringUtils.isNotBlank("")
				&& StringUtils.isNotBlank("")) {
			nopAuthBindCardRequest.setCvv2("");
			nopAuthBindCardRequest.setValidthru("");
			// ID_PHONE(10)2项 身份证+手机号(卡号必填)
			// COMMON_FOUR(15) 普通4项 卡+身份证+姓名+手机号
			// COMMON_THREE(7) 卡+身份证+姓名
			// CREDIT_FOUR(57) 卡+cvv+有效期+手机号
			// CREDIT_SIX(63) 卡+cvv+有效期+手机号+身份证+姓名；
			nopAuthBindCardRequest.setAuthType("CREDIT_SIX");
		}
		nopAuthBindCardRequest.setAuthType("COMMON_FOUR");
		// 风控字段该如何上送
		nopAuthBindCardRequest.setRiskParamExt("");
		// nopAuthBindCardRequest.setOrderValidate();
		nopAuthBindCardRequest.setIsSMS(true);
		nopAuthBindCardRequest.setCardType("");
		nopAuthBindCardRequest.setBindCallBackUrl("");
		AuthBindCardResponseDTO authBindCardRequest = service.authBindCardRequest(nopAuthBindCardRequest);
		System.out.println(authBindCardRequest);	
	}
	@Test
	public void testAuthBindCardConfirm() {
		AuthBindCardConfirmRequestDTO nopAuthBindConfirmRequest = new AuthBindCardConfirmRequestDTO();
		nopAuthBindConfirmRequest.setParentMerchantNo("");
		nopAuthBindConfirmRequest.setMerchantNo("");
		nopAuthBindConfirmRequest.setEmpower(false);
		nopAuthBindConfirmRequest.setSmsCode("");
		AuthBindCardConfirmResponseDTO authBindCardConfirm = service.authBindCardConfirm(nopAuthBindConfirmRequest);
		System.out.println(authBindCardConfirm);
	}
	@Test
	public void testAuthBindCardSMS() {
		String merchantNo = "";
		String requestNo = "";
		AuthBindCardSmsRequestDTO nopRequest = new AuthBindCardSmsRequestDTO();
		nopRequest.setMerchantNo(merchantNo);
		nopRequest.setParentMerchantNo(merchantNo);
		AuthBindCardSmsResponseDTO authBindCardSms = service.authBindCardSms(nopRequest);
		System.out.println(authBindCardSms);
	}
	@Test
	public void testOrderStatus(){
		QueryOrderRequestDTO request = new QueryOrderRequestDTO();
		QueryOrderResponseDTO queryOrder = service.queryOrder(request);
		System.out.println(queryOrder);
	}
	@Test
	public void testQueryOpenStatus(){
		QueryProductStatusRequestDTO request = new QueryProductStatusRequestDTO();
		QueryProductStatusReponseDTO queryProductStatus = service.queryProductStatus(request);
		System.out.println(queryProductStatus);
	}
	@Test
	public void testQueryCardBin(){
		QueryCardBinRequestDTO request = new QueryCardBinRequestDTO();
		QueryCardbinResponseDTO queryCardBin = service.queryCardBin(request);
		System.out.println(queryCardBin);
	}
}
