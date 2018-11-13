package com.yeepay.g3.facade.nccashier.dto;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 16/11/9 18:58
 */
public class BankLimitAmountResponseDTO extends BasicResponseDTO{

    private String bankCode;//银行编码
    private String debit;//借贷记
    private String bankName;
    private long limitOfBill;//单比限额
    private long limitMinOfBill;//单比最小额
    private long limitOfDay;//单日限额
    private long limitOfMonth;

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

    public long getLimitOfBill() {
        return limitOfBill;
    }

    public void setLimitOfBill(long limitOfBill) {
        this.limitOfBill = limitOfBill;
    }

    public long getLimitMinOfBill() {
        return limitMinOfBill;
    }

    public void setLimitMinOfBill(long limitMinOfBill) {
        this.limitMinOfBill = limitMinOfBill;
    }

    public long getLimitOfDay() {
        return limitOfDay;
    }

    public void setLimitOfDay(long limitOfDay) {
        this.limitOfDay = limitOfDay;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public long getLimitOfMonth() {
        return limitOfMonth;
    }

    public void setLimitOfMonth(long limitOfMonth) {
        this.limitOfMonth = limitOfMonth;
    }
}
