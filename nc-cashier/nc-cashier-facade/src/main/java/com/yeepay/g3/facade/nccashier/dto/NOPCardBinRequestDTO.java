package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

/**
 * 
 * @Description 调用NOP查询卡BIN信息DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPCardBinRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantNo未传")
	private String merchantNo;
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "cardNo未传")
	private String cardNo;

	private String cardType;

	private String bizType;

	private FirstBindCardPayRequestDTO bindPayRequest;

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public FirstBindCardPayRequestDTO getBindPayRequest() {
		return bindPayRequest;
	}

	public void setBindPayRequest(FirstBindCardPayRequestDTO bindPayRequest) {
		this.bindPayRequest = bindPayRequest;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPCardBinRequestDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append("cardNo='").append(HiddenCode.hiddenBankCardNO(cardNo)).append('\'');
		sb.append("cardType='").append(cardType).append('\'');
		sb.append("bizType='").append(bizType).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
