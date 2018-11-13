/**
 * 
 */
package com.yeepay.g3.facade.payprocessor.dto;

import java.math.BigDecimal;

import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;

/**
 * @author peile.fan
 *
 */
public class OpenPrePayResponseDTO extends ResponseStatusDTO {
	
	

	private static final long serialVersionUID = 4831808500738284185L;
	
	/**
	 * 商户编号
	 */
	private String customerNumber;
	/**
	 * 订单金额
	 */
	private BigDecimal totalAmount;
	/**
	 * 系统唯一码
	 */
	private String dealUniqueSerialNo;
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
	 * 通道成本
	 */
	private BigDecimal bankTotalCost;
	
	/**
	 * 报备二级商户号
	 */
	private String reportMerchantNo;
	/**
	 * 路由处理状态（1：成功；0：失败）与sceneTypeExt组合使用
	 * 若状态为1，同时场景类型为jsapiH5，此时预路由不返appId也表示有通道可用；
	 * 若状态为1，同时场景类型为normal，此时与原来保持一致。
	 */
	private Integer dealStatus;
	/**
	 * 场景类型扩展（jsapiH5：微信内部H5通道；normal：正常通道）
	 */
	private String sceneTypeExt;
	

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDealUniqueSerialNo() {
		return dealUniqueSerialNo;
	}

	public void setDealUniqueSerialNo(String dealUniqueSerialNo) {
		this.dealUniqueSerialNo = dealUniqueSerialNo;
	}

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpenPrePayResponseDTO [customerNumber=");
		builder.append(customerNumber);
		builder.append(", totalAmount=");
		builder.append(totalAmount);
		builder.append(", dealUniqueSerialNo=");
		builder.append(dealUniqueSerialNo);
		builder.append(", appId=");
		builder.append(appId);
		builder.append(", appSecret=");
		builder.append(HiddenCode.hiddenBankPwd(appSecret));
		builder.append(", payInterface=");
		builder.append(payInterface);
		builder.append(", bankTotalCost=");
		builder.append(bankTotalCost);
		builder.append(", responseCode=");
		builder.append(responseCode);
		builder.append(", responseMsg=");
		builder.append(responseMsg);
		builder.append(", processStatus=");
		builder.append(processStatus);
		builder.append(", dealStatus=");
		builder.append(dealStatus);
		builder.append(", sceneTypeExt=");
		builder.append(sceneTypeExt);
		builder.append(", reportMerchantNo=");
		builder.append(reportMerchantNo );
		builder.append("]");
		return builder.toString();
	}

}
