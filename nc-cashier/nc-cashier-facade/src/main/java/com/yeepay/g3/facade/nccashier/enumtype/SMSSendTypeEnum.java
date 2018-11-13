/**
 * 
 */
package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * 短验类型
 * @author zhen.tan
 *
 */
public enum SMSSendTypeEnum {

	/**
	 * 不发短验
	 */
	NONE,
	/**
	 * 易宝发送短信
	 */
	YEEPAY,
	/**
	 * 易宝发语音验证码
	 */
	VOICE,
	/**
	 * 商户发短信，易宝验证
	 */
	MERCHANT_SEND;
}
