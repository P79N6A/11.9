package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * 下单类型
 * @author zhen.tan
 *
 */
public enum NCCashierOrderTypeEnum {

	/**
	 * 首次支付下单
	 */
	FIRST("首次支付下单"),
	
	/**
	 * 绑卡支付下单
	 */
	BIND("绑卡支付下单");
	
	private String description;
	
	private NCCashierOrderTypeEnum(){
		
	}
	
	private NCCashierOrderTypeEnum(String description){
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
