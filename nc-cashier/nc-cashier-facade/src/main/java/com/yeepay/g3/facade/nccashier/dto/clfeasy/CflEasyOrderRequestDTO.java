package com.yeepay.g3.facade.nccashier.dto.clfeasy;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class CflEasyOrderRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String token;

	private Integer period;

	private Long requestId;
	
	private CardInfoDTO cardInfo;

	public CflEasyOrderRequestDTO() {

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

	public CardInfoDTO getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(CardInfoDTO cardInfo) {
		this.cardInfo = cardInfo;
	}
	
	public void validate() {
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "token未传");
		}
		if (getRequestId() == null || getRequestId() <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
		if (period == null || period.intValue() <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "period格式错误");
		}
		if (cardInfo == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "cardInfo未传");
		}
		cardInfo.basicValidate();
	}
}
