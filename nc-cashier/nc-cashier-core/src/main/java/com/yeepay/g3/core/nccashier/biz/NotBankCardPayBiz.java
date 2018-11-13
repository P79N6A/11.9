package com.yeepay.g3.core.nccashier.biz;

public interface NotBankCardPayBiz {
	/**
	 * 非银行卡支付获取hmac
	 * 
	 * @return
	 */
	String getNotBankCardPayHmac(String merchantNo, String source);
}
