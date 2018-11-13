package com.yeepay.g3.core.nccashier.service;

import java.util.Map;
import java.util.Set;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.ActivityInfoOfPayProduct;

public interface MarketInfoManageService {

	/**
	 * 根据具体的支付方式、类型
	 * 
	 * @param token
	 * @param paymentRequest
	 * @param payProduct
	 *            二级支付方式
	 * @param type
	 *            类型，无卡：值为DEBIT/CREDIT；网银：对公对私，目前对私是不支持营销活动的;聚合类支付类型
	 * @param bankCode
	 * @return
	 */
	Set<String> getMarketActivityInfoByPayInfo(String token, PaymentRequest paymentRequest, String payProduct,
			String type, String bankCode);

	/**
	 * 获取营销活动，先从缓存中获取，取不到再调营销系统查询
	 * 
	 * @param token
	 * @param paymentRequest
	 * @return
	 */
	Map<String, ActivityInfoOfPayProduct> getMarketActivity(String token, PaymentRequest paymentRequest);
}
