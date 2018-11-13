/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 类名称: CombResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/14 下午3:32
 * @version: 1.0.0
 */

public class CombResponseDTO implements Serializable {

    private static final long serialVersionUID = 5345864295372992736L;

    /**
     * 组合支付类型
     */
    private String payOrderType;

    /**
     * 组合支付订单号
     */
    private String payOrderNo;

    /**
     * 组合支付银行订单号
     */
    private String bankOrderNo;

    /**
     * 组合支付金额
     */
    private BigDecimal amount;

    /**
     * 组合支付完成时间
     */
    private Date paySuccDate;

    /**
     * 组合支付通道编码
     */
    private String channelId;

    /**
     * 产品编码
     */
    private String payProduct;

    /**
     * 支付状态
     * @return
     */
    private String status;


    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaySuccDate() {
        return paySuccDate;
    }

    public void setPaySuccDate(Date paySuccDate) {
        this.paySuccDate = paySuccDate;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPayProduct() {
        return payProduct;
    }

    public void setPayProduct(String payProduct) {
        this.payProduct = payProduct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CombResponseDTO{" +
                "payOrderType='" + payOrderType + '\'' +
                ", payOrderNo='" + payOrderNo + '\'' +
                ", bankOrderNo='" + bankOrderNo + '\'' +
                ", amount=" + amount +
                ", paySuccDate=" + paySuccDate +
                ", channelId='" + channelId + '\'' +
                ", payProduct='" + payProduct + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}