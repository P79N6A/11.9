package com.yeepay.g3.core.nccashier.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.NoTradingProcessorBiz;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryBindCardOpenStatusResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.service.NoTradingProcessorFacade;

@Service("noTradingProcessorFacade")
public class NoTradingProcessorFacadeImpl implements NoTradingProcessorFacade {

	@Autowired
	private NoTradingProcessorBiz noTradingProcessorBiz;

	@Override
	public NOPCardBinResponseDTO getNopCardBinInfo(NOPCardBinRequestDTO cardBinRequestDTO) {
		return noTradingProcessorBiz.getNopCardBinInfo(cardBinRequestDTO);
	}

	@Override
	public NOPAuthBindResponseDTO authBindCardRequest(NOPAuthBindRequestDTO authBindRequestDTO) {
		return noTradingProcessorBiz.authBindCardRequest(authBindRequestDTO);
	}

	@Override
	public NOPAuthBindConfirmResponseDTO authBindCardConfirm(NOPAuthBindConfirmRequestDTO authBindConfirmRequest) {
		return noTradingProcessorBiz.authBindCardConfirm(authBindConfirmRequest);
	}

	@Override
	public NOPAuthBindSMSResponseDTO authBindCardReSendSMS(NOPAuthBindSMSRequestDTO authBindSMSRequest) {
		return noTradingProcessorBiz.authBindCardReSendSMS(authBindSMSRequest);
	}

	@Override
	public NOPQueryOrderResponseDTO queryNopOrderStatus(NOPQueryOrderRequestDTO queryOrderRequest) {
		return noTradingProcessorBiz.queryNopOrderStatus(queryOrderRequest);
	}

	@Override
	public NOPQueryBindCardOpenStatusResponseDTO getNopBindCardOpenStatus(String merchantNo) {
		return noTradingProcessorBiz.getNopBindCardOpenStatus(merchantNo);
	}

	
}
