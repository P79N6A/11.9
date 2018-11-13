package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;

public class BindNeedItemInfo {
	
	private SmsSendTypeEnum smsSendTypeEnum;
	private int needItem = 0;
	
	public BindNeedItemInfo(){
		
	}
	
	public BindNeedItemInfo(SmsSendTypeEnum smsSendTypeEnum, int needItem){
		setNeedItem(needItem);
		setSmsSendTypeEnum(smsSendTypeEnum);
	}

	public SmsSendTypeEnum getSmsSendTypeEnum() {
		return smsSendTypeEnum;
	}

	public void setSmsSendTypeEnum(SmsSendTypeEnum smsSendTypeEnum) {
		this.smsSendTypeEnum = smsSendTypeEnum;
	}

	public int getNeedItem() {
		return needItem;
	}

	public void setNeedItem(int needItem) {
		this.needItem = needItem;
	}
	
}
