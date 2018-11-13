/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

/**
 * 类名称: NcPayCflOrderResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/9/26 上午11:18
 * @version: 1.0.0
 */

public class NcPayCflOrderResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = -6111767649565711967L;

    @Override
    public String toString() {
        return "NcPayCflOrderResponseDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", dealUniqueSerialNo='" + dealUniqueSerialNo + '\'' +
                ", payOrderType=" + payOrderType +
                ", customerNumber='" + customerNumber + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", processStatus=" + processStatus +
                '}';
    }
}