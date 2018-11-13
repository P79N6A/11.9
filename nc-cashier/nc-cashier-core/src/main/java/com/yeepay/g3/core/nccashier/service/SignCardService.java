package com.yeepay.g3.core.nccashier.service;

import java.util.List;

import com.yeepay.g3.core.nccashier.vo.*;

public interface SignCardService {

	List<SignRelationInfo> getSignCardList(CashierUserInfo externalUserInfo);

	/**
	 * @title 外部用户签约关系查询接口
	 * @description 根据签约关系ID获取外部用户的签约关系
	 * @param signRelationId
	 * @return
	 */
	SignRelationInfo getSignCardInfoBySignRelationId(String signRelationId);

	/**
	 * 过滤签约关系列表
	 * 将userCenter返回的可用签约关系列表进行二次过滤，与配置中心配置的分期支持银行列表取交集
	 * @param allSignRelationList
	 * @param merchantConfigInfo
	 * @return
	 */
	UserSignRelationCollection filterSignRelationList(List<SignRelationInfo> allSignRelationList,
			MerchantInNetConfigResult merchantConfigInfo);


	SignRelationInfo getSignCardInfoByCardNo(String cardN0, CashierUserInfo externalUser);

}
