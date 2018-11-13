package com.yeepay.g3.facade.nccashier.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 前置收银台交易结果查询返回实体
 * 
 * @author duangduang
 *
 */
public class APIResultQueryResponseDTO extends APIBasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 交易结果
	 */
	private String status;

	/**
	 * 操作类型
	 */
	private String operationType;
	
	/**
	 * 支付订单号
	 */
	private String payOrderId;
	
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
	
	/**
	 * 银行成本
	 */
	private BigDecimal cost;
	
	/**
	 * 银行成功时间
	 */
	private Date bankPaySuccDate;

	public APIResultQueryResponseDTO() {

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
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
		return "APIPreauthCancelResponseDTO [" 
				+ "code=" + getCode() 
				+ ", message=" + getMessage() 
				+ ", merchantNo=" + getMerchantNo() 
				+ ", token=" + getToken() 
				+ ", status=" + status 
				+ ", operationType=" + operationType
				+ ", payOrderId=" + payOrderId
				+ ", frpCode=" + getFrpCode()
				+ ", bankOrderNO=" + getBankOrderNO() 
				+ ", bankTrxId=" + getBankTrxId()
				+ ", cost=" + getCost()
				+ ", =bankPaySuccDate=" + getBankPaySuccDate()
				+ "]";
	}
}
