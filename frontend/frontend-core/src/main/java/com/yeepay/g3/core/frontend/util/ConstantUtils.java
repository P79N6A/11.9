package com.yeepay.g3.core.frontend.util;


import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.config.ConfigParam;
import com.yeepay.g3.utils.config.ConfigurationUtils;

import java.util.List;
import java.util.Map;

/**
 * 常量工具类
 *
 */
public class ConstantUtils {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(ConstantUtils.class);
    
    private static final String CONFIG_PREFIX = "FRONT_END_";
    public static final String REDIS_KEY_PREFIX = "FE_";
    
    public static final String SYS_NO = "FE";
    public static final int PAY_LIMIT_TIMES = 100;
    public static final String BANKROUTER_PRODUCTCODE = "FRONTEND";
    
    public static final String PRODUCT_TYPE_FE = "FE";
    public static final String PRODUCT_TYPE_FEMPAY = "FE_MPAY";
    
    public static final String REQUEST_SYSTEM_CODE = "REQUEST_SYSTEM_CODE"; //修改20170918 与FE-netpay区分
    public static final String MPAY_URL = "MPAY_URL";
    public static final String PAYINTERFACE_TO_PLATFORMTYPE = "PAYINTERFACE_TO_PLATFORMTYPE";
    public static final String H5_PAYINTERFACE = "H5_PAYINTERFACE";
    public static final String ORDERTYPE_TO_SCENETYPE = "ORDERTYPE_TO_SCENETYPE";
    public static final String BASIC_PRODUCT_CODE_TO_SCENETYPE = "BASIC_PRODUCT_CODE_TO_SCENETYPE";

    /*系统商编*/
    public static final String FAN_ROUTE_PARENT_MERCHANTNO= "FAN_ROUTE_PARENT_MERCHANTNO";
    /*订单父编号***可以理解为渠道编号(比如哆啦宝)编号 */
    public static final String PARENT_MERCHANT_NO = "parentMerchantNo";
    /**哆啦宝粉丝路由功能,只有微信公众号才能用到*/
    public static final String WX_REPORT_ID = "reportId";
    public static final String WX_FOCUS_APP_ID = "focusAppId";


    // added by zengzhi.han 20181019  优惠券信息
    public static final String PROMOTION_CASH_FEE = "cashFee";
    public static final String PROMOTION_SETTLEMENT_FEE = "settlementFee";
    public static final String PROMOTION_INFO_DTOS = "promotionInfoDTOS";



    //    public static final String MPAY_PAY_INTERFACE = "WECHAT_WXZF001";
//    public static final String ALIPAY_SCAN_PAYINTERFACE = "ICBC_NET_OPEN_GHXJ6010";
//    public static final String WECHAT_H5_PAYINTERFACE_NEW = "ECITIC_NET_OPEN_BJGC6010";
    public static final String RISK_SYS_NO = "RISK";
    public static final String RISK_SPLIT = "-";
    public static final String RISK_TYPE_SYNC = "SYNC";
    public static final String RISK_TYPE_ASYNC = "ASYNC";
    public static final String RISK_INTERCEPT_CODE = "8000" ;
    public static final String PAYURL_REDIS_CACHE_TIME = "REDIS_CACHE_TIME"; //修改20170918 与FE-netpay区分
    public static final String RISK_SUFFIX = "_FE" ;
    public static final String DOORGOD_REQUEST_URL = "DOORGOD_REQUEST_URL"; //修改20170918 与FE-netpay区分
    
    public static final String MASKMERCHANTNO_REDIS_CACHE_TIME = "MASKMERCHANTNO_REDIS_CACHE_TIME"; //add 20170918

    public static final String STATIC_RESULT = "STATIC_RESULT";
    public static final String STATIC_DATE = "STATIC_DATE";
    public static final String STATIC_DIM = "STATIC_DIM";
    
    public static final String REFUND_CALLBAK_URL = "REFUND_CALLBAK_URL";
    
    public static final int BANKROUTER_RESULT_SUCCESS = 1;
    public static final int BANKROUTER_RESULT_INIT = 3;
    public static final int BANKROUTER_RESULT_PROCESS = 4;


    //fe返回给pp相应参数 map中key 是否需要密码
    public static final String FE_RETURN_PP_RESPONSE_IS_NEEDPASSWORD="isNeedPassword";


    public static final String LIMIT_CREDIT_CUSTOMER_LIST = "LIMIT_CREDIT_CUSTOMER_LIST";

    //调用完银行子系统后,是否缓存支付链接
    public static final String PAYURL_CACHE_ENABLE_SWITCH = "PAYURL_CACHE_ENABLE_SWITCH";

    /** 调用风控接口超时配置  */
    public static final String RISK_SERVICE_TIME_OUT_CONFIG = "RISK_SERVICE_TIME_OUT_CONFIG";
    /** 连接超时  */
    public static final String CONN_TIME_OUT = "CONN_TIME_OUT";
    /** 读超时  */
    public static final String READ_TIME_OUT = "READ_TIME_OUT";
    /** REDIS操作耗时阈值，用于打印日志  */
    public static final String REDIS_TIME_THRESHOLD = "REDIS_TIME_THRESHOLD";
    /** fe是否发送埋点日志  */
    private static final String ENABLE_FE_SPAN_LOG = "ENABLE_FE_SPAN_LOG";
    /** fe同步风控子商编白名单  */
    private static final String SYNC_RISK_CONTROL_WHITE_LIST = "SYNC_RISK_CONTROL_WHITE_LIST";

    private static <T> T getSysConfig(String key) {
    	T result = null;
    	try{
    		@SuppressWarnings("unchecked")
			ConfigParam<T> p = ConfigurationUtils.getSysConfigParam(CONFIG_PREFIX + key);
            if (p != null) {
            	result = p.getValue();
            }
    	}catch(Exception e){
    		logger.error("无法从统一配置获取" + key, e);
    	}
        return result;
    }
    
    /**
     * 获取MPAY URL
     * @return
     */
    public static String getMpayUrl() {
        return getSysConfig(MPAY_URL);
    }

    /**
     * 获取系统商编(比如哆啦宝)
     * @return
     */
    public static List<String> getFanRouteParentMerchantNos(){
        return getSysConfig(FAN_ROUTE_PARENT_MERCHANTNO);
    }
	

    /**
	 * 获取系统配置的请求业务方编码
	 * @return
	 */
	public static List<String> getRequestSystemList(){
		return getSysConfig(REQUEST_SYSTEM_CODE);
	}

    /**
     * 退款中心回调URL
     * @return
     */
    public static String getREFUND_CALLBAK_URL() {
        return getSysConfig(REFUND_CALLBAK_URL);
    }

//    /**
//     * 判断退款中心产品类型
//     * @param payInterface
//     * @return
//     */
//    public static String getProductType(String payInterface){
//        if (MPAY_PAY_INTERFACE.equals(payInterface))
//            return ConstantUtils.PRODUCT_TYPE_FEMPAY;
//        return ConstantUtils.PRODUCT_TYPE_FE;
//    }
    
    /**
     * 获取回调平台类型
     * @param payInterface
     * @return
     */
    public static String getPlatformType(String payInterface){
    	Map<String, String> map = getSysConfig(PAYINTERFACE_TO_PLATFORMTYPE);
    	return map.get(payInterface);
    }

//    /**
//     * 获取主扫的订单类型
//     * @param platform
//     * @return
//     */
//    public static OrderType getActiveScanType(PlatformType platform){
//        if (PlatformType.ALIPAY.equals(platform))
//            return OrderType.ALIPAYSCAN;
//        if (PlatformType.WECHAT.equals(platform))
//            return OrderType.ACTIVESCAN;
//        throw new FrontendBizException(ErrorCode.F0001007);
//    }
    
    /**
     * 获取目前使用的H5通道编码
     * @return
     */
    public static String getH5PayInterface(){
    	return getSysConfig(H5_PAYINTERFACE);
    }
    
    /**
     * 获取路由支付场景，供支付时使用
     * @param orderType 订单类型
     * @return
     */
    public static String getSceneType(String orderType){
    	Map<String, String> map = getSysConfig(ORDERTYPE_TO_SCENETYPE);
    	return map.get(orderType);
	}

    /**
     * 获取路由支付场景。仅根据供预路由时使用
     * @param platformType 平台类型
     * @param basicProductCode 基础产品码
     * @return
     */
    public static String getSceneTypeByBasicProductCode(String platformType, String basicProductCode){
        Map<String, String> map = getSysConfig(BASIC_PRODUCT_CODE_TO_SCENETYPE);
        String sceneType = map.get(basicProductCode);
        if(StringUtils.isNotBlank(sceneType)){
            return sceneType;
        }
        if("ZF_QB_H5".equals(basicProductCode)){
            //钱包支付H5，无法直接根据基础产品码映射到支付场景，需根据平台类型再判断
            if(PlatformType.WECHAT.name().equals(platformType)){
                return getSceneType(OrderType.JSAPI.name());
            }else if(PlatformType.ALIPAY.name().equals(platformType)){
                return getSceneType(OrderType.LN.name());
            }
        }
        return null;
    }

    /**
     * 获取支付链接redis缓存时间
     * @return
     */
	public static int getPayUrlCacheTime(){
        Number cacheTime = getSysConfig(PAYURL_REDIS_CACHE_TIME);
        if (cacheTime == null){
            cacheTime = 3600;   //默认缓存时间为1小时
        }
        return cacheTime.intValue();
    }
	
	 /**
     * 获取微信公众号壳账户redis缓存时间
     * @return
     */
	public static int getMaskMerchantNoCacheTime(){
        Number cacheTime = getSysConfig(MASKMERCHANTNO_REDIS_CACHE_TIME);
        if (cacheTime == null){
            cacheTime = 120;   //默认缓存时间为2分钟
        }
        return cacheTime.intValue();
    }


    /**
     * 获取风控门神服务地址
     * @return
     */
    public static String getDoorGodUrl(){
        String url = getSysConfig(DOORGOD_REQUEST_URL);
        if (url == null){//默认返回生产地址
            url = "http://doorgod.core.3g:80/riskcontrol-service/hessian/DoorgodFacade";
        }
        return url;
    }

    /**
     * 获取禁用信用卡支付的商编列表
     * @return
     */
    public static List<String> getLimitCreditCustomerList(){
        return getSysConfig(LIMIT_CREDIT_CUSTOMER_LIST);
    }

    public static boolean isLimitCredit(String customerNum){
        List<String> limitCreditCustomerList = getLimitCreditCustomerList();
        if(limitCreditCustomerList != null && limitCreditCustomerList.contains(customerNum)){
            return true;
        }
        return false;
    }

    /**
     * 是否禁用缓存支付链接开关,默认不禁用
     * @param key
     * @return
     */
    public static boolean isDisablePayCache(String key){
        List<String> value = getSysConfig(PAYURL_CACHE_ENABLE_SWITCH);
//        logger.info("isDisablePayCache value:" + ToStringBuilder.reflectionToString(value) + ",key:" + key);
        if(value == null){
            return false;
        }
//        logger.info("value.contains" + value.contains(key));
        if(value.contains(key)){
            return true;
        }
        return false;
    }

    /**
     * 获取风控接口连接超时配置
     * @return
     */
    public static Integer getRiskServiceConnectTimeout(){
        Map<String, Long> map = getSysConfig(RISK_SERVICE_TIME_OUT_CONFIG);
        if (map == null || map.get(CONN_TIME_OUT) == null) {
            return 2000;
        }
        Long i = map.get(CONN_TIME_OUT);
        return new Integer( i.intValue());
    }
    /**
     * 获取风控接口读超时配置
     * @return
     */
    public static Integer getRiskServiceReadTimeout(){
        Map<String, Long> map = getSysConfig(RISK_SERVICE_TIME_OUT_CONFIG);
        if (map == null || map.get(READ_TIME_OUT) == null) {
            return 2000;
        }
        Long i = map.get(READ_TIME_OUT);
        return new Integer(i.intValue());
    }

    /**
     * 获取REDIS耗时打印日志的阈值
     * @return
     */
    public static String getRedisTimeThreshold(){
        return getSysConfig(REDIS_TIME_THRESHOLD);
    }

    /**
     * 是否打开埋点日志
     * @return
     */
    public static boolean enableSpanLog(){
        String enableSpanLog ="OFF";
        try{
            enableSpanLog = getSysConfig(ENABLE_FE_SPAN_LOG);
        }catch(Exception e){
            logger.warn("获取统一配置信息失败:",e);
        }
        return "ON".equals(enableSpanLog);
    }

    public static String syncRiskControlWhiteList(){
        String whiteList = "10025265136";
        try{
            whiteList = getSysConfig(SYNC_RISK_CONTROL_WHITE_LIST);
        }catch(Exception e){
            logger.warn("syncRiskControlWhiteList() 获取统一配置信息失败:",e);
        }
        return whiteList;
    }
}
