/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;

import java.math.BigDecimal;

/**
 * 类名称: NcCflEasyResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午12:39
 * @version: 1.0.0
 */

public class NcCflEasyResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = 6241805876860170221L;

    /**
     * 短信发送方式(不发、易宝发、银行发[银行生成验证码])
     */
    private SmsSendTypeEnum smsType;

    /**
     * 需要补充的卡信息项
     */
    private int needItem;

    private BigDecimal payerInterestRate;

    public SmsSendTypeEnum getSmsType() {
        return smsType;
    }

    public void setSmsType(SmsSendTypeEnum smsType) {
        this.smsType = smsType;
    }

    public int getNeedItem() {
        return needItem;
    }

    public void setNeedItem(int needItem) {
        this.needItem = needItem;
    }

    public BigDecimal getPayerInterestRate() {
        return payerInterestRate;
    }

    public void setPayerInterestRate(BigDecimal payerInterestRate) {
        this.payerInterestRate = payerInterestRate;
    }

    @Override
    public String toString() {
        return "NcCflEasyResponseDTO{" +
                "smsType=" + smsType +
                ", needItem=" + needItem +
                ", payerInterestRate=" + payerInterestRate +
                ", recordNo='" + recordNo + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", dealUniqueSerialNo='" + dealUniqueSerialNo + '\'' +
                ", payOrderType=" + payOrderType +
                ", customerNumber='" + customerNumber + '\'' +
                ", combResponseDTO=" + combResponseDTO +
                ", firstPayAmount=" + firstPayAmount +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", processStatus=" + processStatus +
                '}';
    }
}