package com.yeepay.g3.core.payprocessor.errorcode;

/**
 *  错误码, 错误信息维护在错误码系统中.
 *  P9开头
 */
public class ErrorCode {

    /**
     * 系统异常
     */
    public static final String P9001000 = "P9001000";

    /**
     * 参数不合法
     */
    public static final String P9001001 = "P9001001";

    /**
     * 非法请求
     */
    public static final String P9001003 = "P9001003";

    /**
     * 支付次数超过限制
     */
    public static final String P9001006 = "P9001006";
    
    /**
     * 订单类型不合法
     */
    public static final String P9001007 = "P9001007";

    /**
     * 风控拦截
     */
    public static final String P9001008 = "P9001008";

    /**
     * 支付次数限制
     */
    public static final String P9001009 = "P9001009";

    
    /**
     * 订单已经成功
     */
    public static final String P9002001 = "P9002001";
    
    /**
     * 支付子表已经成功
     */
    public static final String P9002002 = "P9002002";

    /**
     * 支付子表已失败（即已终态）
     */
    public static final String P9002003 = "P9002003";
    /**
     * 订单不存在
     */
    public static final String P9002004 = "P9002004";

    /**
     * 订单状态不合法
     */
    public static final String P9002005 = "P9002005";


    /**
     * 支付单不存在
     */
    public static final String P9002006 = "P9002006";

    /**
     * 订单已经冲正
     */
    public static final String P9002007 = "P9002007";

    /**
     * 冲正记录不存在
     */
    public static final String P9002008 = "P9002008";
    
    /**
     * 调用无卡下单失败
     */
    public static final String P9003001 = "P9003001";

    /**
     * 调用FE开放支付下单失败
     */
    public static final String P9003002 = "P9003002";

    /**
     * 调用FE网银下单失败
     */
    public static final String P9003003 = "P9003003";

    /**
     * 调用退款中心失败
     */
    public static final String P9003004 = "P9003004";
    
    /**
     * 发送mq消息失败
     */
    public static final String P9003005 = "P9003005";

    /**
     * 调用FE网银查单失败
     */
    public static final String P9003006 = "P9003006";

    /**
     * 调用清算中心退款查询失败
     */
    public static final String P9003007 = "P9003007";
    
    /**
     * 调用FE预路由失败
     */
    public static final String P9003008 = "P9003008";
    
    /**
     * 调用FE分期支付失败
     */
    public static final String P9003009 = "P9003009";

    /**
     * 调用银行卡分期开通并支付失败
     */
    public static final String P9003010 = "P9003010";

    /**
     * 调用银行卡分期下单失败
     */
    public static final String P9003011 = "P9003011";

    /**
     * 调用银行卡分期发短信失败
     */
    public static final String P9003012 = "P9003012";

    /**
     * 调用银行卡分期确认支付失败
     */
    public static final String P9003013 = "P9003013";

    /**
     * 调用银行卡分期同步确认支付失败
     */
    public static final String P9003014 = "P9003014";

    /**
     * 调用预授权下单接口失败
     */
    public static final String P9003021 = "P9003021";

    /**
     * 调用预授权确认接口失败
     */
    public static final String P9003022 = "P9003022";

    /**
     * 调用预授权(完成)撤销接口失败
     */
    public static final String P9003023 = "P9003023";

    /**
     * 调用预授权完成接口失败
     */
    public static final String P9003024 = "P9003024";

    /**
     * 预授权订单，订单类型和订单状态不正确
     */
    public static final String P9003031 = "P9003031";

    /**
     * 预授权，未知的撤销类型
     */
    public static final String P9003032 = "P9003032";

    /**
     * 支付子表已经失败，无需再次更新失败
     */
    public static final String P9003033 = "P9003033";


    /**
     * 更新主表订单类型和订单状态失败
     */
    public static final String P9003034 = "P9003034";

    /**
     * 通知opr暂停预授权完成撤销失败
     */
    public static final String P9003056 = "P9003056";

    /**
     * 通知业务方状态失败，不允许交易
     */
    public static final String P9003057 = "P9003057";

    /**
     * 预授权冲正已成功
     */
    public static final String P9003058 = "P9003058";

    /**
     * 预授权冲正记录已成功
     */
    public static final String P9003059 = "P9003059";

    /**
     * 预授权完成撤销成功次数超限
     */
    public static final String P9003060 = "P9003060";

    
    /**
     * 担保分期预路由失败
     */
    public static final String P8001001 = "P8001001";
    
    /**
     * 担保分期下单失败
     */
    public static final String P8001002 = "P8001002";

    /**
     * 调用个人会员余额支付失败
     */
    public static final String P9004079 = "P9004079";
    
    /**
     * 调用个人会员支付查单失败
     */
    public static final String P9004080 = "P9004080";


    /**
     * 调用营销系统预冻结异常
     */
    public static final String P9003051 = "P9003051";

    /**
     * 调用营销系统支付异常
     */
    public static final String P9003052 = "P9003052";

    /**
     * 第二支付子单已经成功
     */
    public static final String P9003053 = "P9003053";

    /**
     * 第二支付子单已经失败（即为终态）
     */
    public static final String P9003054 = "P9003054";

    /**
     * 第二支付子单不存在
     */
    public static final String P9003055 = "P9003055";

    /**
     * 调ncpay分期易下单失败
     */
    public static final String P9003081 = "P9003081";

    /**
     * 调ncpay分期易发短信失败
     */
    public static final String P9003082 = "P9003082";

    /**
     * 调ncpay分期易确认支付失败
     */
    public static final String P9003083 = "P9003083";

    /**
     * 调ncpay分期易异步确认支付失败
     */
    public static final String P9003084 = "P9003084";
}
