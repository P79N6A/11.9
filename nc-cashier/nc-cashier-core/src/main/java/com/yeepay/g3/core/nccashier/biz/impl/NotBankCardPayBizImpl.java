package com.yeepay.g3.core.nccashier.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.NotBankCardPayBiz;
import com.yeepay.g3.core.nccashier.service.NotBankCardPayService;

@Service("notBankCardPayBiz")
public class NotBankCardPayBizImpl implements NotBankCardPayBiz {
	
	@Autowired
	private NotBankCardPayService notBankCardPayService;

	@Override
	public String getNotBankCardPayHmac(String merchantNo, String source) {
		return notBankCardPayService.getNotBankCardPayHmac(merchantNo, source);
	}

}
