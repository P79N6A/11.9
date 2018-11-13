package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @Description 绑卡订单流水号
 * @author yangmin.peng
 * @since 2017年8月22日下午4:58:06
 */
public class NopBindCardOrderInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 调用方请求号
	 */
	private String requestFlowId;

	/**
	 * NOP订单号
	 */
	private String nopOrderId;

	public String getRequestFlowId() {
		return requestFlowId;
	}

	public void setRequestFlowId(String requestFlowId) {
		this.requestFlowId = requestFlowId;
	}

	public String getNopOrderId() {
		return nopOrderId;
	}

	public void setNopOrderId(String nopOrderId) {
		this.nopOrderId = nopOrderId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
