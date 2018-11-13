/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayResponseDTO;

/**
 * @author xueping.ni
 * @date 2017年3月9日
 *
 */
public interface SDKCashierBiz {

	/**
	 * @param createOrderRequest
	 * @return
	 */
	public SDKCreateOrderResponseDTO payRequest(
			SDKCreateOrderRequestDTO createOrderRequest);

	/**
	 * @param request
	 * @return
	 */
	public SDKPayResponseDTO pay(SDKPayRequestDTO request);

}
