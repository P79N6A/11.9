package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;

import java.io.Serializable;

/**
 * 支持银行列表信息
 */
public class BankSupportDTO implements Serializable {
	private static final long serialVersionUID = -7858135350441489185L;
	private String bankName;
	private String bankCode;
	private CardTypeEnum banktype;
	private String bankImage;

	public String getBankImage() {
		return bankImage;
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
		this.bankImage=bankCode+".png";
		this.bankCode = bankCode;
	}

	public CardTypeEnum getBanktype() {
		return banktype;
	}

	public void setBanktype(CardTypeEnum banktype) {
		this.banktype = banktype;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BankSupportDTO that = (BankSupportDTO) o;

		if (bankName != null ? !bankName.equals(that.bankName) : that.bankName != null) return false;
		if (bankCode != null ? !bankCode.equals(that.bankCode) : that.bankCode != null) return false;
		if (banktype != that.banktype) return false;
		return !(bankImage != null ? !bankImage.equals(that.bankImage) : that.bankImage != null);

	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("BankSupportDTO{");
		sb.append("bankName='").append(this.bankName).append('\'');
		sb.append(", cardbankCode='").append(this.bankCode).append('\'');
		sb.append(", bankType='").append(this.banktype).append('\'');
		sb.append(", bankImage='").append(this.bankImage).append('\'');
		sb.append('}');
		return sb.toString();
	}

}