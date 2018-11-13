package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

public enum CombTrxStatusEnum {

    DOING("处理中"),

    /**
     * 第二支付预冻结（营销系统有）
     */
    DEPOSIT("预冻结"),

    /**
     * 第二支付成功
     */
    SUCCESS("支付成功"),
    /**
     * 第二支付失败
     */
    FAILURE("支付失败"),
    /**
     * 第二支付冲正
     */
    REVERSE("冲正");

    private String desc;

    CombTrxStatusEnum(String desc) {
        this.desc = desc;
    }

    public static TrxStatusEnum getTrxStatus(String trxStatus) {
        if (StringUtils.isBlank(trxStatus)) {
            return null;
        }
        try {
            return TrxStatusEnum.valueOf(trxStatus);
        } catch (Throwable th) {
            return null;
        }
    }

    public String getDesc() {
        return desc;
    }
}
