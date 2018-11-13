package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class SignRelationQueryRequestDTO implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(SignRelationQueryRequestDTO.class);

	private static final long serialVersionUID = 1L;

	private String userNo;

	private String userType;

	private String merchantNo;

	private String version;

	public SignRelationQueryRequestDTO() {

	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public void validate() {
		if (StringUtils.isBlank(merchantNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",merchantNo不能为空");
		}
		if (!"1.0".equals(version)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",version不合法");
		}
		if (StringUtils.isBlank(userNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",userNo不能为空");
		}
		if (StringUtils.isBlank(userType)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",userType不能为空");
		}
		IdentityType identityType = null;
		try {
			identityType = IdentityType.valueOf(userType);
		} catch (Throwable t) {
			logger.warn(this + "转化userType为IdentityType异常,", t);
		}
		if (identityType == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",userType不合法");
		}
	}

	public void buildSignRelationQueryResponseDTO(SignRelationQueryResponseDTO responseDTO) {
		responseDTO.setMerchantNo(merchantNo);
		responseDTO.setUserNo(userNo);
		responseDTO.setUserType(userType);
	}

}
