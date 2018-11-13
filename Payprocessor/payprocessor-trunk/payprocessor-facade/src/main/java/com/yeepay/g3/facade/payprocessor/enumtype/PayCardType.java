package com.yeepay.g3.facade.payprocessor.enumtype;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
public enum PayCardType {
    CFT("微信零钱"),
    DEBIT("借记卡"),
    CREDIT("贷记卡");

    private String desc;

    PayCardType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static PayCardType getPayCardType(String name){
        if (name == null) {
            return null;
        }
        try {
            return PayCardType.valueOf(name);
        } catch (Throwable th){
            return null;
        }
    }
}
