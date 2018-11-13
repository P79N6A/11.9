package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentComfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentSmsRequestDTO;

public interface APIInstallmentFacade {

	/**
	 * 银行卡分期下单接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO request(APIInstallmentRequestDTO requestDTO);

	/**
	 * 银行卡分期发短验接口（前提：已下单，且是签约卡下单）
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO smsSend(APIInstallmentSmsRequestDTO requestDTO);

	/**
	 * 银行卡分期确认支付
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO confirmPay(APIInstallmentComfirmRequestDTO requestDTO);
	
	

}
