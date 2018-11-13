package com.yeepay.g3.core.payprocessor.enumtype;

/**
 * @author chronos.
 * @createDate 2016/10/13.
 */
public enum  ResultType {
    COUNT("计数"),
    SUM("汇总");

    private String desc;

    public String getDesc() {
        return desc;
    }

    ResultType(String desc){
        this.desc = desc;
    }
}
