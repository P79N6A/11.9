package com.yeepay.g3.core.frontend.entity;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.utils.persistence.Entity;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

public class Promotion implements Entity<Long> {
    private static final long serialVersionUID = -8747433407557797696L;
    private Long id;
    private String payInterface ;// 实体通道编码
    private String payOrderNo ;// 订单号
    private String tradeType ;// 交易类型（支付，退款 ）
    private String promotionId ;// 优惠券编码
    private String promotionName ;// 优惠券名称
    private String promotionScope ;// 范围（全场，单品）
    private String promotionType ;// 类型（预充值，免充值）
    private Amount amount ;// 券金额
    private Amount amountRefund ;// 券退回金额仅退款有值
    private String activityId ;// 优惠券活动id
    private Amount channelContribute ;// 渠道出资
    private Amount merchantContribute ;// 商户出资
    private Amount otherContribute ;// 其他出资
    private String memo ;// 备注信息
    private Date orderTime;//订单的交易时间
    private Date createTime;


    public String getPayInterface() {
        return payInterface;
    }

    public void setPayInterface(String payInterface) {
        this.payInterface = payInterface;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
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

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Amount getAmountRefund() {
        return amountRefund;
    }

    public void setAmountRefund(Amount amountRefund) {
        this.amountRefund = amountRefund;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Amount getChannelContribute() {
        return channelContribute;
    }

    public void setChannelContribute(Amount channelContribute) {
        this.channelContribute = channelContribute;
    }

    public Amount getMerchantContribute() {
        return merchantContribute;
    }

    public void setMerchantContribute(Amount merchantContribute) {
        this.merchantContribute = merchantContribute;
    }

    public Amount getOtherContribute() {
        return otherContribute;
    }

    public void setOtherContribute(Amount otherContribute) {
        this.otherContribute = otherContribute;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
