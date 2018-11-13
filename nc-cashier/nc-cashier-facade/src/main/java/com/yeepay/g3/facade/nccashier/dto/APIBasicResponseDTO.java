package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * API收银台返回值基类
 * 
 * @author duangduang
 * @createTime 2017-09-28
 */
public class APIBasicResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * to 商户 ：状态码，成功返回“CAS0000” 
	 * to 业务方：错误码，成功不返回，失败返回该信息
	 */
	private String code;

	/**
	 * 返回信息，成功返回“成功”
	 */
	private String message;

	/**
	 * 商编
	 */
	private String merchantNo;

	/**
	 * 订单TOKEN
	 */
	private String token;

	/**
	 * 支付记录ID，用于下单后发短验、确认支付时串联请求；目前应用于银行卡分期、一键支付API业务
	 */
	private String recordId;

	public APIBasicResponseDTO() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return "APIBasicResponseDTO [code=" + code 
				+ ", message=" + message 
				+ ", merchantNo=" + merchantNo 
				+ ", token=" + token 
				+ ", recordId=" + recordId 
				+ "]";
	}
}
