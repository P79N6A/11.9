/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SignCardIdEnum;
import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类名称: NcPayCflOpenRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/9/26 下午1:40
 * @version: 1.0.0
 */

public class NcPayCflOpenRequestDTO extends BasicRequestDTO {

    private static final long serialVersionUID = -5120199133077934984L;

    /**
     * 分期付款期数
     */
    private Integer cflCount;

    /**
     * 分期付款手续费率
     */
    private BigDecimal cflRate = new BigDecimal(0);

    /**
     * 商户补贴手续费率
     */
    private BigDecimal merchantFeeSubsidy = new BigDecimal(0);

    /**
     * 商户补贴手续费
     */
    private BigDecimal merchantAmountSubsidy = new BigDecimal(0);

    /**
     * 卡号
     */
    @NotNull
    private String cardNo;

    /**
     * NCPAY定义的业务方
     */
    @NotNull
    private Long bizType;

    /**
     * 会员类型(对外不适用枚举以及易宝自定义类型)
     */
    private MemberTypeEnum memberType;

    /**
     * 会员编号
     */
    private String memberNO;

    /**
     * 签约Id类型
     */
    private SignCardIdEnum signCardIdType;

    /**
     * 签约id
     */
    private Long signCardId;

    /**
     * 前端回调地址
     */
    @NotNull
    private String pageCallBack;

    /**
     * 支付工具
     */
    private String payTool;

    /**
     * 支付场景
     */
    @Length(max = 20, message = "payScene最大长度为20")
    private String payScene;

    /**
     * 零售产品码
     */
    private String retailProductCode;
    /**
     * 基础产品码
     */
    private String basicProductCode;

    public Integer getCflCount() {
        return cflCount;
    }

    public void setCflCount(Integer cflCount) {
        this.cflCount = cflCount;
    }

    public BigDecimal getCflRate() {
        return cflRate;
    }

    public void setCflRate(BigDecimal cflRate) {
        this.cflRate = cflRate;
    }

    public BigDecimal getMerchantFeeSubsidy() {
        return merchantFeeSubsidy;
    }

    public void setMerchantFeeSubsidy(BigDecimal merchantFeeSubsidy) {
        this.merchantFeeSubsidy = merchantFeeSubsidy;
    }

    public BigDecimal getMerchantAmountSubsidy() {
        return merchantAmountSubsidy;
    }

    public void setMerchantAmountSubsidy(BigDecimal merchantAmountSubsidy) {
        this.merchantAmountSubsidy = merchantAmountSubsidy;
    }

    public Long getBizType() {
        return bizType;
    }

    public void setBizType(Long bizType) {
        this.bizType = bizType;
    }

    public MemberTypeEnum getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberTypeEnum memberType) {
        this.memberType = memberType;
    }

    public String getMemberNO() {
        return memberNO;
    }

    public void setMemberNO(String memberNO) {
        this.memberNO = memberNO;
    }

    public SignCardIdEnum getSignCardIdType() {
        return signCardIdType;
    }

    public void setSignCardIdType(SignCardIdEnum signCardIdType) {
        this.signCardIdType = signCardIdType;
    }

    public Long getSignCardId() {
        return signCardId;
    }

    public void setSignCardId(Long signCardId) {
        this.signCardId = signCardId;
    }

    public String getPageCallBack() {
        return pageCallBack;
    }

    public void setPageCallBack(String pageCallBack) {
        this.pageCallBack = pageCallBack;
    }

    public String getPayTool() {
        return payTool;
    }

    public void setPayTool(String payTool) {
        this.payTool = payTool;
    }

    public String getPayScene() {
        return payScene;
    }

    public void setPayScene(String payScene) {
        this.payScene = payScene;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String toString() {
        return "NcPayCflOpenRequestDTO{" +
                "cflCount=" + cflCount +
                ", cflRate=" + cflRate +
                ", merchantFeeSubsidy=" + merchantFeeSubsidy +
                ", merchantAmountSubsidy=" + merchantAmountSubsidy +
                ", cardNo='" + HiddenCode.hiddenBankCardNO(cardNo) + '\'' +
                ", bizType=" + bizType +
                ", memberType=" + memberType +
                ", memberNO='" + memberNO + '\'' +
                ", signCardIdType=" + signCardIdType +
                ", signCardId=" + signCardId +
                ", pageCallBack='" + pageCallBack + '\'' +
                ", payTool='" + payTool + '\'' +
                ", payScene='" + payScene + '\'' +
                ", retailProductCode='" + retailProductCode + '\'' +
                ", basicProductCode='" + basicProductCode + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", dealUniqueSerialNo='" + dealUniqueSerialNo + '\'' +
                ", payOrderType=" + payOrderType +
                ", orderSystem='" + orderSystem + '\'' +
                ", requestSystem='" + requestSystem + '\'' +
                ", requestSysId='" + requestSysId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", amount=" + amount +
                ", userFee=" + userFee +
                ", customerNumber='" + customerNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", productName='" + productName + '\'' +
                ", payerIp='" + payerIp + '\'' +
                ", industryCode='" + industryCode + '\'' +
                ", riskProduction='" + riskProduction + '\'' +
                ", toolsInfo='" + toolsInfo + '\'' +
                '}';
    }
}