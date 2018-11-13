/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;

/**
 * @author TML
 *
 */
@Deprecated
public class AppPayResponseDTO extends BasicResponseDTO{

	private static final long serialVersionUID = 1287291057618734374L;

	/**
	 * H5跳转链接
	 */
	private String codeUrl;
	
	/**
	 * 支付状态
	 */
	private PayStatusEnum payStatus;

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
