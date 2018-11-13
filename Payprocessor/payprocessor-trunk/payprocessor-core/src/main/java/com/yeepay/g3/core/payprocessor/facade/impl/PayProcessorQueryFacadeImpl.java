package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.QueryBiz;
import com.yeepay.g3.facade.payprocessor.dto.QueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.facade.PayProcessorQueryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("payProcessorQueryFacade")
public class PayProcessorQueryFacadeImpl implements PayProcessorQueryFacade {

	@Autowired
	private QueryBiz queryBiz;

	@Override
	public QueryResponseDTO query(QueryRequestDTO requestDTO) {
		return queryBiz.query(requestDTO);
	}


	/**
	 * 特殊定制，历史订单查询，不对外使用
	 * @param requestDTO
	 * @return
	 */
	@Override
	public QueryResponseDTO queryHisOrder(QueryRequestDTO requestDTO) {
		return queryBiz.queryHisOrder(requestDTO);
	}

}
