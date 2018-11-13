package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;

/**
 * 会员支付对外接口
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public interface AccountPayFacade {

	/**
	 * 支付接口
	 * 
	 * @param request
	 * @return
	 */
	CashierAccountPayResponseDTO pay(CashierAccountPayRequestDTO request);

	/**
	 * 校验接口
	 * 
	 * @param request
	 * @return
	 */
	AccountPayValidateResponseDTO validateMerchantAccount(AccountPayValidateRequestDTO request);

}
