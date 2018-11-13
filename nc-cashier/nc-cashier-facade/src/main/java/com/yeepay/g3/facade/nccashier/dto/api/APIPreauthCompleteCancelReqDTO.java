package com.yeepay.g3.facade.nccashier.dto.api;

import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

public class APIPreauthCompleteCancelReqDTO extends APIBasicRequestDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 卡号
	 */
	private String cardNo;

//	/**
//	 * 收银台预授权完成阶段返回的支付子单号
//	 */
//	private String recordId;
	
	/**
	 * 支付处理器的支付记录ID
	 */
	private String paymentOrderNo;

	public APIPreauthCompleteCancelReqDTO() {

	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

//	public String getRecordId() {
//		return recordId;
//	}
//
//	public void setRecordId(String recordId) {
//		this.recordId = recordId;
//	}

	public String getPaymentOrderNo() {
		return paymentOrderNo;
	}

	public void setPaymentOrderNo(String paymentOrderNo) {
		this.paymentOrderNo = paymentOrderNo;
	}

	@Override
	public void validate() {
		super.validate();
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "tokenId未传");
		}
		if (StringUtils.isBlank(cardNo)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "cardNo未传");
		}
		if (StringUtils.isBlank(paymentOrderNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",paymentOrderNo不能为空");
		}
	}

	@Override
	public String toString() {
		return "APIPreauthCompleteCancelReqDTO [paymentOrderNo=" + paymentOrderNo 
				+ ", cardNo=" + HiddenCode.hiddenBankCardNO(cardNo) 
				+ ", bizType=" + getBizType() 
				+ ", merchantNo=" + getMerchantNo()
				+ ", token=" + getToken() 
				+ ", version=" + getVersion() + "]";
	}

}
