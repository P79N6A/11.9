package com.yeepay.g3.facade.frontend.dto;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 公众号预路由返回结果
 * @author wangmeimei
 *
 */
public class PrePayResponseDTO extends BasicResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 公众号ID
	 */
	private String appId;
	
	/**
	 * 公众号应用密钥
	 */
	private String appSecret;
	
	/**
	 * 通道编码
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
	 * 调用预路由状态（1：成功；0：失败）
	 * added by zhijun.wang 2017-05-23
	 */
	private Integer dealStatus = 0;
	
	/**
	 * 场景类型扩展（jsapiH5：微信内部H5通道；normal：正常通道）
	 * added by zhijun.wang 2017-05-23
	 */
	private String sceneTypeExt;

	/**
	 * added by zhijun.wang 20171214
	 * 通道traceId，返回便于业务方查日志
	 */
	private String traceId;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
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

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getSceneTypeExt() {
		return sceneTypeExt;
	}

	public void setSceneTypeExt(String sceneTypeExt) {
		this.sceneTypeExt = sceneTypeExt;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
}
