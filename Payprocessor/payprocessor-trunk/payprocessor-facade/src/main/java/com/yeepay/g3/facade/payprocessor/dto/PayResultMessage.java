package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.payprocessor.enumtype.PayCardType;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 通知消息
 * @author TML
 *
 */
public class PayResultMessage implements Serializable{

	
	private static final long serialVersionUID = -7815830491988623942L;
	
	
	public static final String PAY_PROCESSOR_NOTIFY_QUEUE = "PAY_PROCESSOR_NOTIFY_QUEUE";
	
	/**
	 * 业务方
	 */
	private String requestSystem;

	/**
	 * 业务方订单号
	 */
    private String requestId;
	
    /**
     * 商户编号
     */
    private String customerNumber;
    
    /**
     * 商户订单号
     */
    private String outTradeNo;
    
    
    /**
     * 支付订单号
     */
    private String orderNo;
    
    /**
     * 订单类型
     */
    private OrderType orderType;
    
    /**
     * 平台类型
     */
    private PlatformType platformType;
	
    /**
     * 错误码
     */
    private String responseCode;

    /**
     * 错误码描述
     */
    private String responseMsg;
	
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
	private PayCardType payCardType;

	/**
	 * 支付金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 微信订单号
	 */
	private String transactionId;

	/**
	 * 支付状态
	 */
	private PaymentStatusEnum paymentStatus;

	/**
	 * 支付成本
	 */
	private BigDecimal bankTotalCost;
	
	/**
	 * 支付订单生成时间
	 */
	private Date createTime;

	/**
	 * 支付订单失效时间
	 */
	private Date expireTime;
    
     /**
     * 银行支付成功时间 
     */
    private Date bankSuccessTime;
    
    /**
     * 系统支付成功时间
     */
    private Date paySuccessTime;

	/**
	 * 银行子系统支付接口
	 */
	private String payInterface;

	/**
	 * 零售产品码
	 */
	private String retailProductCode;
	/**
	 * 基础产品码
	 */
	private String basicProductCode;
    
	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}

	public PayCardType getPayCardType() {
		return payCardType;
	}

	public void setPayCardType(PayCardType payCardType) {
		this.payCardType = payCardType;
	}

	public PaymentStatusEnum getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPayInterface() {
        return payInterface;
    }

    public void setPayInterface(String payInterface) {
        this.payInterface = payInterface;
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

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
