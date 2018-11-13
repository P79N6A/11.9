package com.yeepay.g3.facade.payprocessor.dto;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.yeepay.g3.facade.frontend.enumtype.BankAccountType;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;

public class NetPayRequestDTO extends BasicRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3420652348815763604L;

	/**
	 * 前端回调地址
	 */
	@NotBlank(message = "pageCallBack不能为空")
	private String pageCallBack;

	/**
	 * 支付限制类型
	 */
	@NotNull(message = "payBusinessType不能为空")
	private PayBusinessType payBusinessType;

	/**
	 * 支付银行编号
	 */
	@NotBlank(message = "bankId不能为空")
	private String bankId;

	/**
	 * 支付账户类型(对公／对私)
	 */
	@NotNull(message = "bankAccountType不能为空")
	private BankAccountType bankAccountType;

	/**
	 * 浦发／民生／交通B2B 对公银行客户号
	 */
	private String pubBankCusNo;

	/**
	 * 商户等级
	 */
	private String customerLevel;

	/**
	 * 设备信息
	 */
	protected String deviceInfo;

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
	 * 支付场景
	 */
	@Length(max = 50, message = "payScene最大长度为50")
	private String payScene;
	
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

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public BankAccountType getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(BankAccountType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getPubBankCusNo() {
		return pubBankCusNo;
	}

	public void setPubBankCusNo(String pubBankCusNo) {
		this.pubBankCusNo = pubBankCusNo;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
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
	
	public String getPayScene() {
		return payScene;
	}

	public void setPayScene(String payScene) {
		this.payScene = payScene;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this , "goodsInfo");
	}
}
