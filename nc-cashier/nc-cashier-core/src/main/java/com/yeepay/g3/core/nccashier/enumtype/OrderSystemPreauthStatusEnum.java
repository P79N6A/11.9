package com.yeepay.g3.core.nccashier.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

public enum OrderSystemPreauthStatusEnum {

	/**
	 * 初始化,预授权下单
	 */
	WAITPREAUTH("预授权发起还未成功或未开始"),

	/**
	 * 预授权
	 */
	PREAUTH("预授权发起成功"),

	/**
	 * 预授权撤销
	 */
	PREAUTHREPEAL("预授权撤销成功"),

	/**
	 * 预授权完成
	 */
	PREAUTHCOMPLETE("预授权完成成功"),

	/**
	 * 预授权完成暂停 -- 预授权完成成功状态
	 */
	PREAUTHCOMPLETEPAUSE("预授权完成成功"),

	/**
	 * 预授权完成已入账
	 */
	COMPLETEACCOUNT("预授权完成成功，且过完成撤销的有效期");

	private String desc;

	private OrderSystemPreauthStatusEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 将字符串状态值转为枚举
	 * 
	 * @param status
	 * @return
	 */
	public static OrderSystemPreauthStatusEnum getStatus(String status) {
		if(StringUtils.isBlank(status)){
			return null;
		}
		try {
			return OrderSystemPreauthStatusEnum.valueOf(status);
		} catch (Throwable t) {
			return null;
		}
	}

}
