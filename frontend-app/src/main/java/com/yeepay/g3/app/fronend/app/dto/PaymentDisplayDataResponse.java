package com.yeepay.g3.app.fronend.app.dto;

import java.io.Serializable;

/**
 * 支付数据展板-返回类
 */
public class PaymentDisplayDataResponse implements Serializable {
    private static final long serialVersionUID = 1351006207392532560L;
    private String dailyTradeAmount;
    private String dailyOrderCount;
    private String dailyActiveMerchant;
    private String yesterdayTradeAmount;
    private String yesterdayOrderCount;
    private String yesterdayActiveMerchant;

    public String getDailyTradeAmount() {
        return dailyTradeAmount;
    }

    public void setDailyTradeAmount(String dailyTradeAmount) {
        this.dailyTradeAmount = dailyTradeAmount;
    }

    public String getDailyOrderCount() {
        return dailyOrderCount;
    }

    public void setDailyOrderCount(String dailyOrderCount) {
        this.dailyOrderCount = dailyOrderCount;
    }

    public String getDailyActiveMerchant() {
        return dailyActiveMerchant;
    }

    public void setDailyActiveMerchant(String dailyActiveMerchant) {
        this.dailyActiveMerchant = dailyActiveMerchant;
    }

    public String getYesterdayTradeAmount() {
        return yesterdayTradeAmount;
    }

    public void setYesterdayTradeAmount(String yesterdayTradeAmount) {
        this.yesterdayTradeAmount = yesterdayTradeAmount;
    }

    public String getYesterdayOrderCount() {
        return yesterdayOrderCount;
    }

    public void setYesterdayOrderCount(String yesterdayOrderCount) {
        this.yesterdayOrderCount = yesterdayOrderCount;
    }

    public String getYesterdayActiveMerchant() {
        return yesterdayActiveMerchant;
    }

    public void setYesterdayActiveMerchant(String yesterdayActiveMerchant) {
        this.yesterdayActiveMerchant = yesterdayActiveMerchant;
    }
}
