package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

import java.io.Serializable;

/**
 * API收银台，绑卡支付，请求发短验入参
 * Created by ruiyang.du on 2017/8/29.
 */
public class ApiBindPaySendSmsRequestDTO implements Serializable{
    private static final long serialVersionUID = 635307895318903102L;
    /**
     * 订单token
     */
    private String token;

    /**
     * 用于反查订单
     */
    private String bizType;
    /**
     * 接口版本
     */
    private String version;

    /*补充项开始*/
    /**
     * 卡号
     */
    private String cardno;
    /**
     * 持卡人姓名
     */
    private String owner;
    /**
     * 证件号
     */
    private String idno;
    /**
     * 银行预留手机号
     */
    private String phoneNo;
    /**
     * 易宝预留手机号
     */
    private String ypMobile;
    /**
     * 有效期
     */
    private String avlidDate;
    /**
     * cvv
     */
    private String cvv;
    /**
     * 证件类型，ID、OFFICERS、PASSPORT、REENTRY、TAIWAN、POLICE、SOLDIER
     */
    private String idCardType;
    /**
     * 取款密码
     */
    private String bankPWD;

    @Override
    public String toString() {
        return "ApiBindPaySendSmsRequestDTO{" +
                "token='" + token + '\'' +
                ", bizType='" + bizType + '\'' +
                ", version='" + version + '\'' +
                ", cardno='" + HiddenCode.hiddenBankCardNO(cardno) + '\'' +
                ", owner='" + HiddenCode.hiddenName(owner) + '\'' +
                ", idno='" + HiddenCode.hiddenIdentityCode(idno) + '\'' +
                ", phoneNo='" + HiddenCode.hiddenMobile(phoneNo) + '\'' +
                ", ypMobile='" + HiddenCode.hiddenMobile(ypMobile) + '\'' +
                ", avlidDate='【有效期】'\'' " +
                ", cvv='【cvv】'\'' " +
                ", idCardType='" + idCardType + '\'' +
                ", bankPWD=' 【密码】 '\'' " +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getYpMobile() {
        return ypMobile;
    }

    public void setYpMobile(String ypMobile) {
        this.ypMobile = ypMobile;
    }

    public String getAvlidDate() {
        return avlidDate;
    }

    public void setAvlidDate(String avlidDate) {
        this.avlidDate = avlidDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getBankPWD() {
        return bankPWD;
    }

    public void setBankPWD(String bankPWD) {
        this.bankPWD = bankPWD;
    }
}
