package com.yeepay.g3.facade.nccashier.dto.clfeasy;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class CflEasyPreRouterRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 卡号
	 */
	private String cardNo;
	
	private String bankCode;
	
	private String token;
	
	private Integer period;
	
	private Long requestId;
	
	public CflEasyPreRouterRequestDTO(){
		
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public void validate(){
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "token未传");
		}
		if (getRequestId() == null || getRequestId() <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
		if (period==null || period.intValue()<=0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "period格式错误");
		}
		if (StringUtils.isBlank(bankCode)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "bankCode未传");
		}
		if(StringUtils.isBlank(cardNo)){
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "cardNo未传");
		}
	}
}
