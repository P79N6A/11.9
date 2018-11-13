package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 2016/11/9.
 */
public enum  ReversalStatusEnum {
    SUCCESS("成功"),
    FAILURE("失败");

    private String desc;

    public String getDesc() {
        return desc;
    }

    ReversalStatusEnum(String desc) {
        this.desc = desc;
    }

    public static ReversalStatusEnum getReversalStatus(String status){
        if (StringUtils.isBlank(status)){
            return null;
        }
        try {
            return ReversalStatusEnum.valueOf(status);
        } catch (Throwable th){
            return null;
        }
    }

}
