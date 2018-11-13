package com.yeepay.g3.facade.nccashier.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 商家扫码API下单入参
 * 
 * @author duangduang
 * @since 2017-02-20
 */
public class APIMerchantScanPayDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 商户编号 **/
	private String merchantNo;

	/** 订单处理器订单号 **/
	private String token;

	/** 扫码类型 **/
	private String codeType;

	/** 授权码 **/
	private String code;

	/** 门店编码 **/
	private String storeCode;

	/** 设备号 **/
	private String deviceSn;
	
	private String userIp;

	private String bizType;

	public APIMerchantScanPayDTO() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	
	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
