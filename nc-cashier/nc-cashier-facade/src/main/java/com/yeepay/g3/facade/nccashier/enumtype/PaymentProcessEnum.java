package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * 支付步骤枚举
 */
public enum PaymentProcessEnum {

	PAYMENT_PROCESS_REQUEST(1, "支付下单"),
	PAYMENT_PROCESS_SEND_SMS(2,"发送短验"),
	PAYMENT_PROCESS_CONFIRM_PAY(3,"确认支付");


	private int value;

	private String description;

	private PaymentProcessEnum(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
