package com.yeepay.g3.core.nccashier.vo;

import java.math.BigDecimal;

/**
 * @title 银行卡分期商户补贴实体
 * @author duangduang
 *
 */
public class BusinessSubsidie {
	
	private BigDecimal amountSubsidy;
	
	private String rateSubsidy;

	public BigDecimal getAmountSubsidy() {
		return amountSubsidy;
	}

	public void setAmountSubsidy(BigDecimal amountSubsidy) {
		this.amountSubsidy = amountSubsidy;
	}

	public String getRateSubsidy() {
		return rateSubsidy;
	}

	public void setRateSubsidy(String rateSubsidy) {
		this.rateSubsidy = rateSubsidy;
	}
	
}
