package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.APIMerchantScanPayRequestVO;

/**
 * 商家扫码Service
 * 
 * @author duangduang
 * @since 2017-02-20
 */
public interface APIMerchantScanService {

	/**
	 * 商家扫码支付
	 * 
	 * @param param
	 * @return 错误码（若无异常，返回null）
	 */
	void pay(APIMerchantScanPayRequestVO param, String userIp);

}
