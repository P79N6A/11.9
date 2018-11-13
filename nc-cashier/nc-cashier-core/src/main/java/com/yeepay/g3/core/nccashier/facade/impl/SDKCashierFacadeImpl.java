/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.SDKCashierBiz;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayResponseDTO;
import com.yeepay.g3.facade.nccashier.service.SDKCashierFacade;

/**
 * @author xueping.ni
 * @date 2017年3月9日
 *
 */
@Service("sdkCashierFacade")
public class SDKCashierFacadeImpl implements SDKCashierFacade {
	@Resource
	private SDKCashierBiz sdkCashierBiz;
	/**
	 * SDK支付请求服务
	 */
	@Override
	public SDKCreateOrderResponseDTO payRequest(
			SDKCreateOrderRequestDTO createOrderRequest) {
		
		return sdkCashierBiz.payRequest(createOrderRequest);
	}


	/**
	 * SDK支付服务
	 */
	@Override
	public SDKPayResponseDTO pay(SDKPayRequestDTO request) {
		return sdkCashierBiz.pay(request);
	}

}
