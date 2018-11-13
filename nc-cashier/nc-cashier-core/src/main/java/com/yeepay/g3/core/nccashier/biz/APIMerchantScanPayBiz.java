package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;

/**
 * 商家扫码支付biz层接口定义
 * 
 * @author duangduang
 * @since 2017-02-20
 */
public interface APIMerchantScanPayBiz {
	
	/**
	 * 商家扫码支付请求
	 * 
	 * @param request
	 * @return
	 */
	BasicResponseDTO pay(APIMerchantScanPayDTO request);
}
