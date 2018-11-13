package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

/**
 * 
 * @Description 调用NOP订单查询请求DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPQueryOrderRequestDTO implements Serializable {

	private static final long serialVersionUID = 731147649612278786L;
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantNo未传")
	private String merchantNo;
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantFlowId未传")
	private String merchantFlowId;
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantFlowId未传")
	private String requestFlowId;
	private String nopOrderId;

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantFlowId() {
		return merchantFlowId;
	}

	public void setMerchantFlowId(String merchantFlowId) {
		this.merchantFlowId = merchantFlowId;
	}

	public String getRequestFlowId() {
		return requestFlowId;
	}

	public void setRequestFlowId(String requestFlowId) {
		this.requestFlowId = requestFlowId;
	}

	public String getNopOrderId() {
		return nopOrderId;
	}

	public void setNopOrderId(String nopOrderId) {
		this.nopOrderId = nopOrderId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPQueryOrderRequestDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append("merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append("requestFlowId='").append(requestFlowId).append('\'');
		sb.append("nopOrderId='").append(nopOrderId).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
