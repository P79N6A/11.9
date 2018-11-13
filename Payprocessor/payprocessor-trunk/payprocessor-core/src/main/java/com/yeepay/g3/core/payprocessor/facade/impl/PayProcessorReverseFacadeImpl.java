package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.ReverseBiz;
import com.yeepay.g3.facade.payprocessor.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.payprocessor.facade.PayProcessorReverseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("payProcessorReverseFacade")
public class PayProcessorReverseFacadeImpl implements PayProcessorReverseFacade {

    @Autowired
    private ReverseBiz reverseBiz;

	@Override
	public ReverseResponseDTO reverseRequest(ReverseRequestDTO requestDTO) {
		return reverseBiz.reverseRequest(requestDTO);
	}

}
