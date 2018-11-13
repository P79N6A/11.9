
package com.yeepay.g3.facade.payprocessor.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author peile.fan
 *
 */
public class CflPayResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 6699375424379567287L;

	/**
	 * 支付链接
	 */
	private String payUrl;

	/**
	 * 分期系统订单号
	 */
	private String bankOrderNo;
	

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
