package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by xiewei on 15-10-21.
 */
public class UserAccessDTO implements Serializable {
	private static final long serialVersionUID = -5795225941751774976L;
	/**
	 * 支付请求ID
	 */
	@NotBlank
	private String paymentRequestId;
	/**
	 * 支付记录ID
	 */
	private String paymentRecordId;
	/**
	 * 商户编号
	 */
	@NotBlank
	private String merchantNo;
	/**
	 * 交易系统订单号
	 */
	private String tradeSysOrderId;
	/**
	 * 交易系统编码
	 */
	private String tradeSysNo;
	/**
	 * 订单方订单号
	 */
	private String orderOrderId;
	/**
	 * 订单方编码
	 */
	private String orderSysNo;
	/**
	 * TokenID
	 */
	@NotBlank
	private String tokenId;
	/**
	 * 用户IP
	 */
	@NotBlank
	private String userIp;
	/**
	 * 用户UA
	 */
	@NotBlank
	private String userUa;
	/**
	 * 请求信息
	 */
	private RequestInfoDTO requestInfoDTO;

	private String urlParamInfo;

	private String merchantConfigInfo;

	private CashierVersionEnum cashierVersionEnum;

	public String getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(String paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public String getPaymentRecordId() {
		return paymentRecordId;
	}

	public void setPaymentRecordId(String paymentRecordId) {
		this.paymentRecordId = paymentRecordId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTradeSysOrderId() {
		return tradeSysOrderId;
	}

	public void setTradeSysOrderId(String tradeSysOrderId) {
		this.tradeSysOrderId = tradeSysOrderId;
	}

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}

	public String getOrderOrderId() {
		return orderOrderId;
	}

	public void setOrderOrderId(String orderOrderId) {
		this.orderOrderId = orderOrderId;
	}

	public String getOrderSysNo() {
		return orderSysNo;
	}

	public void setOrderSysNo(String orderSysNo) {
		this.orderSysNo = orderSysNo;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserUa() {
		return userUa;
	}

	public void setUserUa(String userUa) {
		this.userUa = userUa;
	}

	public RequestInfoDTO getRequestInfoDTO() {
		return requestInfoDTO;
	}

	public void setRequestInfoDTO(RequestInfoDTO requestInfoDTO) {
		this.requestInfoDTO = requestInfoDTO;
	}

	public CashierVersionEnum getCashierVersionEnum() {
		return cashierVersionEnum;
	}

	public void setCashierVersionEnum(CashierVersionEnum cashierVersionEnum) {
		this.cashierVersionEnum = cashierVersionEnum;
	}

	public String getUrlParamInfo() {
		return urlParamInfo;
	}

	public void setUrlParamInfo(String urlParamInfo) {
		this.urlParamInfo = urlParamInfo;
	}

	public String getMerchantConfigInfo() {
		return merchantConfigInfo;
	}

	public void setMerchantConfigInfo(String merchantConfigInfo) {
		this.merchantConfigInfo = merchantConfigInfo;
	}
}
