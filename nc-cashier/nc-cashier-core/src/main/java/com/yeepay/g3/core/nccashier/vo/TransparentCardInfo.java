/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.IdCardTypeEnum;

/**
 * @author zhen.tan
 *  透传卡信息
 */
public class TransparentCardInfo {

	/**
	 * 银行编码
	 */
	private String bankCode;
	/**
	 * 持卡人姓名
	 */
	private String owner;

	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 卡类型
	 */
	private CardTypeEnum cardType;
	/**
	 * 证件类型
	 */
	private IdCardTypeEnum idcardType;
	/**
	 * 证件号
	 */
	private String idcard;
	/**
	 * 手机号
	 */
	private String phoneNo;
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public CardTypeEnum getCardType() {
		return cardType;
	}
	public void setCardType(CardTypeEnum cardType) {
		this.cardType = cardType;
	}
	public IdCardTypeEnum getIdcardType() {
		return idcardType;
	}
	public void setIdcardType(IdCardTypeEnum idcardType) {
		this.idcardType = idcardType;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
}
