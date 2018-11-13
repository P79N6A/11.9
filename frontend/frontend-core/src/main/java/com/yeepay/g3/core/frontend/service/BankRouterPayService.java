package com.yeepay.g3.core.frontend.service;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.entity.PayRecord;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayResponseDTO;


public interface BankRouterPayService {
	
	/**
	 * 公众号预路由接口
	 * @param prePayRequestDTO
	 * @param prePayResponseDTO
	 */
	void prePayJsapi(PrePayRequestDTO prePayRequestDTO, PrePayResponseDTO prePayResponseDTO);
	
	/**
	 * 调用路由获取支付链接
	 * @param payOrder
	 * @param payRecord
	 * @param payRequestDTO
	 * @return
	 */
	void openPay(PayOrder payOrder,PayRecord payRecord,PayRequestDTO payRequestDTO, PayResponseDTO payResponseDTO);
}
