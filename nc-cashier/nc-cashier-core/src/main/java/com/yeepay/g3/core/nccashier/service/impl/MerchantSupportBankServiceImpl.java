package com.yeepay.g3.core.nccashier.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.service.MerchantSupportBankService;
import com.yeepay.g3.core.nccashier.vo.InstallmentBankInfo;
import com.yeepay.g3.core.nccashier.vo.InstallmentRateInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.SignRelationInfo;
import com.yeepay.g3.core.nccashier.vo.UserSignRelationCollection;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;

@Service("merchantSupportBankService")
public class MerchantSupportBankServiceImpl implements MerchantSupportBankService {

	@Override
	public UserSignRelationCollection filterSignRelationList(List<SignRelationInfo> allSignRelationList,
			MerchantInNetConfigResult merchantConfigInfo) {
		UserSignRelationCollection userSignRelationCollection = new UserSignRelationCollection();
		if (merchantConfigInfo == null) {
			userSignRelationCollection.setUnusableSignRelationList(allSignRelationList);
			return userSignRelationCollection;
		}
		Map<String, List<String>> supportBanks = merchantConfigInfo.getInstallmentCashierTemplate();;
		if (MapUtils.isEmpty(supportBanks)) {
			userSignRelationCollection.setUnusableSignRelationList(allSignRelationList);
			return userSignRelationCollection;
		}

		List<SignRelationInfo> usableSignRelationList = new ArrayList<SignRelationInfo>();
		List<SignRelationInfo> unusableSignRelationList = new ArrayList<SignRelationInfo>();
		for (SignRelationInfo signRelationInfo : allSignRelationList) {
			if (!signRelationInfo.isSignRelationIllegal()) { // 理论上不应该存在，用户中心应该排除掉这种签约
				continue;
			}
			String bankCode = signRelationInfo.getSignCardInfo().getCardInfo().getBank().getBankCode();
			List<String> rates = supportBanks.get(bankCode);
			if(CollectionUtils.isEmpty(rates)){
				unusableSignRelationList.add(signRelationInfo);
			}else{
				usableSignRelationList.add(signRelationInfo);
			}
		}

		userSignRelationCollection.setUnusableSignRelationList(unusableSignRelationList);
		userSignRelationCollection.setUsableSignRelationList(usableSignRelationList);
		return userSignRelationCollection;
	}

	@Override
	public InstallmentBankInfo bankIsSupportedOrNot(MerchantInNetConfigResult merchantConfigInfo, String bankCode,
			BigDecimal orderAmount, int period) {
		List<InstallmentBankInfo> supportBanks = merchantConfigInfo.listInstallmentSupportBank();
		if(CollectionUtils.isEmpty(supportBanks)){
			throw new CashierBusinessException(Errors.SUPPORT_BANK_FAILED);
		}
		for (InstallmentBankInfo bankInfo : supportBanks) {
			if (bankInfo.getBankInfo().getBankCode()
					.equals(bankCode)) {
				BigDecimal minLimit = new BigDecimal(bankInfo.getBankInfo().getMinLimit());
				if (minLimit.compareTo(orderAmount) > 0) {
					throw new CashierBusinessException(Errors.LESS_THAN_MIN_LIMIT);
				}
				for (InstallmentRateInfo rate : bankInfo.getInstallmentRateInfos()) {
					if (rate.getPeriod() == period) {
						return bankInfo.copy(rate);
					}
				}
				throw new CashierBusinessException(Errors.PERIOR_NOT_SUPPPORT);
			}
		}

		throw new CashierBusinessException(Errors.SUPPORT_BANK_FAILED);
	}

}
