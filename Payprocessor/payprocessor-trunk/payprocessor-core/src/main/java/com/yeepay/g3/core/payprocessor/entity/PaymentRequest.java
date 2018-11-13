package com.yeepay.g3.core.payprocessor.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息表,主表
 */
public class PaymentRequest {

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 商户编号
	 */
	private String customerNo;

	/**
	 * 商户名称
	 */
	private String customerName;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;

	/**
	 * 订单方订单号+请求系统为唯一约束
	 */
	private String orderNo;

	/**
	 * 系统唯一码
	 */
	private String dealUniqueSerialNo;

	/**
	 * 支付成功的订单号,payrecord表主键,唯一索引
	 */
	private String recordNo;

	/**
	 * 订单方
	 */
	private String orderSystem;

	/**
	 * 交易金额
	 */
	private BigDecimal amount;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 支付订单状态
	 */
	private String payStatus;

	/**
	 * 订单系统状态
	 */
	private String orderSystemStatus;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 支付时间
	 */
	private Date confirmTime;

	/**
	 * 支付类型,组合支付、拆单支付等,预留字段
	 */
	private String payType;

	/**
	 * 过期时间
	 */
	private Date expireDate;

	/**
	 * 预授权完成的金额
	 */
	private BigDecimal preAuthAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}

	public String getOrderSystem() {
		return orderSystem;
	}

	public void setOrderSystem(String orderSystem) {
		this.orderSystem = orderSystem;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getOrderSystemStatus() {
		return orderSystemStatus;
	}

	public void setOrderSystemStatus(String orderSystemStatus) {
		this.orderSystemStatus = orderSystemStatus;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isExpired() {
		long currentTime = new Date().getTime();
		if (expireDate != null) {
			return expireDate.getTime() < currentTime;
		}
		return true;
	}

	public BigDecimal getPreAuthAmount() {
		return preAuthAmount;
	}

	public void setPreAuthAmount(BigDecimal preAuthAmount) {
		this.preAuthAmount = preAuthAmount;
	}
}