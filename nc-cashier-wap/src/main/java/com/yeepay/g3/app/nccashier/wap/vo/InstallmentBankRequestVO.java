package com.yeepay.g3.app.nccashier.wap.vo;


public class InstallmentBankRequestVO extends BaseVO{

	private static final long serialVersionUID = 1L;
	
	private String bankCode;
	
	private String period;
	
	public InstallmentBankRequestVO(){
		
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "InstallmentBankRequestVO [bankCode=" + bankCode + ", period=" + period + ", token=" + token + "]";
	}
}
