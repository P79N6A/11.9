package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

public class CashierBindPayRequestDTO implements Serializable {

	private static final long serialVersionUID = 8389937341209365007L;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION)
	private String tokenId;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION)
	private String bindId;

	private NeedBankCardDTO needBankCardDTO;

	private String verifycode;
	/**
	 * 支付请求ID
	 */
	@NotEmptyValidate
	private Long paymentRequestId;
	/**
	 * 支付记录ID
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.GET_SMS_FIRST)
	private Long paymentRecordId;
	
	private boolean bkFirst;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public NeedBankCardDTO getNeedBankCardDTO() {
		return needBankCardDTO;
	}

	public void setNeedBankCardDTO(NeedBankCardDTO needBankCardDTO) {
		this.needBankCardDTO = needBankCardDTO;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}


	public Long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(Long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public Long getPaymentRecordId() {
		return paymentRecordId;
	}

	public void setPaymentRecordId(Long paymentRecordId) {
		this.paymentRecordId = paymentRecordId;
	}
	
	public boolean isBkFirst() {
		return bkFirst;
	}

	public void setBkFirst(boolean bkFirst) {
		this.bkFirst = bkFirst;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CashierBindPayRequestDTO [tokenId=");
		builder.append(tokenId);
		builder.append(", bindId=");
		builder.append(bindId);
		builder.append(", needBankCardDTO=");
		builder.append(needBankCardDTO);
		builder.append(", verifycode=");
		builder.append(HiddenCode.hiddenVerifyCode(verifycode));
		builder.append(", paymentRequestId=");
		builder.append(paymentRequestId);
		builder.append(", paymentRecordId=");
		builder.append(paymentRecordId);
		builder.append("]");
		return builder.toString();
	}

}
