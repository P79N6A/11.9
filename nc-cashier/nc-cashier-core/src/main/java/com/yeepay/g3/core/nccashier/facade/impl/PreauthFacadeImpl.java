package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.PreauthBiz;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PreauthBindConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.service.PreauthFacade;

@Service("preauthFacade")
public class PreauthFacadeImpl implements PreauthFacade {

	@Resource
	private PreauthBiz preauthBiz;
	
	@Override
	public CashierPaymentResponseDTO preAuthOrderRequest(CashierPaymentRequestDTO paymentRequestDto) {
		return preauthBiz.preAuthOrderRequest(paymentRequestDto);
	}
	
	@Override
	public CashierPaymentResponseDTO preAuthBindOrderRequest(CashierPaymentRequestDTO paymentRequestDto) {
		return preauthBiz.preAuthBindOrderRequest(paymentRequestDto);
	}
	
	@Override
	public PayConfirmBaseResponseDTO preAuthOrderConfirm(PayConfirmBaseRequestDTO requestDTO) {
		return preauthBiz.preAuthOrderConfirm(requestDTO);
	}

	@Override
	public PayConfirmBaseResponseDTO preAuthBindOrderConfirm(PreauthBindConfirmRequestDTO requestDTO) {
		return preauthBiz.preAuthBindOrderConfirm(requestDTO);
	}

	@Override
	public CashierSmsSendResponseDTO preAuthSendSms(CashierSmsSendRequestDTO smsRequest) {
		return preauthBiz.preAuthSendSms(smsRequest);
	}

}
