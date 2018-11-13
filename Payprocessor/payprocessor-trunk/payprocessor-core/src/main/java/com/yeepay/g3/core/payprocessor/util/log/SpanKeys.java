package com.yeepay.g3.core.payprocessor.util.log;

/**
 * 鉴权中心埋点的关键字段
 * 
 * @author：tao.liu    
 * @since：2018年3月14日 下午3:27:47 
 * @version:
 */
public class SpanKeys {

    /**
     * 埋点类型
     */
    public final static String EVENT = "event";

    /**
     * 指标
     */
    public final static String METRICS = "metrics";
    /**
     * 商户编号
     */
    public final static String MERCHANT_NO = "merchantNo";

    /**
     * 商户订单号
     */
    public final static String MERCHANT_ORDER_NO = "merchantOrderNo";

    public final static String ORDER_TYPE = "orderType";

    public final static String PLATFORM_TYPE = "platFormType";

    /**
     * 场景，主要用于区分主被扫
     */
    public final static String PAY_TOOL = "payTool";

    /**
     * 交易系统编码
     */
    public final static String BIZ_SYS = "bizSys";

    /**
     * WECHAT,ALIPAY,QQ等等
     */
    public final static String PLAT_FORM = "platform";

    public final static String STATUS = "status";

    public final static String RESPONSE_CODE = "responseCode";

    public final static String RESPONSE_MSG = "responseMsg";

    
    
}
