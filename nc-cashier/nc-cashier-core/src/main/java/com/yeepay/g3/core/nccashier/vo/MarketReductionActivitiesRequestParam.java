package com.yeepay.g3.core.nccashier.vo;

import java.math.BigDecimal;

public class MarketReductionActivitiesRequestParam {

	/**
	 * 收单商编
	 */
	private String merchantNo;
	
	/**
	 * 父商编
	 */
	private String parentMerchantNo;
	
	/**
	 * 原订单金额
	 */
	 private BigDecimal orderAmount;
	 
	 /**
	  * 商户订单号
	  */
	 private String merchantOrderNo;
	 
	 public MarketReductionActivitiesRequestParam(){
		 
	 }

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getParentMerchantNo() {
		return parentMerchantNo;
	}

	public void setParentMerchantNo(String parentMerchantNo) {
		this.parentMerchantNo = parentMerchantNo;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}
	 
}
