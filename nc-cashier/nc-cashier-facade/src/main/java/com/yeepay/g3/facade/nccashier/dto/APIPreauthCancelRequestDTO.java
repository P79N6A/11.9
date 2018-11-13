package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 预授权撤销接口
 * @author duangduang
 *
 */
public class APIPreauthCancelRequestDTO extends APIBasicRequestDTO{

	private static final long serialVersionUID = 1L;

	private String payOrderId;
	
	public APIPreauthCancelRequestDTO(){
		
	}

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
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
		return "APIPreauthCancelRequestDTO [payOrderId=" + payOrderId 
				+ ", bizType=" + getBizType() 
				+ ", merchantNo=" + getMerchantNo() 
				+ ", token=" + getToken() 
				+ ", version=" + getVersion() 
				+ "]";
	}
	
	
}
