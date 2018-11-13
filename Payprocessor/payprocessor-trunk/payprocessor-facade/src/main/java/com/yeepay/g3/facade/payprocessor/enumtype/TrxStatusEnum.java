package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos. 支付子表状态值
 * @createDate 2016/11/8.
 */
public enum TrxStatusEnum {

	DOING("处理中"),
	/**
	 * 支付成功:接收到NCPAY或FE回调
	 */
	SUCCESS("支付成功"),
	/**
	 * 支付失败:NCPAY错误
	 */
	FAILUER("支付失败"),
	/**
	 * 冲正:订单处理器过期、失败、或sopay主动发起
	 */
	REVERSE("冲正");

	private String desc;

	TrxStatusEnum(String desc) {
		this.desc = desc;
	}

	public static TrxStatusEnum getTrxStatus(String trxStatus) {
		if (StringUtils.isBlank(trxStatus))
			return null;
		try {
			return TrxStatusEnum.valueOf(trxStatus);
		} catch (Throwable th) {
			return null;
		}
	}

	public String getDesc() {
		return desc;
	}
}
