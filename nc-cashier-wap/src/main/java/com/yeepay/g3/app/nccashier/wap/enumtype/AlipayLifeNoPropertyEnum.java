package com.yeepay.g3.app.nccashier.wap.enumtype;

/**
 * 支付宝生活号相关配置键枚举值
 * 
 * @author duangduang
 *
 */
public enum AlipayLifeNoPropertyEnum {

	/**
	 * 授权地址
	 */
	AUTH2_URL("auth2Url"), 
	
	/**
	 * 授权回调地址
	 */
	CALLBACK_URL("callbackUrl"), 
	
	/**
	 * 支付宝标准版授权回调地址
	 */
	STANDARD_CALLBACK_URL("stdCallbackUrl"),
	
	/**
	 * 支付宝userId获取地址
	 */
	USERID_GET_URL("userIdGetUrl"), 
	
	/**
	 * 支付宝生活号appId
	 */
	ALIPAY_APPID( "alipayAppId"), 
	
	/**
	 * 支付宝生活号appSecret
	 */
	ALIPAY_APPSECRET("alipayAppSecret");

	private String description;

	private AlipayLifeNoPropertyEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
