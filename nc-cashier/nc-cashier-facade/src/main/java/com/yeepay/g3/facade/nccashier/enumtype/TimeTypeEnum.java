package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * 日期枚举
 * 
 * @author duangduang
 * @date 2017-06-06
 */
public enum TimeTypeEnum {

	YEAR("年"),

	MONTH("月"),

	WEEK("星期"),

	DAY("天"),

	HOUR("小时"),

	MINUTE("分钟"),

	SECOND("秒");

	private String desc;

	private TimeTypeEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

}
