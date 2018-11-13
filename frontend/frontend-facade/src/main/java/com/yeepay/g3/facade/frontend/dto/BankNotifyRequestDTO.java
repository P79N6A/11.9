package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行子系统回调信息接口
 * @author TML
 *
 */
public class BankNotifyRequestDTO implements Serializable{

	
	private static final long serialVersionUID = 3163271839035360771L;
	
	/**
	 * FE支付订单号(请求路由下单时传入的tradeOrderNo)
	 */
	private String requestId;

	/**
     * 商户编号
     */
	private String customerNumber;
	
	/**
	 * 银行子系统流水号
	 */
	@NotBlank(message = "orderNo不能为空")
	private String orderNo;
    
    /**
     * 商户订单号
     */
    private String outTradeNo;
	
    /**
     * 微信支付宝/网银银行订单号
     */
//    @NotBlank(message = "transactionId不能为空")
    private String transactionId;
    
    /**
     * 微信支付宝第三方银行的订单号 (银行返的付款凭证号，针对银联）
     */
    private String bankTradeId;
    
    /**
     * 状态
     */
    @NotNull(message = "payStatus不能为空")
    private PayStatusEnum payStatus;
    
    /**
     * 支付银行
     */
    private String payBank;

    /**
     * 支付类型
     */
    private PayBankcardType payBankcardType;
    
    /**
	 * 成本信息
	 */
	private BigDecimal bankTotalCost;
	
	/**
	 * 支付金额
	 */
	@DecimalMin(value="0.01",message = "totalAmount最小值为0.01")
	private BigDecimal totalAmount;
    
    /**
     * 银行支付成功时间 
     */
    private Date bankSuccessTime;
    
    /**
     * 系统支付成功时间
     */
    @NotNull(message = "paySuccessTime不能为空")
    private Date paySuccessTime;

	/**
	 * 支付接口
	 */
    @NotNull(message = "payInterface不能为空")
	private String payInterface;

    /**
     * 报备商户号(银行商户号)
     * added by zhijun.wang 
     * 2017-05-25
     */
    private String reportMerchantNo;
    
    /** 
     * 付款方账户号(卡号)
     * added by zhijun.wang 
     * 2017-05-25
     */
    private String payerBankAccountNo;
    
    /**
     * 优惠信息
     * added by zhijun.wang 
     * 2017-05-25
     */
    private String couponInfo;
    
    /**
     * openId
     * added by zhijun.wang
     * 2017-07-25
     */
    private String openId;


	/**
	 * 商户在易宝的openId
	 */
	private String yeepayOpenId;

	/**
	 * 是否需要支付密码。取值：Y/N
	 */
	private String needPaymentPWD;


	/**
	 * 扩展字段
	 * 目前存业务通道编码
	 * added by zhijun.wang
	 * 2017-12-12
	 */
	private Map<String, String> extParam;


	/**
	 * 现金支付金额
	 */
	private BigDecimal cashFee;

	/**
	 * 应结算金额
	 */
	private BigDecimal settlementFee;

	/**
	 * 优惠券信息
	 */
	private List<PromotionInfoDTO> promotionInfoDTOS;

    
	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
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

	public BigDecimal getBankTotalCost() {
		return bankTotalCost;
	}

	public void setBankTotalCost(BigDecimal bankTotalCost) {
		this.bankTotalCost = bankTotalCost;
	}

	public String getPayInterface() {
		return payInterface;
	}

	public void setPayInterface(String payInterface) {
		this.payInterface = payInterface;
	}
	
	

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);				
	}
	
	public String getReportMerchantNo() {
		return reportMerchantNo;
	}

	public void setReportMerchantNo(String reportMerchantNo) {
		this.reportMerchantNo = reportMerchantNo;
	}

	public String getPayerBankAccountNo() {
		return payerBankAccountNo;
	}

	public void setPayerBankAccountNo(String payerBankAccountNo) {
		this.payerBankAccountNo = payerBankAccountNo;
	}

	public String getCouponInfo() {
		return couponInfo;
	}

	public void setCouponInfo(String couponInfo) {
		this.couponInfo = couponInfo;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}


	public String getYeepayOpenId() {
		return yeepayOpenId;
	}

	public void setYeepayOpenId(String yeepayOpenId) {
		this.yeepayOpenId = yeepayOpenId;
	}

	public String getNeedPaymentPWD() {
		return needPaymentPWD;
	}

	public void setNeedPaymentPWD(String needPaymentPWD) {
		this.needPaymentPWD = needPaymentPWD;
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

	public List<PromotionInfoDTO> getPromotionInfoDTOS() {
		return promotionInfoDTOS;
	}

	public void setPromotionInfoDTOS(List<PromotionInfoDTO> promotionInfoDTOS) {
		this.promotionInfoDTOS = promotionInfoDTOS;
	}
}
