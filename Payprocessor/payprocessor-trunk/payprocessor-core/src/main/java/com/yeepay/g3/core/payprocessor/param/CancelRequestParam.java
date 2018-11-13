package com.yeepay.g3.core.payprocessor.param;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年1月5日 下午2:53:57
 */
public class CancelRequestParam implements Serializable {

	private static final long serialVersionUID = -1481871964457930438L;

	protected String outTradeNo;

	protected String customerNumber;

	protected String customerName;

	protected String orderNo;

	protected String dealUniqueSerialNo;

	protected String orderSystem;

	protected String amount;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public String getOrderSystem() {
		return orderSystem;
	}

	public void setOrderSystem(String orderSystem) {
		this.orderSystem = orderSystem;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
