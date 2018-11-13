package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;

/**
 * 商家扫码支付相关接口
 * 
 * @author duangduang
 * @since 2017-02-20
 */
public interface APIMerchantScanPayFacade {

	/**
	 * 商家扫码支付下单请求
	 * 
	 * @param request
	 * @return
	 */
	BasicResponseDTO pay(APIMerchantScanPayDTO request);
}
