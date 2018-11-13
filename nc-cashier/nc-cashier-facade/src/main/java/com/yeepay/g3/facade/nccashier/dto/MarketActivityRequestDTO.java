package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

public class MarketActivityRequestDTO implements Serializable{


	private static final long serialVersionUID = 8839753565659385791L;
	
	@NumberValidate
	@ErrorDesc(error=Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="requestId未传")
	private long requestId;
	
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION)
	private String tokenId;
	
	public MarketActivityRequestDTO(){
		
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
