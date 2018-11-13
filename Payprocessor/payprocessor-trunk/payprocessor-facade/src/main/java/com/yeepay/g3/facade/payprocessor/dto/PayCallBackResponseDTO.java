package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.yeepay.g3.facade.payprocessor.enumtype.CallBackStatusEnum;

/**
 * @author peile.fan
 *
 */
public class PayCallBackResponseDTO implements Serializable {

	private static final long serialVersionUID = -991655473325547056L;


	/**
	 * 系统唯一码
	 */
	private String dealUniqueSerialNo;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;

	/**
	 * 订单方
	 */
	private String orderSystem;

	/**
	 * 订单方订单号
	 * 
	 */
	private String orderNo;

	/**
	 * 回调订单状态
	 */

	private CallBackStatusEnum callBackStatus;
	/**
	 * 错误码
	 */
	protected String responseCode;

	/**
	 * 错误码描述
	 */
	protected String responseMsg;

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOrderSystem() {
		return orderSystem;
	}

	public void setOrderSystem(String orderSystem) {
		this.orderSystem = orderSystem;
	}

	public CallBackStatusEnum getCallBackStatus() {
		return callBackStatus;
	}

	public void setCallBackStatus(CallBackStatusEnum callBackStatus) {
		this.callBackStatus = callBackStatus;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
