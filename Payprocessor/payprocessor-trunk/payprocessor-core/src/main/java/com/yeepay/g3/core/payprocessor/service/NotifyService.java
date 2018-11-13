package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/11/16.
 */
public interface NotifyService {

	void notify(PayRecord payRecord);

	void notify(PayRecord payRecord,Map<String,Object> extMap);

	/**
	 * 预授权通知业务方
	 */
	void notifyForPreAuth(PayRecord payRecord);
	/**
	 * 预授权通知业务方
	 */
	void notifyOprNotToAccount(PaymentRequest paymentRequest, PayRecord payRecord);

}
