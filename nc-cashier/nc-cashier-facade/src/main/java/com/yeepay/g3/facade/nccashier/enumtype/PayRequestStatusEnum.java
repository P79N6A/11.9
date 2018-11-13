package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * Created by xiewei on 15-10-25.
 */
public enum PayRequestStatusEnum {
	/**
	 * 初始化
	 */
	INIT("INIT"),
	/**
	 * 成功
	 */
	SUCCESS("SUCCESS"),
	/**
	 * 失败
	 */
	FAILED("FAILED"),
	/**
	 * 冲正
	 */
	REVERSE("REVERSE");
	
	private String value;

	PayRequestStatusEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
