package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;


/**
 * 
 * @Description 商户绑卡请求VO
 * @author yangmin.peng
 * @since 2017年8月22日下午4:58:06
 */
public class BindCardMerchantRequestVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String merchantNo;
	private String merchantFlowId;
	private String userNo;
	private String userType;
	private String cardType;
	private String bizType;
	private String bizFlowId;
	private String bindCallBackUrl;
	private String bindFrontCallBackUrl;
	private String ext;

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantFlowId() {
		return merchantFlowId;
	}

	public void setMerchantFlowId(String merchantFlowId) {
		this.merchantFlowId = merchantFlowId;
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

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getBizFlowId() {
		return bizFlowId;
	}

	public void setBizFlowId(String bizFlowId) {
		this.bizFlowId = bizFlowId;
	}

	public String getBindCallBackUrl() {
		return bindCallBackUrl;
	}

	public void setBindCallBackUrl(String bindCallBackUrl) {
		this.bindCallBackUrl = bindCallBackUrl;
	}

	public String getBindFrontCallBackUrl() {
		return bindFrontCallBackUrl;
	}

	public void setBindFrontCallBackUrl(String bindFrontCallBackUrl) {
		this.bindFrontCallBackUrl = bindFrontCallBackUrl;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("BindCardMerchantRequestVO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append("merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append("userNo='").append(userNo).append('\'');
		sb.append("userType='").append(userType).append('\'');
		sb.append("bizType='").append(bizType).append('\'');
		sb.append("bizFlowId='").append(bizFlowId).append('\'');
		sb.append("bindCallBackUrl='").append(bindCallBackUrl).append('\'');
		sb.append("bindFrontCallBackUrl='").append(bindFrontCallBackUrl).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
