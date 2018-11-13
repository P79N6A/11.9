package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * API收银台，绑卡支付，请求下单并获取卡补充项返回结果
 * Created by ruiyang.du on 2017/8/25.
 */
public class ApiBindPayPaymentResponseDTO implements Serializable{
    private static final long serialVersionUID = -1264667179839323326L;
    /**
     * 订单token，同入参返回
     */
    private String token;
    /**
     * 绑卡id，同入参返回
     */
    private String bindId;
    /**
     * 商编，同入参返回
     */
    private String merchantNo;
    /**
     * 状态码
     */
    private String code;
    /**
     * 信息
     */
    private String message;
    /**
     * 需补充项名称的集合，以","分隔
     */
    private String needItems;

    /**
     * 验证码类型(短信验证码/语音验证码/无需验证码) : SMS/VOICE/NONE
     */
    private String verifyCodeType;

    /**
     * 提交补充项场景(请求验证码时/确认支付时/无需补充) ：REQUEST_VERIFY/CONFIRM_PAY/NONE
     */
    private String supplyNeedItemScene;

    /**
     * 支付记录ID
     */
    private String recordId;

    @Override
    public String toString() {
        return "ApiBindPayPaymentResponseDTO{" +
                "token='" + token + '\'' +
                ", bindId='" + bindId + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", needItems='" + needItems + '\'' +
                ", verifyCodeType='" + verifyCodeType + '\'' +
                ", supplyNeedItemScene='" + supplyNeedItemScene + '\'' +
                ", recordId='" + recordId + '\'' +
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

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNeedItems() {
        return needItems;
    }

    public void setNeedItems(String needItems) {
        this.needItems = needItems;
    }

    public String getVerifyCodeType() {
        return verifyCodeType;
    }

    public void setVerifyCodeType(String verifyCodeType) {
        this.verifyCodeType = verifyCodeType;
    }

    public String getSupplyNeedItemScene() {
        return supplyNeedItemScene;
    }

    public void setSupplyNeedItemScene(String supplyNeedItemScene) {
        this.supplyNeedItemScene = supplyNeedItemScene;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
