/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;

import java.io.Serializable;

/**
 * 类名称: NcCflEasyConfirmResponseDTO <br>
 * 类描述: <br>
 * 分期易异步确认支付返回参数
 * @author: zhijun.wang
 * @since: 18/10/15 下午12:05
 * @version: 1.0.0
 */

public class NcCflEasyConfirmResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = 4506662523496102628L;

    /**
     * 短信校验结果
     */
    private SmsCheckResultEnum smsStatus;

    public SmsCheckResultEnum getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(SmsCheckResultEnum smsStatus) {
        this.smsStatus = smsStatus;
    }

    @Override
    public String toString() {
        return "NcCflEasyConfirmResponseDTO{" +
                "smsStatus=" + smsStatus +
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