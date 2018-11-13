package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.vo
 *
 * @author pengfei.chen
 * @since 17/4/12 16:26
 */
public class OrderSysConfigDTO implements Serializable{
    /**
     * 区分反查订单信息应该调用哪个接口(目前三个,DS,G2NET,STANDARD)
     */
        private String accessCode;

    /**
     * STANDARD接口的不同的url
     */
    private String orderRequestUrl;

    /**
     * STANDARD接口的不同的协议
     */
    private String remoteServiceProtocol;

    /**
     * 风控校验的reffer的三种情况：NONE(不调用风控),NOT_NONE(从订单中获取reffer但是不配置白名单),NOT_NONE_AND_WHITE(调用并且配置白名单),
     */
    private String referFromOrder;

    /**
     * 错误码系统编码
     */
    private String errorCodeSysCode;
    
    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getOrderRequestUrl() {
        return orderRequestUrl;
    }

    public void setOrderRequestUrl(String orderRequestUrl) {
        this.orderRequestUrl = orderRequestUrl;
    }

    public String getRemoteServiceProtocol() {
        return remoteServiceProtocol;
    }

    public void setRemoteServiceProtocol(String remoteServiceProtocol) {
        this.remoteServiceProtocol = remoteServiceProtocol;
    }

    public String getReferFromOrder() {
        return referFromOrder;
    }

    public void setReferFromOrder(String referFromOrder) {
        this.referFromOrder = referFromOrder;
    }

    public String getErrorCodeSysCode() {
        return errorCodeSysCode;
    }

    public void setErrorCodeSysCode(String errorCodeSysCode) {
        this.errorCodeSysCode = errorCodeSysCode;
    }
}
