/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.util;

/**
 * 类名称: ValidTypeAndStatusPair <br>
 * 类描述: <br>
 *  预授权，用于校验主单订单类型和订单状态
 * @author: zhijun.wang
 * @since: 17/12/23 下午10:11
 * @version: 1.0.0
 */

public class ValidTypeAndStatusPair {

    /**
     * 订单类型
     */
    private String payType;

    /**
     * 订单状态
     */
    private String status;


    public ValidTypeAndStatusPair(String payType, String status) {
        this.payType = payType;
        this.status = status;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}