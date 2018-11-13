package com.yeepay.g3.facade.nccashier.dto;


import java.io.Serializable;
import java.util.List;

import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * 卡信息
 */
public class BankCardDTO implements Serializable {
	
	private static final long serialVersionUID = -6827917981712558732L;
	private CardTypeEnum cardtype;
	private String bankCode;
	private boolean needCheck = true;
	private String cardlater;
	private String bankName;
	private long bindid;
	private String bankImage;
	private QuataDTO bankQuata;
	private List<BankCardDTO> otherCards;
	private String cardno;
	private String owner;
	private String idno;
	private String phoneNo;
	private String ypMobile;
	
	/** 绑卡不可用原因 **/
	private String unusableCause;

	public QuataDTO getBankQuata() {
		return bankQuata;
	}


	public void setBankQuata(QuataDTO bankQuata) {
		this.bankQuata = bankQuata;
	}


	public String getBankImage() {
		return bankImage;
	}


	public List<BankCardDTO> getOtherCards() {
		return otherCards;
	}

	public void setOtherCards(List<BankCardDTO> otherCards) {
		this.otherCards = otherCards;
	}


	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
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


	public long getBindid() {
		return bindid;
	}

	public void setBindid(long bindid) {
		this.bindid = bindid;
	}

	public CardTypeEnum getCardtype() {
		return cardtype;
	}

	public void setCardtype(CardTypeEnum cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardlater() {
		return cardlater;
	}

	public void setCardlater(String cardlater) {
		this.cardlater = cardlater;
	}

	public boolean isNeedCheck() {
		return needCheck;
	}

	public void setNeedCheck(boolean needCheck) {
		this.needCheck = needCheck;
	}


	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankImage = bankCode + ".png";
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getYpMobile() {
		return ypMobile;
	}


	public void setYpMobile(String ypMobile) {
		this.ypMobile = ypMobile;
	}
	

	public String getUnusableCause() {
		return unusableCause;
	}


	public void setUnusableCause(String unusableCause) {
		this.unusableCause = unusableCause;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("BankCardDTO{");
		sb.append("owner='").append(HiddenCode.hiddenName(owner)).append('\'');
		sb.append(", idno='").append(HiddenCode.hiddenIdentityCode(idno)).append('\'');
		sb.append(", cardno='").append(HiddenCode.hiddenBankCardNO(cardno)).append('\'');
		sb.append(", phoneNo='").append(HiddenCode.hiddenMobile(phoneNo)).append('\'');
		sb.append(", bankCode='").append(bankCode).append('\'');
		sb.append(", bankName='").append(bankName).append('\'');
		sb.append(", bindid='").append(bindid).append('\'');
		sb.append(", cardtype='").append(cardtype).append('\'');
			sb.append(", otherCards'").append(otherCards).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
