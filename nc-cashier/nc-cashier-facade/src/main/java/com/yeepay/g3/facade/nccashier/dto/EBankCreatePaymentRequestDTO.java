package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * pc网银确认支付下单请求入参实体类
 * 
 * @author duangduang
 * @since  2016-11-08
 */
public class EBankCreatePaymentRequestDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 客户ID
	 */
	private String clientId;
	
	/**
	 * 网银账户类型
	 */
	private String eBankAccountType;
	
	/**
	 * 订单请求ID
	 */
	private Long paymentRequestId;
	
	/**
	 * 支付银行编号
	 */
	private String bankId;
	
	/**
	 * 支付请求ID
	 */
	private Long paymentRecordId;
	
	private String token;
	
	/**
	 * 卡种
	 */
	private String cardType;

	/**
	 * 网银支付是否为直连
	 */
	private boolean directEbankPay;


	/**
	 * 支付场景   支持充值支付
	 */
	private String payScene;
	
	private String netPayerIp;

	public String getPayScene() {
		return payScene;
	}

	public void setPayScene(String payScene) {
		this.payScene = payScene;
	}

	public EBankCreatePaymentRequestDTO(){
		
	}

	public Long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(Long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public String geteBankAccountType() {
		return eBankAccountType;
	}

	public void seteBankAccountType(String eBankAccountType) {
		this.eBankAccountType = eBankAccountType;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public Long getPaymentRecordId() {
		return paymentRecordId;
	}

	public void setPaymentRecordId(Long paymentRecordId) {
		this.paymentRecordId = paymentRecordId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public boolean isDirectEbankPay() {
		return directEbankPay;
	}

	public void setDirectEbankPay(boolean directEbankPay) {
		this.directEbankPay = directEbankPay;
	}

	public String getNetPayerIp() {
		return netPayerIp;
	}

	public void setNetPayerIp(String netPayerIp) {
		this.netPayerIp = netPayerIp;
	}
	
}
