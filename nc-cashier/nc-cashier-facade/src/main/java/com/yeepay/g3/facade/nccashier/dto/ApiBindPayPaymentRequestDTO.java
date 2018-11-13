package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

import java.io.Serializable;

/**
 * API收银台，绑卡支付，请求下单并获取卡补充项入参
 * Created by ruiyang.du on 2017/8/25.
 */
public class ApiBindPayPaymentRequestDTO implements Serializable{
    private static final long serialVersionUID = 3966615611619295278L;
    /**
     * 订单token
     */
    private String token;
    /**
     * 绑卡id
     */
    private String bindId;
    /**
     * 用户标识号
     */
    private String userNo;
    /**
     * 用户标识类型
     */
    private String userType;
    /**
     * 用户IP
     */
    private String userIp;
    /**
     * 接口版本号
     */
    private String version;

    /**
     * 商编
     */
    private String merchantNo;

    /**
     * 钱包所属商编
     */
    private String payMerchantNo;

    /**
     * 用于反查订单
     */
    private String bizType;

    @Override
    public String toString() {
        return "ApiBindPayPaymentRequestDTO{" +
                "token='" + token + '\'' +
                ", bindId='" + bindId + '\'' +
                ", userNo='" + HiddenCode.hiddenIdentityId(userNo) + '\'' +
                ", userType='" + userType + '\'' +
                ", userIp='" + userIp + '\'' +
                ", version='" + version + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", payMerchantNo='" + payMerchantNo + '\'' +
                ", bizType='" + bizType + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getPayMerchantNo() {
        return payMerchantNo;
    }

    public void setPayMerchantNo(String payMerchantNo) {
        this.payMerchantNo = payMerchantNo;
    }
}
