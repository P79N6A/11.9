/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 类名称: PayPreAuthCancelRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/18 上午11:19
 * @version: 1.0.0
 */

public class PreAuthCancelRequestDTO implements Serializable {


    private static final long serialVersionUID = 6343540976856819112L;
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
    protected String outTradeNo;

    /**
     * 撤销类型
     * 预授权撤销PREAUTHCANCEL、预授权完成撤销PREAUTHCONFIRMCANCEL
     */
    @NotNull(message = "cancelType不能为空")
    private String cancelType;

    private String goodsInfo;

    /**
     * 卡号
     */
    private String payerCardNo;

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
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

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getPayerCardNo() {
        return payerCardNo;
    }

    public void setPayerCardNo(String payerCardNo) {
        this.payerCardNo = payerCardNo;
    }

    @Override
    public String toString() {
        return "PreAuthCancelRequestDTO{" +
                "recordNo='" + recordNo + '\'' +
                ", bizType=" + bizType +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", cancelType='" + cancelType + '\'' +
                ", goodsInfo='" + goodsInfo + '\'' +
                ", payerCardNo='" + HiddenCode.hiddenBankCardNO(payerCardNo) + '\'' +
                '}';
    }
}