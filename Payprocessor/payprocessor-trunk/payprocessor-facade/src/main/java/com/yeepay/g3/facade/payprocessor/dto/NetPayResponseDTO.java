package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

public class NetPayResponseDTO extends BasicResponseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 平台类型
	 */
	protected PlatformType platformType;

	/**
	 * 支付链接
	 */
	private String payUrl;
	
	/**
	 * 提交银行页面编码
	 */
	private String charset;
	
	/**
	 * 提交银行方法
	 */
	private String method;
	
	/**
	 * 提交银行参数
	 */
	private Map<String, Object> param;
	/**
	 * 银行URL
	 */
	private String bankPayUrl;
	
	
	public String getPayUrl() {
		return payUrl;
	}


	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
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


	public Map<String, Object> getParam() {
		return param;
	}


	public void setParam(Map<String, Object> param) {
		this.param = param;
	}


	public PlatformType getPlatformType() {
		return platformType;
	}


	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}


	public String getBankPayUrl() {
		return bankPayUrl;
	}


	public void setBankPayUrl(String bankPayUrl) {
		this.bankPayUrl = bankPayUrl;
	}


	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

}
