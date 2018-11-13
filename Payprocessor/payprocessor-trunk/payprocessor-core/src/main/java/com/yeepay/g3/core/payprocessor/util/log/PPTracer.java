package com.yeepay.g3.core.payprocessor.util.log;


import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 发送pp监控信息集中管理类
 */
public class PPTracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PPTracer.class);


    /**
     * 发送聚合支付请求信息
     */
    public static void paymentSuccessRateResponseSpan(PaymentRequest paymentRequest, PayRecord payRecord) {
        try {
            // 目前只配置收银台前置API的
            if(!"SY_QZ".equals(payRecord.getCashierVersion())) {
                return;
            }
            // 只打印聚合的
            if(!(PayOrderType.PASSIVESCAN.name().equals(payRecord.getPayOrderType())
                || PayOrderType.ACTIVESCAN.name().equals(payRecord.getPayOrderType())
                || PayOrderType.JSAPI.name().equals(payRecord.getPayOrderType())
                || PayOrderType.H5APP.name().equals(payRecord.getPayOrderType())
                || PayOrderType.LN.name().equals(payRecord.getPayOrderType())
                || PayOrderType.MINI_PROGRAM.name().equals(payRecord.getPayOrderType()))) {
                return;
            }

            NcTracerUtils.span(SpanKeys.EVENT, "apiFusionPaySuccessRate",
                    SpanKeys.MERCHANT_NO, paymentRequest.getCustomerNo(),
                    SpanKeys.MERCHANT_ORDER_NO, paymentRequest.getOutTradeNo(),
                    SpanKeys.BIZ_SYS, paymentRequest.getOrderSystem(),
                    SpanKeys.PAY_TOOL, payRecord.getPayOrderType(),
                    SpanKeys.PLATFORM_TYPE, payRecord.getPlatformType(),
                    SpanKeys.STATUS, "1",
                    SpanKeys.RESPONSE_CODE, null,
                    SpanKeys.RESPONSE_MSG, null);
        } catch (Exception e) {
            LOGGER.error("发送 apiFusionPaySuccessRate 监控信息异常", e);
        }
    }

}
