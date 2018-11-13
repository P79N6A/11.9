package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

public enum CombPayOrderType {


//    ACCOUNT("账户支付"),

//    MEMBER_PAY("个人会员支付"),

    MKTG("营销支付");

    /**
     * 描述
     */
    private String desc;

    public String getDesc() {
        return desc;
    }

    CombPayOrderType(String description) {
        this.desc = description;
    }

    public static PayOrderType getOrderType(String orderType) {
        if (StringUtils.isBlank(orderType)) {
            return null;
        }
        try {
            return PayOrderType.valueOf(orderType);
        } catch (Throwable th) {
            return null;
        }
    }
}
