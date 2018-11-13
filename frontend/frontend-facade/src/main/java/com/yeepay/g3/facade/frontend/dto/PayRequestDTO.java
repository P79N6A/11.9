package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

public class PayRequestDTO extends BasicRequestDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "orderType不能为空")
	private OrderType orderType;

	/**
	 * 微信或支付宝公众号下用户ID
	 */
	private String openId;
	
	/**
	 * 公众号ID
	 */
	private String appId;
	
	/**
	 * 前端回调地址
	 */
	private String pageCallBack;
    
	/**
     * 支付限制类型
     */
	@NotNull(message = "payBusinessType不能为空")
    private PayBusinessType payBusinessType;
	
	/**
	 * 支付产品
	 */
	@NotBlank(message = "paymentProduct不能为空")
	private String paymentProduct;
	
	/**
	 * 零售产品编码
	 */
	@NotBlank(message = "retailProductCode不能为空")
    private String retailProductCode;
	
    /**
     * 基础产品编码
     */
	@NotBlank(message = "basicProductCode不能为空")
	private String basicProductCode;
	
	/**
	 * 系统唯一码
	 */
	@NotBlank(message = "dealUniqueSerialNo不能为空")
	private String dealUniqueSerialNo;
    
	/**
	 * 银行子系统支付接口
	 */
	private String payInterface;
	
	/**
	 * 支付成本
	 */
	private BigDecimal bankTotalCost;
	
	/**
	 * 报备子商户号
	 */
	private String reportMerchantNo;
	
	/**
	 * 微信／支付宝被扫支付授权号 被扫时必填
	 */
	private String payEmpowerNo;
	 
	/**
	 * 支付宝商户门店编号 被扫时必填，长度不超过60
	 */
	private String merchantStoreNo;
	
	/**
	 * 支付宝商户机具终端号  被扫时必填，长度不超过60
	 */
	private String merchantTerminalId;
	
	/**
	 * 通道订单有效期 单位：分钟
	 */
	private int orderExpireMin;
	
	/**
	 * 支付宝个人账号
	 */
	private String payerAccountNo;

	
	private Map<String, String> extParam;
	
    public String getPageCallBack() {
		return pageCallBack;
	}

	public void setPageCallBack(String pageCallBack) {
		this.pageCallBack = pageCallBack;
	}

	public PayBusinessType getPayBusinessType() {
		return payBusinessType;
	}

	public void setPayBusinessType(PayBusinessType payBusinessType) {
		this.payBusinessType = payBusinessType;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
    
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getPaymentProduct() {
		return paymentProduct;
	}

	public void setPaymentProduct(String paymentProduct) {
		this.paymentProduct = paymentProduct;
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

	public String getPayInterface() {
		return payInterface;
	}

	public void setPayInterface(String payInterface) {
		this.payInterface = payInterface;
	}

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public BigDecimal getBankTotalCost() {
		return bankTotalCost;
	}

	public void setBankTotalCost(BigDecimal bankTotalCost) {
		this.bankTotalCost = bankTotalCost;
	}

	public String getReportMerchantNo() {
		return reportMerchantNo;
	}

	public void setReportMerchantNo(String reportMerchantNo) {
		this.reportMerchantNo = reportMerchantNo;
	}
	
	public String getPayEmpowerNo() {
		return payEmpowerNo;
	}

	public void setPayEmpowerNo(String payEmpowerNo) {
		this.payEmpowerNo = payEmpowerNo;
	}

	public String getMerchantStoreNo() {
		return merchantStoreNo;
	}

	public void setMerchantStoreNo(String merchantStoreNo) {
		this.merchantStoreNo = merchantStoreNo;
	}

	public String getMerchantTerminalId() {
		return merchantTerminalId;
	}

	public void setMerchantTerminalId(String merchantTerminalId) {
		this.merchantTerminalId = merchantTerminalId;
	}

	public int getOrderExpireMin() {
		return orderExpireMin;
	}

	public void setOrderExpireMin(int orderExpireMin) {
		this.orderExpireMin = orderExpireMin;
	}

	public String getPayerAccountNo() {
		return payerAccountNo;
	}

	public void setPayerAccountNo(String payerAccountNo) {
		this.payerAccountNo = payerAccountNo;
	}
	
	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}

	@Override
	public String toString(){
		ReflectionToStringBuilder toStringBuilder = new ReflectionToStringBuilder(this){
            @Override
			protected boolean accept(Field field) {
                return !field.getName().equals("payerAccountNo") && !field.getName().equals("extParam");
            }
        };
		return toStringBuilder.toString();
	}

}
