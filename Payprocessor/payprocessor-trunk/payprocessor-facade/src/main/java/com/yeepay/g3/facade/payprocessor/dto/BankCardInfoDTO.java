/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;

/**
 * 类名称: BankCardInfoDTO <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/3/19 下午6:09
 * @version: 1.0.0
 */

public class BankCardInfoDTO implements Serializable {

    private static final long serialVersionUID = -7767529652580040606L;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 卡类型
     */
    private String cardType;

    /**
     * 有效期
     */
    private String expireDate;

    /**
     * cvv2
     */
    private String cvv2;

    /**
     * 密码
     */
    private String pin;

    /**
     * 持卡人姓名
     */
    private String owner;

    /**
     * 银行预留手机号
     */
    private String bankMobile;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 证件类型
     */
    private String idCardType;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public boolean isNullValue() {
        return StringUtils.isBlank(this.bankCode)
                && StringUtils.isBlank(this.bankName)
                && StringUtils.isBlank(this.cardNo)
                && StringUtils.isBlank(this.cardType)
                && StringUtils.isBlank(this.expireDate)
                && StringUtils.isBlank(this.cvv2)
                && StringUtils.isBlank(this.pin)
                && StringUtils.isBlank(this.owner)
                && StringUtils.isBlank(this.bankMobile)
                && StringUtils.isBlank(this.idCard)
                && StringUtils.isBlank(this.idCardType);
    }

    @Override
    public String toString() {
        return "CardInfoDTO{" +
                "bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardNo='" + HiddenCode.hiddenBankCardNO(cardNo) + '\'' +
                ", cardType='" + cardType + '\'' +
                ", expireDate='" + HiddenCode.hiddenAbliddate(expireDate) + '\'' +
                ", cvv2='" + HiddenCode.hiddenCvv(cvv2) + '\'' +
                ", pin='" + HiddenCode.hiddenBankPwd(pin) + '\'' +
                ", owner='" + HiddenCode.hiddenName(owner) + '\'' +
                ", bankMobile='" + HiddenCode.hiddenMobile(bankMobile) + '\'' +
                ", idCard='" + HiddenCode.hiddenIdentityCode(idCard) + '\'' +
                ", idCardType='" + idCardType + '\'' +
                '}';
    }
}