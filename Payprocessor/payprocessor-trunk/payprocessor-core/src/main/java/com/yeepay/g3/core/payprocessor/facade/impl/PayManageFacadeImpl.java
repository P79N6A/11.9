package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.facade.payprocessor.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.biz.NcPayBiz;
import com.yeepay.g3.facade.payprocessor.facade.PayManageFacade;

@Service("payManageFacade")
public class PayManageFacadeImpl implements PayManageFacade {
	@Autowired
	private NcPayBiz nccPayBiz;

	@Override
	public NcSmsResponseDTO sendSms(NcSmsRequestDTO requestDTO) {
		return nccPayBiz.sendSms(requestDTO);
	}

	@Override
	public NcPayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO requestDTO) {
		return nccPayBiz.confirmPay(requestDTO);

	}

	@Override
	public PayRecordResponseDTO synConfirmPay(NcPayConfirmRequestDTO requestDTO) {
		return nccPayBiz.synConfirmPay(requestDTO);

	}


	@Override
	public NcPayCflOpenResponseDTO ncpayCflOpenAndPay(NcPayCflOpenRequestDTO requestDTO) {
		return nccPayBiz.cflOpenAndPay(requestDTO);
	}

	@Override
	public NcPayCflOrderResponseDTO ncpayCflRequest(NcPayCflOrderRequestDTO requestDTO) {
		return nccPayBiz.cflRequest(requestDTO);
	}

	@Override
	public NcPayCflSmsResponseDTO ncpayCflSendSms(NcPayCflSmsRequestDTO requestDTO) {
	    return nccPayBiz.cflSendSms(requestDTO);
	}

	@Override
	public NcPayCflConfirmResponseDTO ncpayCflConfirmPay(NcPayCflConfirmRequestDTO requestDTO) {
		return nccPayBiz.cflConfirmPay(requestDTO);
	}

	@Override
	public PayRecordResponseDTO ncpayCflSynConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO) {
		return nccPayBiz.cflSynConfirmPay(requestDTO);
	}

}
