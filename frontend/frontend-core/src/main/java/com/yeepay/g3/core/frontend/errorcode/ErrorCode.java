package com.yeepay.g3.core.frontend.errorcode;

/**
 *  错误码, 错误信息维护在错误码系统中.
 */
public class ErrorCode {

    /**
     * 系统异常
     */
    public static final String F0001000 = "F0001000";

    /**
     * 参数不合法
     */
    public static final String F0001001 = "F0001001";

    /**
     * 非法请求
     */
    public static final String F0001003 = "F0001003";

    /**
     * 支付次数超过限制
     */
    public static final String F0001006 = "F0001006";
    
    /**
     * 订单类型不合法
     */
    public static final String F0001007 = "F0001007";

    /**
     * 风控拦截
     */
    public static final String F0001008 = "F0001008";
    
    
    /**
     * 订单已经成功
     */
    public static final String F0002001 = "F0002001";

    /**
     * 订单不存在
     */
    public static final String F0002004 = "F0002004";

    /**
     * 订单状态不合法
     */
    public static final String F0002005 = "F0002005";
    
    
    /**
     * 调用银行子系统失败
     */
    public static final String F0003002 = "F0003002";

    /**
     * 调用退款中心失败
     */
    public static final String F0003003 = "F0003003";
    
    /**
     * 发送mq消息失败
     */
    public static final String F0003004 = "F0003004";


    /**
     * 粉丝路由同步数据成功
     */
    public static final String F0004000 = "F0004000";

    /**
     * 粉丝路由 reportId 已存在,但是 customerNum 或者 subCustomerNum 不一致
     */
    public static final String F0004001 = "F0004001";
    /**
     * 粉丝路由业务异常
     */
    public static final String F0004002 = "F0004002";


}
