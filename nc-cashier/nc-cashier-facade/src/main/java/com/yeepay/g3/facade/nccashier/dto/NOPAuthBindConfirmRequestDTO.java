package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

/**
 * @Description 调用NOP进行鉴权绑卡请求DTO
 * @author yangmin.peng
 * @since 2017年8月22日下午6:57:00
 */
public class NOPAuthBindConfirmRequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantNo未传")
	private String merchantNo;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantFlowId未传")
	private String merchantFlowId;

	private String requestSystem;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "requestFlowId未传")
	private String requestFlowId;

	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "smsCode未传")
	private String smsCode;
	/**
	 * 姓名、卡号、身份证号、手机号、cvv、有效期
	 */
	private String userName;
	private String bankCardNo;
	private String idCardNo;
	private String phone;
	private String cvv2;
	private String validthru;

	private FirstBindCardPayRequestDTO bindPayRequest;

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

	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestFlowId() {
		return requestFlowId;
	}

	public void setRequestFlowId(String requestFlowId) {
		this.requestFlowId = requestFlowId;
	}

	public FirstBindCardPayRequestDTO getBindPayRequest() {
		return bindPayRequest;
	}

	public void setBindPayRequest(FirstBindCardPayRequestDTO bindPayRequest) {
		this.bindPayRequest = bindPayRequest;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getValidthru() {
		return validthru;
	}

	public void setValidthru(String validthru) {
		this.validthru = validthru;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NOPAuthBindConfirmRequestDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append(", merchantFlowId='").append(merchantFlowId).append('\'');
		sb.append(", requestFlowId='").append(requestFlowId).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
