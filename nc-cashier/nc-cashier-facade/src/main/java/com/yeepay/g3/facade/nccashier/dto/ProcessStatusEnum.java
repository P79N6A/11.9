package com.yeepay.g3.facade.nccashier.dto;

public enum ProcessStatusEnum {
	SUCCESS("处理成功"), FAILED("处理失败");

	private final String value;

	public String getValue() {
		return value;
	}

	ProcessStatusEnum(String value) {
		this.value = value;
	}

}
