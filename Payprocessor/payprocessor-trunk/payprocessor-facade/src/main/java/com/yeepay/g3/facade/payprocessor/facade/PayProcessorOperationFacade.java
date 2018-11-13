package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;

/**
 * @author chronos.
 * @createDate 2016/11/24.
 */
public interface PayProcessorOperationFacade {

    /**
     * 批量补单
     * @param requestDTO
     * @return
     */
    OperationResponseDTO batchRepair(OperationRequestDTO requestDTO);

    /**
     * 批量补发通知
     * @param requestDTO
     * @return
     */
    OperationResponseDTO batchReNotify(OperationRequestDTO requestDTO);

    /**
     * 批量补发退款
     * @param requestDTO
     * @return
     */
    OperationResponseDTO batchRefund(OperationRequestDTO requestDTO);
}
