package com.yeepay.g3.facade.frontend.facade;

import com.yeepay.g3.facade.frontend.dto.FeOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationResponseDTO;

/**
 * @author chronos.
 * @createDate 16/8/12.
 */
public interface FrontendOperationFacade {

    /**
     * 通过订单ID进行补单
     * @param requestDTO
     * @return
     */
    FeOperationResponseDTO repairOrders(FeOperationRequestDTO requestDTO);

    /**
     * 通过订单ID补发mq消息
     * @param requestDTO
     * @return
     */
    FeOperationResponseDTO reNotifyOrders(FeOperationRequestDTO requestDTO);

    /**
     * 退款请求接口,预留给运营后台使用
     * @param requestDTO
     * @return
     */
    FeOperationResponseDTO refundOrders(FeOperationRequestDTO requestDTO);
}
