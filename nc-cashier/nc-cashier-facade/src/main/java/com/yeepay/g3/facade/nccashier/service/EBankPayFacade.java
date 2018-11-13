package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.EBankCreatePaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankCreatePaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankSupportBanksRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.EBankSupportBanksResponseDTO;

/**
 * pc网银 支付相关facade接口
 * @author duangduang
 * @since  2016-11-08
 */
public interface EBankPayFacade {
	
	/**
	 * 获取商户支持的银行列表
	 * @param request
	 * @return
	 */
	EBankSupportBanksResponseDTO querySupportBankList(EBankSupportBanksRequestDTO request);
	
	/**
	 * 网银确认支付下单
	 * @param request
	 * @return
	 */
	EBankCreatePaymentResponseDTO createPayment(EBankCreatePaymentRequestDTO request);


	EBankSupportBanksResponseDTO getBacLoadSuportBanks(EBankSupportBanksRequestDTO request);



}
