package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.PersonalMemberBanlancePayRequestInfo;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;

public interface PersonalMemberService {
	
	/**
	 * 个人会员余额支付
	 * 
	 * @param requestInfo
	 * @param orderInfo
	 * @param merchantInNetConfig
	 * @param productLevel
	 */
	void balancePay(PersonalMemberBanlancePayRequestInfo requestInfo, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig, ProductLevel productLevel);

	/**
	 * 查询有效的三代会员编号
	 * (会员状态 = MemberStatusEnum.AVAILABLY)
	 * @param merchantNo 商编
	 * @param merchantUserNo 商户的用户编号
	 * @return
	 */
	String queryValidMemberNo(String merchantNo, String merchantUserNo);
}
