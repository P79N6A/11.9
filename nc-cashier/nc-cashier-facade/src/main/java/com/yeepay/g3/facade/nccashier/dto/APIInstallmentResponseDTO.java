package com.yeepay.g3.facade.nccashier.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <前置收银台>
 * 
 * @description 银行卡分期下单返回实体
 * @author duangduang
 *
 */
public class APIInstallmentResponseDTO extends APIBasicResponseDTO {

	private static final long serialVersionUID = 1L;

	private String resultData;

	private String resultType;

	public APIInstallmentResponseDTO() {

	}

	public String getResultData() {
		return resultData;
	}

	public void setResultData(String resultData) {
		this.resultData = resultData;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
