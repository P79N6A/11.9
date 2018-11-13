/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

/**
 * @author xueping.ni
 * @date 2017-03-08 
 *
 */
public class SDKCreateOrderRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5867088946548172068L;
	/**
	 * 订单处理器token
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="token未传")
	private String token;
	/**
	 * 商户编号
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="商户编号未传")
	private String merchantNo;
	

	/**
	 * 用户标识
	 */
	private String userNo;
	/**
	 * 用户类型
	 */
	private String userType;
	/**
	 * 直连类型
	 */
	private String directPayType;
	/**
	 * 时间戳
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="时间戳未传")
	private String timeStamp;
	/**
	 * 风控信息
	 */
	private String riskInfo;
	/**
	 * 
	 */
	private String appId;
	
	
	
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


	public String getUserNo() {
		return userNo;
	}


	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	public String getDirectPayType() {
		return directPayType;
	}


	public void setDirectPayType(String directPayType) {
		this.directPayType = directPayType;
	}


	public String getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getRiskInfo() {
		return riskInfo;
	}


	public void setRiskInfo(String riskInfo) {
		this.riskInfo = riskInfo;
	}



	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}


	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String toString(){
		StringBuffer createOrderRequest = new StringBuffer("SDKCreateOrderRequestDTO{");
		createOrderRequest.append("token='").append(this.token).append('\'').append("merchantNo='").append(this.merchantNo).append('\'')
		.append("userNo='").append(this.userNo).append('\'')
		.append("userType='").append(this.userType).append('\'')
		.append("directPayType='").append(this.directPayType)
		.append("appId='").append(this.appId)
		.append("timeStamp='").append(this.timeStamp).append('}');
		return createOrderRequest.toString();
		
	}
	

}
