package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * 网银银行基本信息
 * 
 * @author duangduang
 * @since 2016-11-10
 */
public class EBankBaseVO {

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 通道类型（OD：仅贷，OC：仅借，DC：借贷）
	 */
	private String cardType;

	/**
	 * 银行账户类型 b2b/b2c
	 */
	private String bankAccountType;

	/**
	 * 是否需要客户ID（针对对公银行）
	 */
	private boolean needClient;

	public EBankBaseVO() {

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

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public boolean isNeedClient() {
		return needClient;
	}

	public void setNeedClient(boolean needClient) {
		this.needClient = needClient;
	}
	
}
