/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;

/**
 * @author zhen.tan
 * 对接商户信息服务
 *
 */
public interface MerchantInfoService {
	
	/**
	 * 获取商户等级信息
	 * @param merchantAccountCode
	 * @return
	 */
	LevelRespDTO getMerchantLevel(String merchantAccountCode);
}
