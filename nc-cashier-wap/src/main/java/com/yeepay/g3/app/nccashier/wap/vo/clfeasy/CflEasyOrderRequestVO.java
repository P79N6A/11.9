package com.yeepay.g3.app.nccashier.wap.vo.clfeasy;

import java.io.Serializable;

import com.yeepay.g3.app.nccashier.wap.vo.BindCardInfoVO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class CflEasyOrderRequestVO extends BindCardInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;

	private String bankCode;

	private int period;

	public CflEasyOrderRequestVO() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public void validate() {
		if (StringUtils.isBlank(getBankCode())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
		}
		if (StringUtils.isBlank(getCardno())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
		}
		if (period <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
		}
	}
}
