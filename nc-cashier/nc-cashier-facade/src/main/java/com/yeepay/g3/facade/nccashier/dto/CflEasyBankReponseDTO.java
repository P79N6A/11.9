package com.yeepay.g3.facade.nccashier.dto;

import java.math.BigDecimal;
import java.util.List;

public class CflEasyBankReponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;

	private List<CflEasyBankDTO> cflEasyBankList;

	public CflEasyBankReponseDTO() {

	}

	public List<CflEasyBankDTO> getCflEasyBankList() {
		return cflEasyBankList;
	}

	public void setCflEasyBankList(List<CflEasyBankDTO> cflEasyBankList) {
		this.cflEasyBankList = cflEasyBankList;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	@Override
	public String toString() {
		return "CflEasyBankReponseDTO{" 
				+ "cflEasyBankList='" + cflEasyBankList 
				+ '\'' + ", returnCode='" + getReturnCode() 
				+ '\'' + ", returnMsg='" + getReturnMsg() 
				+ '\'' + ", orderAmount='" + orderAmount
				+ '}';
	}

}
