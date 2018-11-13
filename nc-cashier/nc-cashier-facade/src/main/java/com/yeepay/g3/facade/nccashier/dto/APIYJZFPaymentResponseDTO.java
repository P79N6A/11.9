package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * API收银台一键支付，请求下单返回
 */
public class APIYJZFPaymentResponseDTO extends APIBasicResponseDTO implements Serializable {

    private static final long serialVersionUID = -585441899428030222L;
    /** 验证码类型(SMS/VOICE/NONE) */
    private String verifyCodeType;
    /** 提交补充项场景(REQUEST_VERIFY/CONFIRM_PAY/NONE) */
    private String needItemScene;

    public String getVerifyCodeType() {
        return verifyCodeType;
    }

    public void setVerifyCodeType(String verifyCodeType) {
        this.verifyCodeType = verifyCodeType;
    }

    public String getNeedItemScene() {
        return needItemScene;
    }

    public void setNeedItemScene(String needItemScene) {
        this.needItemScene = needItemScene;
    }

    @Override
    public String toString() {
        return "APIYJZFPaymentResponseDTO{" +
                "'" + super.toString() + '\'' +
                ", verifyCodeType='" + verifyCodeType + '\'' +
                ", needItemScene='" + needItemScene + '\'' +
                '}';
    }
}
