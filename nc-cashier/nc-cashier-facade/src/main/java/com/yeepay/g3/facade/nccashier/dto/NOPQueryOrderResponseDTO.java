package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 
 * @Description 调用NOP查询订单响应DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPQueryOrderResponseDTO extends BasicResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;
	private String merchantNo;
	/**
	 * NOP订单状态 INIT（初始化）， BIND_SUCCESS（绑卡成功）， BIND_FAILURE（绑卡失败），
	 * DOING（处理中），CLOSED（关闭）
	 */
	private String orderStatus;
	private String requestFlowId;
	private String merchantFlowId;
	private String nopOrderId;

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

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

	public String getMerchantFlowId() {
		return merchantFlowId;
	}

	public void setMerchantFlowId(String merchantFlowId) {
		this.merchantFlowId = merchantFlowId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPQueryOrderResponseDTO{");
		sb.append(", orderStatus='").append(orderStatus).append('\'');
		sb.append(", merchantNo='").append(merchantNo).append('\'');
		sb.append(", merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append(", requestFlowId='").append(requestFlowId).append('\'');
		sb.append(", nopOrderId='").append(nopOrderId).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
