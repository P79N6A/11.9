
package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author peile.fan 回调订单状态
 */
public enum CallBackStatusEnum {

	SUCCESS("支付成功"),

	DOING("处理中"),

	REVERSE("冲正");

	private String desc;

	CallBackStatusEnum(String desc) {
		this.desc = desc;
	}

	public static CallBackStatusEnum getTrxStatus(String trxStatus) {
		if (StringUtils.isBlank(trxStatus))
			return null;
		try {
			return CallBackStatusEnum.valueOf(trxStatus);
		} catch (Throwable th) {
			return null;
		}
	}

	public String getDesc() {
		return desc;
	}

}
