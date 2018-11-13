package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class APINoCardBasicRequestDTO extends APIBasicRequestDTO {

	private static final long serialVersionUID = 1L;

	private String userNo;

	private String userType;

	private String userIp;

	public APINoCardBasicRequestDTO() {
		super();
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Override
	public void validate() {
		super.validate();
		if (StringUtils.isBlank(userIp)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",userIp不能为空");
		}
	}
}
