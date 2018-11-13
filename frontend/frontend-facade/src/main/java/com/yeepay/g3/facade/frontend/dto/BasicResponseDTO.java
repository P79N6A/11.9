/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 返回支付对象的基类
 * @author TML
 */
public abstract class BasicResponseDTO extends ResponseDTO implements Serializable{

	private static final long serialVersionUID = -1033225973342889327L;
	
    /**
	 * 业务方
	 */
	protected String requestSystem;

	/**
	 * 业务方订单号
	 */
    protected String requestId;
	
    /**
     * 商户编号
     */
    protected String customerNumber;
    
    /**
	 * 支付金额
	 */
    protected BigDecimal totalAmount;
    
    /**
     * 商户订单号
     */
    protected String outTradeNo;
    
    
    /**
     * 银行子系统订单号
     */
    protected String orderNo;

	/**
	 * FE支付订单号
	 */
	protected String payOrderNo;

    /**
     * 订单类型
     */
    protected OrderType orderType;
    
    /**
     * 平台类型
     */
    protected PlatformType platformType;
    
    /**
     * 系统唯一码
     */
    protected String dealUniqueSerialNo;
	


	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}


	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

}
