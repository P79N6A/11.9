package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.QueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryResponseDTO;

/**
 * 支付处理器查询接口
 * @author chronos.
 * @createDate 2016/11/8.
 */
public interface PayProcessorQueryFacade {

    /**
     * 订单处理器查询接口
     * @param requestDTO
     * @return
     */
    QueryResponseDTO query(QueryRequestDTO requestDTO);

    /**
     * 查询历史订单，不对外使用
     * @param requestDTO
     * @return
     */
    QueryResponseDTO queryHisOrder(QueryRequestDTO requestDTO);

}
