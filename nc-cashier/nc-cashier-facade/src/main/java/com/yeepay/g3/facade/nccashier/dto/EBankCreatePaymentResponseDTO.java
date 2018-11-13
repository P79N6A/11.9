package com.yeepay.g3.facade.nccashier.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * pc网银确认支付下单 返回值实体类
 * 
 * @author duangduang
 * @since  2016-11-08
 */
public class EBankCreatePaymentResponseDTO extends BasicResponseDTO{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 跳板机的地址pcc.yeepay.com 此次版本上线后，就可以下掉了 2018.03.19
	 */
	@Deprecated
	private String bankPayUrl;
	
	/**
	 * 跳板机的地址pcc.yeepay.com
	 */
	private String payUrl;
	
	/**
	 * 跳转银行的报文信息
	 */
	private UrlInfoDTO urlInfoDTO;
	
	private String toBankPassThroughPccashier;
	
	private String merchantOrderId;//商户订单号（返回给前端弹出层）
	public EBankCreatePaymentResponseDTO(){
		
	}

	public String getBankPayUrl() {
		return bankPayUrl;
	}

	public void setBankPayUrl(String bankPayUrl) {
		this.bankPayUrl = bankPayUrl;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	
	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public UrlInfoDTO getUrlInfoDTO() {
		return urlInfoDTO;
	}

	public void setUrlInfoDTO(UrlInfoDTO urlInfoDTO) {
		this.urlInfoDTO = urlInfoDTO;
	}
	
	public String getToBankPassThroughPccashier() {
		return toBankPassThroughPccashier;
	}

	public void setToBankPassThroughPccashier(String toBankPassThroughPccashier) {
		this.toBankPassThroughPccashier = toBankPassThroughPccashier;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}


}
