/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.facade;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.facade.ncpay.dto.SmsSendRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.SmsSendResponseDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.ncpay.facade.PaymentManageFacade;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.CashierVersion;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * 类名称: PayPreAuthFacadeTest <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 17/12/22 上午10:29
 * @version: 1.0.0
 */

public class PayPreAuthFacadeTest extends BaseTest {

//    @Autowired
//    private PayPreAuthFacade payPreAuthFacade;

	@Autowired
	private PayManageFacade payManageFacade;

    protected  PayPreAuthFacade payPreAuthFacade = RemoteFacadeProxyFactory.getService(PayPreAuthFacade.class, ExternalSystem.PP);


//    PayPreAuthFacade payPreAuthFacade = RemoteServiceFactory.getService(
//            "http://10.151.32.27:30013/payprocessor-hessian/" ,
//            RemotingProtocol.HESSIAN , PayPreAuthFacade.class);

    @Test
    public void testncPreAuthRequest() {
        NcPayOrderRequestDTO requestDTO = new NcPayOrderRequestDTO();
        requestDTO.setAmount(new BigDecimal("0.01"));
        requestDTO.setBizType(20L);
        requestDTO.setCardInfoId(2023841L);
        requestDTO.setCardInfoType(CardInfoTypeEnum.TEMP);
//        requestDTO.setCashierVersion(CashierVersion.WEB);
        requestDTO.setCustomerName("NC测试");
        requestDTO.setCustomerNumber("10040007800");
        requestDTO.setDealUniqueSerialNo("DealUnique" + System.currentTimeMillis());
        requestDTO.setGoodsInfo(
                "userua\":\"Mozilla/5.0 (Linux; Android 6.0; HUAWEI P8max Build/HUAWEIDAV-703L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrows\",\"tooluserip\":\"223.104.5.195\",terminalinfo\":\"{\"terminalId\":\"135819814\",\"terminalType\":\"OTHER\"}");
        requestDTO.setIndustryCode("7993");
        requestDTO.setMemberNO("501236613770");
        requestDTO.setMemberType(MemberTypeEnum.JOINLY);
        requestDTO.setOrderNo("orderNo" + System.currentTimeMillis());
        requestDTO.setOrderSystem("DS");
        requestDTO.setOutTradeNo("out" + System.currentTimeMillis());
        requestDTO.setPayerIp("172.18.166.69");
        requestDTO.setPayProduct("PRE_AUTH");
        requestDTO.setPayScene("YSQ-USUAL");// lindatest
        requestDTO.setPayTool("NCCASHIER");
        requestDTO.setProductName("zhangxh自测");
        requestDTO.setRequestSysId("15026991697222");
        requestDTO.setRequestSystem("nccashier");
        requestDTO.setUserFee(new BigDecimal("0.01"));
        requestDTO.setPayOrderType(PayOrderType.PREAUTH_RE);
        requestDTO.setRetailProductCode("3011001003001A");
        requestDTO.setBasicProductCode("3011001006003");
        BankCardInfoDTO bankCardInfoDTO = new BankCardInfoDTO();

        NcPayOrderResponseDTO response = payPreAuthFacade.ncPreAuthRequest(requestDTO);
        System.out.println(JSON.toJSONString(response));

    }

    @Test
    public void testSendSms() {
        NcSmsRequestDTO requestDTO = new NcSmsRequestDTO();
        requestDTO.setRecordNo("PREAUTH_RE1808101159348295340");
        requestDTO.setSmsSendType(ReqSmsSendTypeEnum.YEEPAY);
        NcSmsResponseDTO smsResponse = payManageFacade.sendSms(requestDTO);
        System.out.println(JSON.toJSONString(smsResponse));
    }

    @Test
    public void testncPreAuthComfirm() {
        NcPayConfirmRequestDTO requestDTO = new NcPayConfirmRequestDTO();
        requestDTO.setRecordNo("PREAUTH_RE1809032251555201189");
//        requestDTO.setSmsCode("489762");
        requestDTO.setTmpCardId(2024009L);
        PayRecordResponseDTO confirmResponse = payPreAuthFacade.ncPreAuthComfirm(requestDTO);
        System.out.println(JSON.toJSONString(confirmResponse));
    }

    @Test
    public void testncPreAuthCancel() {
        PreAuthCancelRequestDTO requestDTO = new PreAuthCancelRequestDTO();
        requestDTO.setBizType(20L);
        requestDTO.setRecordNo("PREAUTH_CM1808211116395143459");
        requestDTO.setOutTradeNo("out" + System.currentTimeMillis());
        requestDTO.setCancelType("PREAUTHCONFIRMCANCEL");// PREAUTHCANCEL、PREAUTHCONFIRMCANCEL
        PreAuthCancelResponseDTO responseDTO = payPreAuthFacade.ncPreAuthCancel(requestDTO);

    }

    @Test
    public void testncPreAuthComplete() {
        PreAuthCompleteRequestDTO requestDTO = new PreAuthCompleteRequestDTO();
        requestDTO.setBizType(20L);
        requestDTO.setRecordNo("PREAUTH_RE1809032251555201189");
        requestDTO.setAmount(new BigDecimal("0.01"));
        requestDTO.setOutTradeNo("out" + System.currentTimeMillis());
        requestDTO.setUserFee(new BigDecimal("0"));
        PreAuthCompleteResponseDTO responseDTO = payPreAuthFacade.ncPreAuthComplete(requestDTO);
    }
}