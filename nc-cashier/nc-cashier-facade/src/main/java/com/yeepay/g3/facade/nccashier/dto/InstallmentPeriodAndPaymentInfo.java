package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class InstallmentPeriodAndPaymentInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 首期还款额
	 */
	private BigDecimal firstPayment;

	/**
	 * 每期还款额
	 */
	private BigDecimal terminalPayment;

	/**
	 * 期数
	 */
	private String period;

	public InstallmentPeriodAndPaymentInfo() {

	}

	public BigDecimal getFirstPayment() {
		return firstPayment;
	}

	public void setFirstPayment(BigDecimal firstPayment) {
		this.firstPayment = firstPayment;
	}

	public BigDecimal getTerminalPayment() {
		return terminalPayment;
	}

	public void setTerminalPayment(BigDecimal terminalPayment) {
		this.terminalPayment = terminalPayment;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Override
	public String toString() {
		return "InstallmentPeriodAndPaymentInfo [" 
				+ ", firstPayment=" + firstPayment 
				+ ", terminalPayment=" + terminalPayment 
				+ ", period=" + period + "]";
	}

}
