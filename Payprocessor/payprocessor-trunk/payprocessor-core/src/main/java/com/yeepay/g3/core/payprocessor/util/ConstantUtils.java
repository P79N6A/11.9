package com.yeepay.g3.core.payprocessor.util;

import com.yeepay.g3.core.payprocessor.entity.ExtendedInfo;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.BankAccountType;
import com.yeepay.g3.facade.payprocessor.constant.Constants;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.encrypt.AES;
import com.yeepay.g3.utils.common.exception.YeepayRuntimeException;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;
import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量工具类
 *
 */
public class ConstantUtils {

	private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(ConstantUtils.class);

	private static final String CONFIG_PREFIX = "PAY_PROCESSOR_";
	public static final String REDIS_KEY_PREFIX = "PP_";
	public static final String SYS_NAME = "PAYPROCESSOR";

	public static final String SYS_NO = "PP";

	public static final int PAY_LIMIT_TIMES = 100;// 支付次数限制

	public static final String NC_PAY_REQUEST_SYS = "payprocessor";

	public static final String REFUND_CALL_BAK_URL = "REFUND_CALL_BAK_URL";
	public static final String DEFAULT_REFUND_CALL_BACK = "http://payprocessor.core.3g/payprocessor-hessian/hessian/RefundResultNotifyFacade";// 退款中心默认回调地址
	public static final String CS_CALL_BACK_URL = "com.yeepay.g3.facade.payprocessor.facade.PayProcessorCsResponseFacade.receiveResponse";// 清算中心回调地址

	public static final String REFUND_PRODUCT_NCPAY = "NOCARD_MOBILEPAY";
	private static final String REFUND_PRODUCT_MEMBER_PAY = "MEMBER_PAY";
	public static final String REFUND_PRODUCT_NET_B2C = "NET_B2C";
	public static final String REFUND_PRODUCT_NET_B2B = "NET_B2B";
	public static final String REFUND_PRODUCT_FE = "FE";
	private static final String REFUND_PRODUCT_CFL = "BK_CFL";
	private static final String REFUND_PRODUCT_ACCOUNT = "YEEPAY_ACCOUNT_PAY";
	private static final String REFUND_PRODUCT_GUAR_CFL = "GUAR_CFL";
	private static final String REFUND_PRODUCT_CFL_EASY = "ZF_FQY";
	public static final String PAY_CALL_BAK_URL = "PAY_CALL_BAK_URL";
	
	public static final int REPAIR_ORDER_START_TIME_OFFSET_DEFAULT_VALUE = -15;
	public static final int REPAIR_ORDER_END_TIME_OFFSET_DEFAULT_VALUE = -5;
	public static final int REPAIR_ORDER_MAX_ROW_COUNT_DEFAULT_VALUE = 1000;
	
	public static final String REPAIR_ORDER_START_TIME_OFFSET_KEY = "REPAIR_ORDER_START_TIME_OFFSET";
	public static final String REPAIR_ORDER_END_TIME_OFFSET_KEY = "REPAIR_ORDER_END_TIME_OFFSET";
	public static final String REPAIR_ORDER_MAX_ROW_COUNT_KEY = "REPAIR_ORDER_MAX_ROW_COUNT";

	// 定时补发通知配置值，时间单位：小时
	public static final String RENOTIFY_ORDER_START_TIME_OFFSET_KEY = "RENOTIFY_ORDER_START_TIME_OFFSET_KEY";
	public static final String RENOTIFY_ORDER_END_TIME_OFFSET_KEY = "RENOTIFY_ORDER_END_TIME_OFFSET_KEY";
	public static final int RENOTIFY_ORDER_START_TIME_OFFSET_DEFAULT_VALUE = -24;
	public static final int RENOTIFY_ORDER_END_TIME_OFFSET_DEFAULT_VALUE = 0;

	// 获取mq和redis转换开关
	public static final String MQ_OR_REDIS_SWITCH = "MQ_OR_REDIS_SWITCH";
	// 账户支付新旧接口切换开关
	public static final String ACCOUNT_PAY_NEW_INTERFACE = "ACCOUNT_PAY_NEW_INTERFACE";

	private static final String ACCOUNT_OPERATOR = "PayProcessor";
    private static final String ACCOUNT_PAY_INTERFACE = "10003";
    private static final String ACCOUNT_PAY_INTERFACE_KEY = "ACCOUNT_PAY_INTERFACE";

    private static final String DEFAULT_NCPAY_BASIC_PRODUCT_CODE = "ZF_YJZF";

	// 预授权请求
	public static final String PRE_AUTH_REQUEST = "PREAUTH_RE";
	// 预授权撤销
	public static final String PRE_AUTH_CANCEL = "PREAUTH_CL";
	// 预授权完成
	public static final String PRE_AUTH_COMPLETE = "PREAUTH_CM";
	// 预授权完成撤销
	public static final String PRE_AUTH_COMPLETE_CANCEL = "PREAUTH_CC";

	// 撤销类型，预授权撤销PREAUTHCANCEL、预授权完成撤销PREAUTHCONFIRMCANCEL
	public static final String PRE_AUTH_CANCEL_TYPE_CANCLE = "PREAUTHCANCEL";
	public static final String PRE_AUTH_CANCEL_TYPE_COMPLETE_CANCEL = "PREAUTHCONFIRMCANCEL";

	// pp调用下游系统，显示自己的系统编码
	public static final String BIZ_SYS_NO = "PP";

	public static final String MKTG_PAY_PRODUCT = "MKTG";

	public static final String REPAIR_COMB_ORDER_START_TIME_OFFSET_KEY = "REPAIR_COMB_ORDER_START_TIME_OFFSET";
	public static final String REPAIR_COMB_ORDER_END_TIME_OFFSET_KEY = "REPAIR_COMB_ORDER_END_TIME_OFFSET";
	public static final String REPAIR_COMB_ORDER_MAX_ROW_COUNT_KEY = "REPAIR_COMB_ORDER_MAX_ROW_COUNT";

	public static final int REPAIR_COMB_ORDER_START_TIME_OFFSET_DEFAULT_VALUE = -15;
	public static final int REPAIR_COMB_ORDER_END_TIME_OFFSET_DEFAULT_VALUE = -5;
	public static final int REPAIR_COMB_ORDER_MAX_ROW_COUNT_DEFAULT_VALUE = 200;


	// OPR的预授权状态--预授权
	public static final String OPR_PRE_AUTH = "PREAUTH";
	// OPR的预授权状态--预授权撤销
	public static final String OPR_PRE_AUTH_REPEAL = "PREAUTHREPEAL";
	// OPR的预授权状态--预授权完成
	public static final String OPR_PRE_AUTH_COMPLETE = "PREAUTHCOMPLETE";
	// OPR的预授权状态--预授权完成撤销
	public static final String OPR_PRE_AUTH_COMPLETEREPEAL = "PREAUTHCOMPLETEREPEAL";
	// OPR的预授权状态--预授权完成暂停入账
	public static final String OPR_PRE_AUTH_COMPLETEPAUSE = "PREAUTHCOMPLETEPAUSE";

	private static final String ENABLE_PP_SPAN_LOG = "ENABLE_PP_SPAN_LOG";


	// added by zengzhi.han 20181019  优惠券信息
	public static final String PROMOTION_CASH_FEE = "cashFee";
	public static final String PROMOTION_SETTLEMENT_FEE = "settlementFee";
	public static final String PROMOTION_INFO_DTOS = "promotionInfoDTOS";

	/**
     * 数据库敏感信息加密KEY
     */
    private static String DB_AES_KEY = null;

	private static final String DB_AES_KEY_PREFIX = "+9G8ya7pzFWQcD3NZHaQhA==";


	private static <T> T getSysConfig(String key) {
		T result = null;
		try {
			ConfigParam<T> p = ConfigurationUtils.getSysConfigParam(CONFIG_PREFIX + key);
			if (p != null) {
				result = p.getValue();
			}
		} catch (Exception e) {
			logger.error("无法从统一配置获取" + key, e);
		}
		return result;
	}

	/**
	 * 退款中心回调URL
	 * 
	 * @return
	 */
	public static String getRefundCallbackUrl() {
		String callBackUrl = getSysConfig(REFUND_CALL_BAK_URL);
		if (StringUtils.isBlank(callBackUrl)) {
			callBackUrl = DEFAULT_REFUND_CALL_BACK;
		}
		return callBackUrl;
	}

	/**
	 * 获取退款产品类型
	 * 
	 * @param orderType
	 * @param bankAccountType
	 * @return
	 */
	public static String getRefundProduct(String orderType, String bankAccountType) {
		if (PayOrderType.SALE.name().equals(orderType)) {
			return REFUND_PRODUCT_NCPAY;
		}
		if(PayOrderType.MEMBER_PAY.name().equals(orderType)){
			return REFUND_PRODUCT_MEMBER_PAY;
		}
		if (PayOrderType.NET.name().equals(orderType)) {
			if (BankAccountType.INDIVIDUAL.name().equals(bankAccountType)) {
				return REFUND_PRODUCT_NET_B2C;
			}
			if (BankAccountType.BUSINESS.name().equals(bankAccountType)) {
				return REFUND_PRODUCT_NET_B2B;
			}
			return REFUND_PRODUCT_NET_B2C;
		}
		if (PayOrderType.ONLINE.name().equals(orderType) || PayOrderType.OFFLINE.name().equals(orderType)) {
			return REFUND_PRODUCT_CFL;
		}
		if(PayOrderType.ACCOUNT.name().equals(orderType)){
			return REFUND_PRODUCT_ACCOUNT;
		}
		if (PayOrderType.GUAR_CFL.name().equals(orderType)){
		    return REFUND_PRODUCT_GUAR_CFL;
		}
		if (PayOrderType.CFL_EASY.name().equals(orderType)) {
			return REFUND_PRODUCT_CFL_EASY;
		}
		return REFUND_PRODUCT_FE;
	}

	/**
	 * @param orderSys
	 * @return
	 */
	public static String getCallBackServiceUrl(String orderSys) {
		String url = "";
		Map<String, String> callBackUrlConfig = getSysConfig(PAY_CALL_BAK_URL);
		if (MapUtils.isNotEmpty(callBackUrlConfig)) {
			// 商编+银行+卡类型
			url = callBackUrlConfig.get(orderSys);
		}
		if (StringUtils.isBlank(url)) {
			throw new YeepayRuntimeException("无法从统一配置获取回调url:" + orderSys);
		}
		return url;
	}
	
	/**
	 * 获取定时补单，开始时间对于当前时间的偏移，单位：分钟
	 * @return
	 */
	public static int getRepairOrderStartTimeOffset() {
	    Long value = getSysConfig(REPAIR_ORDER_START_TIME_OFFSET_KEY);
	    if(value == null) {
	        return REPAIR_ORDER_START_TIME_OFFSET_DEFAULT_VALUE;
	    }
	    return value.intValue();
	}
	
	/**
     * 获取定时补单，结束时间对于当前时间的偏移，单位：分钟
     * @return
     */
	public static int getRepairOrderEndTimeOffset() {
	    Long value = getSysConfig(REPAIR_ORDER_END_TIME_OFFSET_KEY);
        if(value == null) {
            return REPAIR_ORDER_END_TIME_OFFSET_DEFAULT_VALUE;
        }
        return value.intValue();
    }
	
	/**
     * 获取定时补单，单次查询最大返回行数
     * @return
     */
	public static int getRepairOrderMaxRowCount() {
	    Long value = getSysConfig(REPAIR_ORDER_MAX_ROW_COUNT_KEY);
        if(value == null) {
            return REPAIR_ORDER_MAX_ROW_COUNT_DEFAULT_VALUE;
        }
        return value.intValue();
	}
	/**
	 * 转支付记录的extendedInfo扩展信息为字符串
	 * @param extendInfo
	 * @return
	 */
	public static String transferExtendMsg(ExtendedInfo extendInfo){
		String extendedInfo = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(extendInfo.getBankTradeId())) {
			resultMap.put("BANK_TRADE_ID", extendInfo.getBankTradeId());
		}
		if (StringUtils.isNotBlank(extendInfo.getCouponInfo())) {
			resultMap.put("COUPON_INFO", extendInfo.getCouponInfo());
		}
		if (StringUtils.isNotBlank(extendInfo.getPayerBankAccountNo())) {
			resultMap.put(Constants.EXTEND_INFO_PAYER_BANKACCOUNT_NO, extendInfo.getPayerBankAccountNo());
		}
		if (StringUtils.isNotBlank(extendInfo.getReportMerchantNo())) {
			resultMap.put("REPORT_MERCHANT_NO", extendInfo.getReportMerchantNo());
		}
		//会员支付扩展信息
		if( MapUtils.isNotEmpty(extendInfo.getAccountPayExtInfo())) {
		    resultMap.putAll(extendInfo.getAccountPayExtInfo());
		}
		// 业务方定制扩展信息 added by zhijun.wang 2017-12-12
		if(MapUtils.isNotEmpty(extendInfo.getExtParam())) {
			resultMap.putAll(extendInfo.getExtParam());
		}

		try{
			extendedInfo = JSONUtils.toJsonString(resultMap);
		}catch(Throwable e){
			logger.error("扩展信息map转换为json string 失败" , e);
		}
		return extendedInfo;
	}
		
	public static String getAccountPayInterface() {
	    try {
	        String payInterface = getSysConfig(ACCOUNT_PAY_INTERFACE_KEY);
	        if(StringUtils.isNotBlank(payInterface)) {
	            return payInterface;
	        }
	    } catch(Throwable t) {
	        logger.error("获取account pay interface error." , t);
	    }
	    return ACCOUNT_PAY_INTERFACE;
	}
	
	public static String getAccountPayOperator() {
	    return ACCOUNT_OPERATOR;
	}
	
	public static String getDB_AES_KEY() {
        return DB_AES_KEY;
    }

    public static String getDefaultNcpayBasicProductCode() {
		return DEFAULT_NCPAY_BASIC_PRODUCT_CODE;
	}


	/**
	 * 合成敏感信息加密密钥
	 */
	public static void mergeSensitiveKey() {
		try {
			String cipherKeySuffix = getSysConfig("SENSITIVE_INFO_KEY_SUFFIX");
			String keyForkey = getSysConfig("SENSITIVE_INFO_KEY_ENCRYPT_KEY");

			String cipherKeyPrefix = DB_AES_KEY_PREFIX;

			String plainKeyPrefix = AES.decryptFromBase64(cipherKeyPrefix , keyForkey);
			String plainKeySuffix = AES.decryptFromBase64(cipherKeySuffix , keyForkey);

			DB_AES_KEY = plainKeyPrefix + plainKeySuffix;

			logger.info("合成密钥成功");
			
		} catch (Throwable t) {
			logger.error("合成密钥出错" , t);
			throw new Error("合成密钥失败");
		}
	}
	
	
	public static String getAccountPayRefundRemark(){
		Map<String,String> remark = new HashMap<String,String>();
        remark.put("bizType", "ACCOUNT_PAY");
        remark.put("historyType", "REFUND");
        return remark.toString();
	}


	/**
	 * 获取定时补发通知，开始时间对于当前时间的偏移，单位：小时
	 * @return
	 */
	public static int getReNotifyOrderStartTimeOffset() {
		Long value = getSysConfig(RENOTIFY_ORDER_START_TIME_OFFSET_KEY);
		if(value == null) {
			return RENOTIFY_ORDER_START_TIME_OFFSET_DEFAULT_VALUE;
		}
		return value.intValue();
	}

	/**
	 * 获取定时补发通知，结束时间对于当前时间的偏移，单位：小时
	 * @return
	 */
	public static int getReNotifyOrderEndTimeOffset() {
		Long value = getSysConfig(RENOTIFY_ORDER_END_TIME_OFFSET_KEY);
		if(value == null) {
			return RENOTIFY_ORDER_END_TIME_OFFSET_DEFAULT_VALUE;
		}
		return value.intValue();
	}


	/**
	 * 获取mq和redis转换开关，默认是redis
	 * @return
	 */
	public static String getMQorRedisSwitch() {
		String value = getSysConfig(MQ_OR_REDIS_SWITCH);
		if(value == null) {
			return "REDIS";
		}
		return value;
	}

	/**
	 * 账户支付新旧接口调用开关
	 * @return
	 */
	public static String getNewAccountPay() {
		String value = getSysConfig(ACCOUNT_PAY_NEW_INTERFACE);
		if(value == null) {
			return "CLOSE";
		}
		return value;
	}


	/**
	 * 获取定时补单，开始时间对于当前时间的偏移，单位：分钟
	 * @return
	 */
	public static int getRepairCombOrderStartTimeOffset() {
		Long value = getSysConfig(REPAIR_COMB_ORDER_START_TIME_OFFSET_KEY);
		if(value == null) {
			return REPAIR_COMB_ORDER_START_TIME_OFFSET_DEFAULT_VALUE;
		}
		return value.intValue();
	}

	/**
	 * 获取定时补单，结束时间对于当前时间的偏移，单位：分钟
	 * @return
	 */
	public static int getRepairCombOrderEndTimeOffset() {
		Long value = getSysConfig(REPAIR_COMB_ORDER_END_TIME_OFFSET_KEY);
		if(value == null) {
			return REPAIR_COMB_ORDER_END_TIME_OFFSET_DEFAULT_VALUE;
		}
		return value.intValue();
	}

	/**
	 * 获取定时补单，单次查询最大返回行数
	 * @return
	 */
	public static int getRepairCombOrderMaxRowCount() {
		Long value = getSysConfig(REPAIR_COMB_ORDER_MAX_ROW_COUNT_KEY);
		if(value == null) {
			return REPAIR_COMB_ORDER_MAX_ROW_COUNT_DEFAULT_VALUE;
		}
		return value.intValue();
	}

	/**
	 * 是否打开埋点日志
	 * @return
	 */
	public static boolean enableSpanLog(){
		String enableSpanLog ="OFF";
		try{
			enableSpanLog = getSysConfig(ENABLE_PP_SPAN_LOG);
		}catch(Exception e){
			logger.warn("获取统一配置信息失败:",e);
		}
		return "ON".equals(enableSpanLog);
	}
}
