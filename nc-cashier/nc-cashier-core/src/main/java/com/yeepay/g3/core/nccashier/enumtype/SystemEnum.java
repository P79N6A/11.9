package com.yeepay.g3.core.nccashier.enumtype;

/**
 * 
 * @author：peile.fan
 * @since：2016年5月19日 下午6:27:16
 * @version:
 */
public enum SystemEnum {

	NCCWH("cwh-hessian"), NCAUTH("nc-auth-hessian"), NCPAY("nc-pay-hessian"), NCCONFIG(
			"nc-config-hessian"), RISKCONTROL("door-god-hessian"),NCTRADE("nc-api-hessian"),FRONTEND("frontend-hessian"),
			MERCHANT_PLATEFORM("merchant_plateform-hessian"),ORDER_PROCCESOR("order_proccesor_hessian"),
			PAY_PROCCESOR("payproccesor_hessian"),MERCHANT_CONFIG("foundation_hessian"),USER_CENTER("ncmember_hessian"),
	BIBASIC_ORDER("pccashier_hessian"),YOP("yop"),CALL_FEE("callfee_hessian"),
	//综合服务产品组(微信公众号相关)
	WECHAT_APP("wechat-hessian"),
	// 商户平台系统
	MERCHANT_PLATFORM("mp_hessian"),
	//无交易订单处理器
	NO_TRADING_PROCCESSOR("nop-hessian"),
	//三代会员子系统
	MEMBER("member-hessian"),
	
	//营销系统
	MARKETING("mktg-hessian");

	/**
	 * 获取系统名
	 * 
	 * @return
	 */
	public String getSysName() {
		return sysName;
	}

	/**
	 * 系统名
	 */
	private final String sysName;

	/**
	 * 构造函数
	 * 
	 * @param sysName
	 */
	SystemEnum(String sysName) {
		this.sysName = sysName;
	}



}
