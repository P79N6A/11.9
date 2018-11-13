/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类名称: PayPreAuthCompleteRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/18 上午11:01
 * @version: 1.0.0
 */

public class PreAuthCompleteRequestDTO implements Serializable {


    private static final long serialVersionUID = -8189935321459933384L;
    /**
     * 支付订单号
     */
    @NotNull(message = "recordNo不能为空")
    private String recordNo;

    /**
     * NCPAY定义的业务方
     */
    @NotNull
    private Long bizType;

    /**
     * 商户订单号
     */
    @NotBlank(message = "outTradeNo不能为空")
    @Length(min = 1, max = 64, message = "outTradeNo最大长度为64")
    private String outTradeNo;

    /**
     * 支付金额
     */
    @NotNull(message = "amount不能为空")
    @DecimalMin(value = "0.01", message = "totalAmount最小值为0.01")
    private BigDecimal amount;

    /**
     * 用户手续费
     */
    private BigDecimal userFee = new BigDecimal(0);

    private String goodsInfo;


    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getBizType() {
        return bizType;
    }

    public void setBizType(Long bizType) {
        this.bizType = bizType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public BigDecimal getUserFee() {
        return userFee;
    }

    public void setUserFee(BigDecimal userFee) {
        this.userFee = userFee;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public String toString() {
        return "PreAuthCompleteRequestDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", bizType=" + bizType +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", amount=" + amount +
                ", userFee=" + userFee +
                '}';
    }
}