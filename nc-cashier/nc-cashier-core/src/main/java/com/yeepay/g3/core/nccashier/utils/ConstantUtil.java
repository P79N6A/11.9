package com.yeepay.g3.core.nccashier.utils;

import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;

public class ConstantUtil {

	public static final String ALL_TAG = "*";

	public static final String ALL = "ALL";

	public static final String LINE = "-";
	
	public static final String NO_ACTIVITY = "NO_ACTIVITY";
	
	public static final String MARKET_ACTIVITY_SYS_SUCCESS_CODE = "MK000000";

	public static final Map<String, String> MARKETING_PAYMENT_PRODUCT_KEY_MAPPING = new HashMap<String, String>();

	static {
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put(ALL_TAG, ALL);
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("NCPAY", "NCPAY");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("EANK", "EANK");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("WYZF", "EANK");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("SCCANPAY", "SCCANPAY");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("EWALLET", "EWALLET");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("WECHAT_OPENID", "WECHAT_OPENID");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("EWALLETH5", "EWALLETH5");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("ZFB_SHH", "ZFB_SHH");
		MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.put("BK_ZF", "BK_ZF");// TODO 等营销系统改完，改回来
	}

	public static String getMarketingPaymentProductKey(String keyFromMKGT) {
		return MARKETING_PAYMENT_PRODUCT_KEY_MAPPING.get(keyFromMKGT);
	}
	
	/**
	 * 目前的理解是只有1.0版本在使用营销活动时需要用到这个map
	 */
	public static final Map<Integer, String> PAY_TYPE_MAPPING_TO_PAY_TOOL = new HashMap<Integer, String>();
	static {
//		PAY_TYPE_MAPPING_TO_PAY_TOOL.put(PayTypeEnum.WECHAT_H5_WAP.value(), PayTool.EWALLETH5.name());
//		PAY_TYPE_MAPPING_TO_PAY_TOOL.put(PayTypeEnum.WECHAT_OPENID.value(), PayTool.WECHAT_OPENID.name());
		PAY_TYPE_MAPPING_TO_PAY_TOOL.put(PayTypeEnum.BANK_PAY_WAP.value(), PayTool.NCPAY.name());
//		PAY_TYPE_MAPPING_TO_PAY_TOOL.put(PayTypeEnum.ALIPAY.value(), PayTool.EWALLET.name());
		PAY_TYPE_MAPPING_TO_PAY_TOOL.put(PayTypeEnum.CFL.value(), PayTool.CFL.name());
//		PAY_TYPE_MAPPING_TO_PAY_TOOL.put(PayTypeEnum.WECHAT_H5_LOW.value(), PayTool.EWALLETH5.name());
//		PAY_TYPE_MAPPING_TO_PAY_TOOL.put(PayTypeEnum.ALIPAY_H5_STANDARD.value(), PayTool.EWALLETH5.name());
	}







}
