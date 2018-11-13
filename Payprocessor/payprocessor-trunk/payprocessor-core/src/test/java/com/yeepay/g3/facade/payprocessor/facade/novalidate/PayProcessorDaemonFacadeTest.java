package com.yeepay.g3.facade.payprocessor.facade.novalidate;

import com.yeepay.g3.core.payprocessor.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chronos.
 * @createDate 2016/11/17.
 */
public class PayProcessorDaemonFacadeTest extends BaseTest {

    @Autowired
    private PayProcessorDaemonFacade payProcessorDaemonFacade;

    @Test
    public void reNotify() throws Exception {
        payProcessorDaemonFacade.reNotify();
    }

    @Test
    public void sendToCs() throws Exception {
        payProcessorDaemonFacade.sendToCs();
    }

    @Test
    public void queryCsResult() throws Exception {
        payProcessorDaemonFacade.queryCsResult();
    }

    @Test
    public void sendToRefundCenter() throws Exception {
        payProcessorDaemonFacade.sendToRefundCenter();
    }


    @Test
    public void testRepairOrder() throws Exception{
        payProcessorDaemonFacade.repairOrder();
    }

}