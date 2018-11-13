package com.yeepay.g3.facade.payprocessor.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.yeepay.g3.common.Amount;

/**
 * 担保分期下单响应参数
 * 
 * @author：tao.liu
 * @since：2018年1月23日 下午4:00:07
 * @version:
 */
public class NcGuaranteeCflPayResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 支付的实体通道编码
     */
    private String frpCode;

    /**
     * 支付是否短验
     * 与needRedirect不会同时为true
     */
    private Boolean needSMS;

    /**
     * 支付是否跳转
     * 与needSMS不会同时为true
     */
    private Boolean needRedirect;

    /**
     * 跳转请求url
     * 当needRedirect为true时，支付需要跳转时该参数才有意义
     */
    private String redirectUrl;

    /**
     * 跳转请求方法
     * 当needRedirect为true时，支付需要跳转时该参数才有意义
     */
    private String method;

    /**
     * 跳转请求编码
     * 当needRedirect为true时，支付需要跳转时该参数才有意义
     */
    private String encoding;

    /**
     * 跳转请求参数
     * 当needRedirect为true时，支付需要跳转时该参数才有意义
     */
    private Map<String, Object> paramMap;

    /**
     * 银行交易流水号
     */
    private String bankTrxId;

    /**
     * 银行订单号
     */
    private String bankOrderNo;

    /**
     * 交易金额
     */
    private Amount amount;

    /**
     * 支付成本
     */
    private Amount cost;

    /**
     * 分期付款期数
     */
    private Integer cflCount;

    /**
     * 持卡人利率
     */
    private BigDecimal payerInterestRate;

    public String getFrpCode() {
        return frpCode;
    }

    public void setFrpCode(String frpCode) {
        this.frpCode = frpCode;
    }

    public Boolean getNeedSMS() {
        return needSMS;
    }

    public void setNeedSMS(Boolean needSMS) {
        this.needSMS = needSMS;
    }

    public Boolean getNeedRedirect() {
        return needRedirect;
    }

    public void setNeedRedirect(Boolean needRedirect) {
        this.needRedirect = needRedirect;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public String getBankTrxId() {
        return bankTrxId;
    }

    public void setBankTrxId(String bankTrxId) {
        this.bankTrxId = bankTrxId;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Amount getCost() {
        return cost;
    }

    public void setCost(Amount cost) {
        this.cost = cost;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NcGuaranteeCflPayResponseDTO [frpCode=").append(frpCode).append(", needSMS=").append(needSMS).append(", needRedirect=")
                .append(needRedirect).append(", redirectUrl=").append(redirectUrl).append(", method=").append(method).append(", encoding=").append(encoding)
                .append(", paramMap=").append(paramMap).append(", bankTrxId=").append(bankTrxId).append(", bankOrderNo=").append(bankOrderNo)
                .append(", amount=").append(amount).append(", cost=").append(cost).append(", cflCount=").append(cflCount).append(", payerInterestRate=")
                .append(payerInterestRate).append(", recordNo=").append(recordNo).append(", outTradeNo=").append(outTradeNo).append(", orderNo=")
                .append(orderNo).append(", dealUniqueSerialNo=").append(dealUniqueSerialNo).append(", payOrderType=").append(payOrderType)
                .append(", customerNumber=").append(customerNumber).append(", responseCode=").append(responseCode).append(", responseMsg=").append(responseMsg)
                .append(", processStatus=").append(processStatus).append("]");
        return builder.toString();
    }

}
