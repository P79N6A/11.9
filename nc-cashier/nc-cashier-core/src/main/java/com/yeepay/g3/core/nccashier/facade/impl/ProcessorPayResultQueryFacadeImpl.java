package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.ProcessorPayResultQueryBiz;
import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenResponseDTO;
import com.yeepay.g3.facade.nccashier.service.ProcessorPayResultQueryFacade;

/**
 * 支付处理器支付结果处理
 * @author duangduang
 * @since  2016-11-11
 */
@Service("processorPayResultQueryFacade")
public class ProcessorPayResultQueryFacadeImpl implements ProcessorPayResultQueryFacade{
	
	@Resource
	private ProcessorPayResultQueryBiz processorPayResultQueryBiz;

	@Override
	public PayResultQuerySignListenResponseDTO listenCanPayResultQuery(PayResultQuerySignListenRequestDTO request) {
		return processorPayResultQueryBiz.listenCanPayResultQuery(request);
	}

    @Override
    public String getMerchantPageCallBack(RequestInfoDTO requestInfoDTO) {
        return processorPayResultQueryBiz.getMerchantPageCallBack(requestInfoDTO);
    }

}
