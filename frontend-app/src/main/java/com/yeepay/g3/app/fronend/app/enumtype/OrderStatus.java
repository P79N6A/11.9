package com.yeepay.g3.app.fronend.app.enumtype;

public enum OrderStatus {
    /**
     * 初始化
     */
    INIT,
    /**
     * 结算
     */
    SUCCESS,
    /**
     * 关闭
     */
    CLOSE,
    /**
     * 撤销
     */
    CANCEL,
    /**
     * 待结算
     */
    TOSETTLE,
    /**
     * 退款
     */
    REFUND
}
