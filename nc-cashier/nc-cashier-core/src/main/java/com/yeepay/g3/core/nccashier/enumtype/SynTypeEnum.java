package com.yeepay.g3.core.nccashier.enumtype;/**
 * @program: nc-cashier-parent
 * @description: 同步 异步  枚举
 * @author: jimin.zhou
 * @create: 2018-10-19 14:28
 **/

/**
 *
 * @description: 同步 异步  枚举
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-19 14:28
 **/


public enum SynTypeEnum {


    SYN("同步"),

    ASYN("异步");


    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    SynTypeEnum(String desc){
        this.desc = desc;
    }

}
