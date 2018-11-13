package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 支付处理器是否可查标识监听处理请求入参
 * @author duangduang
 * @since  2016-11-10
 *
 */
public class PayResultQuerySignListenRequestDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String token;
	
	private Long paymentRecordId;
	
	private Long paymentRequestId;
	
	
	public PayResultQuerySignListenRequestDTO(){
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getPaymentRecordId() {
		return paymentRecordId;
	}

	public void setPaymentRecordId(Long paymentRecordId) {
		this.paymentRecordId = paymentRecordId;
	}

	public Long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(Long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	
	
	

}
