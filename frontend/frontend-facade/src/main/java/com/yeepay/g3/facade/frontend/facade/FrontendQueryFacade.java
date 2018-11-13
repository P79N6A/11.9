package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.FrontendQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;

/**
 * 订单详情查询接口
 * 
 * @author songscorpio
 *
 */
public interface FrontendQueryFacade {
	/**
	 * 查询订单的支付信息
	 * @param dto
	 * @return
	 */
	public FrontendQueryResponseDTO queryOrderInfo(FrontendQueryRequestDTO dto);
}
