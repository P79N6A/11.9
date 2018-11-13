package com.yeepay.g3.app.nccashier.wap.utils;


public class ConstantUtil {
	
	public static final String QUICK_PAY_QUESTION_URL = "http://www.yeepay.com/customerService/question";

	/**
	 * 移动端使用定制化收银台的商编，统一配置key
	 */
	public static final String WAP_CUSTOMIZED_MERCHANT_NO = "OL_NCCASHIER_WAP_CUSTOMIZED_MERCHANT_NO";
	
	public static final String MERCHANT_LIST_NOT_SHOW_PC_YEEPAY_INFO_KEY = "OL_NCCASHIER_MERCHANT_LIST_NOT_SHOW_PC_YEEPAY_INFO";

	/**
	 * PC端定制了绑卡支付解绑功能的商编，统一配置key
	 */
	public static final String WEB_UNBIND_CARD_MERCHANT_NO = "OL_NCCASHIER_WEB_UNBIND_CARD_MERCHANT_NO";

	/** 微信h5低配版，传送门接口配置，统一配置key */
	public static final String WECHAT_H5_LOW_INTERFACE_CONFIG_KEY = "OL_NCCASHIER_WAP_WECHAT_H5LOW_INTERFACE_CONFIG_KEY";
	/** 微信h5低配版，传送门接口配置，接口地址统一配置field */
	public static final String WECHAT_H5_LOW_INTERFACE_JUMP_URL_V1 = "WECHAT_H5LOW_INTERFACE_JUMP_URL_V1";
	public static final String WECHAT_H5_LOW_INTERFACE_JUMP_URL_V2 = "WECHAT_H5LOW_INTERFACE_JUMP_URL_V2";
	/** 微信h5低配版，传送门接口配置，接口参数key统一配置field */
	public static final String WECHAT_H5_LOW_INTERFACE_PARAM_KEY = "WECHAT_H5LOW_INTERFACE_PARAM_KEY";
	/** 微信h5低配版，传送门接口配置，接口参数f统一配置field */
	public static final String WECHAT_H5_LOW_INTERFACE_PARAM_F = "WECHAT_H5LOW_INTERFACE_PARAM_F";

	/** 微信h5低配版，商户报备的微信oauth2.0回调地址，统一配置key */
	public static final String WECHAT_H5_LOW_MERCHANT_REDIRECT_CONFIG_KEY = "OL_NCCASHIER_WAP_WECHAT_H5LOW_MERCHANT_REDIRECT_CONFIG_KEY";

	/** 钱包h5高低配，用户使用低配版的操作标识，redis键前缀，格式:prefix+token+payType */
	public static final String EWALLET_H5_OPERATE_SIGNAL_REDIS_KEY = "EWALLET_H5_OPERATE_SIGNAL_REDIS_KEY_";
	/** 钱包h5高低配，用户使用低配版的操作标识，redis过期时间，单位：毫秒 */
	public static final int EWALLET_H5_OPERATE_SIGNAL_REDIS_EXPIRE = 60 * 60 * 1000;
	
	
	/**
	 * 各个支付方式页面显示标识
	 */
	public static final String ALIPAY_TYPE = "alipayType"; // 目前使用于支付宝标准版和支付宝生活号
	public static final String ALIPAY = "alipay"; // 支付宝页面标识 
	public static final String ALIPAY_H5 = "alipay_h5"; // 支付宝H5页面标识

	/**
	 * H5收银台支付选择页名称，分为普通页面和定制化页面
	 */
	public static final String COMMON_H5_PAGE_NAME = "newWap"; // 普通的支付选择页（基本上所有的商户都用）
	public static final String CUSTOMIZED_H5_PAGE_NAME = "newWapCustomized"; // 定制化支付选择页（目前应该只有优旅行在用）

	public static final String WPAY_ID_NONE = "none"; // 公众号openId或者支付宝生活号userId获取不到的情况

	/** 公众号支付，可定制由商户获取openId，统一配置key */
	public static final String OPENID_PAY_OAUTH_BY_MERCHANT_CONFIG = "OL_NCCASHIER_OPENID_PAY_OAUTH_BY_MERCHANT_CONFIG";
	/** 公众号支付，可定制由业务方获取openId，统一配置key */
	public static final String OPENID_PAY_OAUTH_BY_BIZ_CONFIG = "OL_NCCASHIER_OPENID_PAY_OAUTH_BY_BIZ_CONFIG";

	/** 公众号H5标准版，QA及内测环境因回调地址问题，无法从微信获取openId，可根据商编配置模拟openid */
	public static final String WECHAT_LOW_MERCHANT_MOCK_OPENID_CONFIG = "OL_NCCASHIER_WECHAT_LOW_MERCHANT_MOCK_OPENID";
	
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String MIDDLE_LINE = "-";
	public static final String MARKET_SUBTRACTION = "立减";
	
	
	public static final String AJAX_SUCCESS = "success";
	public static final String AJAX_FAILED = "failed";
}
