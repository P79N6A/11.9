package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SignCardIdEnum;
import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类名称: NcPayCflOrderRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/9/26 上午10:35
 * @version: 1.0.0
 */

public class NcPayCflOrderRequestDTO extends BasicRequestDTO {

    private static final long serialVersionUID = -8522099426307134087L;

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
     * 支付卡信息类型(临时卡/绑卡)
     */
    @NotNull
    private SignCardIdEnum signCardIdType;

    /**
     * 支付卡信息id(绑卡记录主键id，临时卡主键id)
     */
    @NotNull
    @Min(1)
    private Long signCardId;

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
    @NotNull
    private String retailProductCode;

    /**
     * 基础产品码
     */
    @NotNull
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
        return "NcPayCflOrderRequestDTO{" +
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