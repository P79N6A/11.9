/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类名称: PayPreAuthCompleteResponseDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/18 上午11:13
 * @version: 1.0.0
 */

public class PreAuthCompleteResponseDTO extends BasicResponseDTO {


    private static final long serialVersionUID = -2566728969303641900L;
    /**
     * 支付记录支付状态
     */
    private TrxStatusEnum trxStatus;

    /**
     * 订单方状态
     */
    private OrderSystemStatusEnum orderSystemStatus;

    /**
     * 银行通道编码
     */
    private String frpCode;

    /**
     * 银行订单号
     */
    private String bankOrderNO;

    /**
     * 银行交易流水号
     */
    private String bankTrxId;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 成功时间
     */
    private Date bankPaySuccDate;

    public TrxStatusEnum getTrxStatus() {
        return trxStatus;
    }

    public void setTrxStatus(TrxStatusEnum trxStatus) {
        this.trxStatus = trxStatus;
    }

    public OrderSystemStatusEnum getOrderSystemStatus() {
        return orderSystemStatus;
    }

    public void setOrderSystemStatus(OrderSystemStatusEnum orderSystemStatus) {
        this.orderSystemStatus = orderSystemStatus;
    }

    public String getFrpCode() {
        return frpCode;
    }

    public void setFrpCode(String frpCode) {
        this.frpCode = frpCode;
    }

    public String getBankOrderNO() {
        return bankOrderNO;
    }

    public void setBankOrderNO(String bankOrderNO) {
        this.bankOrderNO = bankOrderNO;
    }

    public String getBankTrxId() {
        return bankTrxId;
    }

    public void setBankTrxId(String bankTrxId) {
        this.bankTrxId = bankTrxId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getBankPaySuccDate() {
        return bankPaySuccDate;
    }

    public void setBankPaySuccDate(Date bankPaySuccDate) {
        this.bankPaySuccDate = bankPaySuccDate;
    }

    @Override
    public String toString() {
        return "PreAuthCompleteResponseDTO{" +
                "trxStatus=" + trxStatus +
                ", orderSystemStatus=" + orderSystemStatus +
                ", frpCode='" + frpCode + '\'' +
                ", bankOrderNO='" + bankOrderNO + '\'' +
                ", bankTrxId='" + bankTrxId + '\'' +
                ", cost=" + cost +
                ", bankPaySuccDate=" + bankPaySuccDate +
                ", recordNo='" + recordNo + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", dealUniqueSerialNo='" + dealUniqueSerialNo + '\'' +
                ", payOrderType=" + payOrderType +
                ", customerNumber='" + customerNumber + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", processStatus=" + processStatus +
                '}';
    }
}