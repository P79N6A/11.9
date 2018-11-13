package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class InstallmentConfirmRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long requestId;
	
	private Long recordId;
	
	private String verifyCode;

	public InstallmentConfirmRequestDTO(){
		
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	public void validate() {
		if (StringUtils.isBlank(verifyCode)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "verifyCode未传");
		}
		if (requestId==null || requestId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
		if (recordId==null || recordId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "recordId未传");
		}
	}

	@Override
	public String toString() {
		return "InstallmentConfirmRequestDTO [requestId=" + requestId + ", recordId=" + recordId + ", verifyCode="
				+ verifyCode + "]";
	}
}
