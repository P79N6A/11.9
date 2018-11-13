package com.yeepay.g3.facade.frontend.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;

/**
 * 公众号预路由请求参数
 * @author wangmeimei
 *
 */
public class PrePayRequestDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 商户编号
     */
	@NotBlank(message = "customerNumber不能为空")
	private String customerNumber;
	
	/**
	 * 商户订单号
	 */
	@NotBlank(message = "dealUniqueSerialNo不能为空")
	private String dealUniqueSerialNo;

	/**
	 * 支付金额
	 */
	@DecimalMin(value="0.01",message = "totalAmount最小值为0.01")
	private BigDecimal totalAmount;
	
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
	 * 公众号ID
	 */
	private String appId;
    
	/**
     * 支付限制类型
     */
	@NotNull(message = "payBusinessType不能为空")
    private PayBusinessType payBusinessType;
	
	/**
	 * 平台类型 WECHAT/ALIPAY
	 */
    private String platformType;
	
	/**
	 * 商户等级
	 */
	protected String customerLevel;
	
	/**
	 * 行业编码
	 */
	protected String industryCode;

	private Map<String, String> extParam;

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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public PayBusinessType getPayBusinessType() {
		return payBusinessType;
	}

	public void setPayBusinessType(PayBusinessType payBusinessType) {
		this.payBusinessType = payBusinessType;
	}
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
}
