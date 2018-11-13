package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.facade.nccashier.dto.UrlInfoDTO;
import com.yeepay.g3.utils.common.BeanUtils;

/**
 * @title url实体
 * @author duangduang
 *
 */
public class UrlInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String url;

	private String method;

	private String charset;

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
	
	public UrlInfoDTO buildUrlInfoDTO(){
		UrlInfoDTO urlInfoDTO = new UrlInfoDTO();
		BeanUtils.copyProperties(this, urlInfoDTO);
		return urlInfoDTO;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
