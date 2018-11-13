package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.enumtype
 *
 * @author pengfei.chen
 * @since 17/3/10 10:44
 */
public enum FeeTypeEnum {
    /**
     * 用户手续费
     */
    USER_FEE("FEE_BY_PAYER"),

    /**
     * 商户手续费
     */
    MERCHANT_FEE("FEE_BY_RECEIVER");

    private String value;
    FeeTypeEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
