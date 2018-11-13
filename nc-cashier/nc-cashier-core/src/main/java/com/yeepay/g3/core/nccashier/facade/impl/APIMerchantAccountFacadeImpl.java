package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIMerchantAccountPayBiz;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.service.APIMerchantAccountFacade;

@Service("apiMerchantAccountFacade")
public class APIMerchantAccountFacadeImpl implements APIMerchantAccountFacade {

	@Resource
	private APIMerchantAccountPayBiz apiMerchantAccountPayBiz;

	@Override
	public APIBasicResponseDTO pay(APIMerchantAccountPayRequestDTO requestDTO) {
		return apiMerchantAccountPayBiz.pay(requestDTO);
	}

}
