package com.yeepay.g3.core.nccashier.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.CashierSetsBizService;
import com.yeepay.g3.facade.nccashier.service.CashierSetsFacade;
@Service("cashierSetsFacade")
public class CashierSetsFacadeImpl implements CashierSetsFacade {
	@Resource
	private CashierSetsBizService  cashierSetsBizService;
	@Override
	public String getSendSMSNo() {
		return cashierSetsBizService.getSendSMSNo();
	}

}
