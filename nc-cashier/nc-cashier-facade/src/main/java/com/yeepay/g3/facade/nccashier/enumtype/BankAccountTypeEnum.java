package com.yeepay.g3.facade.nccashier.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 银行账户类型
 * 
 * @author duangduang
 * @since  2016-11-15
 */
public enum BankAccountTypeEnum {
	
	/**
	 * 对私（B2C）
	 */
	B2C,

	/**
	 * 对公（B2B）
	 */
	B2B;

	public static BankAccountTypeEnum getBankAccountType(String bankAccountTypeStr) {
		BankAccountTypeEnum bankAccountType = null;
		if (StringUtils.isNotBlank(bankAccountTypeStr)) {
			try {
				bankAccountType = BankAccountTypeEnum.valueOf(bankAccountTypeStr);
			} catch (Exception e) {
				return null;
			}
		}
		return bankAccountType;
	}
	
	public static void main(String[] args){
		String bankAccountType = "B2B";
		System.out.println(BankAccountTypeEnum.valueOf(bankAccountType));
	}

}
