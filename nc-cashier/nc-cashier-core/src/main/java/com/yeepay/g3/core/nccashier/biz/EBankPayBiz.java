package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.*;

import java.util.List;

/**
 * 网银支付biz层
 * @author duangduang
 * @since  2016-11-08
 */
public interface EBankPayBiz {

	/**
	 * 查询pc网银支持的银行列表
	 *
	 * @param request
	 * @return
	 */
	EBankSupportBanksResponseDTO querySupportBankList(EBankSupportBanksRequestDTO request);

	/**
	 * 网银确认支付下单
	 *
	 * @param request
	 * @return
	 */
	EBankCreatePaymentResponseDTO createPayment(EBankCreatePaymentRequestDTO request);


	/**
	 * 获取银行列表
	 *
	 * @param request
	 * @return
	 */
	EBankSupportBanksResponseDTO getBacLoadSuportBanks(EBankSupportBanksRequestDTO request);
}
