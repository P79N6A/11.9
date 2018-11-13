/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

import java.math.BigDecimal;
import java.util.List;

import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;

/**
 * @author xueping.ni
 * @date 2017年3月9日
 *
 */
@SuppressWarnings("serial")
public class SDKCreateOrderResponseDTO extends BasicResponseDTO {
	/**
	 * 支付请求ID
	 */
	private long requestId = 0l;
	/**
	 * 商户编号
	 */
	private String merchantNo;
	/**
	 * 商户订单号
	 */
	private String merchantOrderId;
	/**
	 * 商户名称
	 */
	private String merchantName;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	/**
	 * 支付工具列表
	 */
	private List<String> payTypes;
	/**
	 * 唯一订单号
	 */
	private String uniqueOrderNo;
	/**
	 * SDK收银台访问地址
	 */
	private String sdkUrl;
	
	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public List<String> getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(List<String> payTypes) {
		this.payTypes = payTypes;
	}

	public String getSdkUrl() {
		return sdkUrl;
	}

	/**
	 * @return the uniqueOrderNo
	 */
	public String getUniqueOrderNo() {
		return uniqueOrderNo;
	}

	/**
	 * @param uniqueOrderNo the uniqueOrderNo to set
	 */
	public void setUniqueOrderNo(String uniqueOrderNo) {
		this.uniqueOrderNo = uniqueOrderNo;
	}

	public void setSdkUrl(String sdkUrl) {
		this.sdkUrl = sdkUrl;
	}

	public  String toString(){
		StringBuffer orderResp = new StringBuffer("SDKCreateOrderResponseDTO{");
		orderResp.append("requestId='").append(this.requestId).append('\'')
		.append("merchantNo='").append(this.merchantNo).append('\'')
		.append("merchantOrderId='").append(this.merchantOrderId).append('\'')
		.append("merchantName='").append(this.merchantName).append('\'')
		.append("productName='").append(this.productName).append('\'')
		.append("orderAmount='").append(this.orderAmount).append('\'')
		.append("payTypes='").append(this.payTypes).append('\'')
		.append("sdkUrl='").append(this.sdkUrl).append('}');
		return orderResp.toString();
	}
	
	

}
