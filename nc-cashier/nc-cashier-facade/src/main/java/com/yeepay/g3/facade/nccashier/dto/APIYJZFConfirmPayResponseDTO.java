package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * API收银台一键支付，确认支付返回
 */
public class APIYJZFConfirmPayResponseDTO extends APIBasicResponseDTO implements Serializable {

    private static final long serialVersionUID = 6490129277500278272L;

    /** 绑卡id，首次支付支付成功且绑卡，则返回 */
    private String bindId;

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    @Override
    public String toString() {
        return "APIYJZFConfirmPayResponseDTO{" +
                "'" + super.toString() + '\'' +
                ", bindId='" + bindId + '\'' +
                '}';
    }
}
