package com.yeepay.g3.facade.nccashier.dto;/**
 * @program: nc-cashier-parent
 * @description: 组合支付 相关信息DTO
 * @author: jimin.zhou
 * @create: 2018-06-25 15:47
 **/

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @description: 组合支付 相关信息DTO
 *
 * @author: jimin.zhou
 *
 * @create: 2018-06-25 15:47
 **/


public class CombinedPaymentRecordDTO {

    /**
     * 对应paybizorder表中的orderid
     */
    private String orderid;

    /**
     * 备注
     */
    private String remark;

    /**
     * 支付类型 MKTG:营销支付；ACCOUNT：账户支付：
     */
    private String payOrderType;

    /**
     * 支付金额  对应不同支付类型的订单金额
     */
    private BigDecimal amount;

    /**
     * 支付银行订单号  如果是营销订单，则是营销系统操作账户的流水号，当银行订单号使用
     */
    private String bankOrderNo;

    /**
     * 支付通道编码
     */
    private String channelId;

    /**
     * 支付订单号  支付方式为 营销立减时,即为营销系统订单号
     */
    private String payOrderNo;

    /**
     * 支付产品编码
     */
    private String payProduct;

    /**
     * 支付单完成时间
     */
    private Date paySuccDate;


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getPayProduct() {
        return payProduct;
    }

    public void setPayProduct(String payProduct) {
        this.payProduct = payProduct;
    }

    public Date getPaySuccDate() {
        return paySuccDate;
    }

    public void setPaySuccDate(Date paySuccDate) {
        this.paySuccDate = paySuccDate;
    }
}
