package com.yeepay.g3.core.nccashier.enumtype;

/**
 * 订单动作
 * 
 * @author duangduang
 *
 */
public enum OrderAction {

	/**
	 * 下单
	 */
	ORDER,

	/**
	 * 发短验
	 */
	SEND_SMS,

	/**
	 * 确认支付
	 */
	CONFIRM_PAY,
	/**
	 * 预授权下单
	 */
	YSQ_ORDER,

	/**
	 * 预授权发短验
	 */
	YSQ_SEND_SMS,

	/**
	 * 预授权确认支付
	 */
	YSQ_CONFIRM_PAY,

	/**
	 * API一键支付发短验
	 */
	API_YJZF_SEND_SMS,

	/**
	 * API一键支付确认支付
	 */
	API_YJZF_CONFIRM_PAY,

	/**
	 * 分期易发短验
	 */
	CLF_SEND_SMS;


}
