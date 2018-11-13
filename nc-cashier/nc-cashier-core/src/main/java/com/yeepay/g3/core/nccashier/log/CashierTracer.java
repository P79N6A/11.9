package com.yeepay.g3.core.nccashier.log;


import com.yeepay.g3.core.nccashier.vo.MonitorLogSpanInfo;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 发送收银台监控信息集中管理类
 */
public class CashierTracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashierTracer.class);


    /**
     * 发送收银台请求信息
     * 支付成功率：apiPaymentSuccessRate
     * 
merchantNo
merchantOrderNo
bizSys
payTool : 区分主扫、被扫
platform：区分支付宝、微信
status:0
responseCode
responseMsg
     */
	public static void apiFusionPayRequestSpan(MonitorLogSpanInfo monitorLogSpanInfo) {
		try {
			NcTracerUtils.span(SpanKeys.EVENT, monitorLogSpanInfo.getEvent(),
					// SpanKeys.METRICS, "fusionpay_request",
					SpanKeys.MERCHANT_NO, monitorLogSpanInfo.getMerchantNo(), 
					SpanKeys.MERCHANT_ORDER_NO, monitorLogSpanInfo.getMerchantOrderNo(), 
					SpanKeys.BIZ_SYS, monitorLogSpanInfo.getBizSys(),
					SpanKeys.STATUS, monitorLogSpanInfo.getStatus(), 
					SpanKeys.PAY_TOOL, monitorLogSpanInfo.getPayTool(),
					SpanKeys.PLATFORM, monitorLogSpanInfo.getPlatform(), 
					SpanKeys.RESPONSE_CODE, monitorLogSpanInfo.getResponseCode(), 
					SpanKeys.RESPONSE_MSG, monitorLogSpanInfo.getResponseMsg());
		} catch (Exception e) {
			LOGGER.error("发送 apiFusionPayRequestSpan 监控信息异常", e);
		}

	}

    /**
     * 发送收银台接口响应信息
     */
    public static void apiFusionPayResponseSpan(String token, String merchantNo, String payTool, String payType, String msg, String code, long startTime) {

        try {
            NcTracerUtils.span(SpanKeys.EVENT, "api_cashier_fusionpay",
                    SpanKeys.METRICS, "fusionpay_response",
                    SpanKeys.ORDER_TOKEN, token,
                    SpanKeys.MERCHANT_NO, merchantNo,
                    SpanKeys.PAY_TOOL, payTool,
                    SpanKeys.PAY_TYPE, payType,
                    SpanKeys.RESPONSE_MSG, msg,
                    SpanKeys.RESPONSE_CODE, code,
                    SpanKeys.TOTAL_TIME, Long.toString(System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            LOGGER.error("发送 apiFusionPayResponseSpan 监控信息异常", e);
        }

    }


}
