package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIInstallmentBiz;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentComfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.service.APIInstallmentFacade;

@Service("apiInstallmentFacade")
public class APIInstallmentFacadeImpl implements APIInstallmentFacade {

	@Resource
	private APIInstallmentBiz apiInstallmentBiz;

	@Override
	public APIBasicResponseDTO request(APIInstallmentRequestDTO requestDTO) {
		return apiInstallmentBiz.request(requestDTO);
	}

	@Override
	public APIBasicResponseDTO smsSend(APIInstallmentSmsRequestDTO requestDTO) {
		return apiInstallmentBiz.smsSend(requestDTO);
	}

	@Override
	public APIBasicResponseDTO confirmPay(APIInstallmentComfirmRequestDTO requestDTO) {
		return apiInstallmentBiz.confirmPay(requestDTO);
	}

}
