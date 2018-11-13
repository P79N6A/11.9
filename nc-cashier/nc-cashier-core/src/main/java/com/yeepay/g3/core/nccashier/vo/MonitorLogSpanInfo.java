package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

public class MonitorLogSpanInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String event;
	private String merchantOrderNo;
	
	private String merchantNo;
	private String status;
	
	private String bizSys;
	private String payTool;
	private String platform;
	private String responseCode;
	
	private String responseMsg;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBizSys() {
		return bizSys;
	}

	public void setBizSys(String bizSys) {
		this.bizSys = bizSys;
	}

	public String getPayTool() {
		return payTool;
	}

	public void setPayTool(String payTool) {
		this.payTool = payTool;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

}
