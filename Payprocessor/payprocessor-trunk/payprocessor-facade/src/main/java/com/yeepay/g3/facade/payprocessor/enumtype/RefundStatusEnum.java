package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public enum RefundStatusEnum {
    INIT("已接收"),
    CSDOING("清算中心处理中"),
    CSDONE("清算中心处理成功"),
    RFDOING("退款中心处理中"),
    DONE("退款成功");

    private String desc;

    RefundStatusEnum(String desc) {
        this.desc = desc;
    }

    public static RefundStatusEnum getRefundStatus(String refundStatus){
        if (StringUtils.isBlank(refundStatus))
            return null;
        try {
            return RefundStatusEnum.valueOf(refundStatus);
        } catch (Throwable th){
            return null;
        }
    }

    public String getDesc() {
        return desc;
    }
}
