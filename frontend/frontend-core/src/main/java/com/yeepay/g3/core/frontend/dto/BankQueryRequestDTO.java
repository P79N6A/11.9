package com.yeepay.g3.core.frontend.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class BankQueryRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 商户编号
     */
	private String customerNumber;
    
    /**
     * 商户订单号
     */
    private String orderNo;

	/**
	 * 订单类型
	 */
	private String payInterface;

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

    
    public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayInterface() {
		return payInterface;
	}

	public void setPayInterface(String payInterface) {
		this.payInterface = payInterface;
	}

	@Override
    public String toString(){
    	return ToStringBuilder.reflectionToString(this);
    }

}
