package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 
 * @Description 调用NOP进行重发短验响应DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPAuthBindSMSResponseDTO extends BasicResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;
	private String merchantNo;
	private String merchantFlowId;
	private String nopOrderId;
	private String requestFlowId;
	private String smsSender;
	private String smsType;
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

	public String getRequestFlowId() {
		return requestFlowId;
	}

	public void setRequestFlowId(String requestFlowId) {
		this.requestFlowId = requestFlowId;
	}

	public String getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(String smsSender) {
		this.smsSender = smsSender;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPAuthBindSMSResponseDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append("merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append("requestFlowId='").append(requestFlowId).append('\'');
		sb.append("nopOrderId='").append(nopOrderId).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
