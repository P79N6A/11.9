package com.yeepay.g3.core.nccashier.facade;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.service.NccashierDataEncryptFacade;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by ruiyang.du on 2017/8/7.
 */
public class NccashierDataEncryptFacadeTest extends BaseTest {

    @Resource
    private NccashierDataEncryptFacade nccashierDataEncryptFacade;

    @Test
    public void testStopWorkingTask() {
       // NccashierDataEncryptFacade nccashierDataEncryptFacade = RemoteServiceFactory.getService("http://10.151.30.8:8009/nc-cashier-hessian/", RemotingProtocol.HESSIAN,NccashierDataEncryptFacade.class);
        String s = nccashierDataEncryptFacade.stopWorkingTask();
        System.out.println("结果为：" + s);
    }

    @Test
    public void testBatchEncryptData() {
        //String s1 = nccashierDataEncryptFacade.stopWorkingTask();
        String s = nccashierDataEncryptFacade.batchEncryptData(5, 10, 256,1000000,1500000,50000,5000,100,"PaymentRequest");
        System.out.println("结果为： " + s);


        /*NccashierDataEncryptFacade nccashierDataEncryptFacade = RemoteServiceFactory.getService("http://10.151.30.8:8009/nc-cashier-hessian/", RemotingProtocol.HESSIAN,NccashierDataEncryptFacade.class);
        String s = nccashierDataEncryptFacade.batchEncryptData(0,20000,20000,5000,100,"PaymentRequest");
        System.out.println("结果为： " + s);*/
    }
}
