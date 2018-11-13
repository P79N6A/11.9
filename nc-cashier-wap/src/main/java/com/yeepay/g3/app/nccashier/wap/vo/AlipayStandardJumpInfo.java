package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

/**
 * 支付宝标准版从普通浏览器跳转到支付宝浏览器所需参数
 * 
 * @author duangduang
 *
 */
public class AlipayStandardJumpInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String alipayAuth2Url;

	private String authKey;

	private String status;
	
	private boolean needRedirect;

	public AlipayStandardJumpInfo() {

	}

	public String getAlipayAuth2Url() {
		return alipayAuth2Url;
	}

	public void setAlipayAuth2Url(String alipayAuth2Url) {
		this.alipayAuth2Url = alipayAuth2Url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public boolean isNeedRedirect() {
		return needRedirect;
	}

	public void setNeedRedirect(boolean needRedirect) {
		this.needRedirect = needRedirect;
	}

}
