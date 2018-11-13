package com.yeepay.g3.facade.nccashier.dto;


import java.io.Serializable;

public class APIPreauthPaymentResponseDTO extends APIBasicResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 验证码类型(SMS/VOICE/NONE) */
	private String verifyCodeType;
	/** 提交补充项场景(REQUEST_VERIFY/CONFIRM_PAY/NONE) */
	private String needItemScene;
	/** 需补充项名称的集合，以","分隔 */
	private String needItems;

	/**
	 * 支付处理器的子单号
	 */
	private String paymentRecordNo;
	
	public APIPreauthPaymentResponseDTO() {

	}

	public String getVerifyCodeType() {
		return verifyCodeType;
	}

	public void setVerifyCodeType(String verifyCodeType) {
		this.verifyCodeType = verifyCodeType;
	}

	public String getNeedItemScene() {
		return needItemScene;
	}

	public void setNeedItemScene(String needItemScene) {
		this.needItemScene = needItemScene;
	}

	public String getNeedItems() {
		return needItems;
	}

	public void setNeedItems(String needItems) {
		this.needItems = needItems;
	}

	public String getPaymentRecordNo() {
		return paymentRecordNo;
	}

	public void setPaymentRecordNo(String paymentRecordNo) {
		this.paymentRecordNo = paymentRecordNo;
	}

	@Override
	public String toString() {
		return "APIPreauthPaymentResponseDTO [code=" + getCode() 
				+ ", message=" + getMessage() 
				+ ", merchantNo=" + getMerchantNo()
				+ ", token=" + getToken()
				+ ", recordId=" + getRecordId()
				+ ", verifyCodeType=" + verifyCodeType
				+ ", needItemScene=" + needItemScene
				+ ", needItems=" + needItems
				+ ", paymentRecordNo=" + paymentRecordNo
				+ "]";
	}
}
