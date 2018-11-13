package com.yeepay.g3.facade.payprocessor.dto;

import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;

public class OpenPayRequestDTO extends BasicRequestDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4952744334230726723L;


	/**
	 * 公众号下用户ID
	 */
	private String openId;
	

	/**
	 * 公众号APPID
	 */
	private String appId;

	/**
	 * ls 前端回调地址
	 */
	private String pageCallBack;
	
	/**
	 * 支付场景
	 */
	@Length(max = 50, message = "payScene最大长度为50")
	private String payScene;
	/**
	 * 支付限制类型
	 */
	@NotNull(message = "payBusinessType不能为空")
	private PayBusinessType payBusinessType;
	/**
	 * 平台类型
	 */
	@NotNull(message = "platformType不能为空")
	private PlatformType platformType;

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
	 * 通道编码
	 */
	private String payInterface;
	/**
	 * 通道成本
	 */
	private BigDecimal bankTotalCost;
	
	/**
	 * 报备二级商户号
	 */
	private String reportMerchantNo;
	/**
	 * 支付宝个人账号
	 */
	private String payerAccountNo;
	/**
	 * 扩展字段 透传FE
	 */
	private Map<String,String> extParam;
	/**
	 * 高低版本标记 high高配；low低配
	 */
	private String walletLevel;
	
	

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

	public String getPageCallBack() {
		return pageCallBack;
	}

	public void setPageCallBack(String pageCallBack) {
		this.pageCallBack = pageCallBack;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
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

	public PayBusinessType getPayBusinessType() {
		return payBusinessType;
	}

	public void setPayBusinessType(PayBusinessType payBusinessType) {
		this.payBusinessType = payBusinessType;
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

	
	public String getPayScene() {
		return payScene;
	}

	public void setPayScene(String payScene) {
		this.payScene = payScene;
	}
	

	public String getPayerAccountNo() {
		return payerAccountNo;
	}

	public void setPayerAccountNo(String payerAccountNo) {
		this.payerAccountNo = payerAccountNo;
	}

	public Map<String,String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String,String> extParam) {
		this.extParam = extParam;
	}

	public String getWalletLevel() {
		return walletLevel;
	}

	public void setWalletLevel(String walletLevel) {
		this.walletLevel = walletLevel;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this , "goodsInfo");
	}

}
