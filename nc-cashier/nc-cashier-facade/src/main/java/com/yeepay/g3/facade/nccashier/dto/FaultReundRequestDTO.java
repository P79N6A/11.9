package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;


/**
 * 
 * <p>差错退款请求参数</p>
 */
public class FaultReundRequestDTO extends ReverseRequestDTO {
	

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="支付订单号payOrderId未传")
	private String payOrderId;
	
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="交易订单号tradeSysOrderId未传")
	private String tradeSysOrderId;
	
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="交易系统编码tradeSysNo未传")
	private String tradeSysNo;

	public String getTradeSysOrderId() {
		return tradeSysOrderId;
	}

	public void setTradeSysOrderId(String tradeSysOrderId) {
		this.tradeSysOrderId = tradeSysOrderId;
	}

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}
	
	
}
