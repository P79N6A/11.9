package com.yeepay.g3.facade.nccashier.enumtype;

public enum RedirectTypeEnum {
    /**
     * 支付跳转
     */
    PAY("PAY"),

    /**
     * 签约跳转
     */
    SIGN("SIGN"),

    /**
     * 不跳转
     */
    NONE("NONE");

    private String value;
    RedirectTypeEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
