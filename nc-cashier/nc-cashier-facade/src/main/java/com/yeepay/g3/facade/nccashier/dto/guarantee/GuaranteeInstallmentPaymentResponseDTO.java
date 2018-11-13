package com.yeepay.g3.facade.nccashier.dto.guarantee;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;

import java.io.Serializable;
import java.util.Map;

/**
 * 担保分期-支付下单-返回DTO
 */
public class GuaranteeInstallmentPaymentResponseDTO extends BasicResponseDTO implements Serializable {
    private static final long serialVersionUID = -8683846944770708157L;
    /**
     * 支付跳转地址
     */
    private String payUrl;
    /**
     * 跳转请求方法
     */
    private String method;
    /**
     * 跳转请求编码
     */
    private String encoding;
    /**
     * 跳转请求参数
     */
    private Map<String, Object> paramMap;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public String toString() {
        return "GuaranteeInstallmentPaymentResponseDTO{" +
                super.toString()+
                ", payUrl='" + payUrl + '\'' +
                ", method='" + method + '\'' +
                ", encoding='" + encoding + '\'' +
                ", paramMap='" + paramMap + '\'' +
                '}';
    }
}
