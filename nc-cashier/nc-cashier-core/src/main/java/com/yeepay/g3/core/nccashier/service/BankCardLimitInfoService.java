package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
/**
 * 
 * @author xueping.ni 
 * @date 2016-08-01
 * 银行卡限制信息服务
 */
public interface BankCardLimitInfoService {

	public BindLimitInfoResDTO getLimitInfo(PaymentRequest paymentRequest);

	public boolean getShowChangeCard(PaymentRequest paymentRequest);

	public BindLimitInfoResDTO getSamePersonInfo(PaymentRequest paymentRequest);

	public BindLimitInfoResDTO getLimitInfo4bind(PaymentRequest paymentRequest);

	/**
	 * 获取绑卡限制信息(无缓存)
	 * @param paymentRequest
	 * @return
	 */
	public BindLimitInfoResDTO getLimitInfoNoCache(PaymentRequest paymentRequest);

	/**
	 * 获取同人限制信息(无缓存)
	 * @param merchantNo
	 * @param identityId
	 * @param identityType
	 * @param orderSysNo
	 * @return
	 */
	BindLimitInfoResDTO getLimitInfoNoCache(String merchantNo,String identityId,String identityType,String orderSysNo);

	/**
	 * 判断已绑卡达到同人上限后，是否允许继续使用新卡支付
	 * @param merchantNo
	 * @param orderSysNo
	 * @return
	 */
	boolean judge3GFirstPay(String merchantNo, String orderSysNo);
}
