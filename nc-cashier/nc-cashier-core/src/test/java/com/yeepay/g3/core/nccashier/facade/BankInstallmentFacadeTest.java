package com.yeepay.g3.core.nccashier.facade;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationIdOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.service.BankInstallmentFacade;
import com.yeepay.g3.utils.common.encrypt.AES;

public class BankInstallmentFacadeTest extends BaseTest {

	@Resource
	private BankInstallmentFacade bankInstallmentFacade;

	@Test
	public void testRoutePayWay() {
		InstallmentRouteResponseDTO response = bankInstallmentFacade.routePayWay(27704l);
		System.out.println(response);
	}

	@Test
	public void testRateAmount() {
		InstallmentFeeInfoRequestDTO requestDTO = new InstallmentFeeInfoRequestDTO();
		// bankCode=HX, period=6, requestId=28066
		requestDTO.setBankCode("HX");
		requestDTO.setPeriod("6");
		requestDTO.setRequestId(28066);
		InstallmentFeeInfoResponseDTO response = bankInstallmentFacade.getInstallmentFeeInfo(requestDTO);
		System.out.println(response);
	}

	@Test
	public void testSignOrder() {
		// bankCode=CEB, period=3, requestId=27964
		SignRelationIdOrderRequestDTO requestDTO = new SignRelationIdOrderRequestDTO();
		requestDTO.setSignRelationId(600000060);
		requestDTO.setBankCode("CEB");
		requestDTO.setPeriod("3");
		requestDTO.setRequestId(27964);
		BasicResponseDTO response = bankInstallmentFacade.orderBySignRelationId(requestDTO);
		System.out.println(response);
	}

	@Test
	public void testSmsSend() {
		InstallmentSmsRequestDTO requestDTO = new InstallmentSmsRequestDTO();
		requestDTO.setRecordId(41282l);
		requestDTO.setRequestId(27779l);
		BasicResponseDTO response = bankInstallmentFacade.sendSms(requestDTO);
		System.out.println(response);
	}

	@Test
	public void testCardNoOrder() {
		// CardNoOrderRequestDTO [cardNo=, requestId=, bankCode=, period=3]
		CardNoOrderRequestDTO requestDTO = new CardNoOrderRequestDTO();
		// requestDTO.setCardNo("6226890003721015");
		requestDTO.setBankCode("ECITIC");
		requestDTO.setPeriod("3");
		requestDTO.setRequestId(27783);
		requestDTO.setTokenId("b0c43bf2-0b71-4b09-a30e-925a817b4441");
		CardNoOrderResponseDTO response = bankInstallmentFacade.orderByCardNo(requestDTO);
		System.out.println(response);
	}

	public static void main(String[] args) {
		// System.out.println(AES.decryptFromBase64("aD20UaW7nlWY5d1zA+OeMnwO0eV8DKTlK2R8id38RjU=",
		// "qwa7kjuh4gbcv58t"));;
		// System.out.println(AES.encryptToBase64("zml", "qwa7kjuh4gbcv58t"));
		System.out.println(AES.decryptFromBase64("0zv1MpCArc0DrfC/vPRirnwO0eV8DKTlK2R8id38RjU=", "qwa7kjuh4gbcv58t"));
		;

	}
}
