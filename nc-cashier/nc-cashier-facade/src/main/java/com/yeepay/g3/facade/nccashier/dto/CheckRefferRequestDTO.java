/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * @author zhen.tan
 *	分控校验reffer入参
 */
public class CheckRefferRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2203274525903537947L;

	private String merchantAccountCode;
	
	private String reffer;
	
	private String bizOrderId;

	public String getBizOrderId() {
		return bizOrderId;
	}

	public void setBizOrderId(String bizOrderId) {
		this.bizOrderId = bizOrderId;
	}

	public String getMerchantAccountCode() {
		return merchantAccountCode;
	}

	public void setMerchantAccountCode(String merchantAccountCode) {
		this.merchantAccountCode = merchantAccountCode;
	}

	public String getReffer() {
		return reffer;
	}

	public void setReffer(String reffer) {
		this.reffer = reffer;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CheckRefferRequestDTO{");
		sb.append("merchantAccountCode='").append(this.merchantAccountCode).append('\'');
		sb.append(", reffer='").append(this.reffer).append('\'');
		sb.append(", bizOrderId='").append(this.bizOrderId).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
