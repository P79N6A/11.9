package com.yeepay.g3.core.nccashier.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.vo.FeeParam;
import com.yeepay.g3.core.nccashier.vo.InstallmentBankInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel.Product;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;
import com.yeepay.g3.facade.cwh.enumtype.BankCardType;
import com.yeepay.g3.facade.foundation.dto.DefaultErrorCode;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.frontend.enumtype.BankAccountType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankInfo;
import com.yeepay.g3.facade.nccashier.dto.ErrorCodeDTO;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.ErrorCode;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.error.FEErrorCodeMapping;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.constant.Constants;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.foundation.util.ErrorCodeUtility;


import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class CommonUtil {
	private static Logger logger=LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * 是否取消钱包（sdk）支付宝支付，唤起支付宝时先跳转的中间蓝页的统一配置key
	 */
	private static final String CANCLE_ALIPAY_ROUTE_PAGE_SWITCH = "OL_NCCASHIER_CANCLE_ALIPAY_BLUE_PAGE_CONFIG";
	
	/**
	 * 是否取消钱包（sdk）支付宝支付，唤起支付宝时先跳转的中间蓝页的标识
	 */
	private static final String CANCEL_ALIPAY_ROUTE_PAGE_KEY = "cancelFlag";
	
	/**
	 * 取消钱包（sdk）支付宝支付，唤起支付宝时先跳转的中间蓝页时的url
	 */
	private static final String NEW_ALIPAY_PAY_URL_AFTER_CANCEL = "payUrl";
	
	/**
	 * 取消钱包（sdk）支付宝支付，唤起支付宝时先跳转的中间蓝页时的url的变量
	 */
	private static final String NEW_ALIPAY_PAY_URL_VARIABLE = "{prepayCode}";
	
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
	public final static String PREFIX_OL_NCCASHIER_BUSINESS_CONFIG = "OL_NCCASHIER_BUSINESS_CONFIG_";
	public final static String PREFIX_OL_NCCASHIER_BIND_CARD_BIZ_TYPE_CONFIG = "OL_NCCASHIER_BIND_CARD_BIZ_TYPE_CONFIG_";
	public final static String RISK_SMSVERIFY_SWITCH ="OL_NCCASHIER_RISK_SMSVERIFY_SWITCH"; //风控建议的短验类型开关
	// TODO 老的收银台默认域名配置，此次上线后可废除
	public final static String NCCASHIER_ENTER_WAP_URL_PREFIX ="OL_NCCASHIER_ENTER_WAP_URL"; 
	// 新的收银台默认域名配置
	public final static String CASHIER_URL_PREFIX_CONFIG = "OL_NCCASHIER_CASHIER_URL_PREFIX"; 
	public final static String NCCASHIER_ERROR_CODE_MANAGE_SWITCH = "OL_NCCASHIER_ERROR_CODE_MANAGE_SWITCH";
	public final static String ON = "ON";
	public final static String OFF = "OFF";

	/**
	 * PP获得支付结果后是否发送MQ的开关(解决跨系统公用redis)
	 */
	public final static String PP_PAYRESULT_MQ_SWTICH = "OL_PP_PAYRESULT_MQ_SWTICH";
	/**
	 * 开关的三个状态,on完全使用MQ,off使用原来的pp查询接口,transition中间转换状态
	 */
	public final static String PP_PAYRESULT_MQ_SWTICH_ON = "on";
	public final static String PP_PAYRESULT_MQ_SWTICH_OFF = "off";
	public final static String PP_PAYRESULT_MQ_SWTICH_TRANSITION = "transition";
	/**
	 * 获得PPMQ消息后设置自己的redis(解决跨系统公用redis)
	 */
	public final static String PP_HAS_PAY_RESULT_KEY = "HAS_PP_PAYRESULT_";
	
	private final static String MERCHANT_ATTEND_MARKET_ACTIVITY_KEY = "OL_NCCASHIER_MERCHANT_ATTEND_MARKET_ACTIVITY";
	private final static String MERCHANT_LIST_KEY = "merchant_no_list";
	private final static String ORDER_SYS_LIST_KEY = "order_sys_no_list";
	private final static String MARKET_ACTIVITY_TIME_KEY = "OL_NCCASHIER_MARKET_ACTIVITY_TIME";
	private final static String MERCHANT_LEVEL_TIME_KEY = "OL_NCCASHIER_MERCHANT_LEVEL_TIME_KEY";

	private final static String MERCHANT_PLATFORM_AUTH_KEY="OL_NCCASHIER_MERCHANT_PLATFORM_AUTH";
	public final static String AUTH_SYSTEM_KEY="system";
	public final static String AUTH_UUID_KEY="uuid";
	public final static String DEFAULT_AUTH_SYSTEM_VALUE="NCCASHIER";
	public final static String DEFAULT_AUTH_UUID_VALUE="f13d3670d1c3445788b357182af1e4fd";

	public final static String ALLOW_AUTHRITY = "ON_NCCASHIER_ALLOW_AUTHRITY";

	public final static String SHARE_CARD_CASHIER_TIME = "ON_NCCASHIER_SHARE_CARD_CASHIER_TIME";

	//共享卡redis缓存的key
	public static final String SHARE_CARD_CACHE="NC_CASHIER_SHARE_CARD_";

	public static final String BASIC_PRODUCT_CODE = "ON_NCCASHIER_BASIC_PRODUCT_CODE";

	public static final String BIZ_SYS_CODE_KEY = "ON_NCCASHIER_BIZ_SYS_PARAMS";
	//DS业务方映射ncpay业务方key
	public static final String BIZ_SYS_DS_TO_NCPAY_MAPPING_KEY = "ON_NCCASHIER_BIZ_SYS_DS_TO_NCPAY_MAPPING_KEY";
	//监听PP支付结果频率（间隔时间）
	public static final String PP_PAY_RESULT_QUERY_INTERVAL_TIME = "ON_NCCASHIER_PP_PAY_RESULT_QUERY_INTERVAL_TIME";
	/**
	 * 获取业务方配置信息的默认的key
	 */
	public static final String BIZ_SYS_CONFIG_DEFAULT_KEY = "DEFAULT";
	
	private static final String CONFIG_DEFAULT_KEY = "default";
	/**
	 * 旧的接二代网银的没有业务方概念，默认给分配的业务方
	 */
	public static final String G2NET_OLD_KEY = "G2NET_OLD";

	/**
	 * 敏感信息加密存储的key（本地缓存）
	 */
	private static String AES_FIXED_KEY = null;
	
	public static final String INSTALLMENT_RATE_INFO_KEY = "OL_NCCASHIER_INSTALLMENT_RATE_INFO";
	
	private static final String CAL_FEE_INFO_CONFIG_KEY = "OL_NCCASHIER_CAL_FEE_INFO";
	
	private static final String TO_BANK_PASS_THROUGH_PCCASHIER_KEY = "OL_NCCASHIER_TO_BANK_PASS_THROUGH_PCCASHIER";


	/**
	 * 收银台1.0是否迁移pp的配置键 true:迁移 false:未迁移
	 */
	private static final String OL_NCCASHIER_MIGRATE_TO_PP = "OL_NCCASHIER_MIGRATE_TO_PP";

	/** 预授权强制短验开关：配置为ON时，若风控返回无需短验，也需要发易宝短验 */
	private static final String PREAUTH_ALWAYS_VEFIRY_SMS_SWITCH = "OL_NCCASHIER_PREAUTH_ALWAYS_VEFIRY_SMS_SWITCH";

	private static final String PREAUTH_SMS_SEND_OR_NOT_KEY = "OL_NCCASHIER_PREAUTH_SMS_SEND_OR_NOT";


	/**
	 * 充值  配置   缓存键 前缀
	 */
	public final static String LOAD_NEW_CONFIG_REDIS = "LOAD_NEW_CONFIG_REDIS_";
	
	/**
	 * 收银台业务方 风控参数  配置键
	 */
	public final static String OL_NCCASHIER_RISK_PRODUCTION_KEY = "OL_NCCASHIER_RISK_PRODUCTION_KEY";


	/** 获取分期易支持的银行 */
	private static final String OL_NCCASHIER_CFL_EASY_BANK_LIST = "OL_NCCASHIER_CFL_EASY_BANK_LIST";
	
	private static final String CASHIER_VERSION_GET_FROM_USER_REQUEST_KEY = "OL_NCCASHIER_CASHIER_VERSION_GET_FROM_USER_REQUEST";

	private static final String ENABLE_CASHIER_SPAN_LOG = "OL_NCCASHIER_ENABLE_CASHIER_SPAN_LOG";


	private static Map<String,String> riskProductionMap = new HashMap<String, String>();
	static {
		riskProductionMap.put("13","2GTRADE");
		riskProductionMap.put("12","QFT");
		riskProductionMap.put("2","ZHGT");
		riskProductionMap.put("9","YJZF");
		riskProductionMap.put("20","DS");
		riskProductionMap.put("21","DS");
	}
 
	private static Map<String,String> basicProductCodeMap = new HashMap<String, String>();
	static {
		/*通用基础产品码**/
		basicProductCodeMap.put(PayTool.SCCANPAY.name(),"3011001006001");
		basicProductCodeMap.put(PayTool.EWALLET.name(),"3011004004001");
		basicProductCodeMap.put(PayTool.NCPAY.name(),"3011001003001");
		basicProductCodeMap.put(BankAccountTypeEnum.B2C.name(),"3011001002001");
		basicProductCodeMap.put(BankAccountTypeEnum.B2B.name(),"3011001002002");
		basicProductCodeMap.put(PayTool.MSCANPAY.name(), "ZF_SJSM");
		basicProductCodeMap.put(PayTool.WECHAT_OPENID.name(), "3011004004001");
		basicProductCodeMap.put(PayTool.ZF_ZHZF.name(), "3011001004001");
		basicProductCodeMap.put(PayTool.EWALLETH5.name(), "ZF_QB_H5");
		basicProductCodeMap.put(PayTool.ZFB_SHH.name(), "3011001006004"); 
		basicProductCodeMap.put(PayTool.BK_ZF.name(), "3011001003005");
		basicProductCodeMap.put(Constant.BIND_CARD_BASIC_PRODUCT_CODE_KEY, "BK_BK");
		basicProductCodeMap.put(PayTool.YSQ.name(), "ZF_YSQ");
		basicProductCodeMap.put(PayTool.DBFQ.name(), "ZF_DBFQ");
		basicProductCodeMap.put(PayTool.XCX_OFFLINE_ZF.name(), "3011001006006");
		basicProductCodeMap.put(PayTool.ZF_FQY.name(), "3011102005001");

		/*大算特有的基础产品码*/
		basicProductCodeMap.put(PayTool.SCCANPAY.name()+"_"+TradeSysCodeEnum.DS.name(),"ZF_YHSM");
		basicProductCodeMap.put(PayTool.EWALLET.name()+"_"+TradeSysCodeEnum.DS.name(),"ZF_QB");
		basicProductCodeMap.put(PayTool.NCPAY.name()+"_"+TradeSysCodeEnum.DS.name(),"ZF_YJZF");
		basicProductCodeMap.put(PayTool.WECHAT_OPENID.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_GZH");
		basicProductCodeMap.put(BankAccountTypeEnum.B2C.name()+"_"+TradeSysCodeEnum.DS.name(),"ZF_WY_B2C");
		basicProductCodeMap.put(BankAccountTypeEnum.B2B.name()+"_"+TradeSysCodeEnum.DS.name(),"ZF_WY_B2B");
		basicProductCodeMap.put(PayTool.MSCANPAY.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_SJSM");
		basicProductCodeMap.put(PayTool.ZF_ZHZF.name()+"_"+TradeSysCodeEnum.DS.name(), "3011001004001");
		basicProductCodeMap.put(PayTool.EWALLETH5.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_QB_H5");
		basicProductCodeMap.put(PayTool.ZFB_SHH.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_SHH");
		basicProductCodeMap.put(PayTool.BK_ZF.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_BK");
		basicProductCodeMap.put(Constant.BIND_CARD_BASIC_PRODUCT_CODE_KEY+"_"+TradeSysCodeEnum.DS.name(), "BK_BK"); 
		basicProductCodeMap.put(PayTool.YHKFQ_ZF.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_YHKFQ");
		basicProductCodeMap.put(PayTool.GRHYZF.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_GRHY"); // 个人会员支付
		basicProductCodeMap.put(PayTool.DBFQ.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_DBFQ");
		basicProductCodeMap.put(PayTool.XCX_OFFLINE_ZF.name()+"_"+TradeSysCodeEnum.DS.name(), "ZF_XCX_OFFLINE");
		basicProductCodeMap.put(PayTool.ZF_FQY.name()+"_"+TradeSysCodeEnum.DS.name(), "3011102005001");
	}

	public static Map<String, String> payTypeMap = new HashMap<String, String>();
	static{ 
		payTypeMap.put("GRHYCZ_ZFFS_STRING_WECHATH5", PayTypeEnum.WECHAT_H5_WAP.name());
		payTypeMap.put("GRHYCZ_ZFFS_STRING_ALIPAYH5STANDARD", PayTypeEnum.ALIPAY_H5_STANDARD.name());
	}

	private static Map<String,String> orderSysConfigParams = new HashMap<String, String>();
	static {
		orderSysConfigParams.put("DEFAULT", "{\"accessCode\":\"DS\",\"referFromOrder\":\"NONE\"}");
		orderSysConfigParams.put("GENET_OLD", "{\"accessCode\":\"G2NET\",\"referFromOrder\":\"NONE\"}");


	}
	//DS业务方映射ncpay业务方
	private static Map<String,String> mappingConfigDsToNcpay = new HashMap<String, String>();
	static {
		mappingConfigDsToNcpay.put("DEFAULT", "20");
		mappingConfigDsToNcpay.put("DS", "20");
	}
	/** 
	 * 2017.04-27 
	 * modify by meiling.zhuang 
	 * 为某些商户的用户提示自动绑卡
	 */
	// 需要提示自动绑卡的商户
	private static final String MERCHANTS_NEED_AUTO_BIND = "ON_NCCASHIER_MERCHANTS_NEED_AUTO_BIND";
	private static final String MERCHANT_AUTO_BIND_TEXT = "记住该卡并同意《服务协议》";
	
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
	public static Map<String, String> getBusinessConfig(String bizCode){
		try {
			ConfigParam<Map<String,String>> businessConfig = ConfigurationUtils.getSysConfigParam(PREFIX_OL_NCCASHIER_BUSINESS_CONFIG+bizCode);
			Map<String, String> businessInfoMap = null;
			if(null!=businessConfig){
				businessInfoMap=businessConfig.getValue();
			}
			return businessInfoMap;
		} catch (Exception e) {
			logger.error("从三代统一配置中读取业务方配置信息出错", e);
			return null;
		}
	}
	/**
	 * 获取三代统一配置信息
	 * @param key
	 * @return Map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getConfig(String key){
		
		ConfigParam<Map<String,String>> businessConfig = ConfigurationUtils.getSysConfigParam(key);
		Map<String, String> businessInfoMap = null;
		if(null!=businessConfig){
			businessInfoMap=businessConfig.getValue();
		}
		return businessInfoMap;
		
	}
	/**
	 * 是否使用风控系统短验开关，若为ON则使用风控系统短验类型，若为OFF则使用本系统的短验类型
	 * @return
	 */
	public static boolean getRiskVerifyCodeSwitch(){
		String riskVerifySwitch ="";
		try{
				riskVerifySwitch = getSysConfigFrom3G(RISK_SMSVERIFY_SWITCH);
		}catch(Exception e){
			riskVerifySwitch = OFF ;
			logger.error("获取统一配置信息失败:",e);
		}
		return ON.equals(riskVerifySwitch);
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
			if(p==null || p.getValue() == null){
				return null;
			}else{
				config = (T)p.getValue();
			}
		} catch (Exception e) {
			logger.error("获取出错",e);
			return null;
		}
		return config;
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
			errorCodeManageSwitch = OFF ;
			logger.error("getErrorCodeManageSwitchConfig失败:", e);
		}
		return ON.equals(errorCodeManageSwitch);
	}
	
	Map<CashierVersionEnum, List<Product>> map;
	
	
	
	/**
	 * 统一处理异常抛出
	 * @param e
	 * @return
	 */
	public static CashierBusinessException handleException(Errors e){
		return handleException(SysCodeEnum.NCCASHIER.name(), e.getCode(), e.getMsg());
	}

	/**
	 * 统一处理异常抛出
	 * @param sysCode 抛异常系统的系统编码
	 * @param oriErrorCode	原始的错误码
	 * @param oriErrorMsg	原始的错误码描述信息
	 */
	public static CashierBusinessException handleException(String sysCode, String oriErrorCode, String oriErrorMsg){
		logger.info("handleException sysCode:{}, oriErrCode:{}, oriErrMsg:{}", sysCode, oriErrorCode, oriErrorMsg);
		boolean switchValue = getErrorCodeManageSwitch();
		ErrorCodeDTO response = null;
		if(StringUtils.isBlank(oriErrorCode)){
			return new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		if(switchValue){
			// 使用错误码管理系统
			response = mapErrorCode(sysCode, oriErrorCode, oriErrorMsg);
		}
		if(!switchValue || response==null){
			// 不使用错误码管理系统或者调用错误码管理系统失败等情况，使用本地映射，本地没有，则返回原错误码和错误描述信息
			response = mapErrorCodeFromLocal(sysCode, oriErrorCode, oriErrorMsg);
			logger.info("不使用错误码管理系统,objErrCode:{},objErrMsg:{}", response.getExternalErrorCode(), response.getExternalErrorMsg());
		}
		return new CashierBusinessException(response.getExternalErrorCode(), response.getExternalErrorMsg());
	}
	
	/**
	 * 错误码转换 （对接错误码管理系统）
	 * @param sysCode	抛异常系统的系统编码
	 * @param oriErrorCode	原始的错误码
	 * @param oriErrorMsg	原始的错误码描述信息
	 * @return
	 */
	private static ErrorCodeDTO mapErrorCode(String sysCode, String oriErrorCode,String oriErrorMsg){
		ErrorCodeDTO response = new ErrorCodeDTO(sysCode, oriErrorCode, oriErrorMsg);
		
		//NCCASHIER系统内部错误
		if(StringUtils.isBlank(sysCode) || SysCodeEnum.NCCASHIER.name().equals(sysCode)){
			//该错误码描述信息由错误码管理系统维护
			response.setExternalErrorCode(oriErrorCode);
			try{
				String objErrorMsg = ErrorCodeUtility.retrieveErrorCodeMsg(SysCodeEnum.NCCASHIER.name(), oriErrorCode);
				response.setExternalErrorMsg(StringUtils.isBlank(objErrorMsg)?oriErrorMsg:objErrorMsg);
			}catch(Throwable e){
				logger.error("[monitor],event:nccashier_mapErrorCode,sysCode:{},oriErrorCode:{},oriErrorMsg:{},e:{}", sysCode, oriErrorCode, oriErrorMsg, e);
				return null;
			}
			
		}
		
		//下游系统服务抛异常 - TODO 需要配置支付处理器和无卡收银台错误码对应关系
		else{
			Errors defaultErrors = Errors.getDefaultErrorCode(sysCode);
			DefaultErrorCode defaultErrorCode = new DefaultErrorCode(SysCodeEnum.NCCASHIER.name(), defaultErrors.getCode(), defaultErrors.getMsg());
			try{
				ErrorMeta objErrorCode = ErrorCodeUtility.mapErrorMeta(sysCode, oriErrorCode, oriErrorMsg, defaultErrorCode);
				if(objErrorCode==null) return null;
				response.setExternalErrorCode(objErrorCode.getErrorCode());
				response.setExternalErrorMsg(objErrorCode.getErrorMsg());
			}catch(Throwable e){
				logger.error("[monitor],event:nccashier_mapErrorCode,sysCode:{},oriErrorCode:{},oriErrorMsg:{},e:{}", sysCode, oriErrorCode, oriErrorMsg, e);
				return null;
			}
		}
		
		return response;
	}
	
	/**
	 * 错误码转换 （本地）
	 * @param sysCode
	 * @param oriErrorCode
	 * @param oriErrorMsg
	 * @return
	 */
	private static ErrorCodeDTO mapErrorCodeFromLocal(String sysCode, String oriErrorCode,String oriErrorMsg){
		ErrorCodeDTO responseLocal = new ErrorCodeDTO(sysCode, oriErrorCode, oriErrorMsg);
		if(StringUtils.isBlank(sysCode) || SysCodeEnum.NCCASHIER.name().equals(sysCode)){
			responseLocal.setExternalErrorCode(oriErrorCode);
			responseLocal.setExternalErrorMsg(oriErrorMsg);
		}
		else if(SysCodeEnum.NCPAY.name().equals(sysCode)){
			responseLocal = ErrorCode.getErrorInfo(oriErrorCode, oriErrorMsg);
		}
		else if(SysCodeEnum.FRONTEND.name().equals(sysCode)){
			String objCode = FEErrorCodeMapping.translateCode(oriErrorCode);
			Errors error = Errors.getErrorsByCode(objCode);
			responseLocal.setExternalErrorCode(objCode);
			responseLocal.setExternalErrorMsg(error.getMsg());
		}
		else {
			responseLocal.setExternalErrorCode(oriErrorCode);
			responseLocal.setExternalErrorMsg(oriErrorMsg);
		}
		return responseLocal;
	}
	
	/**
	 * 获取商户平台授权信息
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getMerchantPlatformAuth(){
		try {
			ConfigParam<Map<String,String>> businessConfig = ConfigurationUtils.getSysConfigParam(MERCHANT_PLATFORM_AUTH_KEY);
			Map<String, String> businessInfoMap = null;
			if(null!=businessConfig){
				businessInfoMap=businessConfig.getValue();
			}
			if(MapUtils.isEmpty(businessInfoMap)){
				businessInfoMap = new HashMap<String,String>();
				businessInfoMap.put(AUTH_SYSTEM_KEY, DEFAULT_AUTH_SYSTEM_VALUE);
				businessInfoMap.put(AUTH_UUID_KEY, DEFAULT_AUTH_UUID_VALUE);
			}
			return businessInfoMap;
			
		} catch (Exception e) {
			logger.error("从三代统一配置中获取商户平台授权信息出错", e);
			Map<String, String> businessInfoMap = new HashMap<String,String>();
			businessInfoMap.put(AUTH_SYSTEM_KEY, DEFAULT_AUTH_SYSTEM_VALUE);
			businessInfoMap.put(AUTH_UUID_KEY, DEFAULT_AUTH_UUID_VALUE);
			return businessInfoMap;
		}
	}
	
	
	
	/**
	 * 获取监听标识 服务器端超时时间
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
	 * 返回支付处理器所需的对公对私网银银行账户的枚举值
	 * 
	 * @param bankAccountType
	 * @return
	 */
	public static BankAccountType getBankAccountType(BankAccountTypeEnum bankAccountType) {
		if (BankAccountTypeEnum.B2B.equals(bankAccountType)) {
			return BankAccountType.BUSINESS;
		} else if (BankAccountTypeEnum.B2C.equals(bankAccountType)) {
			return BankAccountType.INDIVIDUAL;
		}
		return null;
	}


	public static BankAccountType getBankAccountType(String bankAccountType) {
		if (bankAccountType.contains(BankAccountTypeEnum.B2B.name())) {
			return BankAccountType.BUSINESS;
		} else if (bankAccountType.contains(BankAccountTypeEnum.B2C.name())) {
			return BankAccountType.INDIVIDUAL;
		}
		return null;
	}
	
	/**
	 * 收银台版本类型转换成订单处理器版本类型
	 * PC("PC收银台"), -----WEB(WEB收银台)
	 * WAP("移动收银台"), ——H5(H5收银台)
	 * SDK("SDK收银台")——SDK(SDK收银台)
	 * @param version
	 * @return
	 */
	public static String transformToOPRVersion(String version){
		if("WAP".equals(version)){
			return "SY_H5";
		}else if("PC".equals(version)){
			return "SY_WEB";
		}else if ("API".equals(version)){
			return "SY_QZ";
		}else if("WXGZH".equals(version)){
			return "SY_GZH";
		}else if("SDK".equals(version)){
			return "SY_SDK";
		}else{
			return null;
		}
	}


	//字符串转换成json
	public static JSONObject parseJson(String string){
		try {
			if (com.yeepay.g3.utils.common.StringUtils.isEmpty(string)) {
				return new JSONObject();
			} else {
				JSONObject jsonObject = JSON.parseObject(string);
				return jsonObject;
			}
		}catch (Throwable e){
			logger.error("requestPayment扩展信息extendInfo:"+string+"转化json异常");
		}
		return new JSONObject();
	}

	public static boolean allowAuthority(){
		String allowAuthority ="";
		try{
			allowAuthority = getSysConfigFrom3G(ALLOW_AUTHRITY);
			if(StringUtils.isBlank(allowAuthority)){
				return true;
			}
		}catch(Exception e){
			logger.error("获取统一配置信息失败:",e);
			return true;
		}
		return ON.equals(allowAuthority);
	}
	
	/**
	 * 将卡账户返回的银行卡类型映射到无卡收银台自身的卡类型
	 * 
	 * @param bankCardType
	 * @return
	 */
	public static CardTypeEnum cardTypeTransfer(BankCardType bankCardType) {
		if (bankCardType == null) {
			return null;
		}
		switch (bankCardType) {
		case DEBITCARD:
			return CardTypeEnum.DEBIT;
		case CREDITCARD:
			return CardTypeEnum.CREDIT;
		case QUASI_CREDIT:
			return CardTypeEnum.CREDIT;
		default:
			return null;
		}
	}
	
	/**
	 * 将卡账户返回的银行卡类型映射到无卡收银台自身的卡类型
	 * 
	 * @param bankCardType
	 * @return
	 */
	public static String cardTypeTransferToStr(BankCardType bankCardType) {
		CardTypeEnum cardType = cardTypeTransfer(bankCardType);
		return cardType == null ? null : cardType.name();
	}

	public static int getShareCardCacheTime(){
		try {
			Integer integer = getSysConfigFrom3G(SHARE_CARD_CASHIER_TIME);
			if (integer == null) {
				return 5*60*1000;
			} else {
				return integer;
			}
		}catch (Throwable e){
			logger.error("第三代统一配置获取共享卡的缓存时间异常");
			return 5*60*1000;
		}

	}
	
	/**
	 * 获取定时跑批同人限制值的配置信息
	 * int[0]为当前时间多少分钟前的记录
	 * int[1]为一次处理的记录数
	 * @return
	 */
	public static int[] getAutoSetSamePersonSets() {
		int[] sets = new int[2];
		try{
			Map<String, String> setsInfo = getSysConfigFrom3G(Constant.AUTO_SETSAMEPERSON_SETS);
			if(null==setsInfo){
				logger.info("未配置定时设置同人限制值的配置信息，默认配置为120分钟前的记录，每次处理100条记录");
				sets[0] = 120;
				sets[1] = 100;
			}else{
				if(StringUtils.isNotBlank(setsInfo.get(Constant.AUTO_SETSAMEPERSON_TIME_LENGTH))){
					sets[0] = Integer.parseInt(setsInfo.get(Constant.AUTO_SETSAMEPERSON_TIME_LENGTH));
				}else{
					sets[0] = 120;
				}
				if(StringUtils.isNotBlank(setsInfo.get(Constant.AUTO_SETSAMEPERSON_RECORDS))){
					sets[1] =Integer.parseInt(setsInfo.get(Constant.AUTO_SETSAMEPERSON_RECORDS));
				}else{
					sets[1] = 100;
				}
			}
		}catch(Throwable e){
			logger.error("获取定时设置同人限制值配置参数异常",e);
			sets[0] = 120;
			sets[1] = 100;
		}
		return sets;
	}

//转换对应的风控标识
	public static String getRiskProduction(String key){
		Map<String,String> map = new HashMap<String, String>();
		try{
			map = getSysConfigFrom3G(OL_NCCASHIER_RISK_PRODUCTION_KEY);
		}
		catch (Throwable throwable){
			map = riskProductionMap;
		}
		return map.get(key);
	}


	/**
	 * 根据支付产品+交易系统编项获取基础产品编码
	 *  优先按照支付产品+交易系统编取，否则按照支付产品取
	 * @param productName
	 * @param tradeSysNo 交易系统编码
	 * @return
	 */
	public static String getBasicProductCode(String productName,String tradeSysNo) {
		try {
			Map<String, String> map = getSysConfigFrom3G(BASIC_PRODUCT_CODE);
			if (MapUtils.isNotEmpty(map)) {
				if(StringUtils.isNotBlank(map.get(productName+"_"+tradeSysNo))){
					return map.get(productName+"_"+tradeSysNo);
				} else if(StringUtils.isNotBlank(basicProductCodeMap.get(productName+"_"+tradeSysNo))){
					return basicProductCodeMap.get(productName+"_"+tradeSysNo);
				} else if(StringUtils.isNotBlank(map.get(productName))){
					return map.get(productName);
				} else{
					return basicProductCodeMap.get(productName);
				}		
			}
		} catch (Throwable throwable) {
			logger.error("从三代统一配置获取基础产品码异常:" + productName+"_"+tradeSysNo, throwable);
		}
		
		if(StringUtils.isNotBlank(basicProductCodeMap.get(productName+"_"+tradeSysNo))){
			return basicProductCodeMap.get(productName+"_"+tradeSysNo);
		}else{
			return basicProductCodeMap.get(productName);
		}
	}

	/**
	 * 获取业务方配置的一些信息(reffer的获取地方,url,协议)
	 * @param bizType
	 * @return
	 */
	public static OrderSysConfigDTO getBizSysCnfigParams(String bizType){
		OrderSysConfigDTO orderSysConfigDTO = null;
		try {
			Map<String, String> map = getSysConfigFrom3G(BIZ_SYS_CODE_KEY);
			if(MapUtils.isNotEmpty(map)){
				if(StringUtils.isNotBlank(map.get(bizType))){
					orderSysConfigDTO = JSON.parseObject(map.get(bizType),OrderSysConfigDTO.class);
				}else {
					orderSysConfigDTO = JSON.parseObject(map.get(CommonUtil.BIZ_SYS_CONFIG_DEFAULT_KEY),OrderSysConfigDTO.class);
				}
			}
		}catch (Throwable throwable){
			logger.error("从三代统一配置获取业务方配置的参数信息异常:"+bizType,throwable);
			orderSysConfigDTO = JSON.parseObject(orderSysConfigParams.get(bizType), OrderSysConfigDTO.class);
		}
		if(orderSysConfigDTO == null){
			logger.error("从三代统一配置获取业务方配置的参数信息是空:"+bizType);
			return JSON.parseObject(orderSysConfigParams.get(CommonUtil.BIZ_SYS_CONFIG_DEFAULT_KEY), OrderSysConfigDTO.class);
		}else {
			return orderSysConfigDTO;
		}
	}
	
	public static String getBizMappingConfigDsToNcpay(String bizType){
		String mappedBizSys = "";
		try{
			Map<String, String> map = getSysConfigFrom3G(BIZ_SYS_DS_TO_NCPAY_MAPPING_KEY);
			if(MapUtils.isNotEmpty(map)){
				if(StringUtils.isNotBlank(map.get(bizType))){
					mappedBizSys = map.get(bizType);
				}else{
					mappedBizSys = map.get(CommonUtil.BIZ_SYS_CONFIG_DEFAULT_KEY);
				}
			}
		}catch(Throwable throwable){
			logger.error("从三代统一配置获取业务方配置的参数信息异常:"+bizType,throwable);
		}
		if(StringUtils.isBlank(mappedBizSys)){
			mappedBizSys = mappingConfigDsToNcpay.get(CommonUtil.BIZ_SYS_CONFIG_DEFAULT_KEY);
		}
		return mappedBizSys;
	}
	/**
	 * 收银台业务方映射其他系统业务方
	 * @param bizType
	 * @return
	 */
	public static Map<String, Object> getNcCashierBindCardBizConfig(String bizType) {
		Map<String, Object> bizTypeMap = null;
		try {
			bizTypeMap = getSysConfigFrom3G(PREFIX_OL_NCCASHIER_BIND_CARD_BIZ_TYPE_CONFIG+bizType);
		} catch (Exception e) {
			logger.error("从三代统一配置中读取业务方配置信息出错", e);
		}
		//DS为收银台默认业务方,配置NCPYA业务方20，配置NCAUTH业务方DS
		if (MapUtils.isEmpty(bizTypeMap)) {
			if("DS".equals(bizType)){
				bizTypeMap = new HashMap<String, Object>();
				bizTypeMap.put("BIZTYPE_NCPAY", "20");
				bizTypeMap.put("BIZTYPE_NCAUTH", "DS");
			}else{
				throw CommonUtil.handleException(Errors.BUSINESS_NOT_SUPPORT);
			}
		}
		return bizTypeMap;
	}
	
	/**
	 * 判断商户是否需要自动绑卡提示
	 * 
	 * @param merchantNo
	 * @return
	 */
	public static String merchantNeedAutoBindTip(String merchantNo) {
		try {
			if (StringUtils.isBlank(merchantNo)) {
				return null;
			}
			HashMap<String, String> autoBindTipsConfig = getSysConfigFrom3G(MERCHANTS_NEED_AUTO_BIND);
			if (MapUtils.isNotEmpty(autoBindTipsConfig)) {
				String merchantNoList = autoBindTipsConfig.get("MERCHANT");
				if (StringUtils.isBlank(merchantNoList)) {
					return null;
				}
				if (!merchantNoList.contains(merchantNo)) {
					return null;
				}
				String autoBindText = autoBindTipsConfig.get("TEXT");
				return StringUtils.isBlank(autoBindText) ? MERCHANT_AUTO_BIND_TEXT : autoBindText;
			}
		} catch (Throwable t) {
			logger.error("从三代统一配置获取商户是否需要自动绑卡提示异常");
		}

		return null;
	}

	/**
	 * 查询PC收银台扫码支付缓存有效时间，单位秒
	 * @return
	 */
	public static int getQRCodeCacheExpireTime(){
		String qrcodeExpireConfig = getSysConfigFrom3G(Constant.NCCASHIER_QRCODE_CONFIG_KEY);
		int expireTime;
		if (StringUtils.isEmpty(qrcodeExpireConfig)) {
			// 一般来说是走不到这个分支的。只有统一配置没有配或挂了，才会走到这个分支。
			expireTime = Constant.NCCASHIER_QRCODE_REDIS_EXPIRE;
			logger.warn("[getQRCodeCacheExpireTime()]获取三代统一配置的二维码缓存时间为空，取默认值");
		}else {
			expireTime = Integer.parseInt(qrcodeExpireConfig);
		}
		return expireTime;
	}

	/**
	 * 处理收银台错误码到API收银台错误码的映射
	 * @param oriErrorCode 收银台错误码
	 * @param oriErrorMsg 收银台错误信息
	 * @return
	 */
	public static CashierBusinessException handleApiCashierException(String oriErrorCode, String oriErrorMsg){
		logger.info("handleApiCashierException[String,String] 入参 oriErrorCode:{}, oriErrorMsg:{}", oriErrorCode, oriErrorMsg);
		boolean switchValue = getErrorCodeManageSwitch();
		String sysCodeCashier = SysCodeEnum.NCCASHIER.name();
		String sysCodeCashierApi = SysCodeEnum.NCCASHIER_API.name();
		ErrorCodeDTO response = new ErrorCodeDTO(sysCodeCashierApi, oriErrorCode, oriErrorMsg);
		if(switchValue && StringUtils.isNotBlank(oriErrorCode)){
			DefaultErrorCode defaultErrorCode = new DefaultErrorCode(SysCodeEnum.NCCASHIER_API.name(), APICashierPayResultEnum.SYSTEM_ERROR.getCode(), APICashierPayResultEnum.SYSTEM_ERROR.getMessage());
			try{
				ErrorMeta objErrorCode = ErrorCodeUtility.mapErrorMeta(sysCodeCashier, oriErrorCode, oriErrorMsg, defaultErrorCode);
				if(objErrorCode!=null){
					response.setExternalErrorCode(objErrorCode.getErrorCode());
					response.setExternalErrorMsg(objErrorCode.getErrorMsg());
				}
			}catch(Throwable e){
				logger.error("[monitor],event:handleApiCashierException,sysCode:{},oriErrorCode:{},oriErrorMsg:{},e:{}", sysCodeCashier, oriErrorCode, oriErrorMsg, e);
				response.setExternalErrorCode(APICashierPayResultEnum.SYSTEM_ERROR.getCode());
				response.setExternalErrorMsg(APICashierPayResultEnum.SYSTEM_ERROR.getMessage());
			}
		}
		if(Errors.INPUT_PARAM_NULL.getCode().equals(oriErrorCode)){
			//参数错误的，使用原始错误信息
			response.setExternalErrorMsg(oriErrorMsg);
		}
		return new CashierBusinessException(response.getExternalErrorCode(), response.getExternalErrorMsg());
	}
	
	/**
	 * nc-cashier项目敏感信息改造，配置Map中配置项读取
	 * @param configFiled 0L_NCCASHIER_ENCRYPT_CONFIG_MAP 下属的具体配置项目key
	 * @return
	 */
	public static String getNccashierSensitiveInfoConfig(String configFiled){
		Map<String, String> configMap = CommonUtil.getSysConfigFrom3G(Constant.NCCASHIER_ENCRYPT_CONFIG_MAP_KEY);
		String configValue = configMap == null ? "" : configMap.get(configFiled);
		return configValue;
	}
	
	
	/**
	 * 获取nc-cashier数据库AES加密key
	 * @return
	 */
	public static String getNccashierEncryptKey() {
		if (StringUtils.isNotBlank(AES_FIXED_KEY)) {
			return AES_FIXED_KEY;
		}
		try {
			String aesKeyRightPart = getNccashierSensitiveInfoConfig(Constant.ENCRYPT_KEY_RIGHT_PART);
			String aesKeyForNccashierKey = getNccashierSensitiveInfoConfig(Constant.ENCRYPT_KEY_FOR_KEY);
			if(StringUtils.isBlank(aesKeyRightPart) || StringUtils.isBlank(aesKeyForNccashierKey)){
				logger.error("getNccashierEncryptKey 未成功通过统一配置获取到加解密key，请检查配置集合0L_NCCASHIER_ENCRYPT_CONFIG_MAP");
				//未通过统一配置获取到加解密key，直接返回，避免将错误key缓存
				return "";
			}
			String keyEncrypted = Constant.AES_KEY_LEFT_PART + aesKeyRightPart;
			String key = AESUtil.aesDecrypt(keyEncrypted, aesKeyForNccashierKey);
			AES_FIXED_KEY = key;
			return key;
		}catch (Exception e){
			logger.error("getNccashierEncryptKey 未成功通过统一配置获取到加解密key，请检查配置集合0L_NCCASHIER_ENCRYPT_CONFIG_MAP，异常为:", e);
			//未通过统一配置获取到加解密key，直接返回，避免将错误key缓存
			return "";
		}

	}
	
	/**
	 * 获取监听PP支付结果频率（间隔时间），单位毫秒
	 * @return
	 */
	public static int getPPPayResultQueryIntervalTime(){
		String payResultQueryIntervalTime = "";
		try{
			payResultQueryIntervalTime = getSysConfigFrom3G(PP_PAY_RESULT_QUERY_INTERVAL_TIME);
		}catch (Throwable t) {
			logger.error("从三代统一配置获取监听PP支付结果频率异常");
		}
		if(StringUtils.isBlank(payResultQueryIntervalTime)){
			payResultQueryIntervalTime = Constant.DEFAULT_PP_PAY_RESULT_QUERY_INTERVAL_TIME;
		}
		return Integer.parseInt(payResultQueryIntervalTime);	
	}
	
//	
//	public static void main(String[] args){
//		
//		getPPPayResultQueryIntervalTime();
//	}
	
	/**
	 * 获取支付宝唤起时跳转的支付蓝页取消后，重新拼接的url
	 * 
	 * @return
	 */
	public static String getAlipayPayUrl(String prepayCode) {
		if (StringUtils.isBlank(prepayCode)) {
			return prepayCode;
		}
		Map<String, String> cancelConfig = null;
		try {
			cancelConfig = getSysConfigFrom3G(CANCLE_ALIPAY_ROUTE_PAGE_SWITCH);
		} catch (Throwable t) {
			logger.error("获取钱包/钱包h5支付宝唤起中间蓝页取消相关配置异常:", t);
		}

		if (MapUtils.isEmpty(cancelConfig)) {
			return prepayCode;
		}
		String cancelFlag = cancelConfig.get(CANCEL_ALIPAY_ROUTE_PAGE_KEY);
		if (!ON.equals(cancelFlag)) {
			return prepayCode;
		}

		String payUrl = cancelConfig.get(NEW_ALIPAY_PAY_URL_AFTER_CANCEL);
		if (StringUtils.isBlank(payUrl) || !payUrl.contains(NEW_ALIPAY_PAY_URL_VARIABLE)) {
			return "alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + prepayCode
					+ "?_s=web-other";
		}
		payUrl = payUrl.replace(NEW_ALIPAY_PAY_URL_VARIABLE, prepayCode);
		logger.info("取消支付宝唤起中间蓝页，拼接payUrl={}", payUrl);
		return payUrl;
	}

	/**
	 * 短验类型转换
	 * @param smsSendTypeEnum
	 * @return
	 */
	public static ReqSmsSendTypeEnum transferBindPaySMSType(SmsSendTypeEnum smsSendTypeEnum) {
		ReqSmsSendTypeEnum reqSmsSendTypeEnum = null;
		if (smsSendTypeEnum == SmsSendTypeEnum.VOICE || smsSendTypeEnum == SmsSendTypeEnum.MERCHANT_SEND) {
			reqSmsSendTypeEnum = ReqSmsSendTypeEnum.VOICE;
		} else if (smsSendTypeEnum == SmsSendTypeEnum.YEEPAY || smsSendTypeEnum == SmsSendTypeEnum.BANK) {
			reqSmsSendTypeEnum = ReqSmsSendTypeEnum.YEEPAY;
		} else if (smsSendTypeEnum == SmsSendTypeEnum.NONE) {
			// 当NCPAY/PP返回不发短验时，收银台用来控制是否要发短验的开关配置（生产上配置为ON，也就是返回值是true）
			boolean riskVerifyCodeSwitch = CommonUtil.getRiskVerifyCodeSwitch();
			if (!riskVerifyCodeSwitch) {
				reqSmsSendTypeEnum = ReqSmsSendTypeEnum.YEEPAY;
			}
		}
		// modify by meiling.zhuang:当NCPAY/PP返回不发短验，且收银台用来控制是否要发短验的开关配置为ON时，会返回null
		return reqSmsSendTypeEnum;
	}

	/**
	 * 预授权强制短验开关：打开时，若风控返回无需短验，也需要发易宝短验
	 * @return
	 */
	private static boolean preAuthAlwaysVerifySwitch(){
		String preAuthAlwaysVerifySwitch ="";
		try{
			preAuthAlwaysVerifySwitch = getSysConfigFrom3G(PREAUTH_ALWAYS_VEFIRY_SMS_SWITCH);
		}catch(Exception e){
			preAuthAlwaysVerifySwitch = OFF ;
			logger.error("preAuthAlwaysVerifySwitch() 获取统一配置信息失败:",e);
		}
		return ON.equals(preAuthAlwaysVerifySwitch);
	}
	
	private static boolean preAuthSmsSendSwitch(NCCashierOrderTypeEnum orderType){
		if(orderType == null){
			return false;
		}
		Map<String, String> preAuthSmsSendOrNot = null;
		try{
			preAuthSmsSendOrNot = getSysConfigFrom3G(PREAUTH_SMS_SEND_OR_NOT_KEY);
		}catch(Exception e){
			logger.error("preAuthSmsSendSwitch() 获取统一配置信息失败:",e);
			
		}
		if(MapUtils.isEmpty(preAuthSmsSendOrNot)){
			return NCCashierOrderTypeEnum.BIND == orderType ? false : true;
		}
		return ON.equals(preAuthSmsSendOrNot.get(orderType.name()));
	}
	
	/**
	 * 预授权专用，短验类型转换
	 * @param smsSendTypeEnum
	 * @return
	 */
	public static SmsSendTypeEnum transferPreAuthSMSType(SmsSendTypeEnum smsSendTypeEnum, NCCashierOrderTypeEnum orderType){
		if(smsSendTypeEnum==null){
			return SmsSendTypeEnum.NONE;
		}
		if (smsSendTypeEnum == SmsSendTypeEnum.NONE && CommonUtil.preAuthSmsSendSwitch(orderType)) {
			return SmsSendTypeEnum.YEEPAY;
		}
		return smsSendTypeEnum;
	}

	/**
	 * 检查商编是否需要展示易宝微信二维码(部分大商户定制了无需展示)
	 * @param merchantNo
	 * @return
	 */
	public static boolean checkNeedQRCode(String merchantNo, String orderSysNo) {
		if(StringUtils.isNotBlank(merchantNo)){
			String merchantNumbersNotNeedQRCode = CommonUtil.getSysConfigFrom3G(Constant.MERCHANT_NO_NOT_NEED_QRCODE);
			if(StringUtils.isNotBlank(merchantNumbersNotNeedQRCode) && merchantNumbersNotNeedQRCode.contains(merchantNo)){
				return false; // 优先取商户定制的
			}
		}
		
		if(StringUtils.isNotBlank(orderSysNo)){
			String orderSysNoNotNeedQRCode = CommonUtil.getSysConfigFrom3G(Constant.ORDER_SYS_NO_NOT_NEED_QRCODE);
			if(StringUtils.isNotBlank(orderSysNoNotNeedQRCode) && orderSysNoNotNeedQRCode.contains(orderSysNo)){
				return false; // 再取订单方定制
			}
		}
		
		return true;
	}
	
	/**
	 * 格式化前置收银台的商编入参
	 * 由于yop请求进来的商编带有"OPR:"前缀，例如OPR：10040007800，因此需要将前缀去除
	 * @param merchantNo
	 * @return
	 */
	public static String formatMerchantNo(String merchantNo) {
		if (StringUtils.isNotBlank(merchantNo) && merchantNo.startsWith("OPR:")) {
			return merchantNo.replace("OPR:", "");
		}
		return merchantNo;
	}
	
	/**
	 * 标准化银行编码
	 * @param bankCode
	 * @return
	 */
	public static String standardBankCode(String bankCode) {
		if (StringUtils.isBlank(bankCode)) {
			return null;
		}
		Map<String, String> standardBankCodes = getConfig(Constant.STANDARD_BANKCODE);
		if (MapUtils.isNotEmpty(standardBankCodes)) {
			if (standardBankCodes.containsKey(bankCode)) {
				return standardBankCodes.get(bankCode);
			}
		}
		return bankCode;
	}
	
	public static FeeParam getCalFeeInfo(String orderSysNo, String defaultCalFeeItem, String defaultPayProduct) {
		FeeParam defaultFeeInfo = new FeeParam(defaultCalFeeItem, defaultPayProduct);
		Map<String, String> calFeeInfoConfig = getSysConfigFrom3G(CommonUtil.CAL_FEE_INFO_CONFIG_KEY);
		if (MapUtils.isEmpty(calFeeInfoConfig)) {
			return defaultFeeInfo;
		}
		String orderSysConfig = calFeeInfoConfig.get(orderSysNo);
		if (StringUtils.isBlank(orderSysConfig)) {
			orderSysConfig = calFeeInfoConfig.get(CONFIG_DEFAULT_KEY);
		}
		if (StringUtils.isBlank(orderSysConfig)) {
			return defaultFeeInfo;
		}
		FeeParam feeConfig = null;
		try {
			feeConfig = JSON.parseObject(orderSysConfig, FeeParam.class);
		} catch (Throwable t) {
			logger.error("解析计费项配置异常异常, e=", t);
		}
		
		if(feeConfig == null){
			return defaultFeeInfo;
		}
		if ((StringUtils.isBlank(feeConfig.getCalFeeItem()) && StringUtils.isBlank(feeConfig.getPayProduct()))) {
			return defaultFeeInfo;
		}
		
		if(StringUtils.isBlank(feeConfig.getCalFeeItem())){
			feeConfig.setCalFeeItem(defaultFeeInfo.getCalFeeItem());
		}
		if(StringUtils.isBlank(feeConfig.getPayProduct())){
			feeConfig.setPayProduct(defaultFeeInfo.getPayProduct());
		}
		return feeConfig;
	}
	
	/**
	 * 前置收银台 统一的前端回调地址
	 * 
	 * @param requestId
	 * @return
	 */
	public static String getApiFrontRedirectUrl(String merchantNo, long requestId) {
		String urlPrefix = getCashierUrlPrefix(merchantNo);
		return urlPrefix + "/wap/redirectMerchantCallBack/" + requestId;
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
		return merchantUrlPrefix;
	}

	/**
	 * H5收银台查询结果页地址
	 * @param token
	 * @return
	 */
	public static String getH5FrontCallbackUrl(String merchantNo, String token) {
		String urlPrefix = getCashierUrlPrefix(merchantNo);
		String pageCallbackUrl = urlPrefix+"/wap/query/result?token="+token;
		return pageCallbackUrl;
	}

	public static Map<String, InstallmentBankInfo> getInstallmentInfo(){
		Map<String, String> installmentRateInfos = getInstallmentBankInfo();
		return parseInstallmentRateInfos(installmentRateInfos);
	}
	
	public static Map<String, String> getInstallmentBankInfo() {
		Map<String, String> installmentRateInfos = null;
		try {
			installmentRateInfos = getSysConfigFrom3G(INSTALLMENT_RATE_INFO_KEY);
		} catch (Throwable t) {
			logger.error("获取银行卡分期的费率信息异常, 配置键=" + INSTALLMENT_RATE_INFO_KEY + " e=", t);
		}
		return installmentRateInfos;
	}
	
	/**
	 * 将json字符串转化对象集合
	 * @param installmentRateInfos
	 * @return
	 */
	public static Map<String, InstallmentBankInfo> parseInstallmentRateInfos(Map<String, String> installmentRateInfos) {
		if (MapUtils.isEmpty(installmentRateInfos)) {
			return null;
		}
		Map<String, InstallmentBankInfo> installmentBankInfos = new HashMap<String, InstallmentBankInfo>();
		InstallmentBankInfo info = null;
		try {
			// TODO 看看如何改成entrySet
			for (String key : installmentRateInfos.keySet()) {
				info = JSON.parseObject(installmentRateInfos.get(key), InstallmentBankInfo.class);
				installmentBankInfos.put(key, info);
			}
		} catch (Throwable t) {
			logger.error("解析银行卡分期费率信息异常, e=", t);
		}
		return installmentBankInfos;
	}


	/**
	 * PP监听reids切换至MQ消息开关
	 * @return
	 *  on|off|transition 开|关|两者兼容
	 */
	public static String getPpSendPayResultMQSwtich(){
		return CommonUtil.getSysConfigFrom3G(CommonUtil.PP_PAYRESULT_MQ_SWTICH);
	}


	/**
	 * 通过统一配置开关控制读取哪一个redis
	 * @param orderNum
	 * @return
	 */
	public static String getPpPayResultQueryState(String orderNum){
		String queryState = null;
		String isPpSendPayResultMQ = CommonUtil.getPpSendPayResultMQSwtich();
		if(isPpSendPayResultMQ==null || isPpSendPayResultMQ.equals(PP_PAYRESULT_MQ_SWTICH_OFF))
			queryState = RedisTemplate.getTargetFromRedis(Constants.PP_PAY_RESULT_KEY + orderNum, String.class);
		else if(isPpSendPayResultMQ.equals(PP_PAYRESULT_MQ_SWTICH_ON))
			//开关打开则监听收银台内部的redis
			queryState = RedisTemplate.getTargetFromRedis(CommonUtil.PP_HAS_PAY_RESULT_KEY + orderNum, String.class);
		else{//兼容模式，优先读取内部redis，内容为空则在读取PP的redis
			queryState = RedisTemplate.getTargetFromRedis(CommonUtil.PP_HAS_PAY_RESULT_KEY + orderNum, String.class);
			if(com.yeepay.g3.utils.common.StringUtils.isBlank(queryState))
				queryState = RedisTemplate.getTargetFromRedis(Constants.PP_PAY_RESULT_KEY + orderNum, String.class);
		}
		return queryState;
	}
	
	/**
	 * 通联分期通过配置中心读取支持银行和期数
	 */
	public static Map<String,List<String>> getSupportBankAndPeriodsUtil(){

		Map<String,String> supportBankAndPeriodsOrigin = new HashMap<String, String>();
		Map<String,List<String>> supportBankAndPeriods = new LinkedHashMap<String, List<String>>();
		try {
			supportBankAndPeriodsOrigin = getSysConfigFrom3G(Constant.OL_NCCASHIER_DBFQ_SUPPORT_BANK_PERIOD);
			if(MapUtils.isNotEmpty(supportBankAndPeriodsOrigin)){
				for(Map.Entry map : supportBankAndPeriodsOrigin.entrySet()){
					supportBankAndPeriods.put(map.getKey().toString().trim(), Arrays.asList(map.getValue().toString().split(",")));
				}
			}
			else{
				logger.warn("获取担保分期银行卡期数配置异常为空, 配置键=" + Constant.OL_NCCASHIER_DBFQ_SUPPORT_BANK_PERIOD );
				supportBankAndPeriods = handleGetSupportBankAndPeriodsException();
			}
		} catch (Throwable t) {
			logger.error("获取担保分期银行卡期数配置异常, 配置键=" + Constant.OL_NCCASHIER_DBFQ_SUPPORT_BANK_PERIOD + " e=", t);
			supportBankAndPeriods = handleGetSupportBankAndPeriodsException();
		}
		return supportBankAndPeriods;
	}
	/**
	 * 通联分期通过配置中心读取支持银行和期数异常处理
	 */
	private static Map<String, List<String>> handleGetSupportBankAndPeriodsException(){
		List<String> allSupportBanks = Arrays.asList(new String[]{"DLCB","TJB","HZB","CBHB","TZB","GYCCB","HRB","LJB","GZRCB","SRCB","CQRCB","BJRCB","GZCB","HSB","JSBC","NBCB","NJCB","BSB","SDB","ECITIC","BOC","CMBCHINA","PSBC","CIB","SHB","SPDB","SZPA","ABC","CMBC","BOCO","HX","GDB","CEB","ICBC","BCCB","CCB"});
		Map<String,List<String>> basicDbfqSupportBankAndPeriod = new HashMap<String, List<String>>();
		for(String bank :  allSupportBanks){
			basicDbfqSupportBankAndPeriod.put(bank,Arrays.asList(new String[]{ "6","12","24" }));
		}
		return basicDbfqSupportBankAndPeriod;
	}


	/**
	 * 获取担保分期最低和最高限额
	 * @return
     */
	public static List<Long> getDbfqMaxLimitAndMinLimit(){
		List<Long> maxLimitAndMin = new ArrayList<Long>();
		try {
			maxLimitAndMin = getSysConfigFrom3G(Constant.OL_NCCASHIER_DBFQ_BANK_LIMIT);
		} catch (Throwable t) {
			logger.error("获取担保分期银行限额, 配置键=" + Constant.OL_NCCASHIER_DBFQ_BANK_LIMIT + " e=", t);
			maxLimitAndMin = handleGetDbfqMaxLimitAndMinLimitException();
		}
		return maxLimitAndMin;
	}

	/**
	 * 获取担保分期最低和最高限额异常处理
	 * @return
	 */
	private static List<Long> handleGetDbfqMaxLimitAndMinLimitException(){
		List<Long> maxLimitAndMin = new ArrayList<Long>();
		maxLimitAndMin.add(50000l); 	//最高限额:50000   5万
		maxLimitAndMin.add(600l); 	//最低限额:600     6百
		return maxLimitAndMin;
	}



	/**
	 * 通联分期 获得银行和对应的银行编码
	 */
	public static Map<String,String> getSupportBanKNameAndCode(){
		Map<String,String> bankCodeAndName = null;
		try {
			bankCodeAndName = getSysConfigFrom3G(Constant.OL_NCCASHIER_BANKCODE_BANKNAME);
		} catch (Throwable t) {
			logger.error("获取担保支持银行编码配置异常, 配置键=" + Constant.OL_NCCASHIER_BANKCODE_BANKNAME + " e=", t);
			bankCodeAndName = handelGetBandAndCodeException();
		}
		if(MapUtils.isEmpty(bankCodeAndName)){
			logger.warn("获取担保支持银行编码配置为空, 配置键=" + Constant.OL_NCCASHIER_BANKCODE_BANKNAME);
			bankCodeAndName = handelGetBandAndCodeException();
		}
		return bankCodeAndName;
	}

	private static Map<String,String> handelGetBandAndCodeException(){
		Map<String,String> basicBankAndCode = new HashMap<String, String>();
		basicBankAndCode.put("DLCB","大连银行");
		basicBankAndCode.put("TJB","天津银行");
		basicBankAndCode.put("HZB","杭州银行");
		basicBankAndCode.put("CBHB","渤海银行");
		basicBankAndCode.put("TZB","台州银行");
		basicBankAndCode.put("GYCCB","贵阳银行");
		basicBankAndCode.put("HRB","哈尔滨银行");
		basicBankAndCode.put("LJB","龙江银行");
		basicBankAndCode.put("GZRCB","广州农商银行");
		basicBankAndCode.put("SRCB","上海农商银行");
		basicBankAndCode.put("CQRCB","重庆农村商业银行");
		basicBankAndCode.put("BJRCB","北京农商银行");
		basicBankAndCode.put("GZCB","广州银行");
		basicBankAndCode.put("HSB","徽商银行");
		basicBankAndCode.put("JSBC","江苏银行");
		basicBankAndCode.put("NBCB","宁波银行");
		basicBankAndCode.put("NJCB","南京银行");
		basicBankAndCode.put("BSB","包商银行");
		basicBankAndCode.put("SDB","深圳发展银行");
		basicBankAndCode.put("ECITIC","中信银行");
		basicBankAndCode.put("BOC","中国银行");
		basicBankAndCode.put("CMBCHINA","招商银行");
		basicBankAndCode.put("PSBC","邮政储蓄银行");
		basicBankAndCode.put("CIB","兴业银行");
		basicBankAndCode.put("SHB","上海银行");
		basicBankAndCode.put("SPDB","浦东发展银行");
		basicBankAndCode.put("SZPA","平安银行");
		basicBankAndCode.put("ABC","农业银行");
		basicBankAndCode.put("CMBC","民生银行");
		basicBankAndCode.put("BOCO","交通银行");
		basicBankAndCode.put("HX","华夏银行");
		basicBankAndCode.put("GDB","广东发展银行");
		basicBankAndCode.put("CEB","光大银行");
		basicBankAndCode.put("ICBC","工商银行");
		basicBankAndCode.put("BCCB","北京银行");
		basicBankAndCode.put("CCB","建设银行");
		return basicBankAndCode;
	}

	/**
     * 判断某个银行的网银支付是否走跳板机 【统一配置TO_PCC走跳板机，TO_BANK走银行，没有配置走银行】
	 *
	 * @param bankCode
	 * @return
	 */
	public static String toBankPassThroughPccashier(String bankCode) {
		Map<String, String> passThroughPccashierMap = null;
		try {
			passThroughPccashierMap = getSysConfigFrom3G(TO_BANK_PASS_THROUGH_PCCASHIER_KEY);
		} catch (Throwable t) {
			logger.error("toBankPassThroughPccashier异常, e=", t);
		}
		if (MapUtils.isEmpty(passThroughPccashierMap)) {
			return Constant.TO_BANK;
		}
		String passThroughPccashierOfBankCode = passThroughPccashierMap.get(bankCode);
		if (StringUtils.isBlank(passThroughPccashierOfBankCode)) {
			passThroughPccashierOfBankCode = passThroughPccashierMap.get(CONFIG_DEFAULT_KEY);
		}
		return Constant.TO_PCCASHIER.equals(passThroughPccashierOfBankCode) ? Constant.TO_PCCASHIER : Constant.TO_BANK;
	}



	/**
	 * 根据统一配置的值获得是否迁移pp   key:业务方编码，商编，default ；value：true/false
	 *  暂时不能配置 收款宝 的商编
	 * @return
     */
	public static String getPaySysTypeByUniform(String orderSys ,String merchantNo){

		String paySysCode = "";
		Map<String, String> map = null;
		try {
			map = getSysConfigFrom3G(OL_NCCASHIER_MIGRATE_TO_PP);
		} catch (Throwable t) {
			logger.error("OL_NCCASHIER_MIGRATE_TO_PP读取异常, e=", t);
		}
		if(MapUtils.isNotEmpty(map)){
			if("true".equals(map.get(merchantNo)) || "true".equals(map.get(orderSys)) || "true".equals(map.get("default"))){
				paySysCode = "PAY_PROCCESOR"; // 必须用这个，虽然单词拼错了
			}
		}
		return paySysCode;
	}
	
	
	public static boolean merchantAttendMarketActivity(String merchantNo, String orderSysNo){
		Map<String, String> map = getSysConfigFrom3G(MERCHANT_ATTEND_MARKET_ACTIVITY_KEY);
		if(MapUtils.isEmpty(map) || StringUtils.isBlank(map.get(BIZ_SYS_CONFIG_DEFAULT_KEY))||!ON.equals(map.get(BIZ_SYS_CONFIG_DEFAULT_KEY))){
			// 默认开关配置是关着的话，则商户名单或者订单方名单为白名单
			if(StringUtils.isNotBlank(map.get(MERCHANT_LIST_KEY)) && map.get(MERCHANT_LIST_KEY).contains(merchantNo)){
				return true;
			}
			if(StringUtils.isNotBlank(map.get(ORDER_SYS_LIST_KEY)) && map.get(ORDER_SYS_LIST_KEY).contains(orderSysNo)){
				return true;
			}
			// 开关默认是关，又没有配置商户白名单和订单方白名单，那么认为不参与营销活动
			return false;
		}else if(ON.equals(map.get(BIZ_SYS_CONFIG_DEFAULT_KEY))){
			// 开关默认是开时，商户名单或订单方名单为黑名单
			if(StringUtils.isNotBlank(map.get(MERCHANT_LIST_KEY)) && map.get(MERCHANT_LIST_KEY).contains(merchantNo)){
				return false;
			}
			if(StringUtils.isNotBlank(map.get(ORDER_SYS_LIST_KEY)) && map.get(ORDER_SYS_LIST_KEY).contains(orderSysNo)){
				return false;
			}
			return true;
		}
			return false;
	}
	
	public static int marketActivityRedisTime(){
		int result = 60 * 1000; // 1 min
		
		try{
			String time = getSysConfigFrom3G(MARKET_ACTIVITY_TIME_KEY);
			if(com.yeepay.g3.utils.common.StringUtils.isNotBlank(time)){
				result = Integer.valueOf(time);
			}
			logger.info("获取的缓存时间为" + result);
		}catch(Throwable t){
			logger.error("OL_NCCASHIER_MARKET_ACTIVITY_TIME读取异常, e=", t);
		}
		return result;
	}

	/**
	 * 商编等级缓存时间
	 * @return
	 */
	public static int merchantLevelRedisTime(){
		int result = 5 * 60 * 1000; // 默认5分钟

		try{
			String time = getSysConfigFrom3G(MERCHANT_LEVEL_TIME_KEY);
			if(com.yeepay.g3.utils.common.StringUtils.isNotBlank(time)){
				result = Integer.valueOf(time);
			}
		}catch(Throwable t){
			logger.error("MERCHANT_LEVEL_TIME_KEY读取异常, e=", t);
		}
		return result;
	}



	/**
	 * 查询分期易支持的银行
	 * @return
	 */
	public static Map<String, String> getCflEasyBankInfoMaps(){
		Map<String, String> cflEasyBankInfoMaps = null;
		try{
			cflEasyBankInfoMaps = getSysConfigFrom3G(OL_NCCASHIER_CFL_EASY_BANK_LIST);
		}catch(Exception e){
			logger.error("getCflEasyBankInfoList失败:", e);
			return null;
		}
		return cflEasyBankInfoMaps;
	}
	
	public static boolean cashierVersionGetFromUserRequestInfo(){
		boolean result = false;
		try{
			String getFromRequestInfoFlag = getSysConfigFrom3G(CASHIER_VERSION_GET_FROM_USER_REQUEST_KEY);
			if("YES".equals(getFromRequestInfoFlag)){
				result = true;
			}
		}catch(Throwable t){
			logger.error("cashierVersionGetFromUserRequestInfo失败:", t);
		}
		return result;
	}

	/**
	 * 是否打开埋点日志
	 * @return
	 */
	public static boolean enableSpanLog(){
		String enableSpanLog =OFF;
		try{
			enableSpanLog = getSysConfigFrom3G(ENABLE_CASHIER_SPAN_LOG);
		}catch(Exception e){
			logger.warn("获取统一配置信息失败:",e);
		}
		return ON.equals(enableSpanLog);
	}
}
