/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;

import java.io.Serializable;

/**
 * 类名称: NcCflEasySmsResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午12:50
 * @version: 1.0.0
 */

public class NcCflEasySmsResponseDTO extends ResponseStatusDTO {

    private static final long serialVersionUID = -4888657266933386414L;

    /**
     * 支付订单号，子表主键
     */
    protected String recordNo;

    /**
     * 支付验证码信息
     */
    private String smsCode;

    /**
     * 验证码实际发送方式
     */
    private SmsSendTypeEnum smsSendType;

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public SmsSendTypeEnum getSmsSendType() {
        return smsSendType;
    }

    public void setSmsSendType(SmsSendTypeEnum smsSendType) {
        this.smsSendType = smsSendType;
    }

    @Override
    public String toString() {
        return "NcCflEasySmsResponseDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", smsCode='" + smsCode + '\'' +
                ", smsSendType=" + smsSendType +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", processStatus=" + processStatus +
                '}';
    }
}