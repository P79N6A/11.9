/**
 * 
 */
package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;
/**
 *  @author zhen.tan
 * 微信支付下单请求入参
 *
 */
public class WeChatPayRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1558866518767978496L;

	/**
	 * 必填
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="tokenId未传")
	private String tokenId;

	/**
	 * 支付请求ID，必填
	 */

	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="requestId未传")
	private Long requestId;

	/**
	 * 支付记录ID
	 */
	private long recordId = 0;
	
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="payType未传")
	private String payType;

	/**
	 * 扫码支付需要获取的用户的openId
	 */
	private String openId;
	
	private String appId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

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
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierPaymentRequestDTO{");
		sb.append("tokenId='").append(this.tokenId).append('\'');
		sb.append(", requestId='").append(this.requestId).append('\'');
		sb.append(", recordId='").append(this.recordId).append('\'');
		sb.append(", payType='").append(this.payType).append('\'');
		sb.append(", openId='").append(this.openId).append('\'');
		sb.append('}');
		return sb.toString();
	}


}
