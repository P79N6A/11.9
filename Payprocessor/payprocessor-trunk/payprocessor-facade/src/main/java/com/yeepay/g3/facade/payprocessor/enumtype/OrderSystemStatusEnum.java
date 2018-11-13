package com.yeepay.g3.facade.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * 订单方状态
 * @author chronos.
 * @createDate 2016/11/8.
 */
public enum OrderSystemStatusEnum {
    /**
     * 初始化状态为处理中
     */
    DOING("处理中"),
    /**
     * 订单处理器返回成功
     */
    SUCCESS("成功"),
    /**
     * 订单处理器明确返回失败、取消等状时发起冲正
     */
    REVERSE("冲正"),

    /**
     * 无需通知订单方
     */
    NONE("无需通知");

    private String desc;

    OrderSystemStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderSystemStatusEnum getStatus(String noticeStatus){
        if (StringUtils.isBlank(noticeStatus)){
            return null;
        }
        try {
            return OrderSystemStatusEnum.valueOf(noticeStatus);
        } catch (Throwable th){
            return null;
        }
    }
}
