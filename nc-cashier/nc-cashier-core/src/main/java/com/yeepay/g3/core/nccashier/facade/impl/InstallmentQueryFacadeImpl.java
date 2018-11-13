package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIInstallmentBiz;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.service.BankCardInstallmentQueryFacade;

@Service
public class InstallmentQueryFacadeImpl implements BankCardInstallmentQueryFacade {

	@Resource
	private APIInstallmentBiz apiInstallmentBiz;

	@Override
	public InstallmentInfoResponseDTO queryInstallmentRateInfos(InstallmentInfoRequestDTO rateInfoRequestDTO) {
		return apiInstallmentBiz.queryInstallmentRateInfos(rateInfoRequestDTO);
	}

	@Override
	public SignRelationQueryResponseDTO querySignRelationList(SignRelationQueryRequestDTO queryDTO) {
		return apiInstallmentBiz.querySignRelationList(queryDTO);
	}

}
