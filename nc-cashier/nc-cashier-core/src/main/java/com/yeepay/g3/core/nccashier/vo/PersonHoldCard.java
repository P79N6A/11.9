package com.yeepay.g3.core.nccashier.vo;

public class PersonHoldCard {
	
	private String phoneN0;
	
	private String owner;
	
	private String idno;
	
	private CardInfo card;

	public String getPhoneN0() {
		return phoneN0;
	}

	public void setPhoneN0(String phoneN0) {
		this.phoneN0 = phoneN0;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public CardInfo getCard() {
		return card;
	}

	public void setCard(CardInfo card) {
		this.card = card;
	}
	
	public boolean bankNotNull(){
		return card!=null && card.getBank()!=null;
	}
	
}
