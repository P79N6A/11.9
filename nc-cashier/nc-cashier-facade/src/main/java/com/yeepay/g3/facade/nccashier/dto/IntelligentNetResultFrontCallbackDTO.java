package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class IntelligentNetResultFrontCallbackDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 支付记录
	 */
	private String paymentNo;
	
	public IntelligentNetResultFrontCallbackDTO(){
		
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	
}
