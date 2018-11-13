package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 验证要素是否需要
 */
public class NeedSurportDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Boolean cardnoIsNeed = false;//卡号是否必填
	private Boolean ownerIsNeed = false;//持卡人姓名是否必填
	private Boolean idnoIsNeed = false;//证件号是否必填
	private Boolean phoneNoIsNeed = false;//银行预留手机号是否必填
	private Boolean ypMobileIsNeed = false;//易宝预留手机号是否必填
	private Boolean avlidDateIsNeed = false;//有效期是否必填
	private Boolean cvvIsNeed = false;//cvv是否必填
	private Boolean idCardTypeIsNeed = false;//证件类型是否必填
	private Boolean bankPWDIsNeed = false;//取款密码是否必填
	public NeedSurportDTO() {
		super();
	}
	public Boolean getCardnoIsNeed() {
		return cardnoIsNeed;
	}
	public void setCardnoIsNeed(Boolean cardnoIsNeed) {
		this.cardnoIsNeed = cardnoIsNeed;
	}
	public Boolean getOwnerIsNeed() {
		return ownerIsNeed;
	}
	public void setOwnerIsNeed(Boolean ownerIsNeed) {
		this.ownerIsNeed = ownerIsNeed;
	}
	public Boolean getIdnoIsNeed() {
		return idnoIsNeed;
	}
	public void setIdnoIsNeed(Boolean idnoIsNeed) {
		this.idnoIsNeed = idnoIsNeed;
	}
	public Boolean getPhoneNoIsNeed() {
		return phoneNoIsNeed;
	}
	public void setPhoneNoIsNeed(Boolean phoneNoIsNeed) {
		this.phoneNoIsNeed = phoneNoIsNeed;
	}
	public Boolean getYpMobileIsNeed() {
		return ypMobileIsNeed;
	}
	public void setYpMobileIsNeed(Boolean ypMobileIsNeed) {
		this.ypMobileIsNeed = ypMobileIsNeed;
	}
	public Boolean getAvlidDateIsNeed() {
		return avlidDateIsNeed;
	}
	public void setAvlidDateIsNeed(Boolean avlidDateIsNeed) {
		this.avlidDateIsNeed = avlidDateIsNeed;
	}
	public Boolean getCvvIsNeed() {
		return cvvIsNeed;
	}
	public void setCvvIsNeed(Boolean cvvIsNeed) {
		this.cvvIsNeed = cvvIsNeed;
	}
	public Boolean getIdCardTypeIsNeed() {
		return idCardTypeIsNeed;
	}
	public void setIdCardTypeIsNeed(Boolean idCardTypeIsNeed) {
		this.idCardTypeIsNeed = idCardTypeIsNeed;
	}
	public Boolean getBankPWDIsNeed() {
		return bankPWDIsNeed;
	}
	public void setBankPWDIsNeed(Boolean bankPWDIsNeed) {
		this.bankPWDIsNeed = bankPWDIsNeed;
	}
	
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
}
