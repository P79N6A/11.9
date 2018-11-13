package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * url的基本属性
 * 
 * @author duangduang
 *
 */
public class UrlInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 访问链接
	 */
	private String url;

	/**
	 * 编码 
	 */
	private String charset;

	/**
	 * 表单提交方式，post/get
	 */
	private String method;

	/**
	 * 表单参数
	 */
	private Map<String, Object> params;

	public UrlInfoVO() {

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "UrlInfoVO [url=" + url + ", charset=" + charset + ", method=" + method + ", params=" + params + "]";
	}
}
