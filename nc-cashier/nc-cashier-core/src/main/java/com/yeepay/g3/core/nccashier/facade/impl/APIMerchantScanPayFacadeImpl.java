package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIMerchantScanPayBiz;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.service.APIMerchantScanPayFacade;

/**
 * 商家扫码支付相关接口实现类
 * 
 * @author duangduang
 * @since 2017-02-20
 *
 */
@Service("apiMerchantScanPayFacade")
public class APIMerchantScanPayFacadeImpl implements APIMerchantScanPayFacade{
	
	@Resource
	private APIMerchantScanPayBiz apiMerchantScanPayBiz;

	@Override
	public BasicResponseDTO pay(APIMerchantScanPayDTO request) {
		return apiMerchantScanPayBiz.pay(request);
	}

}
