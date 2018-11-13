package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @Description 商户绑卡请求VO
 * @author yangmin.peng
 * @since 2017年8月22日下午4:58:06
 */
public class BindCardBinInfoResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cardType;
	private String bankName;
	private String bankCode;
	private String cardlater;

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

	public String getCardlater() {
		return cardlater;
	}

	public void setCardlater(String cardlater) {
		this.cardlater = cardlater;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
