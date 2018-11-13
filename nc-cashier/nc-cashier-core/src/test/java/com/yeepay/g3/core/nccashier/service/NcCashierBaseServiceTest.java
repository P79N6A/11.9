package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;

/**
 * Created by jimin.zhou on 18/1/16.
 */
public class NcCashierBaseServiceTest extends BaseTest {
    @Qualifier("riskControlService")
    @Resource
    private NcCashierBaseService ncCashierBaseService;

    @Test
    public void riskInfoTest(){
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setTradeRiskInfo("{\"referer\":\"http://qa.yeepay.com/app-airsupport/AirSupportService.action\"}");
        paymentRequest.setTerminalInfo("{\"terminalId\":\"34\",\"terminalType\":\"12\"}");

//        ncCashierBaseService.buildTradeRiskInfoUseTokenAndRequest("45abe8f9-1499-4358-bc25-968f894b8260",paymentRequest);
//        ncCashierBaseService.buildTradeRiskInfoByTooluseripAndRequest("192.168.1.1",paymentRequest);
        ncCashierBaseService.buildTradeRiskInfoByUseripAndRequest("192.168.1.1",paymentRequest);

    }
}
