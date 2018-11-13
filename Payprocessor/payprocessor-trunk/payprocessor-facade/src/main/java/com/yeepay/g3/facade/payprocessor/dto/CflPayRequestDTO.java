
package com.yeepay.g3.facade.payprocessor.dto;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author peile.fan
 *
 */
public class CflPayRequestDTO extends BasicRequestDTO {

	private static final long serialVersionUID = -748575415868399862L;
	
	/**
	 * 商户等级
	 */
	private String customerLevel;
	
	
	 /**
     * 设备信息
     */
    protected String deviceInfo;
    
	/**
	 * 前端回调地址
	 */
	@NotBlank(message = "pageCallBack不能为空")
	private String pageCallBack;

	/**
	 * 商品类型 VIRTUAL:虚拟 SUBSTANCE:实体
	 */
	@NotBlank(message = "productType不能为空")
	private String productType;

	/**
	 * 商户用户标识
	 */
	@NotEmpty(message = "商户用户标识不能为空")
	private String identityId;

	/**
	 * 移动客户端 IMEI 非必填
	 */
	@Pattern(regexp = "^(\\s&&[^\\f\\n\\r\\t\\v])*|\\w*", message = "IMEI格式不合法")
	private String imei;
	/**
	 * 身份类型
	 */
	private String identityType;

	/**
	 * 零售产品码
	 */
	private String retailProductCode;

	/**
	 * 基础产品码
	 */
	private String basicProductCode;



	public String getPageCallBack() {
		return pageCallBack;
	}

	public void setPageCallBack(String pageCallBack) {
		this.pageCallBack = pageCallBack;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
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

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
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
		return ReflectionToStringBuilder.toStringExclude(this , "goodsInfo");
	}

}
