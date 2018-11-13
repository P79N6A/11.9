package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

public class APIPreauthCompleteCancelRequestDTO extends APIBasicRequestDTO{

	private static final long serialVersionUID = 1L;

	/**
	 * 支付处理器的支付子订单号
	 */
	private String payOrderId;
	
	/**
	 * 卡号
	 */
	private String cardNo;
	
	public APIPreauthCompleteCancelRequestDTO(){
		
	}

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Override
	public void validate(){
		super.validate();
		if(StringUtils.isBlank(payOrderId)){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",payOrderId不能为空");
		}
	}

	@Override
	public String toString() {
		return "APIPreauthCompleteCancelRequestDTO [payOrderId=" + payOrderId 
				+ ", cardNo=" + HiddenCode.hiddenBankCardNO(cardNo) 
				+ ", bizType=" + getBizType()
				+ ", merchantNo=" + getMerchantNo()
				+ ", token=" + getToken()
				+ ", version=" + getVersion()
				+ "]";
	}
	
	
}
