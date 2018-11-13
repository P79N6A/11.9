package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

public class CardItemNecessary implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private boolean needIdNo;
	
	private boolean needBankPWD;
	
	private boolean needOwner;
	
	private boolean needPhoneNo;
	
	private boolean needAvlidDate;
	
	private boolean needCvv;
	
	private boolean merchantSamePersonConf;
	
	public CardItemNecessary(){
		
	}

	public boolean isNeedIdNo() {
		return needIdNo;
	}

	public void setNeedIdNo(boolean needIdNo) {
		this.needIdNo = needIdNo;
	}

	public boolean isNeedBankPWD() {
		return needBankPWD;
	}

	public void setNeedBankPWD(boolean needBankPWD) {
		this.needBankPWD = needBankPWD;
	}

	public boolean isNeedOwner() {
		return needOwner;
	}

	public void setNeedOwner(boolean needOwner) {
		this.needOwner = needOwner;
	}

	public boolean isNeedPhoneNo() {
		return needPhoneNo;
	}

	public void setNeedPhoneNo(boolean needPhoneNo) {
		this.needPhoneNo = needPhoneNo;
	}

	public boolean isNeedAvlidDate() {
		return needAvlidDate;
	}

	public void setNeedAvlidDate(boolean needAvlidDate) {
		this.needAvlidDate = needAvlidDate;
	}

	public boolean isNeedCvv() {
		return needCvv;
	}

	public void setNeedCvv(boolean needCvv) {
		this.needCvv = needCvv;
	}

	public boolean isMerchantSamePersonConf() {
		return merchantSamePersonConf;
	}

	public void setMerchantSamePersonConf(boolean merchantSamePersonConf) {
		this.merchantSamePersonConf = merchantSamePersonConf;
	}

	@Override
	public String toString() {
		return "CardItemNecessary [needIdNo=" + needIdNo 
				+ ", needBankPWD=" + needBankPWD 
				+ ", needOwner=" + needOwner
				+ ", needPhoneNo=" + needPhoneNo 
				+ ", needAvlidDate=" + needAvlidDate
				+ ", needCvv=" + needCvv 
				+ ", merchantSamePersonConf=" + merchantSamePersonConf + "]";
	}

}
