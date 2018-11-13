/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import java.util.Map;

/**
 * 类名称: NcPayCflOpenResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/9/26 下午1:53
 * @version: 1.0.0
 */

public class NcPayCflOpenResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = -6378528451565688462L;

    /**
     * 支付跳转链接
     */
    private String payUrl;

    /**
     * 提交银行页面编码
     */
    private String charset;

    /**
     * 提交银行方法
     */
    private String method;

    /**
     * 提交银行参数
     */
    private Map<String, Object> paramMap;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public String toString() {
        return "NcPayCflOpenResponseDTO{" +
                "payUrl='" + payUrl + '\'' +
                ", charset='" + charset + '\'' +
                ", method='" + method + '\'' +
                ", paramMap=" + paramMap +
                ", recordNo='" + recordNo + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", dealUniqueSerialNo='" + dealUniqueSerialNo + '\'' +
                ", payOrderType=" + payOrderType +
                ", customerNumber='" + customerNumber + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", processStatus=" + processStatus +
                '}';
    }
}