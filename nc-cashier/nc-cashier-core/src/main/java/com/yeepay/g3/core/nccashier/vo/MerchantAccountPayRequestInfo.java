package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

public class MerchantAccountPayRequestInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 收银台token：针对有页面的场景
	 */
	private String tokenId;

	/**
	 * 用户请求IP：有页面的场景取自HttpRequest，无页面的取自商户或上层业务方传值
	 */
	private String userIp;

	/**
	 * 扣款商编：有页面的场景是根据商户在页面输入的登录名从商户平台系统获取到的，无页面的场景是取自交易系统
	 */
	private String debitCustomerNo;
	
	/**
	 * 扣款商户登录账号：对应到商户后台的登录账号，针对有页面的场景
	 */
	private String userAccount;
	
	/**
	 * 密码：针对无页面的场景（前置收银台）预留的字段
	 */
	private String pwsd;

	public MerchantAccountPayRequestInfo() {

	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getDebitCustomerNo() {
		return debitCustomerNo;
	}

	public void setDebitCustomerNo(String debitCustomerNo) {
		this.debitCustomerNo = debitCustomerNo;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getPwsd() {
		return pwsd;
	}

	public void setPwsd(String pwsd) {
		this.pwsd = pwsd;
	}

}
