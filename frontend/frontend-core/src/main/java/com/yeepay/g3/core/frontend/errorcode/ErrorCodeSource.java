package com.yeepay.g3.core.frontend.errorcode;

/**
 * 错误码来源
 */
public enum ErrorCodeSource {
	FRONTEND("FRONTEND"),
	MPAY("MPAY"),
	BANKINTERFACE("bankcommit"),
	BANKROUTER("bankrouter"),
    DOORGOD("doorgod");

    /**
     * 系统编码
     */
    private final String sysCode;

    public String getSysCode() {
        return sysCode;
    }

    /**
     * 构造方法
     * @param sysCode
     */
    ErrorCodeSource(String sysCode) {
        this.sysCode = sysCode;
    }
}
