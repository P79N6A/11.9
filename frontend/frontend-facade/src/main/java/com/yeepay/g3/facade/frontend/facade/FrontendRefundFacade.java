package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.FrontendRefundRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundResponseDTO;

/**
 * 支付系统订单退款
 * @author songscorpio
 *
 */
public interface FrontendRefundFacade {
	/**
	 * 退款请求接口
	 * @param dto
	 * @return
     */
	FrontendRefundResponseDTO refund(FrontendRefundRequestDTO dto);
}
