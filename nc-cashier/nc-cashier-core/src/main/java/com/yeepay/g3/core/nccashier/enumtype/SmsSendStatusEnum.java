package com.yeepay.g3.core.nccashier.enumtype;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.enumtype
 *
 * @author pengfei.chen
 * @since 17/1/6 13:01
 */
public enum SmsSendStatusEnum {

    SEND_SUCCESS("已发送"),
    SEND_FAILED("发送失败"),
    SMS_CONFIRM_SUCCESS("确认成功"),
    SMS_CONFIRM_FAILED("确认失败");


    private final String name;

    /**
     * 构造函数
     *
     * @param name
     */
    SmsSendStatusEnum(String name) {
        this.name = name;
    }
    }
