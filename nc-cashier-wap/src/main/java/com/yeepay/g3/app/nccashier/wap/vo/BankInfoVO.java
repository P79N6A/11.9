package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

/**
 * 银行信息VO
 * 
 * @author duangduang
 *
 */
public class BankInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 银行名称
	 */
	private String bankName;
	
	private String cardType;
	
	private String cardLater4;

	/**
	 * 可用状态，值：unusable、usable
	 */
	private String status;

	/**
	 * 备注，可用来备注不可用原因
	 */
	private String remark;
	
	/**
	 * 手续费收取方式
	 */
	private String rateWay;
	
	public String getRateWay() {
		return rateWay;
	}

	public void setRateWay(String rateWay) {
		this.rateWay = rateWay;
	}

	public BankInfoVO() {

	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardLater4() {
		return cardLater4;
	}

	public void setCardLater4(String cardLater4) {
		this.cardLater4 = cardLater4;
	}

	
	@Override
	public String toString() {
		return "BankInfoVO [bankCode=" + bankCode 
				+ ", bankName=" + bankName 
				+ ", cardType=" + cardType
				+ ", cardLater4=" + cardLater4 
				+ ", status=" + status 
				+ ", remark=" + remark 
				+ ", rateWay=" + rateWay
				+ "]";
	}

}
