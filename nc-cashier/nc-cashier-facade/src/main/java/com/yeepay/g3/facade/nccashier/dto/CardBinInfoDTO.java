package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;

import java.io.Serializable;

/**
 * Created by xiewei on 15-10-16.
 */
public class CardBinInfoDTO extends BasicResponseDTO implements Serializable {
	/**
	 * 
	 */
	private String name;
	private String cardno;
	private String bank;
	private CardTypeEnum cardTypeEnum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public CardTypeEnum getCardTypeEnum() {
		return cardTypeEnum;
	}

	public void setCardTypeEnum(CardTypeEnum cardTypeEnum) {
		this.cardTypeEnum = cardTypeEnum;
	}
}
