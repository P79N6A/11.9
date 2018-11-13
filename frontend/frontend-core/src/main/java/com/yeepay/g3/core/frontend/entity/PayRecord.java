package com.yeepay.g3.core.frontend.entity;

import com.yeepay.g3.utils.persistence.Entity;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录表--用于补充支付订单表，1对1
 * @author TML
 *
 */
public class PayRecord implements Entity<Long> {
	
	private static final long serialVersionUID = -3330729033271305517L;

	private Long id;

    private String recordNo;
    /**
     * 渠道平台，例如：微信，支付宝等
     */
    private String platformType;
    /**
     * 订单类型：例如：微信的h5，jsapi，主扫，被扫；支付宝的xxx，xxx
     */
//    private String orderType;
    /**
     * 交易系统编码
     */
    private String requestSystem;
    /**
     * 商户编号
     */
    private String customerNumber;
    
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 交易系统编号
     */
    private String requestId;
    /**
     * 银行子系统订单号
     */
    private String orderNo;
    /**
     * 支付金额
     */
    private BigDecimal totalAmount;
    /**
     * 给上层返回码
     */
    private String responseCode;
    /**
     * 给上层返回信息
     */
    private String responseMsg;
    /**
     * 银行子系统返回码
     */
    private String nocardCode;
    /**
     * 银行子系统返回码
     */
    private String nocardMsg;
    /**
     * 本订单创建时间
     */
    private Date createTime;

    /**
     * 具体的返回信息
     */
    private String frontValue;
    
    public void updateToFail(String errorCode, String errorMsg){
    	this.responseCode = errorCode;
    	this.responseMsg = errorMsg;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
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

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getNocardCode() {
		return nocardCode;
	}

	public void setNocardCode(String nocardCode) {
		this.nocardCode = nocardCode;
	}

	public String getNocardMsg() {
		return nocardMsg;
	}

	public void setNocardMsg(String nocardMsg) {
		this.nocardMsg = nocardMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFrontValue() {
		return frontValue;
	}

	public void setFrontValue(String frontValue) {
		this.frontValue = frontValue;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}