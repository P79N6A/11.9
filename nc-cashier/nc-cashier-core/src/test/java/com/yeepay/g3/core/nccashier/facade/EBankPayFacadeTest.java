package com.yeepay.g3.core.nccashier.facade;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.EBankCreatePaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankCreatePaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankSupportBanksRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankSupportBanksResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.BankAccountTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.BankEnum;
import com.yeepay.g3.facade.nccashier.service.EBankPayFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class EBankPayFacadeTest extends BaseTest{
	
	private static final Logger logger = LoggerFactory.getLogger(EBankPayFacadeTest.class);
	
	@Autowired
	private EBankPayFacade eBankPayFacade;
	
	@Test
	public void querySupportBankListTest(){
		EBankSupportBanksRequestDTO request = new EBankSupportBanksRequestDTO();
		request.setPaymentRequestId(11229l);
		EBankSupportBanksResponseDTO response = eBankPayFacade.querySupportBankList(request);
		logger.info("querySupportBankListTest response:{}", response);
	}
	
	@Test
	public void createPayment(){
		EBankCreatePaymentRequestDTO request = new EBankCreatePaymentRequestDTO();
		request.seteBankAccountType(BankAccountTypeEnum.B2B.name());
		request.setBankId(BankEnum.ICBC.name());
		request.setPaymentRequestId(11296l);
		request.setToken("4dc3e642-cb33-4943-a7ac-4313ce9ec7ae");
//		request.setClientId(param.getClientId());
		logger.info("createPayment request:{}", request);
		EBankCreatePaymentResponseDTO response = eBankPayFacade.createPayment(request);
		logger.info("createPayment response:{}", response);
	}

}
