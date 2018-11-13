package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.facade.payprocessor.facade.novalidate.PayProcessorDaemonFacade;


public class PayProcessorDaemonFacadeTest extends BaseTest {

//    @Autowired
//    private PayProcessorDaemonFacade facade;

    PayProcessorDaemonFacade facade = RemoteServiceFactory.getService(
            "http://10.132.2.22:30013/payprocessor-hessian/" ,
            RemotingProtocol.HESSIAN , PayProcessorDaemonFacade.class);

    @Test
    public void repairOrder() {
        facade.repairOrder();
    }


    @Test
    public void repairCombOrder() {
        facade.repairCombOrder();
    }


    @Test
    public void sendToRefundCenter() {
        facade.sendToRefundCenter();
    }
}
