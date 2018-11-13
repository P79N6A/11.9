package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * Created by xiewei on 15-10-25.
 */
public enum PayRecordStatusEnum {
	/**
	 * 初始化
	 */
	INIT("INIT"),
	/**
	 * 已下单
	 */
	ORDERED("ORDERED"),
	
	/**
	 * 已发短验证
	 */
	SMS_SEND("SMS_SEND"),
	
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
	 * 预授权完成
	 */
	PREAUTH_COMPLETE_SUCCESS("PREAUTH_COMPLETE_SUCCESS"),

	/**
	 * 预授权撤销
	 */
	PREAUTHCANCEL("PREAUTH_CANCEL_SUCCESS"),

	/**
	 * 预授权完成撤销
	 */
	PREAUTH_COMPLETE_CANCEL_SUCCESS("PREAUTH_COMPLETE_CANCEL_SUCCESS");
	
	private String value;

	PayRecordStatusEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
