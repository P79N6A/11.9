package com.yeepay.g3.facade.frontend.dto;


import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券信息DTO
 */
public class PromotionInfoDTO implements Serializable {

    private static final long serialVersionUID = 9096751609151479599L;
    private String payInterface ;// 实体通道编码
    private String bankOrderNo ;// 订单号
    private String tradeType ;// 交易类型（支付，退款 ）
    private String promotionId ;// 优惠券编码
    private String promotionName ;// 优惠券名称
    private String promotionScope ;// 范围（全场，单品）
    private String promotionType ;// 类型（预充值，免充值）
    private BigDecimal amount ;// 券金额
    private BigDecimal amountRefund ;// 券退回金额仅退款有值
    private String activityId ;// 优惠券活动id
    private String channelContribute ;// 渠道出资
    private String merchantContribute ;// 商户出资
    private String otherContribute ;// 其他出资
    private String memo ;// 备注信息
    private Date orderTime;//订单的交易时间


    public String getPayInterface() {
        return payInterface;
    }

    public void setPayInterface(String payInterface) {
        this.payInterface = payInterface;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionScope() {
        return promotionScope;
    }

    public void setPromotionScope(String promotionScope) {
        this.promotionScope = promotionScope;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountRefund() {
        return amountRefund;
    }

    public void setAmountRefund(BigDecimal amountRefund) {
        this.amountRefund = amountRefund;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getChannelContribute() {
        return channelContribute;
    }

    public void setChannelContribute(String channelContribute) {
        this.channelContribute = channelContribute;
    }

    public String getMerchantContribute() {
        return merchantContribute;
    }

    public void setMerchantContribute(String merchantContribute) {
        this.merchantContribute = merchantContribute;
    }

    public String getOtherContribute() {
        return otherContribute;
    }

    public void setOtherContribute(String otherContribute) {
        this.otherContribute = otherContribute;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }


    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }

}
