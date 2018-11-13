package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayResponseDTO;

public interface FrontendPayFacade {
	
	/**
	 * 公众号预路由接口
	 * @param prePayRequestDTO
	 * @return
	 */
	PrePayResponseDTO prePayJsapi(PrePayRequestDTO prePayRequestDTO);
	
	/**
	 * wx/zfb统一支付接口
	 * @param payRequestDTO
	 * @return
	 */
	PayResponseDTO openPay(PayRequestDTO payRequestDTO);
	
}
