package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

public class CardBinInfoResponseVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String cardType;
	
	private String bankName;
	
	private String bankCode;
	
	private String cardLater4;

	public CardBinInfoResponseVO() {

	}

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

	public String getCardLater4() {
		return cardLater4;
	}

	public void setCardLater4(String cardLater4) {
		this.cardLater4 = cardLater4;
	}

	@Override
	public String toString() {
		return "CardBinInfoResponseVO [cardType=" + cardType
				+ ", bankName=" + bankName 
				+ ", bankCode=" + bankCode
				+ ", cardLater4=" + cardLater4 + "]";
	}

}
