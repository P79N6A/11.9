/**
 * 
 */
package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayResponseDTO;

/**
 * SDK收银台服务
 * @author xueping.ni
 * @since 2017年3月9日
 * @version 1.0
 */
public interface SDKCashierFacade {
	/**
	 * SDK支付请求服务
	 * @param createOrderRequest
	 * @return
	 */
	public SDKCreateOrderResponseDTO payRequest(SDKCreateOrderRequestDTO createOrderRequest);
	/**
	 * SDK支付服务
	 * @param request
	 * @return
	 */
	public SDKPayResponseDTO pay(SDKPayRequestDTO request);
	

}
