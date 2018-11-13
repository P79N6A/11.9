package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;

public interface APICashierBaseBizTemplate {

	/**
	 * 
	 * @param basicRequestDTO
	 * @param responseDTO
	 * @param productLevel
	 */
	void handle(APIBasicRequestDTO basicRequestDTO, APIBasicResponseDTO responseDTO, ProductLevel productLevel);

	APIBasicResponseDTO errorResult(Throwable t, APIBasicRequestDTO basicRequestDTO, APIBasicResponseDTO responseDTO);

	void errorResult(Throwable t, APIBasicResponseDTO responseDTO);
}
