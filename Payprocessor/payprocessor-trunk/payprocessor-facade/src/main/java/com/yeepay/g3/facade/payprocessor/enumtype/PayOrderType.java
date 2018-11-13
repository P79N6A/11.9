package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public enum PayOrderType {

	SALE("消费-无卡"),

	PASSIVESCAN("被扫支付"),

	ACTIVESCAN("主扫支付"),

	JSAPI("微信公众号支付"),

	H5APP("h5调用支付"),

	APP("APP支付接口"),

	NET("网银"),

	ONLINE("分期线上"),

	OFFLINE("分期线下"),
	
	SDK("SDK支付"),
	
	ACCOUNT("账户支付"),
	
	LN("支付宝生活号"),

	BK_CFL("银行卡分期"),

	PREAUTH_RE("预授权请求"),

	PREAUTH_CM("预授权完成"),

	PREAUTH_CL("预授权撤销"),

	PREAUTH_CC("预授权完成撤销"),
	
	MEMBER_PAY("个人会员支付"),
    
    GUAR_CFL("担保分期"),

	MINI_PROGRAM("微信小程序"),

	CFL_EASY("分期易");

	/**
	 * 描述
	 */
	private String desc;

	public String getDesc() {
		return desc;
	}

	PayOrderType(String description) {
		this.desc = description;
	}

	public static PayOrderType getOrderType(String orderType) {
		if (StringUtils.isBlank(orderType))
			return null;
		try {
			return PayOrderType.valueOf(orderType);
		} catch (Throwable th) {
			return null;
		}
	}
}
