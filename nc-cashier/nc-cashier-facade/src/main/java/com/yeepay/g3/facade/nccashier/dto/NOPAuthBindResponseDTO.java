package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 
 * @Description 调用NOP进行鉴权绑卡响应DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPAuthBindResponseDTO extends BasicResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;

    private String merchantNo;

    private String merchantFlowId;

    private String requestSystem;

    private String requestFlowId;

    private String nopOrderId;

    private String bindId;

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

	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
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

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
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
		StringBuffer sb = new StringBuffer("NOPAuthBindResponseDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append("merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append("requestFlowId='").append(requestFlowId).append('\'');
		sb.append("nopOrderId='").append(nopOrderId).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
