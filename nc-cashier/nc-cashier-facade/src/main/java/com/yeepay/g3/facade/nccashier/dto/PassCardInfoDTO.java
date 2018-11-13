package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * Created by xiewei on 15-10-16.
 */
public class PassCardInfoDTO implements Serializable {

	private static final long serialVersionUID = 1876490309001633248L;
	
	/**
	 * 持卡人姓名
	 */
	private String owner;
	
	/**
	 * 手机号
	 */
	private String phone;
	
	/**
	 * 身份证号
	 */
	private String idNo;
	
	/**
	 * 卡号
	 */
	private String cardNo;
	
	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 卡类型（借记卡 or 贷记卡）
	 */
	private String cardType;
	
	/**
	 * 身份证类型
	 */
	private String idType;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("PassCardInfoDTO{");
		sb.append("owner='").append(HiddenCode.hiddenName(owner)).append('\'');
		sb.append(", idNo='").append(HiddenCode.hiddenIdentityCode(idNo)).append('\'');
		sb.append(", cardNo='").append(HiddenCode.hiddenBankCardNO(cardNo)).append('\'');
		sb.append(", bankCode='").append(bankCode).append('\'');
		sb.append(", cardType='").append(cardType).append('\'');
		sb.append(", idType='").append(idType).append('\'');
		sb.append('}');
		return sb.toString();
	}


	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getIdNo() {
		return idNo;
	}


	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}


	public String getCardNo() {
		return cardNo;
	}


	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
}
