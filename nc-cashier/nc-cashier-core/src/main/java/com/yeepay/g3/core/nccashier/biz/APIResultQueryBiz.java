package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPayQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPayResultQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIResultQueryResponseDTO;

/**
 * <前置收银台> 交易结果处理biz层声明
 * 
 * @author duangduang
 *
 */
public interface APIResultQueryBiz {

	/**
	 * 交易结果查询接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	APIResultQueryResponseDTO queryResult(APIBasicRequestDTO requestDTO);


	APIPayResultQueryResponseDTO queryPayResultByRecordId(APIPayQueryRequestDTO requestDTO);

}
