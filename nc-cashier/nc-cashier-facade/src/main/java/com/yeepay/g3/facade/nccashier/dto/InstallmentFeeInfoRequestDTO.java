package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 银行卡分期手续费金额获取请求入参
 * 
 * @author duangduang
 *
 */
public class InstallmentFeeInfoRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 期数
	 */
	private String period;

	/**
	 * paymentRequest的主键
	 */
	private long requestId;

	public InstallmentFeeInfoRequestDTO() {

	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public void validate() {
		if (StringUtils.isBlank(bankCode)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "bankCode未传");
		}
		if (StringUtils.isBlank(period)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "period未传");
		}
		if (requestId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
	}

	@Override
	public String toString() {
		return "InstallmentFeeInfoRequestDTO [bankCode=" + bankCode + ", period=" + period + ", requestId=" + requestId
				+ "]";
	}
}
