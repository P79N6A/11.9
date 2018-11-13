/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 类名称: NcCflEasySmsRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午12:40
 * @version: 1.0.0
 */

public class NcCflEasySmsRequestDTO implements Serializable {

    private static final long serialVersionUID = -1419796298073661434L;

    /**
     * 支付订单号
     */
    @NotBlank(message = "recordNo不能为空")
    private String recordNo;

    /**
     * 验证码类型
     */
    private ReqSmsSendTypeEnum smsSendType;

    /**
     * 需要补充的临时卡信息ID
     */
    private Long tmpCardId;

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

    public ReqSmsSendTypeEnum getSmsSendType() {
        return smsSendType;
    }

    public void setSmsSendType(ReqSmsSendTypeEnum smsSendType) {
        this.smsSendType = smsSendType;
    }

    public Long getTmpCardId() {
        return tmpCardId;
    }

    public void setTmpCardId(Long tmpCardId) {
        this.tmpCardId = tmpCardId;
    }

    public BankCardInfoDTO getBankCardInfoDTO() {
        return bankCardInfoDTO;
    }

    public void setBankCardInfoDTO(BankCardInfoDTO bankCardInfoDTO) {
        this.bankCardInfoDTO = bankCardInfoDTO;
    }

    @Override
    public String toString() {
        return "NcCflEasySmsRequestDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", smsSendType=" + smsSendType +
                ", tmpCardId=" + tmpCardId +
                ", bankCardInfoDTO=" + bankCardInfoDTO +
                '}';
    }
}