package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantAccountPayRequestDTO;

/**
 * 前置收银台支持商户账户支付biz层
 * 
 * @author duangduang
 *
 */
public interface APIMerchantAccountPayBiz {

	/**
	 * 支付接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO pay(APIMerchantAccountPayRequestDTO requestDTO);
}
