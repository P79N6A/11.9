package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

public class InstallmentPeriodAndFeeInfoVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 期数
	 */
	private String period;
	
	/**
	 * 期数对应的每期还款额
	 */
	private String feeAfterSubsidy;
	
	/**
	 * 每期还款额
	 */
	private String firstPayment;
	
	public InstallmentPeriodAndFeeInfoVO(){
		
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getFeeAfterSubsidy() {
		return feeAfterSubsidy;
	}

	public void setFeeAfterSubsidy(String feeAfterSubsidy) {
		this.feeAfterSubsidy = feeAfterSubsidy;
	}

	public String getFirstPayment() {
		return firstPayment;
	}

	public void setFirstPayment(String firstPayment) {
		this.firstPayment = firstPayment;
	}
	
	

}
