package com.yeepay.g3.facade.nccashier.enumtype;

public enum PayOrderStatusEnum {

	/**
	 * 待支付
	 */
	INIT("待支付"),
	/**
	 * 支付中
	 */
	DOING("支付中"),
	/**
	 * 支付成功
	 */
	SUCCESS("支付成功"),
	/**
	 * 支付失败
	 */
	FAILURE("支付失败"),
	/**
	 * 冲正
	 */
	REVERSE("冲正"),
	/**
	 * 差错退款
	 */
	FAULT_REFUND("差错退款"),
	/**
	 * 重复支付退款
	 */
	REPAY_REFUND("重复支付退款");
	
	private String value;
	
	private PayOrderStatusEnum(){
		
	}
	
	private PayOrderStatusEnum(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
