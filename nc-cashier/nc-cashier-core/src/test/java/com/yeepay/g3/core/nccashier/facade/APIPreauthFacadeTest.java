package com.yeepay.g3.core.nccashier.facade;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPreauthCancelRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPreauthCompleteCancelRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPreauthCompleteRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPreauthResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIResultQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteResDTO;
import com.yeepay.g3.facade.nccashier.service.APICommonBusinessFacade;
import com.yeepay.g3.facade.nccashier.service.APIPreauthFacade;

public class APIPreauthFacadeTest extends BaseTest {

	@Autowired
	private APIPreauthFacade apiPreauthFacade;

	@Autowired
	private APICommonBusinessFacade apiCommonBusinessFacade;

	/**
	 * 预授权撤销
	 */
	@Test
	public void preauthCancelTest() {
		APIPreauthCancelRequestDTO requestDTO = new APIPreauthCancelRequestDTO();
		requestDTO.setBizType("DS");
		requestDTO.setMerchantNo("10040007800");
		requestDTO.setPayOrderId("PREAUTH_RE1712281857143493450");
		requestDTO.setToken("37CD1AFEE26F3E5B1873D6E803EEEA85B2D79F10E3944300079C95547A08C3FB");
		requestDTO.setVersion("1.0");
		APIPreauthResponseDTO response = apiPreauthFacade.preauthCancel(requestDTO);
		System.out.println(JSON.toJSONString(response));
	}
	
	@Test
	public void preauthCompleteTest() {
		APIPreauthCompleteRequestDTO requestDTO = new APIPreauthCompleteRequestDTO();
		requestDTO.setAmount(new BigDecimal("0.01"));
		requestDTO.setBizType("DS");
		requestDTO.setMerchantNo("10040007800");
		requestDTO.setPayOrderId("PREAUTH_RE1801021006173385347");
		requestDTO.setToken("85DF219B932B4D21FD1BEC75D6C0983658567B26D5A6B2A991D59C94AF4C4E7F");
		requestDTO.setVersion("1.0");
		APIPreauthResponseDTO response = apiPreauthFacade.preauthComplete(requestDTO);
		System.out.println(JSON.toJSONString(response));
	}
	
	@Test
	public void preauthCompleteCancelTest(){
		APIPreauthCompleteCancelRequestDTO requestDTO = new APIPreauthCompleteCancelRequestDTO();
		requestDTO.setBizType("DS");
		requestDTO.setCardNo("6225768206256669");
		requestDTO.setMerchantNo("10040007800");
		requestDTO.setPayOrderId("PREAUTH_CM1801021018003536628");
		requestDTO.setToken("85DF219B932B4D21FD1BEC75D6C0983658567B26D5A6B2A991D59C94AF4C4E7F");
		requestDTO.setVersion("1.0");
		APIPreauthResponseDTO response = apiPreauthFacade.preauthCompleteCancel(requestDTO);
		System.out.println(JSON.toJSONString(response));
	}

	@Test
	public void queryResultTest() {
		APIBasicRequestDTO requestDTO = new APIBasicRequestDTO();
		requestDTO.setBizType("DS");
		requestDTO.setMerchantNo("10040007800");
		requestDTO.setToken("D996566A8E8C5CEC2A4C51A6494B657070643352F9A109DA4A6789B8AEC88A34");
		requestDTO.setVersion("1.0");
		APIResultQueryResponseDTO response = apiCommonBusinessFacade
				.queryResult(requestDTO);
		System.out.println(JSON.toJSONString(response));
	}
	
	
	// 预授权后三个接口单元测试
	
	@Test
	public void preauthCompleteNormalTest(){
		APIPreauthCompleteReqDTO requestDTO = new APIPreauthCompleteReqDTO();
		requestDTO.setBizType("DS");
//		{
//			"merchantNo" : "10040057338",
//			"recordId" : "67702",
//			"completeAmount"     : "0.01",
//			"bizType"    : "DS",
//			"token"      : "437B1E32F0C32C9B91C9C05DA4B30656DA11719713F664C9343696CEC2F53FD7",
//			"version"    : "1.0"
//			}
		requestDTO.setMerchantNo("10040057338");
		requestDTO.setToken("437B1E32F0C32C9B91C9C05DA4B30656DA11719713F664C9343696CEC2F53FD7");
		requestDTO.setCompleteAmount(new BigDecimal("0.01"));
//		requestDTO.setRecordId("67702");// 预授权请求成功的支付记录ID
		requestDTO.setVersion("1.0");
		APIPreauthCompleteResDTO response = apiPreauthFacade.complete(requestDTO);
		System.out.println(JSON.toJSONString(response));
	}

}
