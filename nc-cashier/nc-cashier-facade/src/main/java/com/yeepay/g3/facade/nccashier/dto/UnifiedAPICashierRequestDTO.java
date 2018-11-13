package com.yeepay.g3.facade.nccashier.dto;



import java.io.Serializable;

/**
 * 统一API收银台，（聚合及被扫支付）请求参数
 * Created by ruiyang.du on 2017/6/28.
 */
public class UnifiedAPICashierRequestDTO implements Serializable{
    private static final long serialVersionUID = 7395035740737287655L;
    /**
     * 支付工具
     */
    private String payTool;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 订单系统token
     */
    private String token;
    /**
     * 用户身份（废弃）
     */
    @Deprecated
    private String userNo;
    /**
     * 用户类型（废弃）
     */
    @Deprecated
    private String userType;
    /**
     * appId，微信公众号支付使用
     */
    private String appId;
    /**
     * openId，微信公众号/支付宝生活号支付使用
     */
    private String openId;

    /**
     * 用于反查订单信息
     */
    private String bizType;
    /**
     * 商编
     */
    private String merchantNo;
    /**
     * 接口版本 
     */
    private String version;
    /**
     * 授权码，用于被扫支付
     */
    private String payEmpowerNo;
    /**
     * 设备号，用于被扫支付
     */
    private String merchantTerminalId;
    /**
     * 门店编码，用于被扫支付
     */
    private String merchantStoreNo;
    /**
     * 用户IP
     */
    private String userIp;

    /**
     * 扩展参数，格式为Map< String,String>的json串，目前包含key：
     * 1，specifyChannelCodes 业务通道编码(用于开放支付)
     */
    private String extParamMap;


    public String getPayTool() {
        return payTool;
    }

    public void setPayTool(String payTool) {
        this.payTool = payTool;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPayEmpowerNo() {
        return payEmpowerNo;
    }

    public void setPayEmpowerNo(String payEmpowerNo) {
        this.payEmpowerNo = payEmpowerNo;
    }

    public String getMerchantTerminalId() {
        return merchantTerminalId;
    }

    public void setMerchantTerminalId(String merchantTerminalId) {
        this.merchantTerminalId = merchantTerminalId;
    }

    public String getMerchantStoreNo() {
        return merchantStoreNo;
    }

    public void setMerchantStoreNo(String merchantStoreNo) {
        this.merchantStoreNo = merchantStoreNo;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    @Override
    public String toString() {
        return "UnifiedAPICashierRequestDTO{" +
                "payTool='" + payTool + '\'' +
                ", payType='" + payType + '\'' +
                ", token='" + token + '\'' +
                ", userNo='" + userNo + '\'' +
                ", userType='" + userType + '\'' +
                ", appId='" + appId + '\'' +
                ", openId='" + openId + '\'' +
                ", bizType='" + bizType + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", version='" + version + '\'' +
                ", payEmpowerNo='" + payEmpowerNo + '\'' +
                ", merchantTerminalId='" + merchantTerminalId + '\'' +
                ", merchantStoreNo='" + merchantStoreNo + '\'' +
                ", userIp='" + userIp + '\'' +
                ", extParamMap='" + extParamMap + '\'' +
                '}';
    }

    public String getExtParamMap() {
        return extParamMap;
    }

    public void setExtParamMap(String extParamMap) {
        this.extParamMap = extParamMap;
    }
}
