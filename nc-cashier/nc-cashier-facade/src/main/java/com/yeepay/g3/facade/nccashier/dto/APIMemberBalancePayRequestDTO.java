package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 个人余额支付请求参数
 * 
 * @author duangduang
 *
 */
public class APIMemberBalancePayRequestDTO extends APIBasicRequestDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 商户用户编号
	 */
	private String merchantUserNo;

	/**
	 * 付款商编
	 */
	private String payMerchantNo;

	/**
	 * 用户IP
	 */
	private String userIp;

	/**
	 * 扩展字段 暂时不用
	 */
	private String extInfo;

	public APIMemberBalancePayRequestDTO() {

	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getExtInfo() {
		return extInfo;
	}

	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}


	public String getMerchantUserNo() {
		return merchantUserNo;
	}

	public void setMerchantUserNo(String merchantUserNo) {
		this.merchantUserNo = merchantUserNo;
	}

	public String getPayMerchantNo() {
		return payMerchantNo;
	}

	public void setPayMerchantNo(String payMerchantNo) {
		this.payMerchantNo = payMerchantNo;
	}

	@Override
	public void validate() {
		super.validate();
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
		}
		if (StringUtils.isBlank(merchantUserNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",merchantUserNo不能为空");
		}
	}

	@Override
	public String toString() {
		return "APIMemberBalancePayRequestDTO{" +
				super.toString() + '\'' +
				", merchantUserNo='" + merchantUserNo + '\'' +
				", payMerchantNo='" + payMerchantNo + '\'' +
				", userIp='" + userIp + '\'' +
				", extInfo='" + extInfo + '\'' +
				'}';
	}
}
