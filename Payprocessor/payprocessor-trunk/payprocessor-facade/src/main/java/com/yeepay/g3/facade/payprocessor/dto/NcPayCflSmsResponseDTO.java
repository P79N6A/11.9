/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

/**
 * 类名称: NcPayCflSmsResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/9/26 上午11:28
 * @version: 1.0.0
 */

public class NcPayCflSmsResponseDTO extends ResponseStatusDTO {

    private static final long serialVersionUID = -2204386885331678809L;

    /**
     * 支付订单号，子表主键
     */
    protected String recordNo;

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    @Override
    public String toString() {
        return "NcPayCflSmsResponseDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", processStatus=" + processStatus +
                '}';
    }
}