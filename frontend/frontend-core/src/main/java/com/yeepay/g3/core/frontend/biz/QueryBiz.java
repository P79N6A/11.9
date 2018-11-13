package com.yeepay.g3.core.frontend.biz;

import com.yeepay.g3.facade.frontend.dto.FeOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationResponseDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;

import java.util.Date;

public interface QueryBiz {

	/**
	 * 定时查单
	 * @param start
	 * @param end
	 * @param platformType
	 */
	void queryBankOrder(Date start, Date end, String platformType);

	/**
	 * 根据订单ID补单
	 * 运营后台使用
	 * @param
	 * @param requestDTO
	 */
	FeOperationResponseDTO repairOrders(FeOperationRequestDTO requestDTO);
	
	/**
	 * 单笔订单查询
	 * @param frontendQueryRequestDTO
	 * @return
	 */
	FrontendQueryResponseDTO queryOrder(FrontendQueryRequestDTO frontendQueryRequestDTO);
}
