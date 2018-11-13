package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.RefundStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.RefundType;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class FrontendQueryResponseDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 8232308976829291389L;

	/**
	 * 工作号id
	 */
	private String openId;

	/**
	 * 支付银行
	 */
	private String payBank;

	/**
	 * 支付银行卡类型
	 */
	private PayBankcardType payBankcardType;

	/**
	 * 微信订单号
	 */
	private String transactionId;
	
	 /**
     * 银行订单号（第三方银行的订单号）
     */
    private String bankTradeId;
    
    /**
     * 银行支付成功时间 
     */
    private Date bankSuccessTime;
    
    /**
     * 系统支付成功时间
     */
    private Date paySuccessTime;

	/**
	 * 支付状态
	 */
	private PayStatusEnum payStatus;

	/**
	 * 支付成本
	 */
	private BigDecimal bankTotalCost;
	
	private RefundStatusEnum refundStatus;

	private RefundType refundType;

	private String payInterface;
	
	/**
	 * 支付产品
	 */
	private String paymentProduct;

	/**
	 * 扩展信息前端需要，通道透传前端，暂时只有银联用，例如：银行商户号、卡号、付款凭证号、优惠信息
	 * added by zhijun.wang 2017-05-24
	 */
	private Map<String, String> extParam;

	/**
	 * 零售产品码
	 * add by dongbo.jiao 2017-06-21
	 */
	private String retailProductCode;

	/**
	 * 基础产品码
	 */
	private String basicProductCode;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}


	public PayBankcardType getPayBankcardType() {
		return payBankcardType;
	}

	public void setPayBankcardType(PayBankcardType payBankcardType) {
		this.payBankcardType = payBankcardType;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public BigDecimal getBankTotalCost() {
		return bankTotalCost;
	}

	public void setBankTotalCost(BigDecimal bankTotalCost) {
		this.bankTotalCost = bankTotalCost;
	}

	
	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

	public RefundStatusEnum getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(RefundStatusEnum refundStatus) {
		this.refundStatus = refundStatus;
	}

	public RefundType getRefundType() {
		return refundType;
	}

	public void setRefundType(RefundType refundType) {
		this.refundType = refundType;
	}

	public String getBankTradeId() {
		return bankTradeId;
	}

	public void setBankTradeId(String bankTradeId) {
		this.bankTradeId = bankTradeId;
	}

	public Date getBankSuccessTime() {
		return bankSuccessTime;
	}

	public void setBankSuccessTime(Date bankSuccessTime) {
		this.bankSuccessTime = bankSuccessTime;
	}

	public Date getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(Date paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
	}

	public String getPayInterface() {
		return payInterface;
	}

	public void setPayInterface(String payInterface) {
		this.payInterface = payInterface;
	}

	public String getPaymentProduct() {
		return paymentProduct;
	}

	public void setPaymentProduct(String paymentProduct) {
		this.paymentProduct = paymentProduct;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}

	public String getRetailProductCode() {
		return retailProductCode;
	}

	public void setRetailProductCode(String retailProductCode) {
		this.retailProductCode = retailProductCode;
	}

	public String getBasicProductCode() {
		return basicProductCode;
	}

	public void setBasicProductCode(String basicProductCode) {
		this.basicProductCode = basicProductCode;
	}
}
