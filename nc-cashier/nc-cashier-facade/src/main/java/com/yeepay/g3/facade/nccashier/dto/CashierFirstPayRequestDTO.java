package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

/**
 * 确认支付请求DTO
 * 
 * @author：peile.fan
 * @since：2016年5月26日 下午4:45:31
 * @version:
 */
public class CashierFirstPayRequestDTO implements Serializable {

	private static final long serialVersionUID = -2822859547464180009L;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION)
	private String tokenId;

	private String verifycode;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION)
	private CardInfoDTO cardInfo;
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


	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public CardInfoDTO getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(CardInfoDTO cardInfo) {
		this.cardInfo = cardInfo;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CashierFirstPayRequestDTO [tokenId=");
		builder.append(tokenId);
		builder.append(", verifycode=");
		builder.append(HiddenCode.hiddenVerifyCode(verifycode));
		builder.append(", cardInfo=");
		builder.append(cardInfo);
		builder.append(", paymentRequestId=");
		builder.append(paymentRequestId);
		builder.append(", paymentRecordId=");
		builder.append(paymentRecordId);
		builder.append("]");
		return builder.toString();
	}

}
