/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

import java.io.Serializable;

/**SDK支付请求DTO
 * @author xueping.ni
 * @since 2017年3月9日
 * @version 1.0
 */
public class SDKPayRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5398830896009327849L;
	/**
	 * 支付请求ID
	 */
	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="支付请求Id未传")
	private long requestId=0l;
	/**
	 * 支付类型：一键支付YJZF、微信WECHAT、支付宝ALIPAY
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="支付类型未传")
	private String payType;
	/**
	 * 时间戳
	 */
	private String timeStamp;
	/**
	 * 商户编号
	 */
	private String merchantNo;
	/**
	 * 用户IP地址
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="用户IP地址不能为空")
	private String userIp;
	
	
	public long getRequestId() {
		return requestId;
	}



	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}



	public String getPayType() {
		return payType;
	}



	public void setPayType(String payType) {
		this.payType = payType;
	}



	public String getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}



	public String getMerchantNo() {
		return merchantNo;
	}



	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}


	/**
	 * @return the userIp
	 */
	public String getUserIp() {
		return userIp;
	}



	/**
	 * @param userIp the userIp to set
	 */
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}



	public String toString(){
		StringBuffer request = new StringBuffer("SDKPayRequestDTO{");
		request.append("requestId=").append(this.requestId).append('\'')
		.append("payType=").append(this.payType).append('\'')
		.append("timeStamp=").append(this.timeStamp).append('\'')
		.append("merchantNo=").append(this.merchantNo).append('\'')
		.append('}');
		return request.toString();
	}
	
	
}
