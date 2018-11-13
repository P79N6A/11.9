package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * API收银台，绑卡支付，确认支付返回
 * Created by ruiyang.du on 2017/8/29.
 */
public class ApiBindPayConfirmResponseDTO implements Serializable{

    private static final long serialVersionUID = -4905805594316087821L;
    /**
     * 订单token
     */
    private String token;
    /**
     * 状态码
     */
    private String code;
    /**
     * 信息
     */
    private String message;

    /**
     * 支付记录ID
     */
    private String recordId;

    @Override
    public String toString() {
        return "ApiBindPayConfirmResponseDTO{" +
                "token='" + token + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", recordId='" + recordId + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
