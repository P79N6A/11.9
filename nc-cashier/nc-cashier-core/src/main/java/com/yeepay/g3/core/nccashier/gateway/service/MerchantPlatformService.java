package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.facade.mp.dto.TradePasswordValidateDTO;

/**
 * 商户平台service
 * 
 * @author duangduang
 * @date 2017-06-06
 */
public interface MerchantPlatformService {

	/**
	 * 商户权限验证接口
	 * 
	 * @param merchantAccountNo
	 * @return
	 */
	boolean validatePermission(String merchantLoginName);

	/**
	 * 商户交易密码验证接口
	 * 
	 * @param merchantLoginName
	 * @param tradePassword
	 */
	TradePasswordValidateDTO validateTradePassword(String merchantLoginName, String tradePassword);
}
