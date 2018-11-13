package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.facade.nccashier.dto.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.yeepay.g3.core.nccashier.biz.EBankPayBiz;
import com.yeepay.g3.core.nccashier.service.EBankPayService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;

/**
 * pc网银 支付相关biz接口实现
 * 
 * @author duangduang
 * @since  2016-11-08
 */
@Component
public class EBankPayBizImpl extends NcCashierBaseBizImpl implements EBankPayBiz{
	
	@Resource
	private EBankPayService eBankPayService;

	@Override
	public EBankSupportBanksResponseDTO querySupportBankList(EBankSupportBanksRequestDTO request) {
		EBankSupportBanksResponseDTO response = new EBankSupportBanksResponseDTO();
		try{
			NcCashierLoggerFactory.TAG_LOCAL.set("[查询pc网银支持的银行列表|querySupportBankList],paymentRequestId="+request.getPaymentRequestId());
			eBankPayService.querySupportBankList(request, response);
		}catch(Throwable t){
			handleException(response, t);
		}

		return response;
	}

	@Override
	public EBankCreatePaymentResponseDTO createPayment(EBankCreatePaymentRequestDTO request) {
		EBankCreatePaymentResponseDTO response = new EBankCreatePaymentResponseDTO();
		try{
			NcCashierLoggerFactory.TAG_LOCAL.set("[网银确认支付下单|createPayment],paymentRequestId="+request.getPaymentRequestId());
			eBankPayService.createPayment(request, response);
			passThroughPccashierOrNot(request.getBankId(), response);
		}catch(Throwable t){
			handleException(response, t);
		}
		
		return response;
	}

	@Override
	public EBankSupportBanksResponseDTO getBacLoadSuportBanks(EBankSupportBanksRequestDTO request) {
		EBankSupportBanksResponseDTO response = new EBankSupportBanksResponseDTO();
		try{
			NcCashierLoggerFactory.TAG_LOCAL.set("[通过支付场景查询pc网银支持的银行列表|getBacLoadSuportBanks],paymentRequestId="+request.getPaymentRequestId());
			eBankPayService.getBacLoadSuportBanks(request,response);
		}catch(Throwable t){
			handleException(response, t);
		}

		return response;
	}

	private void passThroughPccashierOrNot(String bankCode, EBankCreatePaymentResponseDTO response){
		if(StringUtils.isBlank(response.getPayUrl())){ // 如果支付处理器返回的跳转pcc的url为空，则直接跳转银行
			response.setToBankPassThroughPccashier(Constant.TO_BANK);
		}else if(response.getUrlInfoDTO()==null || StringUtils.isBlank(response.getUrlInfoDTO().getUrl())){ // 如果支付处理器返回的跳转银行的url为空，则跳转pcc
			response.setToBankPassThroughPccashier(Constant.TO_PCCASHIER);
		}else{
			String passThroughPccashier = CommonUtil.toBankPassThroughPccashier(bankCode);
			response.setToBankPassThroughPccashier(passThroughPccashier);
		}
	}

}
