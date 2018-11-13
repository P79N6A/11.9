package com.yeepay.g3.core.nccashier.vo;

import java.util.Map;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;

public class IntelligentNetResultFrontCallbackResInfo {
	
	/**
	 * 前端回调地址（未携带参数）
	 */
	private String redirectUrl;
	
	/**
	 * 支付记录ID
	 */
	private String paymentNo;
	
	/**
	 * 业务方编码
	 */
	private Long bizType;
	
	/**
	 * 业务方订单号
	 */
	private String bizOrderNo;
	
	/**
	 * 订单金额
	 */
	private Amount orderAmount;
	
	/**
	 * 实际金额
	 */
	private Amount realAmount;
	
	/**
	 * 状态
	 */
	private PaymentOrderStatusEnum state;
	
	/**
	 * 商编
	 */
	private String merchantNo;
	
	/**
	 * 商户订单号
	 */
	private String requestNo;
	
	/**
	 * 确认时间
	 */
	private String confirmTime;
	
	/**
	 * 跳转方式：POST/GET，目前只有GET形式
	 */
	private String redirectMehod;

	/**
	 * 前端回调地址的参数
	 */
	private Map<String, String> params;
	
	/**
	 * 支付状态
	 */
	private String trxStatus;
	
	public IntelligentNetResultFrontCallbackResInfo(){
		
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getRedirectMehod() {
		return redirectMehod;
	}

	public void setRedirectMehod(String redirectMehod) {
		this.redirectMehod = redirectMehod;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}

	public Long getBizType() {
		return bizType;
	}

	public void setBizType(Long bizType) {
		this.bizType = bizType;
	}

	public String getBizOrderNo() {
		return bizOrderNo;
	}

	public void setBizOrderNo(String bizOrderNo) {
		this.bizOrderNo = bizOrderNo;
	}

	public Amount getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Amount orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Amount getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Amount realAmount) {
		this.realAmount = realAmount;
	}

	public PaymentOrderStatusEnum getState() {
		return state;
	}

	public void setState(PaymentOrderStatusEnum state) {
		this.state = state;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	
	
}
