package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.ProcessorPayResultQueryBiz;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.QueryResultService;
import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;

/**
 * 支付处理器支付结果处理
 * @author duangduang
 * @since  2016-11-14
 */
@Service
public class ProcessorPayResultQueryBizImpl extends NcCashierBaseBizImpl implements ProcessorPayResultQueryBiz{
	
	@Resource
	private QueryResultService queryResultService;
	
	@Resource
	private PaymentRequestService paymentRequestService;

	@Override
	public PayResultQuerySignListenResponseDTO listenCanPayResultQuery(PayResultQuerySignListenRequestDTO request) {
		PayResultQuerySignListenResponseDTO response = new PayResultQuerySignListenResponseDTO();
		try{
			queryResultService.listenCanPayResultQuery(request, response);
			response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		}catch(Throwable t){
			handleException(response, t);
		}
		return response;
	}

	@Override
	public String getMerchantPageCallBack(RequestInfoDTO requestInfoDTO) {
		if (requestInfoDTO == null) {
			return null;
		}
		PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestInfoDTO.getPaymentRequestId());
		if (paymentRequest == null) {
			return null;
		}
		return queryResultService.buildFrontCallbackUrl(paymentRequest);
	}

}
