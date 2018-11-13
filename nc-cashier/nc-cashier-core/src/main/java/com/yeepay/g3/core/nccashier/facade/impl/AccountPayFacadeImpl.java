package com.yeepay.g3.core.nccashier.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.AccountPayBiz;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;
import com.yeepay.g3.facade.nccashier.service.AccountPayFacade;

/**
 * 账户支付对外接口实现
 * 
 * @author duangduang
 * @date 2017-06-06
 */
@Service("accountPayFacade")
public class AccountPayFacadeImpl implements AccountPayFacade {

	@Autowired
	private AccountPayBiz accountPayBiz;

	@Override
	public CashierAccountPayResponseDTO pay(CashierAccountPayRequestDTO request) {
		return accountPayBiz.pay(request);
	}

	@Override
	public AccountPayValidateResponseDTO validateMerchantAccount(AccountPayValidateRequestDTO request) {
		return accountPayBiz.validateMerchantAccount(request);
	}

}
