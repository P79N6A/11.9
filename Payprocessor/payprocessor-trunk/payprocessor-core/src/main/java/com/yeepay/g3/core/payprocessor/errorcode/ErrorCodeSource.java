package com.yeepay.g3.core.payprocessor.errorcode;

/**
 * 错误码来源
 */
public enum ErrorCodeSource {
    PP("PP"),
    NCCASHIER("NCCASHIER"),
	FRONTEND("FRONTEND"),
	NCPAY("NCPAY"),
    DOORGOD("doorgod"),
    ACCOUNTPAY("ACP"),
    MEMBER("MB"),
    MKTG("MKTG");

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
