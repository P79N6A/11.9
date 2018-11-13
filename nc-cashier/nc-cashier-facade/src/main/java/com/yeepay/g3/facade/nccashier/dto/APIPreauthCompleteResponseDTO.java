package com.yeepay.g3.facade.nccashier.dto;

import java.math.BigDecimal;
import java.util.Date;

public class APIPreauthCompleteResponseDTO extends APIPreauthResponseDTO{

	private static final long serialVersionUID = 1L;
	
	private String completedOrderId;
	
	/**
	 * 银行通道编码
	 */
	private String frpCode;
	
	/**
	 * 银行订单号
	 */
	private String bankOrderNO;
	
	/**
	 * 银行交易流水号
	 */
	private String bankTrxId;
	
	private BigDecimal cost;
	
	/**
	 * 银行成功时间
	 */
	private Date bankPaySuccDate;
	
	public APIPreauthCompleteResponseDTO(){
		
	}

	public String getCompletedOrderId() {
		return completedOrderId;
	}

	public void setCompletedOrderId(String completedOrderId) {
		this.completedOrderId = completedOrderId;
	}
	
	
	public String getFrpCode() {
		return frpCode;
	}

	public void setFrpCode(String frpCode) {
		this.frpCode = frpCode;
	}
	

	public String getBankOrderNO() {
		return bankOrderNO;
	}

	public void setBankOrderNO(String bankOrderNO) {
		this.bankOrderNO = bankOrderNO;
	}
	
	public String getBankTrxId() {
		return bankTrxId;
	}

	public void setBankTrxId(String bankTrxId) {
		this.bankTrxId = bankTrxId;
	}
	
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public Date getBankPaySuccDate() {
		return bankPaySuccDate;
	}

	public void setBankPaySuccDate(Date bankPaySuccDate) {
		this.bankPaySuccDate = bankPaySuccDate;
	}

	@Override
	public String toString() {
		return "APIPreauthCancelResponseDTO [code=" + getCode() 
				+ ", message=" + getMessage() 
				+ ", merchantNo=" + getMerchantNo() 
				+ ", token=" + getToken() 
				+ ", payOrderId=" + getPayOrderId() 
				+ ", status=" + getStatus()
				+ ", completedOrderId=" + getCompletedOrderId()
				+ ", frpCode=" + frpCode
				+ ", bankOrderNO=" + bankOrderNO
				+ ", bankTrxId=" + bankTrxId
				+ ", cost=" + cost
				+ ", bankPaySuccDate=" + bankPaySuccDate
				+ "]";
	}
	

}
