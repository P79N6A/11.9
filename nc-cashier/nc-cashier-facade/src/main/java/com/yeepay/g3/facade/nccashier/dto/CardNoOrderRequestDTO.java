package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;


import com.yeepay.g3.facade.cwh.param.HiddenCode;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

public class CardNoOrderRequestDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String cardNo;
	
	private long requestId;
	
	private String bankCode;
	
	private String period;
	
	private String tokenId;
	
	public CardNoOrderRequestDTO(){
		
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public void validate(){
		if(StringUtils.isBlank(cardNo)){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "cardNo未传");
		}
		if(requestId<=0){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "requestId未传");
		}
		if(StringUtils.isBlank(tokenId)){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "tokenId未传");
		}
	}

	@Override
	public String toString() {
		return "CardNoOrderRequestDTO [cardNo=" + HiddenCode.hiddenBankCardNO(cardNo) + ", requestId=" + requestId + ", bankCode=" + bankCode
				+ ", period=" + period + "]";
	}
	
}
