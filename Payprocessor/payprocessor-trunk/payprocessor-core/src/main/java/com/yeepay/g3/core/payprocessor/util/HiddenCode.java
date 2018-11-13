package com.yeepay.g3.core.payprocessor.util;

import org.apache.commons.lang3.StringUtils;

public class HiddenCode {
	private static final String MASK = "***";
	private static final String NAME_MASK = "*";
	private static final String MOBILE_MASK = "****";
	private static final String IDENTITYCODE_MASK = "******";

	/**
	 * 手机号掩码处理,保留后四位
	 * 
	 * @param mobile
	 * @return
	 */
	public static String hiddenMobile(String mobile) {
		if (mobile == null || mobile.length() != 11) {
			return mobile;
		}
		return new StringBuilder().append(mobile.substring(0, 3)).append(MOBILE_MASK).append(mobile.substring(7)).toString();
	}

	/**
	 * 姓名掩码处理，保留首位，其他掩码
	 * 
	 * @param userName
	 * @return
	 */
	public static String hiddenName(String userName) {
		if (userName == null || userName.length() < 2) {
			return userName;
		}
		StringBuilder sbName = new StringBuilder();
		sbName.append(userName.substring(0, 1));
		int len = userName.length() - 1;
		for (int i = 0; i < len; i++) {
			sbName.append(NAME_MASK);
		}
		return sbName.toString();
	}

	/**
	 * 身份证号掩码处理，保留前三后四
	 * 
	 * @param identityCode
	 * @return
	 */
	public static String hiddenIdentityCode(String identityCode) {
		if (identityCode != null && identityCode.length() > 15) {
			return new StringBuilder().append(identityCode.substring(0, 3)).append(IDENTITYCODE_MASK).append(identityCode.substring(identityCode.length() - 4))
					.toString();
		}
		return identityCode;
	}

	/**
	 * 银行卡号掩码处理，保留前6 后四，其他做掩码处理
	 * 
	 * @param bankCardNo
	 * @return
	 */
	public static String hiddenBankCardNO(String bankCardNo) {
		if (bankCardNo != null && bankCardNo.length() > 10) {
			return bankCardNo.substring(0, 6) + MASK
					+ bankCardNo.substring(bankCardNo.length() - 4);
		}
		return bankCardNo;
	}

	public static String hiddenAbliddate(String avliddate) {
		if (!StringUtils.isBlank(avliddate)) {
			return MASK;
		}
		return avliddate;
	}

	/**
	 * CVV做掩码处理
	 */
	public static String hiddenCvv(String cvv) {
		if (!StringUtils.isBlank(cvv)) {
			return MASK;
		}
		return cvv;
	}
	/**
	 * 支付密码做掩码处理
	 */
	public static String hiddenBankPwd(String bankPwd) {
		if (!StringUtils.isBlank(bankPwd)) {
			return MASK;
		}
		return bankPwd;
	}

}
