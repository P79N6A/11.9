package com.yeepay.g3.facade.nccashier.dto.api;

import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;

public class APIPreauthCompleteResDTO extends APIBasicResponseDTO{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 预授权完成 PP子单号
	 */
	private String completePaymentOrderNo;
	
	public APIPreauthCompleteResDTO(){
		
	}

	public String getCompletePaymentOrderNo() {
		return completePaymentOrderNo;
	}

	public void setCompletePaymentOrderNo(String completePaymentOrderNo) {
		this.completePaymentOrderNo = completePaymentOrderNo;
	}
	

}
