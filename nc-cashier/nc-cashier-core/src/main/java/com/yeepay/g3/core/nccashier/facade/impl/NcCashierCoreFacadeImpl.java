/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.NcCashierCoreBiz;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.NcCashierCoreFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tanzhen
 *
 */
@Service("ncCashierCoreFacade")
public class NcCashierCoreFacadeImpl implements NcCashierCoreFacade {

	@Resource
	private NcCashierCoreBiz ncCashierCoreBiz;

	@Override
	public CashierPaymentResponseDTO createPayment(CashierPaymentRequestDTO paymentRequestDto) {
		return ncCashierCoreBiz.createPayment(paymentRequestDto);
	}

	@Override
	public CashierSmsSendResponseDTO sendSms(CashierSmsSendRequestDTO smsRequest) {
		return ncCashierCoreBiz.sendSms(smsRequest);
	}

	@Override
	public CashierPayResponseDTO firstPay(CashierFirstPayRequestDTO payResult) {
		return ncCashierCoreBiz.firstPay(payResult);
	}

	@Override
	public CashierQueryResponseDTO queryPayResult(CashierQueryRequestDTO queryRequestDto) {
		return ncCashierCoreBiz.queryPayResult(queryRequestDto);
	}

	@Override
	public BankLimitAmountListResponseDTO queryBankLimitAmountList(BankLimiAmountRequestDTO bankLimiAmountRequestDTO) {
		return ncCashierCoreBiz.queryBankLimitAmountList(bankLimiAmountRequestDTO);
	}

    @Override
	public CashierPayResponseDTO bindPay(CashierBindPayRequestDTO payResult) {
		return ncCashierCoreBiz.bindPay(payResult);
	}

	@Override
	public String getYeepayWechatQRCode(String requestId) {
		return ncCashierCoreBiz.getYeepayWechatQRCode(requestId);
	}

	@Override
	public String getYeepayWechatQRCode(String merchantNo, String merchantOrderId) {
		return ncCashierCoreBiz.getYeepayWechatQRCode(merchantNo,merchantOrderId);
	}
}
