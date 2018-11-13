/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

/**SDK支付应答DTO
 * @author xueping.ni
 * @since 2017年3月9日
 * @version 1.0
 */
public class SDKPayResponseDTO extends BasicResponseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9060391135034385646L;
	/**
	 * 支付请求ID
	 */
	private long requestId;
	/**
	 * 支付记录ID
	 */
	private long recordId;
	/**
	 * 支付Token
	 */
	private String token;
	/**
	 * 商户编号
	 */
	private String merchantNo;
	/**
	 * 商户订单号
	 */
	private String merchantOrderId;
	/**
	 * 支付类型
	 */
	private String payType;
	/**
	 * 唤醒APP的包
	 */
	private String appMessage;
	
	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	/**
	 * @return the appMessage
	 */
	public String getAppMessage() {
		return appMessage;
	}

	/**
	 * @param appMessage the appMessage to set
	 */
	public void setAppMessage(String appMessage) {
		this.appMessage = appMessage;
	}

	public String toString(){
		StringBuffer response = new StringBuffer("SDKPayResponseDTO{");
		response.append("requestId=").append(this.requestId).append('\'')
		.append("recordId=").append(this.recordId).append('\'')
		.append("token=").append(this.token).append('\'')
		.append("payType=").append(this.payType).append('\'')
		.append("merchantNo=").append(this.merchantNo).append('\'')
		.append("merchantOrderId=").append(this.merchantOrderId)
		.append("appMessage=").append(this.appMessage).append('\'')
		.append('}');
		return response.toString();
	}
	
	
}
