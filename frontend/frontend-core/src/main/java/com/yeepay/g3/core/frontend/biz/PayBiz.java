package com.yeepay.g3.core.frontend.biz;

import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayResponseDTO;

public interface PayBiz {
	
	/**
	 * zfb/wx支付,获取支付链接
	 * @param payRequestDTO
	 * @return
	 */
	PayResponseDTO openPay(PayRequestDTO payRequestDTO);
	
	/**
	 * 公众号预路由接口
	 * @param prePayRequestDTO
	 * @return
	 */
	PrePayResponseDTO prePayJsapi(PrePayRequestDTO prePayRequestDTO);
}
