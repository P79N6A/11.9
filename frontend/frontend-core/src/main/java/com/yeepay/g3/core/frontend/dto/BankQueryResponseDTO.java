package com.yeepay.g3.core.frontend.dto;

import com.yeepay.g3.facade.frontend.dto.BasicResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class BankQueryResponseDTO extends BasicResponseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * 微信订单号
     */
    private String transactionId;
    
	 /**
    * 银行订单号（第三方银行的订单号）
    */
     private String bankTradeId;
    
    /**
     * 状态
     */
    private PayStatusEnum payStatus;
    
    /**
     * 支付银行
     */
    private String payBank;

    /**
     * 支付类型
     */
    private PayBankcardType payBankcardType;
    
    /**
	 * 支付金额
	 */
	private BigDecimal totalAmount;
    
    /**
	 * 支付成本
	 */
	private BigDecimal bankTotalCost;
	
	/**
     * 银行支付成功时间 
     */
    private Date bankSuccessTime;
    
    /**
     * 系统支付成功时间
     */
    private Date paySuccessTime;

	private String payInterface;

	/**
	 * added by zhijun.wang
	 * 2017-07-26
	 */
	private String openId;

	private Map<String, String> extParam;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public PayBankcardType getPayBankcardType() {
		return payBankcardType;
	}

	public void setPayBankcardType(PayBankcardType payBankcardType) {
		this.payBankcardType = payBankcardType;
	}
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getBankTotalCost() {
		return bankTotalCost;
	}

	public void setBankTotalCost(BigDecimal bankTotalCost) {
		this.bankTotalCost = bankTotalCost;
	}

	public String getBankTradeId() {
		return bankTradeId;
	}

	public void setBankTradeId(String bankTradeId) {
		this.bankTradeId = bankTradeId;
	}

	public Date getBankSuccessTime() {
		return bankSuccessTime;
	}

	public void setBankSuccessTime(Date bankSuccessTime) {
		this.bankSuccessTime = bankSuccessTime;
	}

	public Date getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(Date paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}
}
