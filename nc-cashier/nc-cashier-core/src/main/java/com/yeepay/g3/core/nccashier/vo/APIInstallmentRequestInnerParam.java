package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;


public class APIInstallmentRequestInnerParam extends APIInstallmentRequestDTO{

	private static final long serialVersionUID = 1L;
	
	private CashierVersionEnum cashierVersion;
	
	
	public APIInstallmentRequestInnerParam(){
		setCashierVersion(CashierVersionEnum.API);
	}

	public CashierVersionEnum getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(CashierVersionEnum cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public void completeOrderInfo(OrderDetailInfoModel orderInfo) {
		orderInfo.setCashierVersion(cashierVersion);
		orderInfo.setUserIp(getUserIp());
		if (MemberTypeEnum.YIBAO.name().equals(getUserType())) {
			orderInfo.setMemberNo(getUserNo());
			orderInfo.setMemberType(MemberTypeEnum.YIBAO.name());
		} else {
			orderInfo.setIdentityId(getUserNo());
			orderInfo.setIdentityType(
					StringUtils.isNotBlank(getUserType()) ? IdentityType.valueOf(getUserType()) : null);
		}
	}

}
