package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 前置收银台基础入参
 * 
 * @author duangduang
 *
 */
public class APIBasicRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商编
	 */
	private String merchantNo;

	/**
	 * 订单TOKEN
	 */
	private String token;

	/**
	 * 业务方类型
	 */
	private String bizType;

	/**
	 * 版本号，目前只接收值=1.0
	 */
	private String version;

	public APIBasicRequestDTO() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	/**
	 * 参数校验
	 */
	public void validate() {
		if (StringUtils.isBlank(merchantNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",merchantNo不能为空");
		}
		if (StringUtils.isBlank(version)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",version不能为空");
		}
		if (!"1.0".equals(version)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",version格式不合法");
		}
	}

	@Override
	public String toString() {
		return "APIBasicRequestDTO [merchantNo=" + merchantNo 
				+ ", token=" + token 
				+ ", bizType=" + bizType
				+ ", version=" + version 
				+ "]";
	}

}
