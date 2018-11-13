package com.yeepay.g3.core.nccashier.vo;


import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.dto.InstallmentPeriodAndRateInfoDTO;

public class InstallmentRateInfo {

	private int period; //期数
	
	private String rate; // 费率
	
	public InstallmentRateInfo(){
		
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	public InstallmentPeriodAndRateInfoDTO transferToRateInfoDTO(){
		InstallmentPeriodAndRateInfoDTO infoDTO = new InstallmentPeriodAndRateInfoDTO();
		infoDTO.setPeriod(String.valueOf(period));
		infoDTO.setRate(rate);
		return infoDTO;
	}
	
	public BigDecimal formatRate(){
		BigDecimal rateBigDecimal = new BigDecimal(rate);
		return rateBigDecimal.divide(new BigDecimal(100));
	}
	
}
