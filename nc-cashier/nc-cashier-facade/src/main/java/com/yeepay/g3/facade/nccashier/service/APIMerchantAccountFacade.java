package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantAccountPayRequestDTO;

/**
 * 前置收银台支持企业账户支付的对外服务
 * 
 * @author duangduang
 *
 */
public interface APIMerchantAccountFacade {

	APIBasicResponseDTO pay(APIMerchantAccountPayRequestDTO requestDTO);

}
