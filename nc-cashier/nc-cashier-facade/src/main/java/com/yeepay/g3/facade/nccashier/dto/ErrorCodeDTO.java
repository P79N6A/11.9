package com.yeepay.g3.facade.nccashier.dto;

public class ErrorCodeDTO {

	/**
	 * 原始错误码
	 */
	private String originErrorCode;
	/**
	 * 支付系统编码
	 */
	private String paySysCode;
	/**
	 * 原始错误信息
	 */
	private String originErrorMsg;
	/**
	 * 对外提示错误码
	 */
	private String externalErrorCode;
	/**
	 * 对外提示错误信息
	 */
	private String externalErrorMsg;
	
	
	public ErrorCodeDTO(){
		
	}
	
	public ErrorCodeDTO(String paySysCode, String originErrorCode, String originErrorMsg){
		this.paySysCode = paySysCode;
		this.originErrorCode = originErrorCode;
		this.originErrorMsg = originErrorMsg;
	}

	public String getOriginErrorCode() {
		return originErrorCode;
	}

	public void setOriginErrorCode(String originErrorCode) {
		this.originErrorCode = originErrorCode;
	}

	public String getPaySysCode() {
		return paySysCode;
	}

	public void setPaySysCode(String paySysCode) {
		this.paySysCode = paySysCode;
	}

	public String getOriginErrorMsg() {
		return originErrorMsg;
	}

	public void setOriginErrorMsg(String originErrorMsg) {
		this.originErrorMsg = originErrorMsg;
	}

	public String getExternalErrorCode() {
		return externalErrorCode;
	}

	public void setExternalErrorCode(String externalErrorCode) {
		this.externalErrorCode = externalErrorCode;
	}

	public String getExternalErrorMsg() {
		return externalErrorMsg;
	}

	public void setExternalErrorMsg(String externalErrorMsg) {
		this.externalErrorMsg = externalErrorMsg;
	}
}
