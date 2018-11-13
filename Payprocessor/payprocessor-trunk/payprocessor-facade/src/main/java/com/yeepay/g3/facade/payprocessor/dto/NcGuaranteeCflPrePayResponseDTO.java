package com.yeepay.g3.facade.payprocessor.dto;

import java.math.BigDecimal;

import com.yeepay.g3.common.Amount;

/**
 * 担保分期预路由响应参数
 * 
 * @author：tao.liu
 * @since：2018年1月23日 下午2:53:32
 * @version:
 */
public class NcGuaranteeCflPrePayResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 卡类型
     */
    private String cardType;

    /**
     * 是否需要签约
     */
    private Boolean needSign;

    /**
     * 签约是否需要短验
     * 当needSign为true时，需要签约的前提下，该参数才有意义
     * 且与signNeedRedirect不会同时为true
     */
    private Boolean signNeedSMS;

    /**
     * 签约是否需要跳转
     * 当needSign为true时，需要签约的前提下，该参数才有意义
     * 且与signNeedSMS不会同时为true
     */
    private Boolean signNeedRedirect;

    /**
     * 签约必填必验项
     */
    private Integer requiredVerifySignItem;

    /**
     * 签约必填不验项
     */
    private Integer requiredUnverifySignItem;

    /**
     * 支付是否短验
     */
    private Boolean payNeedSMS;

    /**
     * 支付是否跳转
     */
    private Boolean payNeedRedirect;

    /**
     * 支付必填必验项
     */
    private Integer requiredVerifyPayItem;

    /**
     * 支付必填不验项
     */
    private Integer requiredUnverifyPayItem;

    /**
     * 支付成本
     */
    private Amount payOrderCost;

    /**
     * 分期期数
     */
    private Integer cflCount;

    /**
     * 持卡人利率
     * 以小数形式传，比如执卡人费率为3%，则该值为0.03
     */
    private BigDecimal payerInterestRate;

    /**
     * 过期时间
     */
    private Integer traceIdExpireSec;

    /**
     * 预路由Id
     */
    private Long preRouteId;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Boolean getNeedSign() {
        return needSign;
    }

    public void setNeedSign(Boolean needSign) {
        this.needSign = needSign;
    }

    public Boolean getSignNeedSMS() {
        return signNeedSMS;
    }

    public void setSignNeedSMS(Boolean signNeedSMS) {
        this.signNeedSMS = signNeedSMS;
    }

    public Boolean getSignNeedRedirect() {
        return signNeedRedirect;
    }

    public void setSignNeedRedirect(Boolean signNeedRedirect) {
        this.signNeedRedirect = signNeedRedirect;
    }

    public Integer getRequiredVerifySignItem() {
        return requiredVerifySignItem;
    }

    public void setRequiredVerifySignItem(Integer requiredVerifySignItem) {
        this.requiredVerifySignItem = requiredVerifySignItem;
    }

    public Integer getRequiredUnverifySignItem() {
        return requiredUnverifySignItem;
    }

    public void setRequiredUnverifySignItem(Integer requiredUnverifySignItem) {
        this.requiredUnverifySignItem = requiredUnverifySignItem;
    }

    public Boolean getPayNeedSMS() {
        return payNeedSMS;
    }

    public void setPayNeedSMS(Boolean payNeedSMS) {
        this.payNeedSMS = payNeedSMS;
    }

    public Boolean getPayNeedRedirect() {
        return payNeedRedirect;
    }

    public void setPayNeedRedirect(Boolean payNeedRedirect) {
        this.payNeedRedirect = payNeedRedirect;
    }

    public Integer getRequiredVerifyPayItem() {
        return requiredVerifyPayItem;
    }

    public void setRequiredVerifyPayItem(Integer requiredVerifyPayItem) {
        this.requiredVerifyPayItem = requiredVerifyPayItem;
    }

    public Integer getRequiredUnverifyPayItem() {
        return requiredUnverifyPayItem;
    }

    public void setRequiredUnverifyPayItem(Integer requiredUnverifyPayItem) {
        this.requiredUnverifyPayItem = requiredUnverifyPayItem;
    }

    public Amount getPayOrderCost() {
        return payOrderCost;
    }

    public void setPayOrderCost(Amount payOrderCost) {
        this.payOrderCost = payOrderCost;
    }

    public Integer getCflCount() {
        return cflCount;
    }

    public void setCflCount(Integer cflCount) {
        this.cflCount = cflCount;
    }

    public BigDecimal getPayerInterestRate() {
        return payerInterestRate;
    }

    public void setPayerInterestRate(BigDecimal payerInterestRate) {
        this.payerInterestRate = payerInterestRate;
    }

    public Integer getTraceIdExpireSec() {
        return traceIdExpireSec;
    }

    public void setTraceIdExpireSec(Integer traceIdExpireSec) {
        this.traceIdExpireSec = traceIdExpireSec;
    }

    public Long getPreRouteId() {
        return preRouteId;
    }

    public void setPreRouteId(Long preRouteId) {
        this.preRouteId = preRouteId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NcGuaranteeCflPrePayResponseDTO [bankCode=").append(bankCode).append(", cardType=").append(cardType).append(", needSign=")
                .append(needSign).append(", signNeedSMS=").append(signNeedSMS).append(", signNeedRedirect=").append(signNeedRedirect)
                .append(", requiredVerifySignItem=").append(requiredVerifySignItem).append(", requiredUnverifySignItem=").append(requiredUnverifySignItem)
                .append(", payNeedSMS=").append(payNeedSMS).append(", payNeedRedirect=").append(payNeedRedirect).append(", requiredVerifyPayItem=")
                .append(requiredVerifyPayItem).append(", requiredUnverifyPayItem=").append(requiredUnverifyPayItem).append(", payOrderCost=")
                .append(payOrderCost).append(", cflCount=").append(cflCount).append(", payerInterestRate=").append(payerInterestRate)
                .append(", traceIdExpireSec=").append(traceIdExpireSec).append(", preRouteId=").append(preRouteId).append(", recordNo=").append(recordNo)
                .append(", outTradeNo=").append(outTradeNo).append(", orderNo=").append(orderNo).append(", dealUniqueSerialNo=").append(dealUniqueSerialNo)
                .append(", payOrderType=").append(payOrderType).append(", customerNumber=").append(customerNumber).append(", responseCode=")
                .append(responseCode).append(", responseMsg=").append(responseMsg).append(", processStatus=").append(processStatus).append("]");
        return builder.toString();
    }

}
