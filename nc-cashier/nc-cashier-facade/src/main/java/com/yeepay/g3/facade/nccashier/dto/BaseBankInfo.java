package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.enumtype.BankAccountTypeEnum;

/**
 * 基本银行信息
 * 
 * @author duangduang
 * @since 2016-11-10
 */
public class BaseBankInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 银行账户类型
	 */
	private BankAccountTypeEnum bankAccountType;

	/**
	 * 卡类型
	 */
	private String cardType;

	/**
	 * 是否大额
	 */
	private boolean largeAmount;

	public BaseBankInfo() {

	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public BankAccountTypeEnum getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(BankAccountTypeEnum bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public boolean isLargeAmount() {
		return largeAmount;
	}

	public void setLargeAmount(boolean largeAmount) {
		this.largeAmount = largeAmount;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
}
