package com.yeepay.g3.app.nccashier.wap.vo;

public class BasicResVO extends BaseVO{

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	private String errorMsg;
	
	private String bizStatus;
	
	public BasicResVO(){
		
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

	public String getBizStatus() {
		return bizStatus;
	}

	public void setBizStatus(String bizStatus) {
		this.bizStatus = bizStatus;
	}

	@Override
	public String toString() {
		return "BasicResVO [errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", bizStatus=" + bizStatus + "]";
	}

}
