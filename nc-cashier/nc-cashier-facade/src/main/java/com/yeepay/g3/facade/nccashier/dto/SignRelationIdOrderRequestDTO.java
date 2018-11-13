package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 签约关系ID下单请求入参
 * 
 * @author duangduang
 *
 */
public class SignRelationIdOrderRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 签约关系ID
	 */
	private long signRelationId;

	/**
	 * 期数
	 */
	private String period;

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * paymentRequestId
	 */
	private long requestId;

	/**
	 * token
	 */
	private String tokenId;

	public SignRelationIdOrderRequestDTO() {

	}

	public long getSignRelationId() {
		return signRelationId;
	}

	public void setSignRelationId(long signRelationId) {
		this.signRelationId = signRelationId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public void validate() {
		if (StringUtils.isBlank(bankCode)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "bankCode未传");
		}
		if (StringUtils.isBlank(period)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "period未传");
		}
		if (requestId <= 0) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "requestId未传");
		}
		if (StringUtils.isBlank(tokenId)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "token未传");
		}
		if (signRelationId <= 0) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "signRelationId未传");
		}
	}

	@Override
	public String toString() {
		return "SignRelationIdOrderRequestDTO [signRelationId=" + signRelationId + ", period=" + period + ", bankCode="
				+ bankCode + ", requestId=" + requestId + "]";
	}

}
