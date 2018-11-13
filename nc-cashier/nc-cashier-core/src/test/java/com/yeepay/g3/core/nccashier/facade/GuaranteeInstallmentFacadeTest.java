package com.yeepay.g3.core.nccashier.facade;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;
import com.yeepay.g3.facade.nccashier.service.GuaranteeInstallmentFacade;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import org.junit.Test;

import javax.annotation.Resource;

public class GuaranteeInstallmentFacadeTest extends BaseTest {

	@Resource
	private GuaranteeInstallmentFacade guaranteeInstallmentFacade;

	private static long requestId = 0l;
	private static String bankCode = "";
	private static int period = 3;

	@Test
	public void testPrePay() {
		GuaranteeInstallmentPrePayRequestDTO requestDTO = new GuaranteeInstallmentPrePayRequestDTO();
		requestDTO.setRequestId(requestId);
		requestDTO.setBankCode(bankCode);
		requestDTO.setPeriod(period);
		GuaranteeInstallmentPrePayResponseDTO response = guaranteeInstallmentFacade.prePay(requestDTO);
		System.out.println(response);
	}

	@Test
	public void testRequestPayment() {
		GuaranteeInstallmentPaymentRequestDTO requestDTO = new GuaranteeInstallmentPaymentRequestDTO();
		requestDTO.setRequestId(requestId);
		requestDTO.setBankCode(bankCode);
		requestDTO.setPeriod(period);
		requestDTO.setPreRouteId("");
		requestDTO.setCardNo("");
		requestDTO.setIdNo("");
		requestDTO.setOwner("");
		requestDTO.setPhoneNo("");
		requestDTO.setCvv("");
		requestDTO.setAvlidDate("");
		requestDTO.setBankPWD("");
		GuaranteeInstallmentPaymentResponseDTO response = guaranteeInstallmentFacade.requestPayment(requestDTO);
		System.out.println(response);
	}

	@Test
	public void testGetSupportBankAndPeriods() {
		InstallmentRouteResponseDTO response = guaranteeInstallmentFacade.getSupportBankAndPeriods(33571l);
		System.out.println(response);
	}

	public static void main(String[] args) {
		NCPayParamMode nCPayParamMode = new NCPayParamMode(111);
		System.out.println(nCPayParamMode);
	}
}
