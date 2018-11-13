package com.yeepay.g3.facade.frontend.facade.novalidate;

/**
 * 定时补发通知
 * @author chronos.
 * @createDate 16/8/31.
 */
public interface FrontendReNotifyDaemonFacade {

    /**
     * 定时补发通知
     * @param minutes 当前时间前n分钟
     */
    void reNotifyWX(Integer minutes);
    
    /**
     * 定时补发通知
     * @param minutes 当前时间前n分钟
     */
    void reNotifyZFB(Integer minutes);
    
    /**
     * 定时补发通知
     * @param minutes 当前时间前n分钟
     */
    void reNotifyUPOP(Integer minutes);
    
    /**
     * 定时补发通知
     * @param minutes 当前时间前n分钟
     */
    void reNotifyJD(Integer minutes);

    /**
     * 定时补发通知
     * @param minutes 当前时间前n分钟
     */
    void reNotifyQQ(Integer minutes);
}
