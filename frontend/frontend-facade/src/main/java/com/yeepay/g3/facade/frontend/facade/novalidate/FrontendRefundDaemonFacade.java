package com.yeepay.g3.facade.frontend.facade.novalidate;

/**
 * 退款定时
 * @author chronos.
 * @createDate 16/8/4.
 */
public interface FrontendRefundDaemonFacade {
    /**
     * 定时退款
     * @param minutes 当前时间前n分钟
     */
    void errorRefundWX(int minutes);
    
    /**
     * 定时退款
     * @param minutes 当前时间前n分钟
     */
    void errorRefundZFB(int minutes);

    /**
     * 定时退款
     * @param minutes 当前时间前n分钟
     */
    void errorRefundUPOP(int minutes);
    
    /**
     * 定时退款
     * @param minutes 当前时间前n分钟
     */
    void errorRefundJD(int minutes);

    /**
     * 定时退款
     * @param minutes 当前时间前n分钟
     */
    void errorRefundQQ(int minutes);
    
    /**
     * 通过时间段定时退款,时间格式为yyyyMMddHHmm
     * @param start 开始时间
     * @param end 结束时间
     */
//    void errorRefundByDate(String start,String end);

}
