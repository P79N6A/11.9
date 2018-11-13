package com.yeepay.g3.app.nccashier.wap.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;


import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {
	private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	public final static String BIZ_CODE = "BIZ_CODE";
	public final static String BIZ_NAME = "BIZ_NAME";
	public final static String BIZ_KEY = "BIZ_KEY";
	public final static String BACK_NOTIFY_WAY = "BACK_NOTIFY_WAY";
	public final static String BACK_NOTIFY_ADRESS = "BACK_NOTIFY_ADRESS";
	public final static String FRONT_NOTIFY_WAP = "FRONT_NOTIFY_WAY";
	public final static String QUERY_TRADE_RESULT_INTERFCE = "QUERY_TRADE_RESULT_INTERFCE";
	public final static String MQ = "MQ";
	public final static String HESSIAN = "HESSIAN";
	public final static String ACTIVE = "ACTIVE";
	public final static String PASSIVE = "PASSIVE";
	public final static String PREFIX_NCCASHIER = "NCCASHIER_";
	public final static String PREFIX_OL_NCCASHIER_BUSINESS_CONFIG =
			"OL_NCCASHIER_BUSINESS_CONFIG_";
	public final static String RISK_SMSVERIFY_SWITCH = "OL_NCCASHIER_RISK_SMSVERIFY_SWITCH";
	// TODO 老的收银台默认域名配置，此次上线后可废除
	public final static String NCCASHIER_ENTER_WAP_URL_PREFIX = "OL_NCCASHIER_ENTER_WAP_URL";
	// 新的收银台默认域名配置
	public final static String CASHIER_URL_PREFIX_CONFIG = "OL_NCCASHIER_CASHIER_URL_PREFIX"; 
	private static final String CONFIG_DEFAULT_KEY = "default";
	public final static String ON = "ON";
	public final static String OFF = "OFF";
	public final static String MQ_LISTENER_TIMEOUT ="OL_NCCASHIER_MQ_LISTENER_TIMEOUT";
	public final static String NCCASHIER_ERROR_CODE_MANAGE_SWITCH = "OL_NCCASHIER_ERROR_CODE_MANAGE_SWITCH";
	
	private final static String NET_PAYER_IP_CONFIG_KEY = "OL_NCCASHIER_NET_PAYER_IP_CONFIG";

	/**
	 * 扫码支付微信公众号的配置
	 */
	public final static String SCCANPAY_APP_CONFIG = "OL_NCCASHIER_SCCANPAY_APP_CONFIG";
	/**
	 * 扫码支付，微信授权Auth2的URL的配置
	 */
	public final static String SCCANPAY_WEXIN_AUTH2_URL = "OL_NCCASHIER_SCCANPAY_WEXIN_AUTH2_URL";
	
	private final static String ALIPAY_LIFE_NO_CONFIG_KEY = "OL_NCCASHIER_ALIPAY_LIFE_NO_CONFIG";
	
	/**
	 * 扫码支付，微信获取openId的url配置
	 */
	public final static String SCCANPAY_WEXIN_OPENID_URL = "OL_NCCASHIER_SCCANPAY_WEXIN_OPENID_URL";
	
	public final static String ALIPAY_LIFE_NO_USER_ID_GET_URL_KEY = "OL_NCCASHIER_ALIPAY_LIFE_NO_USER_ID_GET_URL";
	
	/**
	 * 扫码支付，配置微信回调URL
	 */
	public final static String SCCANPAY_AUTH2_CALLBACK_URL = "OL_NCCASHIER_SCCANPAY_AUTH2_CALLBACK_URL";

	/**
	 * 微信h5低配版支付，配置微信回调URL
	 */
	public final static String WECHAT_H5_LOW_AUTH2_CALLBACK_URL = "OL_NCCASHIER_WECHAT_H5_LOW_AUTH2_CALLBACK_URL";

	/**
	 * auth2授权回调地址
	 */
	public final static String AUTH2_CALLBACK_URL_KEY = "OL_NCCASHIER_AUTH2_CALLBACK_URL";
	
	/**
	 * 业务方配置键
	 */
	public static final String BIZ_SYS_CODE_KEY = "ON_NCCASHIER_BIZ_SYS_PARAMS";
	
	public static final String BIZ_SYS_CUSTOM_CASHIER_TYPE = "cashierVersion";

	/**
	 * 前端js监听支付结果轮询次数
	 */
	public final static String JS_LISTENER_TIMES = "OL_NCCASHIER_JS_LISTENER_TIMES";
	
	
	/**
	 * 需要客户ID的网银对公银行列表
	 */
	public final static String EBANK_TO_PUBLIC_LIST_NEED_CLIENTID = "OL_NCCASHIER_EBANK_TO_PUBLIC_LIST_NEED_CLIENTID";

	/**
	 * 默认重定向url前置
	 */
	public final static String REDIREC_HOST_URL = "https://cash.yeepay.com";

	/**
	 * 重定向url前置白名单
	 */
	public final static String REDIREC_HOST_URL_MERCHANTNO = "OL_NCCASHIER_REDIREC_HOST_URL_MERCHANTNO";
	
	/**
	 * 是否取消支付宝支付的路由页面 
	 */
	public static final String CANCLE_ALIPAY_ROUTE_PAGE_SWITCH = "OL_NCCASHIER_CANCLE_ALIPAY_BLUE_PAGE_CONFIG";
	
	public static final String CANCEL_ALIPAY_ROUTE_PAGE_KEY = "cancelFlag";
	
	/**
	 * 取消钱包（sdk）支付宝支付，唤起支付宝时先跳转的中间蓝页时的url
	 */
	private static final String NEW_ALIPAY_PAY_URL_AFTER_CANCEL = "payUrl";
	
	/**
	 * 取消钱包（sdk）支付宝支付，唤起支付宝时先跳转的中间蓝页时的url的变量
	 */
	private static final String NEW_ALIPAY_PAY_URL_VARIABLE = "{prepayCode}";
	
	/**
	 * 商家扫码URL时间戳有效期（秒）
	 */
	public static final String API_MSCAN_TIMESTAMP_RANGE_WITCH = "OL_NCCASHIER_API_MSCAN_URL_EXPDATE";

	/**
	 * 商户定制wap成功页展示广告统一配置key
	 */
	public static final String MERCHANT_AD_CUSTOM_KEY = "ON_NCCASHIER_MERCHANT_AD_CUSTOM_KEY";
	/**
	 * 商户收银台定制化使用VM默认收银台开关
	 */
	public final static String YEEPAY_VM_CASHIER_SWITCH ="OL_NCCASHIER_YEEPAY_CASHIER_CUSTOMIZED_SWITCH"; 
	/**
	 * 商户收银台定制化全局开关，如果打开，则使用非定制化收银台
	 */
	public final static String CASHIER_CUSTOMIZED_SWITCH = "OL_NCCASHIER_CUSTOMIZED_SWITCH"; 
	/**
	 * 非银行卡支付url
	 */
	public final static String NOT_BANK_CARD_PAY_URL = "OL_NCCASHIER_NOT_BANK_CARD_PAY_URL";
	
	public final static String DEFAULT_NOT_BANK_CARD_PAY_URL = "http://www.yeeyk.com/yeex-xcard-app/createOrderForOld";
	
	private static final String DEFAULT_KEY = "default";
	
	/**
	 * 网银直连报错时，跳错误页的商户号，统一配置key
	 */
	public static final String WEB_DIRECT_EBANK_FAIL_MERCHANT_NO = "OL_NCCASHIER_WEB_DIRECT_EBANK_FAIL_MERCHANT_NO";
	
	/**
	 * 微信H5直连，返回给前端唤醒微信的链接，统一配置key
	 */
	public static final String WECHAT_H5_DIRECT_TO_BLANK_MERCHANT_NO = "OL_NCCASHIER_WECHAT_H5_DIRECT_TO_BLANK_MERCHANT_NO";
	/**
	 * 收款宝定制商户，收银台不展示易宝公司相关信息
	 */
	public static final String NOT_SHOW_YEEPAY_COMPANY_INFO_MERCHANT_NO = "OL_NCCASHIER_NOT_SHOW_YEEPAY_COMPANY_INFO_MERCHANT_NO";
	
	public static final String NOT_SHOW_YEEPAY_COMPANY_INFO_ORDER_SYS_NO = "OL_NCCASHIER_NOT_SHOW_YEEPAY_COMPANY_INFO_ORDER_SYS_NO";
	
	/**
	 * 收款宝定制商户，收银台不展示易宝支付协议信息
	 */
	public static final String NOT_SHOW_YEEPAY_AGREEMENT_INFO_MERCHANT_NO = "OL_NCCASHIER_NOT_SHOW_YEEPAY_AGREEMENT_INFO_MERCHANT_NO";
	// 不展示快捷支付协议订单方配置键
	private static final String NOT_SHOW_YEEPAY_AGREEMENT_INFO_ORDER_SYS_NO_KEY = "OL_NCCASHIER_NOT_SHOW_YEEPAY_AGREEMENT_INFO_ORDER_SYS_NO";
	public static final String NOT_SHOW_YEEPAY_AGREEMENT_INFO_FLAG = "notShowYeepayAgreementInfo";
	public static final String NOT_SHOW_YEEPAY_COMPANY_INFO_FLAG = "notShowYeepayCompanyInfo";
	
	public static final String ALIPAY_TRANSFER_DOOR_AUTHKEY = "OL_NCCASHIER_ALIPAY_TRANSFER_DOOR_AUTHKEY";

	/**
	 * 跳页签约收银台回调加密salt
	 */
	public static final String SALT = "1s1@#$!~qzww";

	/**
	 * 首次支付仅走预路由分支
	 */
	public static final String ONLY_PREROUTER = "OL_NCCASHIER_ONLY_PREROUTER";

	/**
	 * 商家扫码扫码类型与NCCASHIER支付类型的映射
	 */
	private static Map<String, String> merchantScanTypeMapping = new HashMap<String, String>();

	/** 针对担保分期下单，判断下单返回的错误码(由PP转换到收银台的)是否需跳转到收银台失败页，统一配置KEY */
	public static final String GUA_INS_PAY_ERROR_TO_FAILPAGE = "OL_NCCASHIER_GUA_INS_PAY_ERROR_TO_FAILPAGE";

	static {
		merchantScanTypeMapping.put("WX", PayTypeEnum.WECHAT_SCAN.name());
		merchantScanTypeMapping.put("ZFB", PayTypeEnum.ALIPAY_SCAN.name());
		merchantScanTypeMapping.put("UPOP", PayTypeEnum.UPOP_PASSIVE_SCAN.name());
	}
	
	/**
	 * 根据商家扫码扫码类型获取支付类型
	 * 
	 * @param scanType
	 * @return
	 */
	public static String getPayTypeByScanType(String scanType) {
		return merchantScanTypeMapping.get(scanType);
	}
	
	public static boolean checkBizType(String bizType){
		return StringUtils.isBlank(bizType);
	}
	
	/**
	 * 字符串反转
	 * 
	 * @param s
	 * @return
	 */
	public static String reverse(String s) {
		char[] str = s.toCharArray();
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < str.length; i++)
			stack.push(str[i]);

		String reversed = "";
		for (int i = 0; i < str.length; i++)
			reversed += stack.pop();

		return reversed;
	}

	/**
	 * 获取字符串后七位
	 * 
	 * @param str
	 * @return
	 */
	public static String getLastSevenNo(String str) {
		String s = "";
		if (str.length() >= 7) {
			s = str.substring(str.length() - 7);
		} else {
			s = str;
			for (int i = 0; i < 7 - str.length(); i++) {
				s = s + "0";
			}
		}
		return s;
	}
	
	/**
	 * 根据业务方编码获取业务方配置信息
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getBusinessConfig(String bizCode) {
		try {
			ConfigParam<Map<String, String>> businessConfig = ConfigurationUtils
					.getSysConfigParam(PREFIX_OL_NCCASHIER_BUSINESS_CONFIG + bizCode);
			Map<String, String> businessInfoMap = null;
			if (null != businessConfig) {
				businessInfoMap = businessConfig.getValue();
			}
			return businessInfoMap;
		} catch (Exception e) {
			logger.error("从三代统一配置中读取业务方配置信息出错", e);
			return null;
		}
	}
	
	/**
	 * 获取三代统一配置信息
	 * 
	 * @param key
	 * @return Map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getConfig(String key) {

		ConfigParam<Map<String, String>> businessConfig = ConfigurationUtils.getSysConfigParam(key);
		Map<String, String> businessInfoMap = null;
		if (null != businessConfig) {
			businessInfoMap = businessConfig.getValue();
		}
		return businessInfoMap;

	}
	
	/**
	 * 是否使用风控系统短验开关，若为ON则使用风控系统短验类型，若为OFF则使用本系统的短验类型
	 * 
	 * @return
	 */
	public static boolean getRiskVerifyCodeSwitch() {
		String riskVerifySwitch = "";
		try {
			riskVerifySwitch = getSysConfigFrom3G(RISK_SMSVERIFY_SWITCH);
		} catch (Exception e) {
			riskVerifySwitch = OFF;
			logger.error("获取统一配置信息失败:", e);
		}
		return ON.equals(riskVerifySwitch);
	}
	
	
	/**
	 * 返回商家扫码URL的有效期（以秒为单位）
	 * 
	 * @return
	 */
	public static long getMscanUrlExpDate() {
		long expDate = 120; // 秒为单位
		try {
			String expDateStr = getSysConfigFrom3G(API_MSCAN_TIMESTAMP_RANGE_WITCH);
			expDate = Long.parseLong(expDateStr);
		} catch (Throwable t) {
			expDate = 120;
			logger.error("获取统一配置信息失败:{}", t);
		}
		return expDate;
	}

	/**
	 * 获取监听FE MQ支付标识超时时间,单位秒
	 * 
	 * @return
	 */
	public static long getMQListerTimeout() {
		Long timeout = null;
		try {
			timeout = getSysConfigFrom3G(MQ_LISTENER_TIMEOUT);
		} catch (Exception e) {
			logger.error("三代统一配置获取监听FE MQ支付标识超时时间失败:", e);
		}
		if(timeout == null){
			timeout = 20l;
		}
		return timeout;
	}

	/**
	 * 获取监听标识 服务器端超时时间 【包括pc已扫标识 & 支付结果可查标识】
	 * @return
	 */
	public static long getListenerServerTimeout(){
		Long timeout =null;
		try{
			String timeoutStr = getSysConfigFrom3G(Constant.LISTENER_SERVER_TIMEOUT);
			if(StringUtils.isNotBlank(timeoutStr)){
				timeout = Long.valueOf(timeoutStr);
			}
		}catch(Exception e){
			logger.error("三代统一配置获取监听标识超时时间失败:",e);
		}
		if(timeout == null){
			timeout = 5l;
		}
		return timeout;
	}

	/**
	 * 获取前端js监听支付结果轮询次数
	 * 
	 * @return
	 */
	public static long getJSListenerTimes() {
		Long timeout =null;
		try {
			timeout = getSysConfigFrom3G(JS_LISTENER_TIMES);
		} catch (Exception e) {
			logger.error("三代统一配置获取监听FE MQ支付标识超时时间失败:", e);
		}
		if(timeout == null){
			timeout = 5l;
		}
		return timeout;
	}
	
	
	/**
	 * 是否对接错误码管理系统开关配置值获取
	 * @return
	 */
	public static boolean getErrorCodeManageSwitch(){
		String errorCodeManageSwitch ="";
		try{
			errorCodeManageSwitch = getSysConfigFrom3G(NCCASHIER_ERROR_CODE_MANAGE_SWITCH);
		}catch(Exception e){
			logger.error("getErrorCodeManageSwitch 获取统一配置信息失败:", e);
		}
		return ON.equals(errorCodeManageSwitch);
	}
	
	/**
	 * 获得3代统一配置数据
	 * 
	 * @param key
	 * @return
	 */
	public static <T> T getSysConfigFrom3G(String key) {

		T config = null;
		try {
			ConfigParam p = ConfigurationUtils.getSysConfigParam(key);
			if (p == null || p.getValue() == null) {
				return null;
			} else {
				config = (T) p.getValue();
			}
		} catch (Exception e) {
			logger.error("获取出错", e);
			return null;
		}
		return config;
	}
	
	/**
	 * 获取需要客户ID的对公银行列表 [银行编码-银行名称]
	 * @return
	 */
	public static Set<String> ebanksToPublicNeedClientId(){
		logger.info("获取需要客户ID的对公银行列表");
		try{
			Map<String, String> ebanksToPublicNeedClientId = getSysConfigFrom3G(EBANK_TO_PUBLIC_LIST_NEED_CLIENTID);
			if(MapUtils.isNotEmpty(ebanksToPublicNeedClientId)){
				return ebanksToPublicNeedClientId.keySet();
			}
		}catch(Exception e){
			logger.error("ebankListNeedClientId 获取统一配置需要客户ID的对公银行列表失败:", e);
		}
		return null;
	}
	
	
	/**
	 * 获取扫码支付用的微信公众号APPID 和密码
	 * 
	 * @return
	 */
	public static Map<String, String> getSccanPayAppId() {
		Map<String, String> appConfig = getSysConfigFrom3G(SCCANPAY_APP_CONFIG);
		if (appConfig == null) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		return appConfig;
	}

	/**
	 * 获取微信授权的url配置
	 * 
	 * @return
	 */
	public static String getWeAuthUrl() {
		String authUrl = getSysConfigFrom3G(SCCANPAY_WEXIN_AUTH2_URL);
		if (StringUtils.isBlank(authUrl)) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		return authUrl;
	}
	
	/**
	 * 获取支付宝生活号某个属性配置
	 * 
	 * @param propertyKey
	 *            可能取值：auth2Url、callbackUrl、userIdGetUrl、alipayAppId、
	 *            alipayAppSecret
	 * @return
	 */
	public static String getAlipayLifeNoConfig(String propertyKey) {
		Map<String, String> alipayLifeNoConfig = null;
		try {
			alipayLifeNoConfig = getSysConfigFrom3G(ALIPAY_LIFE_NO_CONFIG_KEY);
		} catch (Throwable t) {
			logger.error("获取支付宝生活号相关配置异常, ex=", t);
		}
		logger.info("获取支付宝生活号相关配置, alipayLifeNoConfig={}", alipayLifeNoConfig);
		return MapUtils.isNotEmpty(alipayLifeNoConfig) ? alipayLifeNoConfig.get(propertyKey) : null;
	}
	

	/**
	 * 微信获取openID的api接口url
	 * 
	 * @return
	 */
	public static String getWeOpenIdUrl() {
		String openUrl = getSysConfigFrom3G(SCCANPAY_WEXIN_OPENID_URL);
		if (StringUtils.isBlank(openUrl)) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		return openUrl;
	}

	/**
	 * 获取微信oauth2回调地址，公众号支付使用
	 * @return
	 */
	public static String getAuth2CallBack() {
		String openUrl = getSysConfigFrom3G(SCCANPAY_AUTH2_CALLBACK_URL);
		if (StringUtils.isBlank(openUrl)) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		return openUrl;
	}
	/**
	 * 获取微信oauth2回调地址，微信h5低配版使用
	 * @return
	 */
	public static String getAuth2CallBackForH5Low() {
		String openUrl = getSysConfigFrom3G(WECHAT_H5_LOW_AUTH2_CALLBACK_URL);
		if (StringUtils.isBlank(openUrl)) {
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		return openUrl;
	}

	/**
	 * 转换银行账户类型
	 * @param str
	 * @return
	 */
	public static String getBankAccountType(String str){
		if("B2C".equals(str)){
			return "INDIVIDUAL";
		}else if("B2B".equals(str)) {
			return "BUSINESS";
		}else {
			return null;
		}
	}

	/**
	 * 获取重定向的前置请求地址
	 * @return
	 */
	public static String getPreUrl(String merchantNo,HttpServletRequest request){
		String stringUrl = "https://"+request.getServerName()+request.getContextPath();
		if(StringUtils.isEmpty(merchantNo)){
			return stringUrl;
		}
		try {
			Map<String,String> map = getSysConfigFrom3G(REDIREC_HOST_URL_MERCHANTNO);
			if(map == null){
				return stringUrl;
			}else {
				//如果配置了商户编号对应的url前缀
				String urlPre = map.get(merchantNo);
				if(StringUtils.isNotEmpty(urlPre)){
					return urlPre;
//					return "http://localhost:8084/nc-cashier-wap";
				}else {
					return stringUrl;
				}
			}
		}catch (Throwable e){
			logger.error("[monitor],event:nccashierwap_getPreUrl, 获取重定向url前置失败");
		}
		return stringUrl;
	}

	
	/**
	 * 商户url是否过期
	 * 
	 * @param timestampInput （秒为单位）
	 * @return
	 */
	public static void checkUrlOutOfExpDate(long timestampInput) {
		long distance = System.currentTimeMillis() - (timestampInput*1000);
		long expdate = CommonUtil.getMscanUrlExpDate() * 1000;
		if(distance > expdate){
			throw new CashierBusinessException(Errors.TIMESTAMP_OUT_OF_EXPIRE_DATE.getCode(), Errors.TIMESTAMP_OUT_OF_EXPIRE_DATE.getMsg());
		}
	}

	public static boolean regexRealm(String string){
		String regex = "(((http|https)://){0,1}\\w+\\.(yeepay.com))(.*)";
		return (StringUtils.isNotBlank(string) && string.matches(regex));
	}

	/**
	 * 获取业务方定制的收银台版本
	 * 
	 * @param bizType
	 * @return
	 */
	public static CashierVersionEnum getCustomCashierVersionByBizType(String bizType) {
		if (StringUtils.isBlank(bizType)) {
			return null;
		}
		try {
			Map<String, String> map = getSysConfigFrom3G(BIZ_SYS_CODE_KEY);
			if (MapUtils.isNotEmpty(map) && StringUtils.isNotBlank(map.get(bizType))) {
				JSONObject configValues = JSON.parseObject(map.get(bizType));
				String cashVersion = (String) configValues.get(BIZ_SYS_CUSTOM_CASHIER_TYPE);
				if(StringUtils.isNotBlank(cashVersion)){
					return CashierVersionEnum.valueOf(cashVersion);
				}
			}
		} catch (Throwable t) {
			logger.error("从三代统一配置获取业务方配置的参数信息异常:" + bizType, t);
		}
		return null;
	}
	
	
	/**
	 * 商户定制wap成功页展示广告
	 * 
	 * @param merchantNo
	 * @return
	 */
	public static boolean merchantShowAdWhenH5PaySuccess(String merchantNo) {
		if (StringUtils.isBlank(merchantNo)) {
			return false;
		}
		try {
			Map<String, String> merchantAdCustomMap = getSysConfigFrom3G(MERCHANT_AD_CUSTOM_KEY);
			// 开关明确关或开
			if (MapUtils.isNotEmpty(merchantAdCustomMap)) {
				if (Constant.OFF.equals(merchantAdCustomMap.get(merchantNo))) {
					return false;
				} else if (Constant.ON.equals(merchantAdCustomMap.get(merchantNo))) {
					return true;
				}
			}
		} catch (Throwable t) {
			logger.error("从三代统一配置获取商户定制h5成功页展示广告异常，merchantNo=" + merchantNo, t);
		}

		return Constant.SHOW_ADVETISEMENT_MERCHANT_NO.equals(merchantNo);
	}

	/**
	 * 是否使用易宝保底收银台，ON为使用非定制化默认收银台，OFF为使用定制化默认收银台，默认值为ON
	 * @return
	 */
	public static boolean getYeepayVmCashierSwitch() {
		String yeepayVmSwitch = ON;
		try {
			yeepayVmSwitch = getSysConfigFrom3G(YEEPAY_VM_CASHIER_SWITCH);
		} catch (Exception e) {
			yeepayVmSwitch = ON;
			logger.error("获取统一配置信息失败:", e);
		}
		return ON.equals(yeepayVmSwitch);
	}
	/**
	 * 收银台模版定制化全局开关，OFF为使用定制化收银台，ON为使用默认收银台，默认值为OFF
	 * @return
	 */
	public static boolean getCashierCustomizeWholeSwitch(){
		String cashierCustomizedSwitch = OFF;
		try{
			cashierCustomizedSwitch = getSysConfigFrom3G(CASHIER_CUSTOMIZED_SWITCH);
		}catch(Exception e){
			cashierCustomizedSwitch = OFF;
			logger.error("获取统一配置信息失败:", e);
		}
		return ON.equals(cashierCustomizedSwitch);
	}

	/**
	 * 预路由开关
	 * @return
	 */
	public static boolean getPrerouterSwitch(){
		String prerouterSwitch = OFF;
		try{
			prerouterSwitch = getSysConfigFrom3G(ONLY_PREROUTER);
		}catch(Exception e){
			prerouterSwitch = OFF;
			logger.error("获取统一配置信息失败:", e);
		}
		return ON.equals(prerouterSwitch);
	}

	// 根据商户选择的支付工具排序和商户开通的支付工具，综合获取商户最终选择的支付工具（有排序）
	public static String[] getValidPayTools(String[] payTools, String[] payToolsOrder) {
		// 商户开通的支付工具转换成list
		List<String> payToolsList = Arrays.asList(payTools);
		//对商户开通的支付工具进行处理后的结果
		List<String> defaultPayToolOrderList = new ArrayList<String>();
		//如果CFL和SCCANPAY同时存在，则不添加defaultPayToolOrderList；
		//如果CFL存在，SCCANPAY不存在，则将SCCANPAY添加至defaultPayToolOrderList；
		//其他情形都添加至defaultPayToolOrderList
		for (int i = 0; i < payTools.length; i++) {
			if (PayTool.CFL.name().equals(payTools[i]) && !payToolsList.contains(PayTool.SCCANPAY.name())) {
				defaultPayToolOrderList.add(PayTool.SCCANPAY.name());
			}else if(PayTool.CFL.name().equals(payTools[i]) && payToolsList.contains(PayTool.SCCANPAY.name())){
				continue;
			}else{
				defaultPayToolOrderList.add(payTools[i]);
			}
		}	
		// 如果商户未对支付工具进行排序，则返回默认开通的支付工具排序
		if (payToolsOrder == null || payToolsOrder.length == 0) {		
			String[] defaultPayToolOrder = new String[defaultPayToolOrderList.size()];
			return defaultPayToolOrderList.toArray(defaultPayToolOrder);
		}
		// 商户选择的支付工具排序
		List<String> payToolsOrderList = Arrays.asList(payToolsOrder);
		// 筛选后的支付工具
		List<String> validPayToolsList = new ArrayList<String>();
		// （判断选择的支付工具是否开通）判断商户选择的是否在开通的支付工具中，如果在，则保存至list
		for (String validPayTool : payToolsOrderList) {
			if (defaultPayToolOrderList.contains(validPayTool)) {
				validPayToolsList.add(validPayTool);
			}
		}
		// （开通的支付工具比选择的多）判断商户开通的是否在商户选择的排序的支付工具中，如果不在，则保存至list
		for (String validPayTool : defaultPayToolOrderList) {
			if(!payToolsOrderList.contains(validPayTool)){
				validPayToolsList.add(validPayTool);
			}
		}
		String[] validPayTools = new String[validPayToolsList.size()];
		return validPayToolsList.toArray(validPayTools);
	}
	
	/**
	 * 非银行卡支付请求url
	 * @return
	 */
	public static String getNotBankCardPayUrl() {
		String notBankCardPayurl = "";
		try {
			notBankCardPayurl = getSysConfigFrom3G(NOT_BANK_CARD_PAY_URL);
		} catch (Exception e) {
			logger.error("获取统一配置信息失败:", e);
		}
		if(StringUtils.isBlank(notBankCardPayurl)){
			notBankCardPayurl = DEFAULT_NOT_BANK_CARD_PAY_URL;
		}
		return notBankCardPayurl;
	}
	
	//收款宝订单方编码
	public static final String SKB_ORDER_SYS_NO = "7";
	public static final String ORDER_SYS_NO_LIST = "OL_NCCASHIER_SUCCESS_PAGE_ORDER_SYS_NO_LIST";
	public static final List<String> getOrderSysNoList(){
		List<String> list = null;
		try{
			list = getSysConfigFrom3G(ORDER_SYS_NO_LIST);
		}catch (Exception e) {
			logger.error("获取统一配置信息失败:", e);
		}
		if(CollectionUtils.isEmpty(list)){
			list.add(CommonUtil.SKB_ORDER_SYS_NO);
		}
		return list;
	}
	/**
	 * 网银直连时，配置中的商户跳转错误页
	 * @param merchantNo
	 * @return
	 */
	public static boolean checkDirectEbankToFailPage(String merchantNo) {
		String customizedMerchant = "";
		try{
			customizedMerchant = getSysConfigFrom3G(WEB_DIRECT_EBANK_FAIL_MERCHANT_NO);
		}catch (Exception e) {
			logger.error("获取统一配置信息失败:", e);
		}
		if(StringUtils.isNotBlank(customizedMerchant) && customizedMerchant.contains(merchantNo)){
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 微信H5直连时，配置中的商户号先跳转空白页
	 * @param merchantNo
	 * @return
	 */
	public static boolean checkDirectWechartH5ToBlankPage(String merchantNo) {
		String customizedMerchant = "";
		try{
			customizedMerchant = getSysConfigFrom3G(WECHAT_H5_DIRECT_TO_BLANK_MERCHANT_NO);
		}catch (Exception e) {
			logger.error("获取统一配置信息失败:", e);
		}
		if(StringUtils.isNotBlank(customizedMerchant) && customizedMerchant.contains(merchantNo)){
			return true;
		}else {
			return false;
		}
	}
	private static final String COMPANY_INFO_SHOW_TEXT_SWITCH = "OL_NCCASHIER_COMPANY_INFO_SHOW_TEXT";
	private static final String SHOW_EMPTY_COMPANY_INFO_KEY = "showEmpty";
	private static final String SHOW_EMPTY_COMPANY_INFO_OF_ORDER_SYS_NO_KEY = "showEmptyOrderSys";
	private static final String SHOW_PREAUTH_DEFAULT_COMPANY_INFO_KEY = "preauthDefault";
	
	/**
	 * 针对H5收银台的页面的【付款给***】做定制 - 以后H5收银台做了定制之后可以去掉
	 * 
	 * @param merchantNo
	 * @param companyName
	 * @param cusType 如果传PREAUTH，默认展示preauthTextToShow；如果传SALE或者不传，默认展示textToShow
	 * @return
	 */
	public static final String getCompanyInfoShowText(String merchantNo, String orderSysNo, String companyName, String cusType) {
		String textToShow = "付款给" + companyName; 
		String preauthTextToShow = "预授权给" + companyName;
		Map<String, String> showTextMap = null;
		try {
			showTextMap = getSysConfigFrom3G(COMPANY_INFO_SHOW_TEXT_SWITCH);
		} catch (Throwable t) {
			logger.error("获取商户定制的付款方展示文案异常:", t);
		}

		if (MapUtils.isEmpty(showTextMap)) {
			return textToShow;
		}

		// 商编有专门的定制，则返回该商编定制的内容
		String merchantNoText = showTextMap.get(merchantNo);
		if (StringUtils.isNotBlank(merchantNoText)) {
			if (merchantNoText.contains("<>")) {
				merchantNoText = merchantNoText.replace("<>", companyName);
			}
			return merchantNoText;
		}

		// 商编在不展示付款方信息的列表里或者订单方系统编码在不展示付款方信息的订单方列表里，则返回空
		if (StringHelper.contains(showTextMap.get(SHOW_EMPTY_COMPANY_INFO_KEY), merchantNo)
				|| StringHelper.contains(showTextMap.get(SHOW_EMPTY_COMPANY_INFO_OF_ORDER_SYS_NO_KEY), orderSysNo)) {
			return "";
		}
		
		if(Constant.BNAK_RULE_CUSTYPE_PREAUTH.equals(cusType)){
			String preauthDefaultText = showTextMap.get(SHOW_PREAUTH_DEFAULT_COMPANY_INFO_KEY);
			if(StringUtils.isBlank(preauthDefaultText)){
				return preauthTextToShow;
			}
			if(StringUtils.isNotBlank(preauthDefaultText) && preauthDefaultText.contains("<>")){
				preauthDefaultText = preauthDefaultText.replace("<>", companyName);
			}
			return preauthDefaultText;
		}else{
			String defaultText = showTextMap.get(DEFAULT_KEY);
			if (StringUtils.isBlank(defaultText)) {
				return textToShow;
			}
			if (StringUtils.isNotBlank(defaultText) && defaultText.contains("<>")) {
				defaultText = defaultText.replace("<>", companyName);
			}
			return defaultText;
		}
	}
	
	
	/**
	 * 获取支付宝唤起时跳转的支付蓝页取消后，重新拼接的url
	 * 
	 * @return
	 */
	public static Map<String, Object> getAlipayPayUrl(String prepayCode) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cancelFlag", false);
		result.put("payUrl", prepayCode);
		if (StringUtils.isBlank(prepayCode)) {
			return result;
		}
		Map<String, String> cancelConfig = null;
		try {
			cancelConfig = getSysConfigFrom3G(CANCLE_ALIPAY_ROUTE_PAGE_SWITCH);
		} catch (Throwable t) {
			logger.error("获取钱包/钱包h5支付宝唤起中间蓝页取消相关配置异常:", t);
		}

		if (MapUtils.isEmpty(cancelConfig)) {
			return result;
		}
		String cancelFlag = cancelConfig.get(CANCEL_ALIPAY_ROUTE_PAGE_KEY);
		if (!ON.equals(cancelFlag)) {
			return result;
		}
		
		result.put("cancelFlag", true);
		String payUrl = cancelConfig.get(NEW_ALIPAY_PAY_URL_AFTER_CANCEL);
		if (StringUtils.isBlank(payUrl) || !payUrl.contains(NEW_ALIPAY_PAY_URL_VARIABLE)) {
			result.put("payUrl", "alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode="
					+ prepayCode + "?_s=web-other");
		} else {
			payUrl = payUrl.replace(NEW_ALIPAY_PAY_URL_VARIABLE, prepayCode);
			result.put("payUrl", payUrl);
		}
		logger.info("取消支付宝唤起中间蓝页，拼接payUrl={}", payUrl);
		return result;
	}

	/**
	 * 微信h5低配版，获取商户配置的微信oauth2.0回调地址
	 * @param merchantNo
	 * @return
	 */
	public static String getWechatH5LowMerchantRedirect(String merchantNo) {
		Map<String, String> map = CommonUtil.getSysConfigFrom3G(ConstantUtil.WECHAT_H5_LOW_MERCHANT_REDIRECT_CONFIG_KEY);
		if (map == null) {
			logger.error("getWechatH5LowMerchantRedirect() 获取失败，读取统一配置map失败，请检查统一配置key={}", ConstantUtil.WECHAT_H5_LOW_MERCHANT_REDIRECT_CONFIG_KEY);
			throw new CashierBusinessException(Errors.OAUTH_URI_NOT_CONFIG_EXCEPTION.getCode(), Errors.OAUTH_URI_NOT_CONFIG_EXCEPTION.getMsg());
		}
		String merchantRedirect = map.get(merchantNo);
		if (com.yeepay.g3.utils.common.StringUtils.isBlank(merchantRedirect)) {
			logger.error("getWechatH5LowMerchantRedirect() 获取失败，merchantNo={}，读取统一配置map未获取到该商编对应配置,请检查统一配置key={}", merchantNo, ConstantUtil.WECHAT_H5_LOW_MERCHANT_REDIRECT_CONFIG_KEY);
			throw new CashierBusinessException(Errors.OAUTH_URI_NOT_CONFIG_EXCEPTION.getCode(), Errors.OAUTH_URI_NOT_CONFIG_EXCEPTION.getMsg());
		}
		return merchantRedirect;
	}

	/**
	 * 微信h5低配版，获取调用传送门API的相关配置
	 * @param field 配置项field
	 * @return
	 */
	public static String getWechatH5LowAPIConfig(String field) {
		Map<String, String> map = CommonUtil.getSysConfigFrom3G(ConstantUtil.WECHAT_H5_LOW_INTERFACE_CONFIG_KEY);
		if (map == null) {
			logger.error("getWechatH5LowAPIConfig() 获取失败，读取统一配置map失败，请检查统一配置key={}", ConstantUtil.WECHAT_H5_LOW_INTERFACE_CONFIG_KEY);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		String merchantRedirect = map.get(field);
		if (com.yeepay.g3.utils.common.StringUtils.isBlank(merchantRedirect)) {
			logger.error("getWechatH5LowAPIConfig() 获取失败，field={}，读取统一配置map未获取到该域对应配置,请检查统一配置key={}", field, ConstantUtil.WECHAT_H5_LOW_INTERFACE_CONFIG_KEY);
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		}
		return merchantRedirect;
	}
	
	/**
	 * 收款宝商户定制，收银台页面不展示易宝公司信息
	 * 
	 * @param merchantNo
	 * @param orderSysNo
	 * @return
	 */
	public static boolean notShowYeepayCompanyInfo(String merchantNo, String orderSysNo) {
		boolean notShowYeepayCompanyInfo = false; // 不展示收银台底部信息
		if (StringUtils.isNotBlank(merchantNo)) {
			notShowYeepayCompanyInfo = getConfigByConfigKeyAndBusiKeyWithVerticalLine(
					NOT_SHOW_YEEPAY_COMPANY_INFO_MERCHANT_NO, merchantNo);
			if (!notShowYeepayCompanyInfo) {
				if(StringUtils.isBlank(orderSysNo)){
					return true;
				}
				// 没有配置商户，则取业务方
				return getConfigByConfigKeyAndBusiKeyWithVerticalLine(NOT_SHOW_YEEPAY_COMPANY_INFO_ORDER_SYS_NO,
						orderSysNo);
			}
		}
		return notShowYeepayCompanyInfo;
	}
	
	/**
	 * 字符类配置键，可以以竖线等分割，其实都无所谓
	 * 
	 * @param configKey
	 * @param busiKey
	 * @return
	 */
	public static boolean getConfigByConfigKeyAndBusiKeyWithVerticalLine(String configKey, String busiKey) {
		String customizedValueStr = "";
		try {
			customizedValueStr = getSysConfigFrom3G(configKey);
		} catch (Exception e) {
			logger.error("获取统一配置信息失败:", e);
		}
		if (StringUtils.isNotBlank(customizedValueStr) && customizedValueStr.contains(busiKey)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 收款宝商户定制，收银台页面不展示易宝协议信息
	 * 
	 * @param merchantNo
	 * @return
	 */
	public static boolean notShowYeepayAgreementInfo(String merchantNo, String orderSysNo) {
		
		if (StringUtils.isNotBlank(merchantNo)) {
			// 商编不为空且定制不展示，则直接返回
			boolean merchantNotShow = getConfigByConfigKeyAndBusiKeyWithVerticalLine(
					NOT_SHOW_YEEPAY_AGREEMENT_INFO_MERCHANT_NO, merchantNo);
			if (merchantNotShow) {
				return merchantNotShow;
			}
		}

		if (StringUtils.isNotBlank(orderSysNo)) {
			// 商编不定制，则返回订单方定制结果
			return getConfigByConfigKeyAndBusiKeyWithVerticalLine(NOT_SHOW_YEEPAY_AGREEMENT_INFO_ORDER_SYS_NO_KEY,
					orderSysNo);
		}
		
		// 默认返回展示
		return false;
	}
	
	/**
	 * 获取从普通浏览器跳转到支付宝浏览器，黑科技技术进行传送所需要的key
	 * 
	 * @return
	 */
	public static String getAuthKeyOfAlipayTransferDoor() {
		try {
			return getSysConfigFrom3G(ALIPAY_TRANSFER_DOOR_AUTHKEY);
		} catch (Throwable t) {
			logger.error("获取统一配置信息失败:", t);
		}
		return null; 
	}
	
	/**
	 * 获取H5收银台支付选择页面的页面名称
	 * 
	 * @param merchantNo
	 * @return
	 */
	public static String h5PageCustomizedOrNot(String merchantNo) {
		String customizedMerchant = CommonUtil.getSysConfigFrom3G(ConstantUtil.WAP_CUSTOMIZED_MERCHANT_NO);
		if (StringUtils.isNotBlank(customizedMerchant) && customizedMerchant.contains(merchantNo)) {
			return ConstantUtil.CUSTOMIZED_H5_PAGE_NAME;
		} else {
			return ConstantUtil.COMMON_H5_PAGE_NAME;
		}
	}
	
	/**
	 * 获取不展示WEB收银台底部的商户黑名单列表，校验商户是否要展示商户黑名单
	 * 
	 * @param merchantNo
	 * @return 不展示返回true，展示返回false
	 */
	public static boolean judgeMerchantNotShowPcYeepayInfo(String merchantNo) {
		List<String> merchantNoList = null;
		try {
			merchantNoList = getSysConfigFrom3G(ConstantUtil.MERCHANT_LIST_NOT_SHOW_PC_YEEPAY_INFO_KEY);
		} catch (Throwable t) {
			logger.error("judgeMerchantShowPcYeepayInfoOrNot获取统一配置信息失败:", t);
		}
		// 配置了黑名单 且商户在黑名单内，返回true，不展示
		if (CollectionUtils.isNotEmpty(merchantNoList) && merchantNoList.contains(merchantNo)) {
			return true;
		}
		return false;
	}


	/**
	 * 获取嘉年华最小金额限制
	 * @return
     */
	public static String getJnhMinAmount(){
		//活动默认的最小金额
		String defaultAmount = "1000";
		String amount = null;
		try{
			amount = CommonUtil.getSysConfigFrom3G("OL_NCCASHIER_CARNIVAL_2018_0402_MIN_AMOUNT");
		}
		catch (Exception e ){
			amount = defaultAmount;
		}
		if (StringUtils.isEmpty(amount))
			amount = defaultAmount;
		return amount;

	}


	/**
	 * 获取嘉年华商户白名单列表
	 * @return
     */
	public static List<String> getJnhWhiteCheckList(){
		List<String> whiteCheckList = new ArrayList<String>();
		try {
			String whiteCheck = CommonUtil.getSysConfigFrom3G("OL_NCCASHIER_CARNIVAL_2018_0402_MER_WHITECHECK");
			if(StringUtils.isNotBlank(whiteCheck)) {
				whiteCheckList = Arrays.asList(whiteCheck.split("\n|\r\n|\r"));
			}
		}
		catch (Exception e){

		}
		return whiteCheckList;
	}

	/**
	 * 获取成功页面商户白名单配置
	 * @return
	 */
	public static List<String> getSuccessActivateMerchantNoList(){
		List<String> merchantNoList = new ArrayList<String>();
		try{
			Map<String,String>  businessInfoMap = CommonUtil.getSysConfigFrom3G("OL_NCCASHIER_SUCCESS_ACT_SHOW");
			if (null != businessInfoMap){
				String merchantno = businessInfoMap.get("MERCHANTNO");
				merchantNoList = Arrays.asList(merchantno.split(","));
			}
		}catch (Exception e){
			logger.error("获取统一配置信息失败:", e);
		}
		return merchantNoList;
	}


	/**
	 * 获得活动支持的开始和结束时间
	 */
	public static boolean getSuccessActivitiesCanJoin(){
		boolean canJoin = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		try {
			Date start = null;// 开始时间
			Date end = null;// 结束时间
			Map<String, String> businessInfoMap = CommonUtil.getSysConfigFrom3G("OL_NCCASHIER_SUCCESS_ACT_SHOW");
			if (null != businessInfoMap) {
				// 活动开始时间与结束时间
				String startTime = businessInfoMap.get("START_TIME");
				String endTime = businessInfoMap.get("END_TIME");
				if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
					try {
						start = sdf.parse(startTime);
						end = sdf.parse(endTime);
						if (now.after(start) && now.before(end)) {
							canJoin = true;
							logger.info("处于活动期间");
						}

					} catch (ParseException e) {
						logger.warn("活动时间设置错误", e);
					}
				}
			}
		} catch (Throwable e) {
			logger.error("获取参加活动时间区间时报错", e);
		}
		return canJoin;
	}


	/**
	 * 获取活动地址
	 * @return
	 */
	public static String getSuccessActivitiesUrl(){
		String activateUrl = null;
		try {
			Map<String, String> businessInfoMap = CommonUtil.getSysConfigFrom3G("OL_NCCASHIER_SUCCESS_ACT_SHOW");
			if (null != businessInfoMap) {
				// 活动开始时间与结束时间
				activateUrl = businessInfoMap.get("ACTIVATE_URL");
			}
		} catch (Throwable e) {
			logger.error("获取活动地址", e);
		}
		return activateUrl;
	}

	/**
	 * 根据商编和订单方编码判断是否要展示H5收银台支付选择页面上的客服服务
	 * 
	 * @param merchantNo
	 * @param orderSysNo
	 * @return true if show
	 */
	public static boolean wapShowCustomService(String merchantNo, String orderSysNo) {
		boolean showCustomServiceOfMerchantNo = getConfigByConfigKeyAndBusiKey(
				"OL_NCCASHIER_WAP_SHOW_CUSTOM_SERVICE_KEY", merchantNo);
		if (showCustomServiceOfMerchantNo) {
			return false;
		}
		if(StringUtils.isBlank(orderSysNo)){
			return true;
		}
		return !getConfigByConfigKeyAndBusiKey("OL_NCCASHIER_WAP_SHOW_CUSTOM_SERVICE_ORDER_SYS_NO_KEY", orderSysNo);
	}

	/**
	 * 判断统一配置(换行符分隔)是否包含所需配置值
	 * @param configKey
	 * @param busiKey
	 * @return true if contains
	 */
	private static boolean getConfigByConfigKeyAndBusiKey(String configKey, String busiKey) {
		List<String> configList = new ArrayList<String>();
		try {
			String busiConfigStr = CommonUtil.getSysConfigFrom3G(configKey);
			if (StringUtils.isNotBlank(busiConfigStr)) {
				configList = Arrays.asList(busiConfigStr.split("\n|\r\n|\r"));
			}
		} catch (Exception e) {
			logger.error("获取H5收银台上客服服务异常,e:", e);
		}
		return CollectionUtils.isNotEmpty(configList) && configList.contains(busiKey);
	}
	
	/**
	 * wap 根据商户订单号 判断是否显示 '常见问题' 按钮
	 * 
	 * @param merchantNo
	 * @return
	 */
	public static boolean wapShowFAQ(String merchantNo, String orderSysNo) {
		if(StringUtils.isBlank(merchantNo)){
			return true;
		}
		boolean showFAQOfMerchantNo = getConfigByConfigKeyAndBusiKey("OL_NCCASHIER_WAP_SHOW_FAQ_KEY", merchantNo);
		if (showFAQOfMerchantNo) {
			return false;
		}
		if(StringUtils.isBlank(orderSysNo)){
			return true;
		}
		return !getConfigByConfigKeyAndBusiKey("OL_NCCASHIER_WAP_SHOW_FAQ_ORDER_SYS_NO_KEY", orderSysNo);
	}

	/**
	 * 获得活动支持的开始和结束时间
     */
	public static boolean activitiesTimeJudge(){
		boolean canJoin = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		try {
			Date start = null;// 开始时间
			Date end = null;// 结束时间
			Map<String, String> businessInfoMap = CommonUtil.getSysConfigFrom3G("OL_NCCASHIER_ACTIVITY_START_AND_END_TIME_KEY");
			if (null != businessInfoMap) {
				// 活动开始时间与结束时间
				String startTime = businessInfoMap.get("START_TIME");
				String endTime = businessInfoMap.get("END_TIME");
				if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
					try {
						start = sdf.parse(startTime);
						end = sdf.parse(endTime);
						if (now.after(start) && now.before(end)) {
							canJoin = true;
							logger.info("处于活动期间");
						}

					} catch (ParseException e) {
						logger.warn("活动时间设置错误", e);
					}
				}
			}
		} catch (Throwable e) {
			logger.error("获取参加活动时间区间时报错", e);
		}
		return canJoin;
	}


	/**
	 * 支付回调
	 * @param token
	 * @return
	 */
	public static String getPayRedirectUrl(String token){
		return CommonUtil.getCashierUrlDefaultPrefix()+"/wap/query/result?token=" + token;
	}

	/**
	 * 签约回调
	 * @param token
	 * @param orderType
	 * @param bindId
	 * @return
	 */
	public static String getSignRedirectUrl(String token,String orderType,String bindId){
		StringBuffer params = new StringBuffer();
		params.append("token=").append(token).append("&orderType=").append(orderType).append("&bindId=").append(bindId).append("&salt=").append(SALT);
		String sign = MD5Util.encryptMD5(params.toString());
		return CommonUtil.getCashierUrlDefaultPrefix() + "/sign/callback?token=" + token + "&orderType=" + orderType + "&bindId=" + bindId +"&sign="+sign;
	}


	/**
	 * 判断下商户或业务方是否定制了由业务方获取openId，如果有，返回拼接完成的跳转地址
	 * @param merchantNo
	 * @param orderSysNo
	 * @param token
	 * @param appId
	 * @return 未定制由业务方获取openId时返回null；定制了由业务方获取openId时返回业务方中转接口url
	 */
	public static String getBizOauthUrl(String merchantNo, String orderSysNo, String token, String appId) {
		try {
			String hostUrl = getBizOauthHost(merchantNo, orderSysNo);
			if (StringUtils.isBlank(hostUrl)) {
				logger.warn("公众号支付,预路由appSecret为空，且未配置商户或业务方获取openId的地址，请检查，merchantNo={},orderSysNo={}", merchantNo, orderSysNo);
				return null;
			}
			//拼接参数
			StringBuffer params = new StringBuffer();
			params.append("appid=").append(appId);
			params.append("&timestamp=").append(System.currentTimeMillis());
			params.append("&token=").append(token);
			//签名明文及签名
			StringBuffer signText = new StringBuffer(params).append("&CASHIER");
			String sign = MD5Util.encryptMD5(signText.toString());
			//完整跳转URL
			StringBuffer targetUrl = new StringBuffer(hostUrl);
			targetUrl.append("?").append(params);
			targetUrl.append("&sign=").append(sign);
			logger.info("公众号支付定制商户或业务方获取openId，将跳转到的业务方或商户接口地址={}，商编={}，orderSysNo={}", targetUrl.toString(), merchantNo, orderSysNo);
			return targetUrl.toString();
		} catch (Exception e) {
			logger.warn("公众号支付定制商户或业务方获取openId，处理商户或业务方接口地址，异常为：", e);
			return null;
		}
	}


	/**
	 * 【模拟业务方获取openid】若QA环境无法正常接受微信回调并获取openid，可读取统一配置，获取配置好的固定的openid;
	 * 注：1，此方法仅供QA环境模拟业务方业务流程；
	 * 2，若QA环境公众号可以从微信获取回调，请将此同一配置置为空
	 * @return
	 */
	public static String mockOpenIdForBiz(){
		return getSysConfigFrom3G("OL_NCCASHIER_OPENID_PAY_OAUTH_BY_BIZ_MOCK_OPENID");
	}

	/**
	 * 判断下商户或业务方是否定制了由业务方获取openId，如果有，返回配置的商户或业务方域名
	 * @param merchantNo
	 * @param orderSysNo
	 * @return
	 */
	private static String getBizOauthHost(String merchantNo, String orderSysNo){
		String hostUrl = null;
		//1，先检查是否定制了由商户获取openId
		Map<String, String> merchantConfig = getSysConfigFrom3G(ConstantUtil.OPENID_PAY_OAUTH_BY_MERCHANT_CONFIG);
		if (MapUtils.isNotEmpty(merchantConfig) && StringUtils.isNotBlank(merchantConfig.get(merchantNo))) {
			hostUrl = merchantConfig.get(merchantNo);
		} else {
			//2，没有商户配置，再检查是否定制了由业务方获取openId
			Map<String, String> bizConfig = getSysConfigFrom3G(ConstantUtil.OPENID_PAY_OAUTH_BY_BIZ_CONFIG);
			if (MapUtils.isNotEmpty(bizConfig) && StringUtils.isNotBlank(bizConfig.get(orderSysNo))) {
				hostUrl = bizConfig.get(orderSysNo);
			}
		}
		return hostUrl;
	}
	/**
	 * 微信h5低配版，获取商编配置的模拟openid
	 * @param merchantNo
	 * @return
	 */
	public static String getWechatH5LowMockOpenid(String merchantNo) {
		Map<String, String> mockOpenids = getSysConfigFrom3G(ConstantUtil.WECHAT_LOW_MERCHANT_MOCK_OPENID_CONFIG);
		if (MapUtils.isEmpty(mockOpenids) || StringUtils.isBlank(mockOpenids.get(merchantNo))) {
			return null;
		}else {
			return mockOpenids.get(merchantNo);
		}
	}
	
	/**
	 * 针对担保分期下单，判断下单返回的错误码(由PP转换到收银台的)是否需跳转到收银台失败页
	 */
	public static boolean guaranteeInstallPaymentErrorToFailPage(String errorCode){
		if(StringUtils.isBlank(errorCode)){
			return true;
		}
		try{
			List<String> errorCodeList = getSysConfigFrom3G(GUA_INS_PAY_ERROR_TO_FAILPAGE);
			if(errorCodeList!=null && errorCodeList.contains(errorCode)){
				return true;
			}
			return false;
		}catch (Exception e) {
			logger.error("获取统一配置信息失败:", e);
			return true;
		}
	}
	
	/**
	 * 获取商户配置的域名前缀信息
	 * 
	 * @param merchantNo
	 * @return
	 */
	public static String getCashierUrlPrefix(String merchantNo) {
		String merchantUrlPrefix = null;
		try {
			Map<String, String> cashierUrlPrefixConfig = CommonUtil
					.getSysConfigFrom3G(CommonUtil.CASHIER_URL_PREFIX_CONFIG);
			if (MapUtils.isNotEmpty(cashierUrlPrefixConfig)) { // 配置不为空时
				if (StringUtils.isNotBlank(merchantNo)) { // 先取商编配置
					merchantUrlPrefix = cashierUrlPrefixConfig.get(merchantNo);
				}
				if (StringUtils.isBlank(merchantUrlPrefix)) { // 商编配置为空，取默认配置
					merchantUrlPrefix = cashierUrlPrefixConfig.get(CONFIG_DEFAULT_KEY);
				}
			}
		} catch (Throwable t) {
			logger.error("getCashierUrlPrefix 获取收银台域名前缀异常，merchantNo=" + merchantNo, t);
		}
		if (StringUtils.isBlank(merchantUrlPrefix)) {
			merchantUrlPrefix = "https://cash.yeepay.com/";
		}
		logger.info("merchantNo={}最终使用域名为{}", merchantNo, merchantUrlPrefix);
		return merchantUrlPrefix;
	}
	
	public static String getCashierUrlDefaultPrefix(){
		return getCashierUrlPrefix(null);
	}
	
	public static String[] getNetPayerIpConfig(String bankCode){
		if(StringUtils.isBlank(bankCode)){
			return null;
		}
		Map<String, String> netPayerIpConfig = null;
		try {
			netPayerIpConfig = CommonUtil
					.getSysConfigFrom3G(CommonUtil.NET_PAYER_IP_CONFIG_KEY);
		} catch (Throwable t) {
			logger.error("getNetPayerIpConfig异常，bankCode=" + bankCode, t);
		}
		if (MapUtils.isNotEmpty(netPayerIpConfig)) { // 配置不为空时
			String bankNetPayerIpCOnfig = netPayerIpConfig.get(bankCode); 
			if(StringUtils.isNotBlank(bankNetPayerIpCOnfig)){
				return bankNetPayerIpCOnfig.split(",");
			}
		}
		return null;
	}
}
