package com.yeepay.g3.core.nccashier.facade;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryBindCardOpenStatusResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.service.NoTradingProcessorFacade;

public class NoTradingProcessorFacadeTest extends BaseTest{
	
	@Autowired
	private NoTradingProcessorFacade noTradingProcessorFacade;
	
	@Test
	public void testAuthBindRequest(){
		NOPAuthBindRequestDTO nopAuthBindCardRequest = new NOPAuthBindRequestDTO();
		nopAuthBindCardRequest.setMerchantNo("10040007800");
		nopAuthBindCardRequest.setMerchantFlowId("20170906123457");
		nopAuthBindCardRequest.setUserNo("pengym");
		nopAuthBindCardRequest.setUserType("USER_ID");
		nopAuthBindCardRequest.setBankCardNo("6217870100000056092");
		nopAuthBindCardRequest.setUserName("彭洋旻");
		nopAuthBindCardRequest.setIdCardNo("411526198710235432");
		nopAuthBindCardRequest.setPhone("13552573077");
		nopAuthBindCardRequest.setCardType("DC");
		nopAuthBindCardRequest.setBindCallBackUrl("www.baidu.com");
		NOPAuthBindResponseDTO response = noTradingProcessorFacade.authBindCardRequest(nopAuthBindCardRequest);
		
		System.out.println("testAuthBindRequest，response=" + response);
		
		
	}
	@Test
	public void testAuthBindConfirm(){
		NOPAuthBindConfirmRequestDTO request = new NOPAuthBindConfirmRequestDTO();
		request.setMerchantNo("10040007800");
		request.setMerchantFlowId("20170906123457");
		request.setRequestFlowId("16084976-822a-48ff-9305-fc226c9d254e");
		request.setSmsCode("351200");
		NOPAuthBindConfirmResponseDTO authBindCardConfirm = noTradingProcessorFacade.authBindCardConfirm(request);
		System.out.println("testAuthBindConfirm，response=" + authBindCardConfirm);
	}
	@Test
	public void testAuthBindSMS(){
		NOPAuthBindSMSRequestDTO requestSMS = new NOPAuthBindSMSRequestDTO();
		requestSMS.setMerchantNo("10040007800");
		requestSMS.setMerchantFlowId("20170906123457");
		requestSMS.setRequestFlowId("ce952371-2a7d-45bd-aecf-66e4b53e58c3");
		NOPAuthBindSMSResponseDTO authBindCardReSendSMS = noTradingProcessorFacade.authBindCardReSendSMS(requestSMS);
		System.out.println("testAuthBindSMS，response=" + authBindCardReSendSMS);
	}
	
	@Test
	public void testBindCardOpenStatus(){
		NOPQueryBindCardOpenStatusResponseDTO response = noTradingProcessorFacade.getNopBindCardOpenStatus("10040007800");
		System.out.println("testBindCardOpenStatus，response=" + response);
	}
	@Test
	public void testQueryBindCardOrder(){
		NOPQueryOrderRequestDTO request = new NOPQueryOrderRequestDTO();
		request.setMerchantNo("10040007800");
		request.setMerchantFlowId("20170906123457");
		request.setRequestFlowId("16084976-822a-48ff-9305-fc226c9d254e");
		NOPQueryOrderResponseDTO queryNopOrderStatus = noTradingProcessorFacade.queryNopOrderStatus(request);
		System.out.println("testQueryBindCardOrder，response=" + queryNopOrderStatus);
	}
	@Test
	public void testQueryCardBinInfo(){
		NOPCardBinRequestDTO cardBinRequestDTO = new NOPCardBinRequestDTO();
		cardBinRequestDTO.setMerchantNo("10040007800");
		cardBinRequestDTO.setCardNo("6217870100000056091");
		//cardBinRequestDTO.setBiz("20");
		cardBinRequestDTO.setCardType("DC");
		NOPCardBinResponseDTO nopCardBinInfo = noTradingProcessorFacade.getNopCardBinInfo(cardBinRequestDTO);
		System.out.println("testQueryCardBinInfo，response=" + nopCardBinInfo);
	}
}
