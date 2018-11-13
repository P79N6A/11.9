package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 银行信息
 * 
 * @author duangduang
 *
 */
public class BankInfoDTO implements Serializable {

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
	 * 最低限额 -- 银行卡分期业务中，银行有最低限额属性
	 */
	private String minLimit;

	public BankInfoDTO() {

	}

	public BankInfoDTO(String bankCode, String bankName) {
		setBankCode(bankCode);
		setBankName(bankName);
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

	public String getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(String minLimit) {
		this.minLimit = minLimit;
	}

	@Override
	public String toString() {
		return "BankInfoDTO [bankCode=" + bankCode + ", bankName=" + bankName + ", minLimit=" + minLimit + "]";
	}

}
