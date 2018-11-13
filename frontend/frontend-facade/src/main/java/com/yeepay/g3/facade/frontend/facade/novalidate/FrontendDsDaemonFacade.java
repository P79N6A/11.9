package com.yeepay.g3.facade.frontend.facade.novalidate;

/**
 * @author chronos.yl
 * @createDate 16/9/9.
 * 数据通知接口
 * Data Statistics
 */
public interface FrontendDsDaemonFacade {

    /**
     * 日交易数据统计
     */
    void dailyDataStatistic();

    /**
     * 月交易数据统计
     */
    void monthlyDataStatistic();

    /**
     * 调整某日交易数据
     * @param day 格式为YYYY-MM-DD
     */
    void adjustDailyData(String day);

    /**
     * 调整某月交易数据
     * @param month 格式为YYYY-MM
     */
    void adjustMonthlyData(String month);
}
