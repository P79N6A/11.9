package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

/**
 * 账户支付校验接口
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public class AccountPayValidateRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 账户名
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "userAccount未传")
	private String userAccount;

	/**
	 * 交易密码 
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "tradePassword未传")
	private String tradePassword;

	public AccountPayValidateRequestDTO() {

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

	@Override
	public String toString() {
		// 日志不输出交易密码
		return "userAccount=" + this.getUserAccount();
	}
}
