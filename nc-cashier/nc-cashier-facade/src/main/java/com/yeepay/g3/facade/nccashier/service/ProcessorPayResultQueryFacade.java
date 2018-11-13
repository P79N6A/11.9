package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayResultQuerySignListenResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;

/**
 * 支付处理器支付结果处理
 * @author duangduang
 * @since  2016-11-11
 */
public interface ProcessorPayResultQueryFacade {
	
	/**
	 * 支付处理器是否可查标识监听处理
	 * @param request
	 * @return
	 */
	PayResultQuerySignListenResponseDTO listenCanPayResultQuery(PayResultQuerySignListenRequestDTO request);

	/**
	 * 拼接并返回商户回调地址
	 * @param requestInfoDTO
	 * @return
	 */
	String getMerchantPageCallBack(RequestInfoDTO requestInfoDTO);
}
