package com.yeepay.g3.facade.payprocessor.dto;

import java.math.BigDecimal;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * 担保分期下单请求参数
 * 
 * @author：tao.liu
 * @since：2018年1月23日 下午3:45:13
 * @version:
 */
public class NcGuaranteeCflPayRequestDTO extends BasicRequestDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 预路由ID
     */
    @NotNull
    private Long preRouteId;

    /**
     * 分期付款期数
     */
    private Integer cflCount;

    /**
     * NCPAY定义的业务方
     */
    @NotNull
    private Long bizType;

    /**
     * 场景类型
     * 取值范围：PC、MB（移动端）、API
     */
    @NotNull
    private String sceneType;

    /**
     * 银行卡卡号
     */
    private String cardNo;

    /**
     * 银行卡类型（借记卡、贷记卡）
     */
    private String cardType;

    /**
     * 银行卡账户类型（对公、对私）
     */
    private String accountType;

    /**
     * 开户名
     */
    private String accountName;

    /**
     * 证件号
     */
    private String idNo;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * cvv
     */
    private String creditCardCvv;

    /**
     * 有效期 格式为：YYMM
     */
    private String creditCardExpiryDate;

    /**
     * 取款密码
     */
    private String bankPwd;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 支付Ip
     * 一般移动端需要
     */
    private String payerIp;

    /**
     * 页面重定向地址
     * 页面跳转模式使用
     */
    private String pageRedirectUrl;

    /**
     * 商户补贴手续费率
     * 以小数形式传，比如执卡人费率为3%，则该值为0.03
     */
    private BigDecimal merchantFeeSubsidy;

    /**
     * 附加参数
     */
    private Map<String, String> extParamMap;

    /**
     * 零售产品码
     */
    @NotNull
    private String retailProductCode;

    /**
     * 基础产品码
     */
    @NotNull
    private String basicProductCode;

    public Long getPreRouteId() {
        return preRouteId;
    }

    public void setPreRouteId(Long preRouteId) {
        this.preRouteId = preRouteId;
    }

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

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCreditCardCvv() {
        return creditCardCvv;
    }

    public void setCreditCardCvv(String creditCardCvv) {
        this.creditCardCvv = creditCardCvv;
    }

    public String getCreditCardExpiryDate() {
        return creditCardExpiryDate;
    }

    public void setCreditCardExpiryDate(String creditCardExpiryDate) {
        this.creditCardExpiryDate = creditCardExpiryDate;
    }

    public String getBankPwd() {
        return bankPwd;
    }

    public void setBankPwd(String bankPwd) {
        this.bankPwd = bankPwd;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPayerIp() {
        return payerIp;
    }

    public void setPayerIp(String payerIp) {
        this.payerIp = payerIp;
    }

    public String getPageRedirectUrl() {
        return pageRedirectUrl;
    }

    public void setPageRedirectUrl(String pageRedirectUrl) {
        this.pageRedirectUrl = pageRedirectUrl;
    }

    public BigDecimal getMerchantFeeSubsidy() {
        return merchantFeeSubsidy;
    }

    public void setMerchantFeeSubsidy(BigDecimal merchantFeeSubsidy) {
        this.merchantFeeSubsidy = merchantFeeSubsidy;
    }

    public Map<String, String> getExtParamMap() {
        return extParamMap;
    }

    public void setExtParamMap(Map<String, String> extParamMap) {
        this.extParamMap = extParamMap;
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
        builder.append("NcGuaranteeCflPayRequestDTO [preRouteId=").append(preRouteId).append(", cflCount=").append(cflCount).append(", bizType=")
                .append(bizType).append(", sceneType=").append(sceneType).append(", cardNo=").append(cardNo).append(", cardType=").append(cardType)
                .append(", accountType=").append(accountType).append(", accountName=").append(accountName).append(", idNo=").append(idNo).append(", idType=")
                .append(idType).append(", mobileNo=").append(mobileNo).append(", creditCardCvv=").append(creditCardCvv).append(", creditCardExpiryDate=")
                .append(creditCardExpiryDate).append(", bankPwd=").append(bankPwd).append(", bankCode=").append(bankCode).append(", payerIp=").append(payerIp)
                .append(", pageRedirectUrl=").append(pageRedirectUrl).append(", merchantFeeSubsidy=").append(merchantFeeSubsidy).append(", extParamMap=")
                .append(extParamMap).append(", retailProductCode=").append(retailProductCode).append(", basicProductCode=").append(basicProductCode)
                .append(", outTradeNo=").append(outTradeNo).append(", dealUniqueSerialNo=").append(dealUniqueSerialNo).append(", payOrderType=")
                .append(payOrderType).append(", orderSystem=").append(orderSystem).append(", requestSystem=").append(requestSystem).append(", requestSysId=")
                .append(requestSysId).append(", orderNo=").append(orderNo).append(", amount=").append(amount).append(", userFee=").append(userFee)
                .append(", customerNumber=").append(customerNumber).append(", customerName=").append(customerName).append(", productName=").append(productName)
                .append(", industryCode=").append(industryCode).append(", riskProduction=").append(riskProduction).append(", goodsInfo=").append(goodsInfo)
                .append(", toolsInfo=").append(toolsInfo).append("]");
        return builder.toString();
    }

}
