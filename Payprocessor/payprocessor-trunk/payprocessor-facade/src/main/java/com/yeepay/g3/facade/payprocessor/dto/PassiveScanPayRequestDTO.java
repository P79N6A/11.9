package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Description:被扫请求参数
 * 
 * @author peile.fan
 * @since:2017年2月10日 下午5:31:50
 */
public class PassiveScanPayRequestDTO extends BasicRequestDTO {
	
	private static final long serialVersionUID = 4428818108454264417L;

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
	@NotBlank(message = "retailProductCode不能为空")
	private String retailProductCode;
	/**
	 * 基础产品码
	 */
	@NotBlank(message = "basicProductCode不能为空")
	private String basicProductCode;

	/**
	 * 微信被扫支付授权号
	 */
	@NotBlank(message = "payEmpowerNo不能为空")
	private String payEmpowerNo;

	/**
	 * 商户门店编号
	 */
	@NotBlank(message = "merchantStoreNo不能为空")
	@Length(max = 60, message = "merchantStoreNo最大长度为50")
	private String merchantStoreNo;

	/**
	 * 商户机具终端号
	 */
	@NotBlank(message = "merchantTerminalId不能为空")
	@Length(max = 60, message = "merchantTerminalId最大长度为50")
	private String merchantTerminalId;
	
	
	/**
	 * ls 前端回调地址
	 */
	private String pageCallBack;

	/**
	 * appId
	 */
	private String appId;

	private Map<String, String> extParam;
	

	public PayBusinessType getPayBusinessType() {
		return payBusinessType;
	}

	public void setPayBusinessType(PayBusinessType payBusinessType) {
		this.payBusinessType = payBusinessType;
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

	
	public String getPageCallBack() {
		return pageCallBack;
	}

	public void setPageCallBack(String pageCallBack) {
		this.pageCallBack = pageCallBack;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this , "goodsInfo");
	}

}
