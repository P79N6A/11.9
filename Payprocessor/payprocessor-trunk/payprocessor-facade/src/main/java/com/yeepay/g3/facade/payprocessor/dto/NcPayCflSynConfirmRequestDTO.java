/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 类名称: NcPayCflSynConfirmRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/1/10 下午6:10
 * @version: 1.0.0
 */

public class NcPayCflSynConfirmRequestDTO implements Serializable {

    private static final long serialVersionUID = -8392268644924589664L;

    /**
     * 支付订单号
     */
    @NotNull(message = "recordNo不能为空")
    private String recordNo;

    /**
     * 短信验证码
     */
    @NotNull(message = "短信验证码不能为空")
    @Size(min = 1, message = "短信验证码不能为空")
    private String smsCode;

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "NcPayCflSynConfirmRequestDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}