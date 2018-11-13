package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class CardBinResDTO implements Serializable {

	private static final long serialVersionUID = -3162826176721857814L;
	private String bankCode;
	private String bankName;
	private String cardType;
	private String cardlater4;

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

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getCardlater4() {
		return cardlater4;
	}

	public void setCardlater4(String cardlater4) {
		this.cardlater4 = cardlater4;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CardBinResDTO{");
		sb.append("bankCode=").append(bankCode);
		sb.append(", bankName=").append(bankName);
		sb.append(", cardType=").append(cardType);
		sb.append("}");
		return sb.toString();
	}
}
