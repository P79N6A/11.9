package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.facade.payprocessor.dto.*;

/**
 * 查单业务处理
 * @author chronos.
 * @createDate 2016/11/11.
 */
public interface QueryBiz {

    /**
     * 订单处理器查单业务处理
     * @param requestDTO
     * @return
     */
    QueryResponseDTO query(QueryRequestDTO requestDTO);

    /**
     * 请求系统查单业务处理
     * @param requestDTO
     * @return
     */
    PayRecordResponseDTO query(PayRecordQueryRequestDTO requestDTO);

    /**
     * 定时补单业务处理
     */
    void repairOrder();

    /**
     * 批量补单
     * @param requestDTO
     * @return
     */
    OperationResponseDTO batchRepair(OperationRequestDTO requestDTO);

    /**
     * 订单处理器查单业务处理
     * 历史订单
     * @param requestDTO
     * @return
     */
    QueryResponseDTO queryHisOrder(QueryRequestDTO requestDTO);

    /**
     * 定时处理组合支付的订单
     */
    void repairCombOrder();
}
