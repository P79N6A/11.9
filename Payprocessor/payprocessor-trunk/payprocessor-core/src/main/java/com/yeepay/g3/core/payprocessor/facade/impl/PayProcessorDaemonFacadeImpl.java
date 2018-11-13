package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.NotifyBiz;
import com.yeepay.g3.core.payprocessor.biz.QueryBiz;
import com.yeepay.g3.core.payprocessor.biz.RefundBiz;
import com.yeepay.g3.facade.payprocessor.facade.novalidate.PayProcessorDaemonFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chronos.
 * @createDate 2016/11/17.
 */
@Service("payProcessorDaemonFacade")
public class PayProcessorDaemonFacadeImpl implements PayProcessorDaemonFacade {

    @Autowired
    private NotifyBiz notifyBiz;

    @Autowired
    private RefundBiz refundBiz;

    @Autowired
    private QueryBiz queryBiz;

    @Override
    public void reNotify() {
        notifyBiz.reNotify();
    }

    @Override
    public void sendToCs() {
        refundBiz.sendToCs();
    }

    @Override
    public void queryCsResult() {
        refundBiz.queryCsResult();
    }

    @Override
    public void sendToRefundCenter() {
        refundBiz.sendToRefundCenter();
    }

    @Override
    public void repairOrder() {
        queryBiz.repairOrder();
    }


    @Override
    public void repairCombOrder() {
        queryBiz.repairCombOrder();
    }
}
