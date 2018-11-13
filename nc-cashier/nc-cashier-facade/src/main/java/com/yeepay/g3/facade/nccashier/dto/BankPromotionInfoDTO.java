package com.yeepay.g3.facade.nccashier.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BankPromotionInfoDTO implements Serializable{

    private static final long serialVersionUID = -2242418316644810156L;
    private String payInterface;
    private String bankOrderNo;
    private String tradeType;
    private String promotionId;
    private String promotionName;
    private String promotionScope;
    private String promotionType;
    private BigDecimal amount;
    private BigDecimal amountRefund;
    private String activityId;
    private String channelContribute;
    private String merchantContribute;
    private String otherContribute;
    private String memo;
    private Date orderTime;

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
