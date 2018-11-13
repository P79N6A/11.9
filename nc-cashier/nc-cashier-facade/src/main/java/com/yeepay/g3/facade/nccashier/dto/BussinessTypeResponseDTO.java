package com.yeepay.g3.facade.nccashier.dto;


import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.enumtype.BusinessTypeEnum;

/**
 * 卡信息
 */
public class BussinessTypeResponseDTO extends BasicResponseDTO implements Serializable {
	private static final long serialVersionUID = -6827917981712558732L;
	/**
	 * 支付业务类型
	 */
	public BusinessTypeEnum businessTypeEnum;
	/**
	 * 支付请求ID
	 */
	public long requestId;



	public BusinessTypeEnum getBusinessTypeEnum() {
		return businessTypeEnum;
	}



	public void setBusinessTypeEnum(BusinessTypeEnum businessTypeEnum) {
		this.businessTypeEnum = businessTypeEnum;
	}



	public long getRequestId() {
		return requestId;
	}



	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
