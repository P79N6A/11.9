package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;

public interface NotBankCardPayService {
	/**
	 * 获取非银行卡支付请求链接
	 * @return
	 */
	public String getNotBankCardPayUrl(RequestInfoDTO requestInfo);
}
