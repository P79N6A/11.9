/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 公众号支付请求DTO
 * @author TML
 */
@Deprecated
public class ActiveScanJsapiRequestDTO extends BasicRequestDTO{
	
	private static final long serialVersionUID = 7571277979701717300L;
	
	/**
	 * 公众号下用户ID
	 */
	@NotBlank(message = "openId不能为空")
	private String openId;
	
	/**
	 * 公众号ID
	 */
	private String appId;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	

}
