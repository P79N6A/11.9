package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;


public class NeedValidatesDTO implements Serializable{
	private static final long serialVersionUID = -458018466396125100L;
	private boolean needIdno;
	private boolean needName;
	private boolean needMobile;
	private boolean needPass;
	private boolean needCVV;
	private boolean needValid;
	private boolean needSms;
	private BankCardDTO bankCard;
	private boolean isPreRouter;
	
	/**
	 * 商户是否是同人配置
	 */
	private boolean merchantSamePersonConfig;

	public boolean isNeedIdno() {
		return needIdno;
	}

	public void setNeedIdno(boolean needIdno) {
		this.needIdno = needIdno;
	}

	public boolean isNeedName() {
		return needName;
	}

	public void setNeedName(boolean needName) {
		this.needName = needName;
	}

	public boolean isNeedMobile() {
		return needMobile;
	}

	public void setNeedMobile(boolean needMobile) {
		this.needMobile = needMobile;
	}

	public boolean isNeedPass() {
		return needPass;
	}

	public void setNeedPass(boolean needPass) {
		this.needPass = needPass;
	}

	public boolean isNeedCVV() {
		return needCVV;
	}

	public void setNeedCVV(boolean needCVV) {
		this.needCVV = needCVV;
	}

	public boolean isNeedValid() {
		return needValid;
	}

	public void setNeedValid(boolean needValid) {
		this.needValid = needValid;
	}

	public boolean isNeedSms() {
		return needSms;
	}

	public void setNeedSms(boolean needSms) {
		this.needSms = needSms;
	}

	public BankCardDTO getBankCard() {
		return bankCard;
	}

	public void setBankCard(BankCardDTO bankCard) {
		this.bankCard = bankCard;
	}
	
	public boolean isMerchantSamePersonConfig() {
		return merchantSamePersonConfig;
	}

	public void setMerchantSamePersonConfig(boolean merchantSamePersonConfig) {
		this.merchantSamePersonConfig = merchantSamePersonConfig;
	}

	public boolean isPreRouter() {
		return isPreRouter;
	}

	public void setPreRouter(boolean preRouter) {
		isPreRouter = preRouter;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("NeedValidatesDTO{");
		sb.append("needIdno=").append(needIdno);
		sb.append(", needName=").append(needName);
		sb.append(", needMobile=").append(needMobile);
		sb.append(", needPass=").append(needPass);
		sb.append(", needCVV=").append(needCVV);
		sb.append(", needValid=").append(needValid);
		sb.append(", needSms=").append(needSms);
		sb.append(", bankCard=").append(bankCard);
		sb.append(", isPreRouter=").append(isPreRouter);
		sb.append('}');
		return sb.toString();
	}
}
