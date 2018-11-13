package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class PayConfirmBaseRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String tokenId;
	
	private Long requestId;
	
	private Long recordId;
	
	private String verifyCode;

	public PayConfirmBaseRequestDTO(){
		
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
	
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public void validate() {
		if(StringUtils.isEmpty(tokenId)){
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "tokenId未传");
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
		return "PayConfirmBaseRequestDTO [tokenId=" + tokenId + ", requestId=" + requestId + ", recordId=" + recordId
				+ ", verifyCode=" + verifyCode + "]";
	}
}
