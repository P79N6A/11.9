package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class UserAccessResponseDTO extends BasicResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 9031938520092712306L;

	private Long UserAccessId;
	/**
	 * 支付请求ID
	 */
	private Long paymentRequestId;

	public Long getUserAccessId() {
		return UserAccessId;
	}

	public void setUserAccessId(Long userAccessId) {
		UserAccessId = userAccessId;
	}

	public Long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(Long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

}
