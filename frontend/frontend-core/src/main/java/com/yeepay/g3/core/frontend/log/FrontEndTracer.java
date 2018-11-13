package com.yeepay.g3.core.frontend.log;


import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 发送fe监控信息集中管理类
 */
public class FrontEndTracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndTracer.class);


    /**
     * 通道成功率分母：fe请求路由
     */
    public static void frontendRequestRouterSpan(String merchantNo,String merchantOrderNo,  String platform, String payTool) {

        try {
            FETracerUtils.span(SpanKeys.EVENT, "fe_fusionpay_channel",
                    SpanKeys.METRICS, "fusionpay_channel_request",
                    SpanKeys.MERCHANT_NO, merchantNo,
                    SpanKeys.MERCHANT_ORDER_NO, merchantOrderNo,
                    SpanKeys.PLATFORM, platform,
                    SpanKeys.PAY_TOOL, payTool
            );
        } catch (Exception e) {
            LOGGER.error("发送 frontendRequestRouterSpan 监控信息异常", e);
        }

    }

    /**
     * 通道成功率分子：fe接收到银子成功回调、fe获取路由返回支付结果(仅被扫免密)
     */
    public static void frontendOrderCompleteSpan(String merchantNo, String merchantOrderNo, String platform, String payTool, String status, String msg, String code) {

        try {
            FETracerUtils.span(SpanKeys.EVENT, "fe_fusionpay_channel",
                    SpanKeys.METRICS, "fusionpay_channel_complete",
                    SpanKeys.MERCHANT_NO, merchantNo,
                    SpanKeys.MERCHANT_ORDER_NO, merchantOrderNo,
                    SpanKeys.PLATFORM, platform,
                    SpanKeys.PAY_TOOL, payTool,
                    SpanKeys.STATUS, status,
                    SpanKeys.RESPONSE_MSG, msg,
                    SpanKeys.RESPONSE_CODE, code);
        } catch (Exception e) {
            LOGGER.error("发送 frontendOrderCompleteSpan 监控信息异常", e);
        }

    }


}
