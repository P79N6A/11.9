package com.yeepay.g3.app.nccashier.wap.vo.externalsys;

public class NcauthReceiveBankRequestParam {

	private String requestId;
	
	private String errorCode;
	
	private String errorMsg;
	
	/**
	 * 是否鉴权成功
	 */
	private boolean success;
	
	public NcauthReceiveBankRequestParam(){
		
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
