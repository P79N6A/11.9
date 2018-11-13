package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.EBankPayBiz;
import com.yeepay.g3.facade.nccashier.dto.EBankCreatePaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankCreatePaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankSupportBanksRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankSupportBanksResponseDTO;
import com.yeepay.g3.facade.nccashier.service.EBankPayFacade;

/**
 * pc网银 支付相关facade实现
 * 
 * @author duangduang
 * @since  2016-11-08
 */
@Service("eBankPayFacade")
public class EBankPayFacadeImpl implements EBankPayFacade{
	
	@Resource
	private EBankPayBiz eBankPayBiz;

	@Override
	public EBankSupportBanksResponseDTO querySupportBankList(EBankSupportBanksRequestDTO request) {
		return eBankPayBiz.querySupportBankList(request);
	}

	@Override
	public EBankCreatePaymentResponseDTO createPayment(EBankCreatePaymentRequestDTO request) {
		return eBankPayBiz.createPayment(request);
	}

	@Override
	public EBankSupportBanksResponseDTO getBacLoadSuportBanks(EBankSupportBanksRequestDTO request) {
		return eBankPayBiz.getBacLoadSuportBanks(request);
	}

}
