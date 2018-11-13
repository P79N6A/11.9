package com.yeepay.g3.facade.nccashier.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

/**
 * 账户支付支付下单入参
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public class CashierAccountPayRequestDTO extends BasicPayRequestDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * 账户名
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "userAccount未传")
	private String userAccount;
	
	/**
	 * 付款商编
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "debitCustomerNo未传")
	private String debitCustomerNo;

	public CashierAccountPayRequestDTO() {

	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	
	public String getDebitCustomerNo() {
		return debitCustomerNo;
	}

	public void setDebitCustomerNo(String debitCustomerNo) {
		this.debitCustomerNo = debitCustomerNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
