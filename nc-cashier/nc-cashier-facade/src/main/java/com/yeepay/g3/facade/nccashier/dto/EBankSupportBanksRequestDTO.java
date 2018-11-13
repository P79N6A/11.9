package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * pc网银 请求支持的银行列表的入参实体
 * 
 * @author duangduang
 * @since  2016-11-08
 */
public class EBankSupportBanksRequestDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商户编号
	 */
	private String merchantNo;
	
	/**
	 * 订单请求ID（PaymentRequest主键）
	 */
	private Long paymentRequestId;


	/**
	 * 充值网银类型  网银/代付汇入
	 */
	private String bacNetType;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBacNetType() {
		return bacNetType;
	}

	public void setBacNetType(String bacNetType) {
		this.bacNetType = bacNetType;
	}

	public EBankSupportBanksRequestDTO(){
		
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public Long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(Long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	
	
}
