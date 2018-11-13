package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPayQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPayResultQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIResultQueryResponseDTO;

/**
 * 通用业务facade
 * 
 * @author duangduang
 *
 */
public interface APICommonBusinessFacade {

	/**
	 * 前置收银台交易结果查询接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIResultQueryResponseDTO queryResult(APIBasicRequestDTO requestDTO);


	/**
	 * 前置收银台支付结果查询
	 * @return
     */
	APIPayResultQueryResponseDTO queryPayResultByRecordId(APIPayQueryRequestDTO requestDTO);








}
