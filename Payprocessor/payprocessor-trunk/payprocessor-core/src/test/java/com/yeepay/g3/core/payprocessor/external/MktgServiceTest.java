/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.external;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.dao.CombPayRecordDao;
import com.yeepay.g3.core.payprocessor.dao.PayRecordDao;
import com.yeepay.g3.core.payprocessor.dao.PaymentRequestDao;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.core.payprocessor.external.service.MktgService;
import com.yeepay.g3.facade.mktg.dto.DepositRequestDTO;
import com.yeepay.g3.facade.mktg.dto.DepositResponseDTO;
import com.yeepay.g3.facade.mktg.facade.PaymentFacade;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类名称: MktgServiceTest <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/20 下午1:45
 * @version: 1.0.0
 */

public class MktgServiceTest extends BaseTest {

    protected com.yeepay.g3.facade.mktg.facade.PaymentFacade paymentFacade = RemoteFacadeProxyFactory.getService(com.yeepay.g3.facade.mktg.facade.PaymentFacade.class, ExternalSystem.MKTG);

//    PaymentFacade paymentFacade = RemoteServiceFactory.getService("http://10.151.32.27:30104/mktg-hessian/hessian/PaymentFacade",
//            RemotingProtocol.HESSIAN, PaymentFacade.class);

    @Autowired
    private PayRecordDao payRecordDao;

    @Autowired
    private PaymentRequestDao paymentRequestDao;

    @Autowired
    private CombPayRecordDao combPayRecordDao;

    @Autowired
    private MktgService mktgService;

    @Test
    public void testDeposit() {
        DepositRequestDTO depositRequestDTO = new DepositRequestDTO();
        depositRequestDTO.setPaymentBizOrderNo("pp" + System.currentTimeMillis());
        depositRequestDTO.setMerchantOrderNo("out" + System.currentTimeMillis());
        depositRequestDTO.setMarketingNo("1001201806220000000164");
        depositRequestDTO.setMerchantNo("44444444");
        depositRequestDTO.setMerchantName("2222222");
        depositRequestDTO.setParentMerchantNo("3333333");
        depositRequestDTO.setTradeSysNo("DS");
        depositRequestDTO.setBizSysNo("PP");
        depositRequestDTO.setAccountingProductCode("111");
        depositRequestDTO.setSalesProductCode("222");
        depositRequestDTO.setOrderAmount(new BigDecimal(2.00));
        depositRequestDTO.setPaymentProduct("YJZF");
        depositRequestDTO.setPaymentType("DEBIT");
        depositRequestDTO.setBankCode("CCB");
        depositRequestDTO.setRequestDate(new Date(System.currentTimeMillis() - 300000));
        System.out.println("请求参数：" + depositRequestDTO);
        DepositResponseDTO depositResponseDTO = paymentFacade.deposit(depositRequestDTO);
        System.out.println("响应参数：" + depositResponseDTO);

    }

    @Test
    public void testPayment() {
        PayRecord payRecord = payRecordDao.selectByPrimaryKey("SALE1806261009331742647");
        PaymentRequest paymentRequest = paymentRequestDao.selectByPrimaryKey(payRecord.getRequestId());
        CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo("SALE1806261009331742647");
        mktgService.payment(payRecord, combPayRecord, paymentRequest);
    }
}