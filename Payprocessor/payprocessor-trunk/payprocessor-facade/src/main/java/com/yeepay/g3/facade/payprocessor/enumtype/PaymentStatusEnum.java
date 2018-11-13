package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public enum PaymentStatusEnum {
	
    DOING("初始化"),
    SUCCESS("支付成功"),
    REVERSE("冲正"),
    FAILURE("失败");

    private String desc;

    PaymentStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static PaymentStatusEnum getPayStatus(String payStatus){
        if (StringUtils.isBlank(payStatus))
            return null;
        try {
            return PaymentStatusEnum.valueOf(payStatus);
        } catch (Throwable th){
            return null;
        }
    }
}
