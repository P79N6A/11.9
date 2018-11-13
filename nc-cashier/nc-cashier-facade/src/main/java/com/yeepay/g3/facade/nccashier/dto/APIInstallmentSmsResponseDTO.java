package com.yeepay.g3.facade.nccashier.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

public class APIInstallmentSmsResponseDTO extends APIBasicResponseDTO {

	private static final long serialVersionUID = 1L;


	public APIInstallmentSmsResponseDTO() {

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
