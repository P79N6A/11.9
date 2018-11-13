package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.biz.PayBiz;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendPayFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrontendPayFacadeImpl implements FrontendPayFacade{

	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendPayFacadeImpl.class);

	@Autowired
	private PayBiz payBiz;

	@Override
	public PayResponseDTO openPay(PayRequestDTO payRequestDTO) {
		try{
            FeLoggerFactory.TAG_LOCAL.set("[钱包支付请求] requestId = "+payRequestDTO.getRequestId());
            logger.info(payRequestDTO.toString());
            logger.info(String.format("[monitor],event:%s,orderType:%s,merchantno:%s,requestno:%s,payno:%s,status:%s", "FE_Openpay_Request", payRequestDTO.getOrderType(), payRequestDTO.getCustomerNumber(), payRequestDTO.getOutTradeNo(),
            		payRequestDTO.getRequestId(), "INIT"));
            PayResponseDTO payResponseDTO = payBiz.openPay(payRequestDTO);
            logger.info(payResponseDTO.toString());
            logger.info(String.format("[monitor],event:%s,merchantno:%s,requestno:%s,payno:%s,status:%s,responseCode:%s", "FE_Openpay_Response", payRequestDTO.getCustomerNumber(), payRequestDTO.getOutTradeNo(),
            		payRequestDTO.getRequestId(), payResponseDTO.getPayStatus(), payResponseDTO.getResponseCode()));
            return payResponseDTO;
        }finally {
        	FeLoggerFactory.TAG_LOCAL.set(null);
        }
	}

	@Override
	public PrePayResponseDTO prePayJsapi(PrePayRequestDTO prePayRequestDTO) {
		try{
            FeLoggerFactory.TAG_LOCAL.set("[钱包支付预路由请求]");
            logger.info(prePayRequestDTO.toString());
            PrePayResponseDTO prePayResponseDTO = payBiz.prePayJsapi(prePayRequestDTO);
            logger.info(prePayResponseDTO.toString());
            return prePayResponseDTO;
        }finally {
        	FeLoggerFactory.TAG_LOCAL.set(null);
        }
	}

}
