package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * API收银台一键支付，首次支付，请求下单返回
 */
public class APIYJZFFirstPaymentResponseDTO extends APIYJZFPaymentResponseDTO implements Serializable {
    private static final long serialVersionUID = -1275378189065759990L;

    /** 是否需补充取款密码 */
    private boolean needBankPWD;

    public boolean isNeedBankPWD() {
        return needBankPWD;
    }

    public void setNeedBankPWD(boolean needBankPWD) {
        this.needBankPWD = needBankPWD;
    }


    @Override
    public String toString() {
        return "APIYJZFFirstPaymentResponseDTO{" +
                "'" + super.toString() + '\'' +
                ", needBankPWD='" + needBankPWD + '\'' +
                '}';
    }
}
