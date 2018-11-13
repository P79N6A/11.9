
package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.QueryBiz;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordQueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.facade.payprocessor.facade.PayRecordQueryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author peile.fan 支付单查询接口
 */
@Service("payRecordQueryFacade")
public class PayRecordQueryFacadeImpl implements PayRecordQueryFacade {

	@Autowired
	private QueryBiz queryBiz;

	@Override
	public PayRecordResponseDTO query(PayRecordQueryRequestDTO requestDTO) {
		return queryBiz.query(requestDTO);
	}
}
