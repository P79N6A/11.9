package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.ReverseResponseDTO;

/**
 * 支付冲正接口
 * @author chronos.
 * @createDate 2016/11/8.
 */
public interface PayProcessorReverseFacade {

    /**
     * 冲正请求接口
     * @param requestDTO
     * @return
     */
    ReverseResponseDTO reverseRequest(ReverseRequestDTO requestDTO);
}
