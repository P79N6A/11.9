package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * API收银台一键支付，二次支付，请求下单返回
 */
public class APIYJZFBindPaymentResponseDTO extends APIYJZFPaymentResponseDTO implements Serializable {

    private static final long serialVersionUID = 7461892652930656086L;

    /** 需补充项名称的集合，以","分隔 */
    private String needItems;

    public String getNeedItems() {
        return needItems;
    }

    public void setNeedItems(String needItems) {
        this.needItems = needItems;
    }

    @Override
    public String toString() {
        return "APIYJZFBindPaymentResponseDTO{" +
                "'" + super.toString() + '\'' +
                ", needItems='" + needItems + '\'' +
                '}';
    }
}
