package com.yeepay.g3.app.nccashier.wap.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * pc网银 确认支付的页面请求的入参实体
 * 
 * @author duangduang
 * @since 2016-11-03
 */
public class EBankPayRequestVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 银行编码
	 */
	private String bankCode;

	/**
	 * 网银账户类型 b2b/b2c
	 */
	private String ebankAccountType;

	/**
	 * 客户ID（针对对公b2b账户）(非必需)
	 */
	private String clientId;

	/**
	 * 网银支付是否是直连
	 */
	private boolean directEbankPay;


	/**
	 * 支付场景
	 */
	private String payScene;
	
	private String netPayerIp;

	public String getPayScene() {
		return payScene;
	}

	public void setPayScene(String payScene) {
		this.payScene = payScene;
	}

	public EBankPayRequestVO() {

	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getEbankAccountType() {
		return ebankAccountType;
	}

	public void setEbankAccountType(String ebankAccountType) {
		this.ebankAccountType = ebankAccountType;
	}

	public boolean isDirectEbankPay() {
		return directEbankPay;
	}

	public void setDirectEbankPay(boolean directEbankPay) {
		this.directEbankPay = directEbankPay;
	}
	
	public String getNetPayerIp() {
		return netPayerIp;
	}

	public void setNetPayerIp(String netPayerIp) {
		this.netPayerIp = netPayerIp;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
