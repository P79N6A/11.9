package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteResDTO;
import com.yeepay.g3.facade.nccashier.service.APIPreauthFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIPreauthBiz;

/**
 * 预授权下单、发短验证、确认支付、完成、撤销、完成撤销接口
 * 
 * @author duangduang
 *
 */
@Service("apiPreauthFacade")
public class APIPreauthFacadeImpl implements APIPreauthFacade {

	@Autowired
	private APIPreauthBiz apiPreauthBiz;

	@Override
	public APIPreauthResponseDTO preauthCancel(APIBasicRequestDTO requestDTO) {
		return apiPreauthBiz.preauthCancel(requestDTO);
	}

	@Override
	public APIPreauthCompleteResponseDTO preauthComplete(APIBasicRequestDTO requestDTO) {
		return apiPreauthBiz.preauthComplete(requestDTO);
	}

	@Override
	public APIPreauthResponseDTO preauthCompleteCancel(APIBasicRequestDTO requestDTO) {
		return apiPreauthBiz.preauthCompleteCancel(requestDTO);
	}

	// --------  以下是给商户提供的预授权下单、预授权发短验、预授权短验确认、预授权完成、预授权撤销、预授权完成撤销 -----------
	
	@Override
	public APIPreauthPaymentResponseDTO preAuthFirstRequest(APIPreauthFirstRequestDTO requestDTO) {
		return apiPreauthBiz.preAuthFirstRequest(requestDTO);
	}

	@Override
	public APIPreauthPaymentResponseDTO preAuthBindRequest(APIPreauthBindRequestDTO requestDTO) {
		return apiPreauthBiz.preAuthBindRequest(requestDTO);
	}

	@Override
	public APIBasicResponseDTO preauthSmsSend(APIPreauthSmsSendRequestDTO requestDTO) {
		return apiPreauthBiz.preauthSmsSend(requestDTO);
	}

	@Override
	public APIPreauthConfirmResponseDTO preauthOrderConfirm(APIPreauthConfirmRequestDTO requestDTO) {
		return apiPreauthBiz.preAuthOrderConfirm(requestDTO);
	}
	
	@Override
	public APIPreauthCompleteResDTO complete(APIPreauthCompleteReqDTO reqDTO) {
		return apiPreauthBiz.complete(reqDTO);
	}

	@Override
	public APIPreauthCancelResDTO cancle(APIPreauthCancelReqDTO reqDTO) {
		return apiPreauthBiz.cancle(reqDTO);
	}

	@Override
	public APIPreauthCompleteCancelResDTO completeCancle(APIPreauthCompleteCancelReqDTO reqDTO) {
		return apiPreauthBiz.completeCancle(reqDTO);
	}

}
