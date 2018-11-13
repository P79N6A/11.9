package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yeepay.g3.facade.nccashier.enumtype.CombPayOrderTypeEnum;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.enumtype.PayRequestStatusEnum;

public class OrderNoticeDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2057611861516765180L;
	/**
	 * 交易系统编码
	 */
	private String tradeSysNo;
	/**
	 * 交易系统订单号
	 */
	private String tradeSysOrderId;
	/**
	 * 订单方订单号
	 */
	private String orderOrderId;
	/**
	 * 订单方编码
	 */
	private String orderSysNo;
	/**
	 * 商户编号
	 */
	private String merchantNo;
	/**
	 * 商户订单号
	 */
	private String merchantOrderId;
	/**
	 * 支付订单号
	 */
	private String paymentOrderNo;
	/**
	 * 支付金额
	 */
	private BigDecimal paymentAmount;
	/**
	 * 成本
	 */
	private BigDecimal cost;
	/**
	 * 支付时间
	 */
	private Date pay_time;
	/**
	 * 支付状态
	 */
	private PayRequestStatusEnum paymentStatus;
	/**
	 * 错误编码
	 */
	private String errorCode;
	/**
	 * 错误描述
	 */
	private String errorMsg;
	/**
	 * 支付记录明细
	 */
	private List<PaymentRecordDTO> paymentRecordDTOList;
	/**
	 * 银行子系统订单号
	 */
	private String tradeSerialNo;
	
	/*** 基础产品码*/
	private String basicProductCode;

	public String getBasicProductCode() {
		return basicProductCode;
	}

	public void setBasicProductCode(String basicProductCode) {
		this.basicProductCode = basicProductCode;
	}

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}

	public String getTradeSysOrderId() {
		return tradeSysOrderId;
	}

	public void setTradeSysOrderId(String tradeSysOrderId) {
		this.tradeSysOrderId = tradeSysOrderId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getPaymentOrderNo() {
		return paymentOrderNo;
	}

	public void setPaymentOrderNo(String paymentOrderNo) {
		this.paymentOrderNo = paymentOrderNo;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Date getPay_time() {
		return pay_time;
	}

	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
	}

	public PayRequestStatusEnum getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PayRequestStatusEnum paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<PaymentRecordDTO> getPaymentRecordDTOList() {
		return paymentRecordDTOList;
	}

	public void setPaymentRecordDTOList(List<PaymentRecordDTO> paymentRecordDTOList) {
		this.paymentRecordDTOList = paymentRecordDTOList;
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

	public String getTradeSerialNo() {
		return tradeSerialNo;
	}

	public void setTradeSerialNo(String tradeSerialNo) {
		this.tradeSerialNo = tradeSerialNo;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


}
