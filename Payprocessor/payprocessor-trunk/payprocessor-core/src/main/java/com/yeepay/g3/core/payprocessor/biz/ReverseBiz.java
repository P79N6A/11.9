package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.facade.payprocessor.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.ReverseResponseDTO;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
public interface ReverseBiz {

    /**
     * 冲正处理逻辑
     * @param requestDTO
     * @return
     */
    ReverseResponseDTO reverseRequest(ReverseRequestDTO requestDTO);
}
