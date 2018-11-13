package com.yeepay.g3.core.nccashier.entity;

import java.util.Date;

/**
 * Created by xiewei on 15-10-20.
 */
public class UserAccount{
	private static final long serialVersionUID = -3720419615698032975L;
	
	private Long id;
	private long paymentRequestId;
	private String paymentRecordId;
	private String tokenId;
	private String merchantNo;
	private String tradeSysOrderId;
	private String tradeSysNo;
	private String orderOrderId;
	private String orderSysNo;
	private String userIp;
	private String userUa;
	private Date createTime;
	private Date updateTime;
	private ParamShowInfo paramShowInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTradeSysOrderId() {
		return tradeSysOrderId;
	}

	public void setTradeSysOrderId(String tradeSysOrderId) {
		this.tradeSysOrderId = tradeSysOrderId;
	}

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}

	public String getOrderOrderId() {
		return orderOrderId;
	}

	public void setOrderOrderId(String orderOrderId) {
		this.orderOrderId = orderOrderId;
	}

	public String getOrderSysNo() {
		return orderSysNo;
	}

	public void setOrderSysNo(String orderSysNo) {
		this.orderSysNo = orderSysNo;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserUa() {
		return userUa;
	}

	public void setUserUa(String userUa) {
		this.userUa = userUa;
	}


	public String getPaymentRecordId() {
		return paymentRecordId;
	}

	public void setPaymentRecordId(String paymentRecordId) {
		this.paymentRecordId = paymentRecordId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public ParamShowInfo getParamShowInfo() {
		return paramShowInfo;
	}

	public void setParamShowInfo(ParamShowInfo paramShowInfo) {
		this.paramShowInfo = paramShowInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserAccount [id=");
		builder.append(id);
		builder.append(", paymentRequestId=");
		builder.append(paymentRequestId);
		builder.append(", paymentRecordId=");
		builder.append(paymentRecordId);
		builder.append(", tokenId=");
		builder.append(tokenId);
		builder.append(", merchantNo=");
		builder.append(merchantNo);
		builder.append(", tradeSysOrderId=");
		builder.append(tradeSysOrderId);
		builder.append(", tradeSysNo=");
		builder.append(tradeSysNo);
		builder.append(", orderOrderId=");
		builder.append(orderOrderId);
		builder.append(", orderSysNo=");
		builder.append(orderSysNo);
		builder.append(", userIp=");
		builder.append(userIp);
		builder.append(", userUa=");
		builder.append(userUa);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", paramShowInfo=");
		builder.append(paramShowInfo);
		builder.append("]");
		return builder.toString();
	}

}
