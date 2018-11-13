package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @Description 商户绑卡请求VO
 * @author yangmin.peng
 * @since 2017年8月22日下午4:58:06
 */
public class BindCardAuthBindResponseVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 易宝绑卡流水号
	 */
	private String nopOrderId;

	/**
	 * 绑卡ID
	 */
	private String bindId;
	/**
	 * 请求方请求号
	 */
	private String requestFlowId;

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getNopOrderId() {
		return nopOrderId;
	}

	public void setNopOrderId(String nopOrderId) {
		this.nopOrderId = nopOrderId;
	}

	public String getRequestFlowId() {
		return requestFlowId;
	}

	public void setRequestFlowId(String requestFlowId) {
		this.requestFlowId = requestFlowId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
