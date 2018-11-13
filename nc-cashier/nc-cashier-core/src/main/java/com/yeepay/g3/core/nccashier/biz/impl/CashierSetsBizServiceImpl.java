package com.yeepay.g3.core.nccashier.biz.impl;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.CashierSetsBizService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;
@Service("cashierSetsBizService")
public class CashierSetsBizServiceImpl implements CashierSetsBizService {
	/**
	 * 从三代统一配置的发送短验的号码
	 */
	@Override
	public String getSendSMSNo() {
		String sendSMSNo = CommonUtil.getSysConfigFrom3G(Constant.SMSSEND_NO);
		return sendSMSNo;
	}

}
