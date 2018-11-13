package com.yeepay.g3.facade.nccashier.enumtype;

public enum BusinessTypeEnum {
	/**
	 * 首次支付无透传卡信息
	 */
	FIRSTPAY,
	/**
	 * 绑卡支付无透传卡信息
	 */
	BINDPAY,
	/**
	 * 首次支付透传非卡号信息
	 */
	FIRSTPASS,
	/**
	 * 绑卡支付透传非卡号信息
	 */
	BINDPASS,
	/**
	 * 绑卡支付透传卡号信息
	 */
	BINDPASSCARDNO,
	/**
	 * 首次支付透传卡号信息
	 */
	FIRSTPASSCARDNO
}
