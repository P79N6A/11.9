package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 前置收银台账户支付请求入参
 * 
 * @author duangduang
 *
 */
public class APIMerchantAccountPayRequestDTO extends APIBasicRequestDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户请求IP，需要传到goodsInfo，由支付系统传给风控系统的
	 */
	private String userIp;

	/**
	 * 支付密码，预留字段
	 */
	private String pwsd;
	
	/**
	 * 扩展字段 暂时不用
	 */
	private String extInfo;

	public APIMerchantAccountPayRequestDTO() {

	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getPwsd() {
		return pwsd;
	}

	public void setPwsd(String pwsd) {
		this.pwsd = pwsd;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	@Override
	public void validate() {
		super.validate();
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
		}
		if (StringUtils.isBlank(userIp)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",userIp不能为空");
		}
	}

	@Override
	public String toString() {
		return "APIMerchantAccountPayRequestDTO [userIp=" + userIp 
				+ ", merchantNo=" + getMerchantNo() 
				+ ", bizType=" + getBizType() 
				+ ", token=" + getToken() 
				+ ", version=" + getVersion() 
				+ ", extInfo=" + getExtInfo()
				+ "]";
	}

}
