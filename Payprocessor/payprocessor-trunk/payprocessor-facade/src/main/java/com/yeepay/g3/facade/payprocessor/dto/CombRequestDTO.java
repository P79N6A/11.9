/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.utils.IsInStringArrayPermitNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类名称: CombRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/14 下午3:24
 * @version: 1.0.0
 */

public class CombRequestDTO implements Serializable {


    private static final long serialVersionUID = 8515874665915838006L;

    /**
     * 组合支付类型
     */
    @IsInStringArrayPermitNull(permitValue = {"MKTG"}, message = "不支持的组合支付类型")
    private String payOrderType;

    /**
     * 组合支付订单号
     */
    private String marketingNo;

    /**
     * 营销系统二级产品类型，收银台透传pp，pp传营销系统
     */
    private String paymentType;


    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public String getMarketingNo() {
        return marketingNo;
    }

    public void setMarketingNo(String marketingNo) {
        this.marketingNo = marketingNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "CombRequestDTO{" +
                "payOrderType='" + payOrderType + '\'' +
                ", marketingNo='" + marketingNo + '\'' +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}