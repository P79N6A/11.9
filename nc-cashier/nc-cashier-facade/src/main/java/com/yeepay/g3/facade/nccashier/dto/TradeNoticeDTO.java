package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.facade.nccashier.enumtype.TradeStateEnum;

public class TradeNoticeDTO implements Serializable{

	private static final long serialVersionUID = -7141086536744532197L;
	/**
	 * 交易状态
	 */
	private TradeStateEnum tradeState;
	/**
	 * 支付金额
	 */
	private BigDecimal paymentAmount;
	/**
	 * 收银台 wap pc
	 */
	private String cashierVersion;
	/**
	 * 支付请求id
	 */
	private long paymentRequestId;
	/**
	 * 错误编码
	 */
	private String errorCode;
	/**
	 * 错误原因
	 */
	private String errorMsg;
	/**
	 * 返回商户URL
	 */
	private String frontCallBackUrl;
	/**
	 * 商户编 号
	 */
	private String merchantNo;
	/**
	 * 商户流水号
	 */
	private String merchantOrderId;
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
	 * 银行订单号
	 * @return
	 */
	private String bankOrderNo;
	/**
	 * 银行渠道编码
	 */
	private String bankChannelNo;
	/**
	 * 成本
	 */
	private BigDecimal cost;
	/**
	 * 支付完成时间
	 */
	private Date payCompleteDate;
	/**
	 * 卡信息id
	 * @return
	 */
	private String cardInfoId;
	/**
	 * 支付订单号
	 */
	private String paymentOrderNo;
	/**
	 * 支付类型
	 * @return
	 */
	private String payType;
	/**
	 * 持卡人姓名
	 */
	private String owner;
	/**
	 * 卡类型
	 */
	private String cardType;
	/**
	 * 卡号
	 */
	private String cardNo;
	/**
	 * 证件类型
	 */
	private String idCardType;
	/**
	 * 手机号
	 */
	private String phoneNo;
	/**
	 * 证件号
	 */
	private String idCard;
	
	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 银行子系统订单号
	 */
	private String tradeSerialNo;
	
	/**
	 * 支付方式
	 */
	private String payTool;
	
	/**
	 * 期数
	 */
	private String period;

	/**
	 * 担保分期-分期手续费率
	 */
	private String serviceChargeRate;
	
	/**
	 * 实际支付金额
	 */
	private BigDecimal actualAmount;
	
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPayTool() {
		return payTool;
	}

	public void setPayTool(String payTool) {
		this.payTool = payTool;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public TradeStateEnum getTradeState() {
		return tradeState;
	}

	public void setTradeState(TradeStateEnum tradeState) {
		this.tradeState = tradeState;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(String cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
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

	public String getFrontCallBackUrl() {
		return frontCallBackUrl;
	}

	public void setFrontCallBackUrl(String frontCallBackUrl) {
		this.frontCallBackUrl = frontCallBackUrl;
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
	
	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getBankChannelNo() {
		return bankChannelNo;
	}

	public void setBankChannelNo(String bankChannelNo) {
		this.bankChannelNo = bankChannelNo;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Date getPayCompleteDate() {
		return payCompleteDate;
	}

	public void setPayCompleteDate(Date payCompleteDate) {
		this.payCompleteDate = payCompleteDate;
	}

	public String getCardInfoId() {
		return cardInfoId;
	}

	public void setCardInfoId(String cardInfoId) {
		this.cardInfoId = cardInfoId;
	}

	public String getPaymentOrderNo() {
		return paymentOrderNo;
	}

	public void setPaymentOrderNo(String paymentOrderNo) {
		this.paymentOrderNo = paymentOrderNo;
	}

	public String getTradeSerialNo() {
		return tradeSerialNo;
	}

	
	public void setTradeSerialNo(String tradeSerialNo) {
		this.tradeSerialNo = tradeSerialNo;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer("TradeNoticeDTO{");
		sb.append("tradeState='").append(tradeState).append('\'');
		sb.append(", paymentAmount='").append(paymentAmount).append('\'');
		sb.append(", cashierVersion='").append(cashierVersion).append('\'');
		sb.append(", paymentRequestId='").append(paymentRequestId).append('\'');
		sb.append(", errorCode='").append(errorCode).append('\'');
		sb.append(", errorMsg='").append(errorMsg).append('\'');
		sb.append(", frontCallBackUrl='").append(frontCallBackUrl).append('\'');
		sb.append(", merchantNo='").append(merchantNo).append('\'');
		sb.append(", merchantOrderId='").append(merchantOrderId).append('\'');
		sb.append(", tradeSysOrderId='").append(tradeSysOrderId).append('\'');
		sb.append(", tradeSysNo='").append(tradeSysNo).append('\'');
		sb.append(", orderOrderId='").append(orderOrderId).append('\'');
		sb.append(", orderSysNo='").append(orderSysNo).append('\'');
		sb.append(", tradeSerialNo='").append(tradeSerialNo).append('\'');
		sb.append(", payTool='").append(payTool).append('\'');
		sb.append(", period='").append(period).append('\'');
		sb.append(", serviceChargeRate='").append(serviceChargeRate).append('\'');
		sb.append(", actualPayAmount='").append(actualAmount).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public String getServiceChargeRate() {
		return serviceChargeRate;
	}

	public void setServiceChargeRate(String serviceChargeRate) {
		this.serviceChargeRate = serviceChargeRate;
	}

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}
	
}
