package com.yeepay.g3.facade.nccashier.dto.api;

import java.math.BigDecimal;

import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 预授权完成请求入参
 * 
 * @author duangduang
 *
 */
public class APIPreauthCompleteReqDTO extends APIBasicRequestDTO {

	private static final long serialVersionUID = 1L;

//	/**
//	 * 收银台的支付子单号
//	 */
//	private String recordId;
	
	/**
	 * 预授权完成金额
	 */
	private BigDecimal completeAmount;
	
	/**
	 * 支付处理器子单号
	 */
	private String paymentOrderNo;

	public APIPreauthCompleteReqDTO() {

	}
	
//	public String getRecordId() {
//		return recordId;
//	}
//
//	public void setRecordId(String recordId) {
//		this.recordId = recordId;
//	}
	
	

	public BigDecimal getCompleteAmount() {
		return completeAmount;
	}

	public void setCompleteAmount(BigDecimal completeAmount) {
		this.completeAmount = completeAmount;
	}

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
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "token未传");
		}
		if (completeAmount==null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "completeAmount未传");
		}
		if (StringUtils.isBlank(paymentOrderNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",paymentOrderNo不能为空");
		}
	}

	@Override
	public String toString() {
		return "APIPreauthCompleteReqDTO [" 
				+ "paymentOrderNo=" + paymentOrderNo 
				+ ", bizType=" + getBizType() 
				+ ", merchantNo=" + getMerchantNo() 
				+ ", token=" + getToken() 
				+ ", version=" + getVersion() 
				+ "]";
	}

}
