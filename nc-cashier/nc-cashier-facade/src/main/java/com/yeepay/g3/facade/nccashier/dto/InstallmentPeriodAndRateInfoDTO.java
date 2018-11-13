package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;


/**
 * 分期对应的期数及费率信息
 * 
 * @author duangduang
 *
 */
public class InstallmentPeriodAndRateInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 期数
	 */
	private String period;

	/**
	 * 费率
	 */
	private String rate;

	public InstallmentPeriodAndRateInfoDTO() {

	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "InstallmentPeriodAndRateInfoDTO [period=" + period + ", rate=" + rate + "]";
	}

}
