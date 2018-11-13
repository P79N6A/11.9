package com.yeepay.g3.facade.frontend.dto;

import java.io.Serializable;

/**
 * 响应的基础类
 */
public class ResponseDTO implements Serializable{

    private static final long serialVersionUID = 7943378856510528337L;
    /**
     * 错误码
     */
    protected String responseCode;

    /**
     * 错误码描述
     */
    protected String responseMsg;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
