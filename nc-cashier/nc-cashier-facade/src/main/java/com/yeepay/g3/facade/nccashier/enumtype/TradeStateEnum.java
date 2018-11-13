package com.yeepay.g3.facade.nccashier.enumtype;

public enum TradeStateEnum {

	/**
	 * 初始化
	 */
	INIT("INIT"),
	/**
	 * 已下单
	 */
	ORDERED("ORDERED"),
	/**
	 * 支付中
	 */
	PAYING("PAYING"),
	/**
	 * 成功
	 */
	SUCCESS("SUCCESS"),
	/**
	 * 失败
	 */
	FAILED("FAILED"),
	/**
	 * 支付冲正
	 */
	PAYREVERSE("PAYREVERSE"),
	/**
	 * 交易冲正
	 */
	TRADEREVERSE("TRADEREVERSE"),
	/**
	 * 冲正
	 */
	REVERSE("REVERSE"),
	/**
	 * 撤销
	 */
	CANCEL("CANCEL");
	
	private String value;

	TradeStateEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
