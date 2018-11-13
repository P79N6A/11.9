package com.yeepay.g3.facade.nccashier.util;

import org.apache.commons.lang3.StringUtils;

public class HiddenCode {
	private static final String MOBILE_MASK = "*******";
	private static final String IDENTITYCODE_MASK = "**********";
	private static final String MASK = "*";
	private static final String BANK_PWD_MASK = "******";
	private static final String CVV_MASK = "***";
	private static final String AVLIDDATE_MASK = "****";
	private static final String VERIFYCODE_MASK = "******";

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
		return mobile.replaceFirst(mobile.substring(3, 7), MOBILE_MASK);
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
		String _name = userName.substring(1, userName.length());
			_name = MASK+_name;
		return _name;
	}

	/**
	 * 身份证号掩码处理，6-15位做掩码处理
	 * 
	 * @param identityCode
	 * @return
	 */
	public static String hiddenIdentityCode(String identityCode) {
		if (identityCode != null && identityCode.length() > 15) {
			return identityCode = identityCode.substring(0, 5)
					+ IDENTITYCODE_MASK
					+ identityCode.substring(15, identityCode.length());
		}
		return identityCode;
	}

	/**
	 * IdentityId掩码处理，保留收尾各1个字符，其余做掩码处理
	 *
	 * @param identityCode
	 * @return
	 */
	public static String hiddenIdentityId(String identityCode) {
		if (identityCode != null && identityCode.length() > 3) {
			return identityCode = identityCode.substring(0, 1) + MASK + identityCode.substring(identityCode.length()-1, identityCode.length());
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
			int len = bankCardNo.length();
			StringBuffer slash = new StringBuffer();
			for (int i = 0; i != len - 10; i++) {
				slash.append(MASK);
			}
			return bankCardNo.substring(0, 6) + slash
					+ bankCardNo.substring(len - 4);
		}
		return bankCardNo;
	}

	public static String hiddenAbliddate(String avliddate) {
		if (!StringUtils.isBlank(avliddate)) {
			return AVLIDDATE_MASK;
		}
		return avliddate;
	}

	/**
	 * CVV做掩码处理
	 */
	public static String hiddenCvv(String cvv) {
		if (!StringUtils.isBlank(cvv)) {
			return CVV_MASK;
		}
		return cvv;
	}
	/**
	 * 支付密码做掩码处理
	 */
	public static String HiddenBankPwd(String bankPwd) {
		if (!StringUtils.isBlank(bankPwd)) {
			return BANK_PWD_MASK;
		}
		return bankPwd;
	}

	/**
	 * 支付密码做掩码处理
	 */
	public static String hiddenVerifyCode(String verifyCode) {
		if (!StringUtils.isBlank(verifyCode)) {
			return VERIFYCODE_MASK;
		}
		return verifyCode;
	}

}
