package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

/**
 * 支付下单基本入参
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public class BasicPayRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 收银台token
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "tokenId未传")
	private String tokenId;

	/**
	 * 支付请求ID
	 */
	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "requestId未传")
	private Long requestId;

	/**
	 * 支付记录ID
	 */
	private Long recordId = 0l;

	public BasicPayRequestDTO() {

	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
