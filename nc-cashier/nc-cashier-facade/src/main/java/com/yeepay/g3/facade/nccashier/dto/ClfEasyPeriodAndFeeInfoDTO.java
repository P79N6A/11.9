package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class ClfEasyPeriodAndFeeInfoDTO extends InstallmentPeriodAndRateInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 首期应还金额
	 */
	private String firstPayment;

	/**
	 * 每期应该金额
	 */
	private String terminalPayment;

	public ClfEasyPeriodAndFeeInfoDTO() {

	}

	public String getFirstPayment() {
		return firstPayment;
	}

	public void setFirstPayment(String firstPayment) {
		this.firstPayment = firstPayment;
	}

	public String getTerminalPayment() {
		return terminalPayment;
	}

	public void setTerminalPayment(String terminalPayment) {
		this.terminalPayment = terminalPayment;
	}

}
