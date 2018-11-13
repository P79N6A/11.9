package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

/**
 * @Description 调用NOP重新发送短验请求DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPAuthBindSMSRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantNo未传")
	private String merchantNo;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantFlowId未传")
	private String merchantFlowId;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "requestFlowId未传")
	private String requestFlowId;

	private String smsTemplateId;

	private String smsInfo;

	private String adviceSmsType;

	private FirstBindCardPayRequestDTO bindPayRequest;

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

	public FirstBindCardPayRequestDTO getBindPayRequest() {
		return bindPayRequest;
	}

	public void setBindPayRequest(FirstBindCardPayRequestDTO bindPayRequest) {
		this.bindPayRequest = bindPayRequest;
	}

	public String getSmsTemplateId() {
		return smsTemplateId;
	}

	public void setSmsTemplateId(String smsTemplateId) {
		this.smsTemplateId = smsTemplateId;
	}

	public String getSmsInfo() {
		return smsInfo;
	}

	public void setSmsInfo(String smsInfo) {
		this.smsInfo = smsInfo;
	}

	public String getAdviceSmsType() {
		return adviceSmsType;
	}

	public void setAdviceSmsType(String adviceSmsType) {
		this.adviceSmsType = adviceSmsType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPAuthBindSMSRequestDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append(", merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append(", requestFlowId='").append(requestFlowId).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
