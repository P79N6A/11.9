package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 银行卡分期开通并支付的url
 * 
 * @author duangduang
 *
 */
public class UrlInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 银行卡分期开通并支付的url
	 */
	private String url;

	/**
	 * 银行卡分期开通并支付的method
	 */
	private String method;

	/**
	 * 银行卡分期开通并支付的编码
	 */
	private String charset;

	/**
	 * 银行卡分期开通并支付的参数
	 */
	private Map<String, Object> params;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "UrlInfoDTO [url=" + url + ", method=" + method + ", charset=" + charset + ", params=" + params + "]";
	}
}
