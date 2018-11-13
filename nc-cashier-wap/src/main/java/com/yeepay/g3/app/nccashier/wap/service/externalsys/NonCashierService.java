package com.yeepay.g3.app.nccashier.wap.service.externalsys;

import com.yeepay.g3.app.nccashier.wap.vo.externalsys.NcauthReceiveBankRequestParam;

/**
 * 非依赖nc-cashier-hessian服务的的外部依赖
 * 
 * @author duangduang
 *
 */
public interface NonCashierService {

	/**
	 * 获取鉴权中心的鉴权前端回调地址
	 * 
	 * @param ncauthReceiveBankRequestParam
	 * @return
	 */
	String receiveFrontendNotify(NcauthReceiveBankRequestParam ncauthReceiveBankRequestParam);
	
	/**
	 * 获取智能网银前端回调地址
	 * 
	 * @param paymentNo
	 * @return
	 */
	String receiveIntelligentNetResultFrontCallback(String paymentNo);
}
