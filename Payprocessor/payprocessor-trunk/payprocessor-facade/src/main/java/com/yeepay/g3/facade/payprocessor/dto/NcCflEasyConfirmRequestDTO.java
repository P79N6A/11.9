/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 类名称: NcCflEasyConfirmRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午12:54
 * @version: 1.0.0
 */

public class NcCflEasyConfirmRequestDTO implements Serializable {

    private static final long serialVersionUID = 569750213158014208L;

    /**
     * 支付订单号
     */
    @NotBlank(message = "recordNo不能为空")
    private String recordNo;

    /**
     * 需要补充的临时卡信息ID
     */
    private Long tmpCardId;

    private String smsCode;

    /**
     * 卡信息
     */
    private BankCardInfoDTO bankCardInfoDTO;

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public Long getTmpCardId() {
        return tmpCardId;
    }

    public void setTmpCardId(Long tmpCardId) {
        this.tmpCardId = tmpCardId;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public BankCardInfoDTO getBankCardInfoDTO() {
        return bankCardInfoDTO;
    }

    public void setBankCardInfoDTO(BankCardInfoDTO bankCardInfoDTO) {
        this.bankCardInfoDTO = bankCardInfoDTO;
    }

    @Override
    public String toString() {
        return "NcCflEasyConfirmRequestDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", tmpCardId=" + tmpCardId +
                ", smsCode='" + smsCode + '\'' +
                ", bankCardInfoDTO=" + bankCardInfoDTO +
                '}';
    }
}