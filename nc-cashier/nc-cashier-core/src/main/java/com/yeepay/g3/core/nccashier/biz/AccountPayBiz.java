package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;

/**
 * 账户支付biz层
 * 
 * @author duangduang
 * @date 2017-06-06
 */
public interface AccountPayBiz {

	/**
	 * 账户支付支付下单接口
	 * 
	 * @param request
	 * @return
	 */
	CashierAccountPayResponseDTO pay(CashierAccountPayRequestDTO request);

	/**
	 * 账户支付校验接口
	 * 
	 * @param request
	 * @return
	 */
	AccountPayValidateResponseDTO validateMerchantAccount(AccountPayValidateRequestDTO request);

}
