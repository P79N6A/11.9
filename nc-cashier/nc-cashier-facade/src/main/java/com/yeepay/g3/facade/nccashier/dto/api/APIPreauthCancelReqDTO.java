package com.yeepay.g3.facade.nccashier.dto.api;

import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 预授权撤销返回值 - 给商户提供的接口
 * @author duangduang
 *
 */
public class APIPreauthCancelReqDTO extends APIBasicRequestDTO{

	private static final long serialVersionUID = 1L;

	/**
	 * 收银台发起的支付子单号
	 */
//	private String recordId;
	
	/**
	 * 支付处理器子单号
	 */
	private String paymentOrderNo;
	 
	public APIPreauthCancelReqDTO(){
		
	}

	public String getPaymentOrderNo() {
		return paymentOrderNo;
	}

	public void setPaymentOrderNo(String paymentOrderNo) {
		this.paymentOrderNo = paymentOrderNo;
	}

//	public String getRecordId() {
//		return recordId;
//	}
//
//	public void setRecordId(String recordId) {
//		this.recordId = recordId;
//	}
	
	@Override
	public String toString() {
		return "APIPreauthCancelReqDTO [" 
				+ "paymentOrderNo=" + paymentOrderNo 
				+ ", bizType=" + getBizType() 
				+ ", merchantNo=" + getMerchantNo() 
				+ ", token=" + getToken() 
				+ ", version=" + getVersion() 
				+ "]";
	}
	
	@Override
	public void validate() {
		super.validate();
		if (StringUtils.isEmpty(getToken())) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "token未传");
		}
		if (StringUtils.isBlank(paymentOrderNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",paymentOrderNo不能为空");
		}
	}
	
}
