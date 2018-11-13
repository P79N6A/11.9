package com.yeepay.g3.core.nccashier.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.vo.*;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.UserCenterService;
import com.yeepay.g3.core.nccashier.service.SignCardService;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;

@Service("signCardService")
public class SignCardServiceImpl implements SignCardService {

	@Resource
	private UserCenterService userCenterService;


	@Override
	public List<SignRelationInfo> getSignCardList(CashierUserInfo externalUser) {
		return userCenterService.getSignRelationList(externalUser);
	}

	@Override
	public UserSignRelationCollection filterSignRelationList(List<SignRelationInfo> allSignRelationList,
			MerchantInNetConfigResult merchantConfigInfo) {
		UserSignRelationCollection userSignRelationCollection = new UserSignRelationCollection();
		if (merchantConfigInfo == null) {
			userSignRelationCollection.setUnusableSignRelationList(allSignRelationList);
			return userSignRelationCollection;
		}
		Map<String, List<String>> supportBanks = merchantConfigInfo.getInstallmentCashierTemplate();
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
			List<String> numbers = supportBanks.get(bankCode);
			if(CollectionUtils.isNotEmpty(numbers)){
				usableSignRelationList.add(signRelationInfo);
			}else{
				unusableSignRelationList.add(signRelationInfo);
			}
		}

		userSignRelationCollection.setUnusableSignRelationList(unusableSignRelationList);
		userSignRelationCollection.setUsableSignRelationList(usableSignRelationList);
		return userSignRelationCollection;
	}

	@Override
	public SignRelationInfo getSignCardInfoBySignRelationId(String signRelationId) {
		SignRelationInfo signRelationInfo = userCenterService.getUserSignInfoBySignRelationId(signRelationId);
		if (signRelationInfo == null || !signRelationInfo.isSignRelationIllegal()) {
			throw new CashierBusinessException(Errors.SYSTEM_SIGNRID_NULL);
		}
		return signRelationInfo;
	}


	@Override
	public SignRelationInfo getSignCardInfoByCardNo(String cardN0, CashierUserInfo externalUser) {
		return userCenterService.getSignCardInfoByCardN0(cardN0, externalUser);
	}


}
