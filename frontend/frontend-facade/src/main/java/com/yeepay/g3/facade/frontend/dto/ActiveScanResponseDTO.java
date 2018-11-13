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
public class ActiveScanResponseDTO extends BasicResponseDTO{

	private static final long serialVersionUID = 5622899716832997350L;

	
	/**
	 * 支付状态
	 */
	private PayStatusEnum payStatus;

	/**
	 * 二维码url
	 */
	private String codeUrl;
	
	/**
	 * 预支付id
	 */
	private String prepayId;


	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
