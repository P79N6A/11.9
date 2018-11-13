package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * 基础返回VO，err信息为非驼峰
 * @author duangduang
 *
 */
public class BasicResponseVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	protected String errorcode;

	/**
	 * 错误描述信息
	 */
	protected String errormsg;

	protected String bizStatus;

	public BasicResponseVO() {

	}

	public BasicResponseVO(String bizStatus) {
		this.bizStatus = bizStatus;
	}

	public BasicResponseVO(String errorcode, String errormsg) {
		this.errorcode = errorcode;
		this.errormsg = errormsg;
	}

	public BasicResponseVO(String errorcode, String errormsg, String bizStatus) {
		this(errorcode, errormsg);
		this.bizStatus = bizStatus;
	}

	public String getBizStatus() {
		return bizStatus;
	}

	public void setBizStatus(String bizStatus) {
		this.bizStatus = bizStatus;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	@Override
	public String toString() {
		return "BasicResponseVO [errorcode=" + errorcode + ", errormsg=" + errormsg + ", bizStatus=" + bizStatus + "," + super.toString() + "]";
	}

}
