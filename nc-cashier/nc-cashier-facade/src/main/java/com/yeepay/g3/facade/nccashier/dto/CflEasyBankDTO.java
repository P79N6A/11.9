package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.List;

public class CflEasyBankDTO implements Serializable {

	private static final long serialVersionUID = -8028283016719683118L;
	
	/**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;
	
    private List<InstallmentPeriodAndPaymentInfo> periodAndPaymentInfos;

	public CflEasyBankDTO() {
	}

	public CflEasyBankDTO(String bankCode, String bankName) {
		setBankCode(bankCode);
		setBankName(bankName);
	}
	
	public List<InstallmentPeriodAndPaymentInfo> getPeriodAndPaymentInfos() {
		return periodAndPaymentInfos;
	}

	public void setPeriodAndPaymentInfos(List<InstallmentPeriodAndPaymentInfo> periodAndPaymentInfos) {
		this.periodAndPaymentInfos = periodAndPaymentInfos;
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


	@Override
	public String toString() {
		return "CflEasyBankDTO{" 
				+ "bankCode='" + getBankCode() + '\'' 
				+ "bankName='" + getBankName() + '\'' 
				+ ", periodAndPaymentInfos='" + periodAndPaymentInfos 
				+ '}';
	}

}
