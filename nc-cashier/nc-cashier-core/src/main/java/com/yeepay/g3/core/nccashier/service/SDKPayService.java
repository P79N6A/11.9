/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;


import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayResponseDTO;

/**
 * @author xueping.ni
 * @date 2017年3月14日
 *
 */
public interface SDKPayService {

	/**
	 * @param response
	 * @param paymentRequest
	 * @param request 
	 */
	void pay(SDKPayResponseDTO response, PaymentRequest paymentRequest, SDKPayRequestDTO request);

	/**
	 * @param createOrderRequest
	 * @param payTypes 
	 * @return
	 */
	PaymentRequest createPayRequestWhenUnexsit(SDKCreateOrderRequestDTO createOrderRequest, OrderDetailInfoModel orderInfo, MerchantInNetConfigResult merchantInNetConfigResult);
	
}
