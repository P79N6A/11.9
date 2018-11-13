package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;

/**
 * @author peile.fan
 *
 */
public class OpenPrePayRequestDTO implements Serializable {
	
	private static final long serialVersionUID = -8886500428905482956L;
	
	/**
	 * 零售产品码
	 */
	@NotNull(message = "retailProductCode不能为空")
	private String retailProductCode;
	/**
	 * 基础产品码
	 */
	@NotNull(message = "basicProductCode不能为空")
	private String basicProductCode;

	/**
	 * 商户编号
	 */
	@NotBlank(message = "customerNumber不能为空")
	@Length(max = 20, message = "customerNumber最大长度为20")
	private String customerNumber;
	/**
	 * 系统唯一码
	 */
	@NotBlank(message = "dealUniqueSerialNo不能为空")
	@Length(min = 1, max = 50, message = "dealUniqueSerialNo最大长度为50")
	private String dealUniqueSerialNo;

	/**
	 * 订单金额
	 */
	@DecimalMin(value = "0.01", message = "totalAmount最小值为0.01")
	private BigDecimal totalAmount;
	/**
	 * 商户等级
	 */
	private String customerLevel;
	/**
	 * 行业编码
	 */
	private String industryCode;
	/**
	 * 支付限制类型
	 */
	@NotNull(message = "payBusinessType不能为空")
	private PayBusinessType payBusinessType;

	/**
	 * 公众号ID
	 */
	private String appId;
	
	/**
	 * 请求系统
	 */
	@NotBlank(message = "requestSystem不能为空")
	@Length(max = 20, message = "requestSystem最大长度为20")
	private String requestSystem;
	/**
	 * 平台类型
	 */
	private String platformType;

	/**
	 * 扩展参数
	 */
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

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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

	public PayBusinessType getPayBusinessType() {
		return payBusinessType;
	}

	public void setPayBusinessType(PayBusinessType payBusinessType) {
		this.payBusinessType = payBusinessType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
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
	public String toString() {
		return "OpenPrePayRequestDTO{" +
				"retailProductCode='" + retailProductCode + '\'' +
				", basicProductCode='" + basicProductCode + '\'' +
				", customerNumber='" + customerNumber + '\'' +
				", dealUniqueSerialNo='" + dealUniqueSerialNo + '\'' +
				", totalAmount=" + totalAmount +
				", customerLevel='" + customerLevel + '\'' +
				", industryCode='" + industryCode + '\'' +
				", payBusinessType=" + payBusinessType +
				", appId='" + appId + '\'' +
				", requestSystem='" + requestSystem + '\'' +
				", platformType='" + platformType + '\'' +
				", extParam=" + extParam +
				'}';
	}

}
