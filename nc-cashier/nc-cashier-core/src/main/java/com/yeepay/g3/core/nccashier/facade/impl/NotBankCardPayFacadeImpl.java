package com.yeepay.g3.core.nccashier.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.NotBankCardPayBiz;
import com.yeepay.g3.facade.nccashier.service.NotBankCardPayFacade;

@Service("notBankCardPayFacade")
public class NotBankCardPayFacadeImpl implements NotBankCardPayFacade {
	@Autowired
	private NotBankCardPayBiz notBankCardPayBiz;

	@Override
	public String getNotBankCardPayHmac(String merchantNo, String source) {
		return notBankCardPayBiz.getNotBankCardPayHmac(merchantNo, source);
	}
}
