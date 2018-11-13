package com.yeepay.g3.facade.payprocessor.facade.novalidate;

/**
 * 定时任务接口
 * @author chronos.
 * @createDate 2016/11/17.
 */
public interface PayProcessorDaemonFacade {
    /**
     * 定时向订单处理器发送通知
     * 发送七天内订单成功但订单处理器状态为DOING的订单
     */
    void reNotify();

    /**
     * 定时将已受理的冲正请求发送到清算中心
     */
    void sendToCs();

    /**
     * 定时查询清算中心处理状态
     */
    void queryCsResult();

    /**
     * 定时将清算中心处理成功的冲正请求发送到退款中心
     */
    void sendToRefundCenter();

    /**
     * 定时补单
     */
    void repairOrder();

    /**
     * 定时补单
     * 第一支付单成功，但是第二支付单失败的订单
     */
    void repairCombOrder();
}
