package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;

/**
 * 校验支付请求入参服务
 * @author xueping.ni
 *
 */
public interface CashierCheckRequestInfoService {
	/**
	 * 校验支付请求入参
	 * @param paymentRequest
	 */
	public void checkPassInfo(PaymentRequest paymentRequest);

}
