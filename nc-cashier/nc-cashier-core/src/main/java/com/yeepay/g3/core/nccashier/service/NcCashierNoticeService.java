package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;

/**
 * 回调通知上游系统
 * 
 * @author：peile.fan
 * @since：2016年6月3日 上午10:05:53
 * @version:
 */
public interface NcCashierNoticeService {
	// MQ方式
	void sendNoticeToTradeSys(PaymentRequest paymentRequest);
	//HESSIAN方式
	void sendNoticeTradeByHessian(PaymentRequest paymentRequest);
}
