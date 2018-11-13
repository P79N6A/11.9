package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import com.yeepay.g3.facade.nccashier.dto.APIPayQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIPayResultQueryResponseDTO;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIResultQueryBiz;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIResultQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.service.APICommonBusinessFacade;

/**
 * <前置收银台> 交易结果处理
 * 
 * @author duangduang
 *
 */
@Service("apiCommonBusinessFacade")
public class APIResultQueryFacadeImpl implements APICommonBusinessFacade {

	@Resource
	private APIResultQueryBiz apiResultQueryBiz;

	@Override
	public APIResultQueryResponseDTO queryResult(APIBasicRequestDTO requestDTO) {
		return apiResultQueryBiz.queryResult(requestDTO);
	}

	@Override
	public APIPayResultQueryResponseDTO queryPayResultByRecordId(APIPayQueryRequestDTO requestDTO) {
		return apiResultQueryBiz.queryPayResultByRecordId(requestDTO);
	}

}
