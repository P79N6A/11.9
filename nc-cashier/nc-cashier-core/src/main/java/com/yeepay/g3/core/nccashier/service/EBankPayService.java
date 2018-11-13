package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.*;

import java.util.List;

/**
 * pc网银支付业务 service
 * 
 * @author duangduang
 * @since  2016-11-08
 */
public interface EBankPayService {
	
	/**
	 * 获取支持的网银银行列表
	 * @param request
	 * @param response
	 */
	void querySupportBankList(EBankSupportBanksRequestDTO request, EBankSupportBanksResponseDTO response);
	
	/**
	 * 网银确认支付下单
	 * @param request
	 * @param response
	 */
	void createPayment(EBankCreatePaymentRequestDTO request, EBankCreatePaymentResponseDTO response);

	/**
	 * 根据支付场景 获取银行模板
     * @return
     */
	void getBacLoadSuportBanks(EBankSupportBanksRequestDTO request, EBankSupportBanksResponseDTO response);






}
