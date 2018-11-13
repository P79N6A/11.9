/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.nccashier.dto;

import java.math.BigDecimal;

/**
 * 类名称: APICflEasyPaymentResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午12:00
 * @version: 1.0.0
 */

public class APICflEasyPaymentResponseDTO extends APIBasicResponseDTO {

    private static final long serialVersionUID = -5063628837793859836L;

    /**
     * 验证码类型(SMS/VOICE/NONE)
     */
    private String verifyCodeType;
    /**
     * 提交补充项场景(REQUEST_VERIFY/CONFIRM_PAY/NONE)
     */
    private String needItemScene;
    /**
     * 需补充项名称的集合，以","分隔
     */
    private String needItems;

    /**
     * 持卡人手续费
     */
    private BigDecimal payerRate;


    public APICflEasyPaymentResponseDTO() {
    }

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

    public String getNeedItems() {
        return needItems;
    }

    public void setNeedItems(String needItems) {
        this.needItems = needItems;
    }

    public BigDecimal getPayerRate() {
        return payerRate;
    }

    public void setPayerRate(BigDecimal payerRate) {
        this.payerRate = payerRate;
    }

    @Override
    public String toString() {
        return "APICflEasyPaymentResponseDTO{" +
                "'" + super.toString() + '\'' +
                ", verifyCodeType='" + verifyCodeType + '\'' +
                ", needItemScene='" + needItemScene + '\'' +
                ", needItems='" + needItems + '\'' +
                ", payerRate='" + payerRate + '\'' +
                '}';
    }
}