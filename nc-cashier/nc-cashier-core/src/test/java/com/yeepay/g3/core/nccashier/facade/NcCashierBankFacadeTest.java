package com.yeepay.g3.core.nccashier.facade;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.BankCardReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CardValidateResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.dto.SupportBanksResponseDTO;
import com.yeepay.g3.facade.nccashier.service.NcCashierBankFacade;

public class NcCashierBankFacadeTest extends BaseTest {
	@Autowired
	private NcCashierBankFacade ncCashierBankFacade;


	@Test
	public void testGetBankCardInfo(){
		BankCardReponseDTO BankCardReponseDTO = ncCashierBankFacade.getBankCardInfo(5940l,Constant.BNAK_RULE_CUSTYPE_SALE);
		System.out.println(BankCardReponseDTO.toString());
		
	}
	
	@Test
	@Ignore
	public void testGetValidates(){
		CardValidateRequestDTO requestDto = new CardValidateRequestDTO();
		requestDto.setCardno("6222020200108256211");
		requestDto.setRequestId(1911);
		CardValidateResponseDTO response = ncCashierBankFacade.getCardValidates(requestDto);
		System.out.println(response);
		Assert.assertTrue(response !=null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
	}
	
	@Test
	@Ignore
	public void testSupportList(){
		SupportBanksResponseDTO response = ncCashierBankFacade.supportBankList(1911,Constant.BNAK_RULE_CUSTYPE_SALE);
		System.out.println(response);
		Assert.assertTrue(response !=null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
	}
}
