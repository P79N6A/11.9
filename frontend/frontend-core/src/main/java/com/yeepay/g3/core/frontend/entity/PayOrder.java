package com.yeepay.g3.core.frontend.entity;

import com.yeepay.g3.utils.persistence.Entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表信息
 *
 * @author TML
 */
public class PayOrder implements Entity<Long> {

    private static final long serialVersionUID = -5371156161535973011L;

    private Long id;
    /**
     * 渠道平台，例如：微信，支付宝等
     */
    private String platformType;
    /**
     * 订单类型：例如：微信的h5，jsapi，主扫，被扫；支付宝的xxx，xxx
     */
    private String orderType;
    /**
     * 交易系统编码
     */
    private String requestSystem;
    /**
     * 商户编号
     */
    private String customerNumber;
    
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 交易系统编号
     */
    private String requestId;

    /**
     * 哆啦宝粉丝路由功能新增字段
     */
    private String reportId;

    /**
     * 银行子系统订单号
     */
    private String orderNo;
    /**
     * 第三方渠道的订单号，例如：微信对接的中信银行，深圳金融结算中心；支付宝对接的xxx，xxx
     */
    private String bankTradeId;
    /**
     * 支付渠道方订单号，实际微信，支付宝的订单号
     */
    private String transactionId;
    
    /**
     * 支付状态
     */
    private String payStatus;
    /**
     * 通知状态
     */
    private String notifyStatus;
    /**
     * 用户在公众号的openid
     */
    private String openId;

    /**
     *  商户在易宝的openId
     */
    private String yeepayOpenId;

    /**
     * 付款银行
     */
    private String payBank;
    /**
     * 付款卡种
     */
    private String payBankcardType;
    /**
     * 支付金额
     */
    private BigDecimal totalAmount;
    /**
     * 渠道成本
     */
    private BigDecimal bankTotalCost;
    /**
     * 订单描述
     */
    private String goodsDescription;
    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 前端回调地址
     */
    private String pageCallBack;

    /**
     * 客户端ip
     */
    private String payerIp;
    
    /**
     * 本订单创建时间
     */
    private Date createTime;
    /**
     * 本订单更新时间
     */
    private Date modifyTime;
    /**
     * 本订单过期时间
     */
    private Date expireTime;
    /**
     * 银行子系统支付成功时间或第三方渠道的支付成功时间
     */
    private Date bankSuccessTime;

    /**
     * 渠道支付成功时间
     */
    private Date paySuccessTime;

    /**
     * 差错退款请求时间
     */
    private Date refundDate;
    /**
     * 差错退款状态
     */
    private String refundStatus;
    /**
     * 差错退款类型
     */
    private String refundType;
    /**
     * 银行子系统支付接口
     */
    private String payInterface;

    /**
     * 支付限制类型
     */
    private String payLimitType;
    /**
     * 业务方
     */
    private String orderSystem;
    
    /**
	 * 系统唯一码
	 */
    private String dealUniqueSerialNo;
    
    /**
	 * 支付产品
	 */
    private String paymentProduct;

    /**
     * 零售产品码
     */
    private String retailProductCode;

    /**
     * 基础产品码
     */
    private String basicProductCode;

    /**
     * 扩展字段，FE不用
     * 上下游用，需要保存
     */
    private String extParam;

    /**
     * FE支付订单号
     */
    private String payOrderNo;


    /**
     * 现金支付金额
     */
    private BigDecimal cashFee;

    /**
     * 应结算金额
     */
    private BigDecimal settlementFee;


    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
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

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus;
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


    public String getPayBankcardType() {
        return payBankcardType;
    }

    public void setPayBankcardType(String payBankcardType) {
        this.payBankcardType = payBankcardType;
    }

    public BigDecimal getBankTotalCost() {
        return bankTotalCost;
    }

    public void setBankTotalCost(BigDecimal bankTotalCost) {
        this.bankTotalCost = bankTotalCost;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getPageCallBack() {
        return pageCallBack;
    }

    public void setPageCallBack(String pageCallBack) {
        this.pageCallBack = pageCallBack;
    }

    public String getPayerIp() {
        return payerIp;
    }

    public void setPayerIp(String payerIp) {
        this.payerIp = payerIp;
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

    public String getBankTradeId() {
        return bankTradeId;
    }

    public void setBankTradeId(String bankTradeId) {
        this.bankTradeId = bankTradeId;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getPayInterface() {
        return payInterface;
    }

    public void setPayInterface(String payInterface) {
        this.payInterface = payInterface;
    }

	public String getPayLimitType() {
		return payLimitType;
	}

	public void setPayLimitType(String payLimitType) {
		this.payLimitType = payLimitType;
	}

    public String getOrderSystem() {
        return orderSystem;
    }

    public void setOrderSystem(String orderSystem) {
        this.orderSystem = orderSystem;
    }

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public String getPaymentProduct() {
		return paymentProduct;
	}

	public void setPaymentProduct(String paymentProduct) {
		this.paymentProduct = paymentProduct;
	}

    public String getBasicProductCode() {
        return basicProductCode;
    }

    public void setBasicProductCode(String basicProductCode) {
        this.basicProductCode = basicProductCode;
    }

    public String getRetailProductCode() {
        return retailProductCode;
    }

    public void setRetailProductCode(String retailProductCode) {
        this.retailProductCode = retailProductCode;
    }

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getYeepayOpenId() {
        return yeepayOpenId;
    }

    public void setYeepayOpenId(String yeepayOpenId) {
        this.yeepayOpenId = yeepayOpenId;
    }

    public BigDecimal getCashFee() {
        return cashFee;
    }

    public void setCashFee(BigDecimal cashFee) {
        this.cashFee = cashFee;
    }

    public BigDecimal getSettlementFee() {
        return settlementFee;
    }

    public void setSettlementFee(BigDecimal settlementFee) {
        this.settlementFee = settlementFee;
    }
}