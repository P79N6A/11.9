package com.yeepay.g3.core.nccashier.enumtype;

public enum PreauthPayTypeEnum {

	/**
	 * 首次预授权发起
	 */
	FIRST("首次预授权发起"),

	/**
	 * 二次预授权发起
	 */
	BIND("绑卡预授权发起"),
	
	
	COMPLETE("预授权完成"),
	
	CANCLE("预授权撤销"),
	
	COMPLETE_CANCEL("预授权完成撤销");

	private String desc;

	private PreauthPayTypeEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
