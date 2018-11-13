/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 类名称: NcPayCflSmsRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/9/26 上午11:24
 * @version: 1.0.0
 */

public class NcPayCflSmsRequestDTO implements Serializable {

    private static final long serialVersionUID = 5619931963539837534L;

    /**
     * 支付订单号
     */
    @NotNull(message = "recordNo不能为空")
    private String recordNo;

    private String mobileNo;


    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "NcPayCflSmsRequestDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}