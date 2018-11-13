/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 类名称: NcCflEasyRequestDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午12:20
 * @version: 1.0.0
 */

public class NcCflEasyRequestDTO extends BasicRequestDTO {

    private static final long serialVersionUID = -44160096739085187L;

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
    private CardInfoTypeEnum cardInfoType;

    /**
     * 支付卡信息id(绑卡记录主键id，临时卡主键id)
     */
    private Long cardInfoId;

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

    /**
     * 卡信息
     */
    private BankCardInfoDTO bankCardInfoDTO;

    /**
     * 分期期数
     */
    private int cflCount;

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

    public CardInfoTypeEnum getCardInfoType() {
        return cardInfoType;
    }

    public void setCardInfoType(CardInfoTypeEnum cardInfoType) {
        this.cardInfoType = cardInfoType;
    }

    public Long getCardInfoId() {
        return cardInfoId;
    }

    public void setCardInfoId(Long cardInfoId) {
        this.cardInfoId = cardInfoId;
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

    public BankCardInfoDTO getBankCardInfoDTO() {
        return bankCardInfoDTO;
    }

    public void setBankCardInfoDTO(BankCardInfoDTO bankCardInfoDTO) {
        this.bankCardInfoDTO = bankCardInfoDTO;
    }

    public int getCflCount() {
        return cflCount;
    }

    public void setCflCount(int cflCount) {
        this.cflCount = cflCount;
    }

    @Override
    public String toString() {
        return "NcCflEasyRequestDTO{" +
                "bizType=" + bizType +
                ", memberType=" + memberType +
                ", memberNO='" + memberNO + '\'' +
                ", cardInfoType=" + cardInfoType +
                ", cardInfoId=" + cardInfoId +
                ", payTool='" + payTool + '\'' +
                ", payScene='" + payScene + '\'' +
                ", retailProductCode='" + retailProductCode + '\'' +
                ", basicProductCode='" + basicProductCode + '\'' +
                ", bankCardInfoDTO=" + bankCardInfoDTO +
                ", cflCount=" + cflCount +
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
                ", goodsInfo='" + goodsInfo + '\'' +
                ", toolsInfo='" + toolsInfo + '\'' +
                ", combRequestDTO=" + combRequestDTO +
                '}';
    }
}