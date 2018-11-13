package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 
 * @Description 调用NOP查询绑卡产品是否开通响应DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPQueryBindCardOpenStatusResponseDTO extends BasicResponseDTO implements Serializable {

	private static final long serialVersionUID = 731147649612278786L;
	private String merchantNo;
	private String status;

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPQueryBindCardOpenStatusResponseDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append(", status='").append(status).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
