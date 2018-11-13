package com.yeepay.g3.core.nccashier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.HmacKeyService;
import com.yeepay.g3.core.nccashier.service.NotBankCardPayService;
import com.yeepay.g3.core.nccashier.utils.CustomerSecurityHelper;

@Service("notBankCardPayService")
public class NotBankCardPayServiceImpl implements NotBankCardPayService {
	
	@Autowired
	private HmacKeyService hmacKeyService;

	@Override
	public String getNotBankCardPayHmac(String merchantNo, String source) {
		String key = hmacKeyService.getHmacKey(merchantNo).getHmacKey();
		String hmac = CustomerSecurityHelper.hmacSign(source, key);
		return hmac;
	}
}
