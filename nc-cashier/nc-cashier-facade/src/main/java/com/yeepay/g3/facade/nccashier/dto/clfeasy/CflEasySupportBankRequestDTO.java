package com.yeepay.g3.facade.nccashier.dto.clfeasy;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class CflEasySupportBankRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;

	private Long requestId;

	public CflEasySupportBankRequestDTO() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public void validate() {
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "token未传");
		}
		if (getRequestId() == null || getRequestId() <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
	}

}
