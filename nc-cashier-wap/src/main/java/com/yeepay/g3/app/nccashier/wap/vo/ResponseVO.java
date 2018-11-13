package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * 页面返回
 * 
 * @author duangduang
 * @date 2017-06-02
 */
public class ResponseVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	protected String errorCode;

	/**
	 * 错误描述信息
	 */
	protected String errorMsg;

	protected String bizStatus;

	public ResponseVO() {

	}

	public ResponseVO(String bizStatus) {
		this.bizStatus = bizStatus;
	}

	public ResponseVO(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public ResponseVO(String errorCode, String errorMsg, String bizStatus) {
		this(errorCode, errorMsg);
		this.bizStatus = bizStatus;
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

}
