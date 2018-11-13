package com.yeepay.g3.facade.nccashier.enumtype;
/**
 * 业务方请求的短验类型
 * @author：yp-tc-m-2804    
 * @since：2015年12月16日 下午5:19:33 
 * @version:
 */
public enum ReqSmsSendTypeEnum {
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
