package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;

/**
 * 账户支付下单入参
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public class AccountPayRequestVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 收银台token
	 */
	@NotBlank
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg="token为空")
	private String token;

	/**
	 * 账户名
	 */
	@NotBlank
	@ErrorDesc(error = Errors.ACCOUNT_PAY_USER_ACCOUNT_NULL)
	private String userAccount;

	/**
	 * 交易密码
	 */
	@NotBlank
	@ErrorDesc(error = Errors.ACCOUNT_PAY_TRADE_PASSWORD_NULL)
	private String tradePassword;

	/**
	 * 验证码
	 */
	@NotBlank
	@ErrorDesc(error = Errors.ACCOUNT_PAY_CAPTCHA_NULL)
	private String captcha;
	
	public AccountPayRequestVO() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getTradePassword() {
		return tradePassword;
	}

	public void setTradePassword(String tradePassword) {
		this.tradePassword = tradePassword;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	@Override
	public String toString() {
		return "AccountPayRequestVO{" +
				"token='" + token + '\'' +
				", userAccount='" + userAccount + '\'' +
				", tradePassword='" + "【密码不打印】" + '\'' +
				", captcha='" + captcha + '\'' +
				'}';
	}
}
