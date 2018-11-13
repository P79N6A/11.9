package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

/**
 * 查询支付请求对应银行卡必填项入参
* @author zhen.tan
* @since：2016年5月27日 上午10:27:39
*/
public class CardValidateRequestDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 850352132752575150L;

	/**
	 * 支付请求ID
	 */
	@NumberValidate
	@ErrorDesc(error=Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="requestId未传")
	private long requestId;

	@NotEmptyValidate
	@ErrorDesc(error=Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="cardno未传")
	private String cardno;
	/**
	 * 查询银行规则需要
	 */
	private String cusType;
	
	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}	

	public String getCusType() {
		return cusType;
	}

	public void setCusType(String cusType) {
		this.cusType = cusType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CardValidateRequestDTO{");
		sb.append(", requestId='").append(this.requestId).append('\'');
		sb.append(", cardno='").append(HiddenCode.hiddenBankCardNO(cardno)).append('\'');
		sb.append(", cusType='").append(cusType).append('\'');
		sb.append('}');
		return sb.toString();
	}
	
}
