package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.facade.nccashier.enumtype.PayOrderStatusEnum;

public class PaymentResultDTO implements Serializable {

	private static final long serialVersionUID = -8993316089465518128L;
	private String merchantNo;
	private long bizType;
	private String bizOrderNum;
	private String payOrderNum;
	private long payCompleteDate;
	private Amount payAmount;
	private PayOrderStatusEnum payStatus;
	private String errorCode;
	private String errorMsg;
	private String accountNo;
	/**
	 * 银行订单号
	 */
	private String bankOrderNo;

	/**
	 * 银行渠道编码
	 */
	private String frpCode;

	/**
	 * 成本
	 */
	private Amount cost;
	/**
	 * 银行子系统订单号
	 */
	private String tradeSerialNo;

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public long getBizType() {
		return bizType;
	}

	public void setBizType(long bizType) {
		this.bizType = bizType;
	}

	public String getBizOrderNum() {
		return bizOrderNum;
	}

	public void setBizOrderNum(String bizOrderNum) {
		this.bizOrderNum = bizOrderNum;
	}

	public String getPayOrderNum() {
		return payOrderNum;
	}

	public void setPayOrderNum(String payOrderNum) {
		this.payOrderNum = payOrderNum;
	}

	public long getPayCompleteDate() {
		return payCompleteDate;
	}

	public void setPayCompleteDate(long payCompleteDate) {
		this.payCompleteDate = payCompleteDate;
	}

	public Amount getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Amount payAmount) {
		this.payAmount = payAmount;
	}

	public PayOrderStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayOrderStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getFrpCode() {
		return frpCode;
	}

	public void setFrpCode(String frpCode) {
		this.frpCode = frpCode;
	}

	public Amount getCost() {
		return cost;
	}

	public void setCost(Amount cost) {
		this.cost = cost;
	}


	public String getTradeSerialNo() {
		return tradeSerialNo;
	}

	public void setTradeSerialNo(String tradeSerialNo) {
		this.tradeSerialNo = tradeSerialNo;
	}

}
