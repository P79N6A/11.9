package com.yeepay.g3.facade.nccashier.service;

/**
 * 
 * @Description 非银行卡支付对外接口
 * @author yangmin.peng
 * @since 2017年8月3日下午4:16:00
 */
public interface NotBankCardPayFacade {

	/**
	 * 非银行卡支付获取hmac
	 * 
	 * @return
	 */
	String getNotBankCardPayHmac(String merchantNo, String source);
}
