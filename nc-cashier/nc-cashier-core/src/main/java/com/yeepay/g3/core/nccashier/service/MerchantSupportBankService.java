package com.yeepay.g3.core.nccashier.service;

import java.math.BigDecimal;
import java.util.List;

import com.yeepay.g3.core.nccashier.vo.InstallmentBankInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.SignRelationInfo;
import com.yeepay.g3.core.nccashier.vo.UserSignRelationCollection;

public interface MerchantSupportBankService {

	/**
	 * 过滤商户的
	 * @param allSignRelationList
	 * @param merchantConfigInfo
	 * @return
	 */
	UserSignRelationCollection filterSignRelationList(List<SignRelationInfo> allSignRelationList,
			MerchantInNetConfigResult merchantConfigInfo);
	
	InstallmentBankInfo bankIsSupportedOrNot(MerchantInNetConfigResult merchantConfigInfo,
			String bankCode, BigDecimal orderAmount, int period);	
	
}
