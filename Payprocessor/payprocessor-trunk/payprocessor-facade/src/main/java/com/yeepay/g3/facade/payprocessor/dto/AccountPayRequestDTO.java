package com.yeepay.g3.facade.payprocessor.dto;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.json.JSONUtils;

public class AccountPayRequestDTO extends BasicRequestDTO {

    private static final long serialVersionUID = 4532300341351324583L;

    /**
     * 零售产品码
     */
    private String retailProductCode;
    /**
     * 基础产品码
     */
    private String basicProductCode;

    /**
     * 付款商编
     */
    @NotNull
    @Size(max = 32)
    private String debitCustomerNo;

    /**
     * 付款账号
     */
    @Size(max = 32)
    private String debitAccountNo;

    /**
     * 付款账户名
     */
    @Size(max = 128)
    private String debitUserName;

    /**
     * 扩展信息
     */
    @NotNull
    private Map<String, String> extInfo;

    /**
     * 付款商户登录名
     */
    private String debitCustomerLoginName;

    /**
     * 付款商户密码
     */
    private String debitCustomerPwd;

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

    public String getDebitCustomerNo() {
        return debitCustomerNo;
    }

    public void setDebitCustomerNo(String debitCustomerNo) {
        this.debitCustomerNo = debitCustomerNo;
    }

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
    }

    public String getDebitUserName() {
        return debitUserName;
    }

    public void setDebitUserName(String debitUserName) {
        this.debitUserName = debitUserName;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public String getDebitCustomerLoginName() {
        return debitCustomerLoginName;
    }

    public void setDebitCustomerLoginName(String debitCustomerLoginName) {
        this.debitCustomerLoginName = debitCustomerLoginName;
    }

    public String getDebitCustomerPwd() {
        return debitCustomerPwd;
    }

    public void setDebitCustomerPwd(String debitCustomerPwd) {
        this.debitCustomerPwd = debitCustomerPwd;
    }


    @Override
    public String toString() {
        return "AccountPayRequestDTO{" +
                "retailProductCode='" + retailProductCode + '\'' +
                ", basicProductCode='" + basicProductCode + '\'' +
                ", debitCustomerNo='" + debitCustomerNo + '\'' +
                ", debitAccountNo='" + debitAccountNo + '\'' +
                ", debitUserName='" + debitUserName + '\'' +
                ", extInfo=" + extInfo +
                ", debitCustomerLoginName='" + (StringUtils.isNotBlank(debitCustomerLoginName) ? debitCustomerLoginName.substring(0, 1) + "***" : null) + '\'' +
                ", debitCustomerPwd='" + (StringUtils.isNotBlank(debitCustomerPwd) ? "******" : null) + '\'' +
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
                ", CombRequestDTO='" + combRequestDTO + '\'' +
                '}';
    }
}
