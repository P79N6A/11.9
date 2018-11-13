package com.yeepay.g3.core.nccashier.facade;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.nccashier.service.PreauthFacade;

public class preauthFacadeTest extends BaseTest{
	
	@Autowired
	private PreauthFacade preauthFacade;
	
	@Test
	public void testPreauthOrderRequest(){
		CashierPaymentRequestDTO requestDTO = new CashierPaymentRequestDTO();
		CardInfoDTO cardInfoDto = buildCardInfoDTO();
		requestDTO.setCardInfo(cardInfoDto);
		requestDTO.setRequestId(30224L);
		requestDTO.setRecordId(0);
		requestDTO.setTokenId("993a3c41-e8e9-4ff8-a632-6ea41d9f1383");
		requestDTO.setOrderType(NCCashierOrderTypeEnum.FIRST);
		preauthFacade.preAuthOrderRequest(requestDTO);
	}
	private CardInfoDTO buildCardInfoDTO() {
		CardInfoDTO cardInfoDto = new CardInfoDTO();
		cardInfoDto.setCardno("6227080252051517");
		cardInfoDto.setValid("0724");
		cardInfoDto.setCvv2("605");
		cardInfoDto.setIdno("150207199210104713");
		cardInfoDto.setName("菅猛");
		cardInfoDto.setPhone("18301618700");
//		cardInfoDto.setBankCode();
//		cardInfoDto.setBankName();
//		cardInfoDto.setCardType();
		return cardInfoDto;
	}
	@Test
	public void testPreauthOrderConfirm(){
		PayConfirmBaseRequestDTO requestDTO = new PayConfirmBaseRequestDTO();
		requestDTO.setRequestId(30224L);
		requestDTO.setRecordId(45528L);
		requestDTO.setTokenId("993a3c41-e8e9-4ff8-a632-6ea41d9f1383");
		preauthFacade.preAuthOrderConfirm(requestDTO);
	}
	
}
