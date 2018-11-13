package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

/**
 * 银行限额数据VO类
 * Created by ruiyang.du on 2017/7/31.
 */
public class BankLimitAmountResponseVO implements Serializable {
    private static final long serialVersionUID = -6887434025566069076L;
    private String bankCode;//银行编码
    private String debit;//借贷记
    private String bankName;
    private String limitOfBill;//单比限额，单位：元
    private String limitMinOfBill;//单比最小额，单位：元
    private String limitOfDay;//单日限额，单位：元
    private String limitOfMonth; //单月限额，单位：元

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLimitOfBill() {
        return limitOfBill;
    }

    public void setLimitOfBill(String limitOfBill) {
        this.limitOfBill = limitOfBill;
    }

    public String getLimitMinOfBill() {
        return limitMinOfBill;
    }

    public void setLimitMinOfBill(String limitMinOfBill) {
        this.limitMinOfBill = limitMinOfBill;
    }

    public String getLimitOfDay() {
        return limitOfDay;
    }

    public void setLimitOfDay(String limitOfDay) {
        this.limitOfDay = limitOfDay;
    }

    public String getLimitOfMonth() {
        return limitOfMonth;
    }

    public void setLimitOfMonth(String limitOfMonth) {
        this.limitOfMonth = limitOfMonth;
    }
}
