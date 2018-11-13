package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 
 * @Description 调用NOP查询卡BIN信息DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPCardBinResponseDTO extends BasicResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;
	private String cardType;
	private String bankName;
	private String bankCode;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPCardBinResponseDTO{");
		sb.append("cardType='").append(cardType).append('\'');
		sb.append(", bankName='").append(bankName).append('\'');
		sb.append(", bankCode='").append(bankCode).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
