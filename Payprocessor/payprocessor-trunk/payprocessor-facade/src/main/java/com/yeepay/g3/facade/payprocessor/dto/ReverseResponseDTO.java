package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.PayCardType;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.ReversalStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public class ReverseResponseDTO extends BasicResponseDTO {
	
	private static final long serialVersionUID = 5320357407842275982L;

	/**
	 * 订单状态
	 */
	private PaymentStatusEnum payStatus;

	/***
	 * 冲正状态:(成功、失败)
	 */
	private ReversalStatusEnum reverseStatus;

	/**
	 * 支付订单号
	 */
	private String recordNo;

	/**
	 * 通知状态
	 */
	private OrderSystemStatusEnum orderStatus;

	/**
	 * 确认支付时间
	 */
	private Date confirmTime;

	/**
	 * 银行成功时间
	 */
	private Date bankSuccessDate;

	/**
	 * 银行子系统订单号
	 */
	private String bankOrderNo;

	/**
	 * 银行子系统流水号
	 */
	private String bankSeq;

	/**
	 * 银行交易流水号
	 */
	private String bankTrxId;

	/**
	 * 支付银行编号
	 */
	private String bankId;

	/**
	 * 通道编码
	 */
	private String frpCode;

	/**
	 * 成本
	 */
	private BigDecimal cost;

	/**
	 * 用户手续费
	 */
	private BigDecimal userFee;

	/**
	 * 支付卡种
	 */
	private PayCardType payCardType;

	/**
	 * 绑卡信息ID
	 */
	private String bindCardinfoId;

	/**
	 * 卡号
	 */
	private String accountNo;

	/**
	 * 姓名
	 */
	private String accountName;

	/**
	 * 手机号
	 */
	private String mobile;

	public OrderSystemStatusEnum getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderSystemStatusEnum orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getBankSuccessDate() {
		return bankSuccessDate;
	}

	public void setBankSuccessDate(Date bankSuccessDate) {
		this.bankSuccessDate = bankSuccessDate;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getBankSeq() {
		return bankSeq;
	}

	public void setBankSeq(String bankSeq) {
		this.bankSeq = bankSeq;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getFrpCode() {
		return frpCode;
	}

	public void setFrpCode(String frpCode) {
		this.frpCode = frpCode;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getUserFee() {
		return userFee;
	}

	public void setUserFee(BigDecimal userFee) {
		this.userFee = userFee;
	}

	public PayCardType getPayCardType() {
		return payCardType;
	}

	public void setPayCardType(PayCardType payCardType) {
		this.payCardType = payCardType;
	}

	public String getBindCardinfoId() {
		return bindCardinfoId;
	}

	public void setBindCardinfoId(String bindCardinfoId) {
		this.bindCardinfoId = bindCardinfoId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBankTrxId() {
		return bankTrxId;
	}

	public void setBankTrxId(String bankTrxId) {
		this.bankTrxId = bankTrxId;
	}

	public PaymentStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PaymentStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public ReversalStatusEnum getReverseStatus() {
		return reverseStatus;
	}

	public void setReverseStatus(ReversalStatusEnum reverseStatus) {
		this.reverseStatus = reverseStatus;
	}

	public String getRecordNo() {
		return recordNo;
	}

	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
}
