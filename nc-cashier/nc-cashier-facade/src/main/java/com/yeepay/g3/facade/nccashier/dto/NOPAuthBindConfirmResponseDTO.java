package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 
 * @Description 调用NOP进行鉴权绑卡响应DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPAuthBindConfirmResponseDTO extends BasicResponseDTO implements Serializable {

	private static final long serialVersionUID = 731147649612278786L;
	private String merchantNo;
	private String merchantFlowId;
	private String bindId;
	private String nopOrderId;
	private String requestFlowId;
	private FirstBindCardPayResponseDTO bindPayResponse;

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

	public String getNopOrderId() {
		return nopOrderId;
	}

	public void setNopOrderId(String nopOrderId) {
		this.nopOrderId = nopOrderId;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getRequestFlowId() {
		return requestFlowId;
	}

	public void setRequestFlowId(String requestFlowId) {
		this.requestFlowId = requestFlowId;
	}

	public FirstBindCardPayResponseDTO getBindPayResponse() {
		return bindPayResponse;
	}

	public void setBindPayResponse(FirstBindCardPayResponseDTO bindPayResponse) {
		this.bindPayResponse = bindPayResponse;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPAuthBindConfirmResponseDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append("merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append("requestFlowId='").append(requestFlowId).append('\'');
		sb.append("nopOrderId='").append(nopOrderId).append('\'');
		sb.append("bindId='").append(bindId).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
