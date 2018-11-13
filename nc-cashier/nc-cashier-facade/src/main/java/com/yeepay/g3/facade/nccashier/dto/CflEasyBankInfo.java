/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 类名称: CflEasyBankInfo <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/5 下午6:28
 * @version: 1.0.0
 */

public class CflEasyBankInfo implements Serializable {

    private static final long serialVersionUID = -8028283016719683118L;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 支持期数
     */
    private String supportPeriods;

    /**
     * 支持的金额范围
     */
    private String limitAmounts;
    

    public CflEasyBankInfo() {
    }

    public CflEasyBankInfo(String bankCode, String bankName, String supportPeriods, String limitAmounts) {
        super();
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.supportPeriods = supportPeriods;
        this.limitAmounts = limitAmounts;
    }

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

    public String getSupportPeriods() {
        return supportPeriods;
    }

    public void setSupportPeriods(String supportPeriods) {
        this.supportPeriods = supportPeriods;
    }

    public String getLimitAmounts() {
        return limitAmounts;
    }

    public void setLimitAmounts(String limitAmounts) {
        this.limitAmounts = limitAmounts;
    }

    @Override
    public String toString() {
        return "CflEasyBankInfo{" +
                "bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", supportPeriods='" + supportPeriods + '\'' +
                ", limitAmounts='" + limitAmounts + '\'' +
                '}';
    }
}