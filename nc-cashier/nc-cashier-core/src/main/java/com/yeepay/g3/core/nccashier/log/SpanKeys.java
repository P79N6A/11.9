package com.yeepay.g3.core.nccashier.log;

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
     * 订单token
     */
    public final static String ORDER_TOKEN = "token";

    /**
     * 商户编号
     */
    public final static String MERCHANT_NO = "merchantNo";

    /**
     * 商户订单号
     */
    public final static String MERCHANT_ORDER_NO = "merchantOrderNo";
    
    /**
     * 交易系统编码
     */
    public final static String BIZ_SYS = "bizSys";
    
    /**
     * 支付方式
     */
    public final static String PAY_TOOL = "payTool";
    
    /**
     * 平台
     */
    public final static String PLATFORM = "platform";
    
    public final static String STATUS = "status";

    public final static String PAY_TYPE = "payType";

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
