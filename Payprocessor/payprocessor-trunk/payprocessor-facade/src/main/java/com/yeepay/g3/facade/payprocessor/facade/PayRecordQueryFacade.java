package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.PayRecordQueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;

/**
 * 统一收银台子单查询接口
 * @author peile.fan
 */
public interface PayRecordQueryFacade {
	 /**
     * 查询接口
     * @param requestDTO
     * @return
     */
	PayRecordResponseDTO query(PayRecordQueryRequestDTO requestDTO);
}
