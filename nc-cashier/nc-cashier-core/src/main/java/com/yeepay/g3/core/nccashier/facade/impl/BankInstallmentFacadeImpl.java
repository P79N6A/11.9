package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.BankInstallmentBiz;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationIdOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.service.BankInstallmentFacade;

@Service("bankInstallmentFacade")
public class BankInstallmentFacadeImpl implements BankInstallmentFacade{
	
	@Resource
	private BankInstallmentBiz bankInstallmentBiz;

	@Override
	public InstallmentRouteResponseDTO routePayWay(long requestId) {
		return bankInstallmentBiz.routePayWay(requestId);
	}

	@Override
	public InstallmentFeeInfoResponseDTO getInstallmentFeeInfo(InstallmentFeeInfoRequestDTO requestDTO) {
		return bankInstallmentBiz.getInstallmentFeeInfo(requestDTO);
	}

	@Override
	public CardNoOrderResponseDTO orderByCardNo(CardNoOrderRequestDTO cardNoOrderRequestDTO) {
		return bankInstallmentBiz.orderByCardNo(cardNoOrderRequestDTO);
		
	}

	@Override
	public BasicResponseDTO orderBySignRelationId(SignRelationIdOrderRequestDTO requestDTO) {
		return bankInstallmentBiz.orderBySignRelationId(requestDTO);
	}

	@Override
	public BasicResponseDTO sendSms(InstallmentSmsRequestDTO requestDTO) {
		return bankInstallmentBiz.sendSms(requestDTO);
	}

	@Override
	public BasicResponseDTO confirmPay(InstallmentConfirmRequestDTO requestDTO) {
		return bankInstallmentBiz.confirmPay(requestDTO);
	}

}
