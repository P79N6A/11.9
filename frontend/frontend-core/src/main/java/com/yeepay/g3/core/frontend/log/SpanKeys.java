package com.yeepay.g3.core.frontend.log;

/**
 * fe埋点的关键字段
 *
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


    /**
     * SCANPAY/MSCANPAY等
     */
    public final static String PAY_TOOL = "payTool";

    /**
     * WECHAT/ALIPAY等
     */
    public final static String PLATFORM = "platform";

    /**
     * 支付状态
     */
    public final static String STATUS = "status";
    /**
     * 返回码
     */
    public final static String RESPONSE_CODE = "responseCode";

    /**
     * 返回信息描述
     */
    public final static String RESPONSE_MSG = "responseMsg";

    /**
     * 耗时时间
     */
    public final static String TOTAL_TIME = "totalTime";
    
    
}
