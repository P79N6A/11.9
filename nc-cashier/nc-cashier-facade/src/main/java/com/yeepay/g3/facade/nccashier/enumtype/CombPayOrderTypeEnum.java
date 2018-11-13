package com.yeepay.g3.facade.nccashier.enumtype;

/**
 * @program: nc-cashier-parent
 * @description: 组合支付  第二支付类型
 * @author: jimin.zhou
 * @create: 2018-06-25 16:03
 **/
public enum CombPayOrderTypeEnum {

    MKTG("营销立减"),
    ACCOUNT("账户支付");
    private String desc;


    CombPayOrderTypeEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }


}
