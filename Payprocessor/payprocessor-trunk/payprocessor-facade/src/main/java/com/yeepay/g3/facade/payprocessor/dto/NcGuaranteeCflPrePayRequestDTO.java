package com.yeepay.g3.facade.payprocessor.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 担保分期预路由请求参数
 * 
 * @author：tao.liu
 * @since：2018年1月23日 下午2:00:11
 * @version:
 */
public class NcGuaranteeCflPrePayRequestDTO extends BasicRequestDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 分期付款期数
     */
    @NotNull(message = "cflCount不能为空")
    private Integer cflCount;

    /**
     * NCPAY定义的业务方
     */
    @NotNull(message = "bizType不能为空")
    private Long bizType;

    /**
     * 场景类型
     * 取值范围：PC、MB（移动端）、API
     */
    @NotBlank(message = "sceneType不能为空")
    private String sceneType;

    /**
     * 是否接受签约短验
     */
    @NotNull(message = "acceptSignSMS不能为空")
    private Boolean acceptSignSMS;

    /**
     * 是否接受支付短验
     */
    @NotNull(message = "acceptPaySMS不能为空")
    private Boolean acceptPaySMS;

    /**
     * 银行卡卡号
     * 若卡号不为空，优先根据卡号路由，
     * 否则根据 cardType + bankCode 进行判断
     */
    private String cardNo;

    /**
     * 卡类型
     */
    @NotBlank(message = "cardType不能为空")
    private String cardType;

    /**
     * 银行编码
     */
    @NotBlank(message = "bankCode不能为空")
    private String bankCode;

    /**
     * 银行卡的账户类型
     * 取值范围：对私、对公
     */
    @NotBlank(message = "accountType不能为空")
    private String accountType;

    /**
     * 零售产品码
     */
    @NotBlank(message = "retailProductCode不能为空")
    private String retailProductCode;

    /**
     * 基础产品码
     */
    @NotBlank(message = "basicProductCode不能为空")
    private String basicProductCode;

    public Integer getCflCount() {
        return cflCount;
    }

    public void setCflCount(Integer cflCount) {
        this.cflCount = cflCount;
    }

    public Long getBizType() {
        return bizType;
    }

    public void setBizType(Long bizType) {
        this.bizType = bizType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public Boolean getAcceptSignSMS() {
        return acceptSignSMS;
    }

    public void setAcceptSignSMS(Boolean acceptSignSMS) {
        this.acceptSignSMS = acceptSignSMS;
    }

    public Boolean getAcceptPaySMS() {
        return acceptPaySMS;
    }

    public void setAcceptPaySMS(Boolean acceptPaySMS) {
        this.acceptPaySMS = acceptPaySMS;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getRetailProductCode() {
        return retailProductCode;
    }

    public void setRetailProductCode(String retailProductCode) {
        this.retailProductCode = retailProductCode;
    }

    public String getBasicProductCode() {
        return basicProductCode;
    }

    public void setBasicProductCode(String basicProductCode) {
        this.basicProductCode = basicProductCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NcGuaranteeCflPrePayRequestDTO [cflCount=").append(cflCount).append(", bizType=").append(bizType).append(", sceneType=")
                .append(sceneType).append(", acceptSignSMS=").append(acceptSignSMS).append(", acceptPaySMS=").append(acceptPaySMS).append(", cardNo=")
                .append(cardNo).append(", cardType=").append(cardType).append(", bankCode=").append(bankCode).append(", accountType=").append(accountType)
                .append(", retailProductCode=").append(retailProductCode).append(", basicProductCode=").append(basicProductCode).append(", outTradeNo=")
                .append(outTradeNo).append(", dealUniqueSerialNo=").append(dealUniqueSerialNo).append(", payOrderType=").append(payOrderType)
                .append(", orderSystem=").append(orderSystem).append(", requestSystem=").append(requestSystem).append(", requestSysId=").append(requestSysId)
                .append(", orderNo=").append(orderNo).append(", amount=").append(amount).append(", userFee=").append(userFee).append(", customerNumber=")
                .append(customerNumber).append(", customerName=").append(customerName).append(", productName=").append(productName).append(", payerIp=")
                .append(payerIp).append(", industryCode=").append(industryCode).append(", riskProduction=").append(riskProduction).append(", toolsInfo=")
                .append(toolsInfo).append("]");
        return builder.toString();
    }

}
