/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.nccashier.facade;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.enumtype.SystemEnum;
import com.yeepay.g3.core.nccashier.gateway.service.RemoteFacadeProxyFactory;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.APICflEasyFacade;
import com.yeepay.g3.facade.payprocessor.facade.PayProcessorQueryFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类名称: APICflEasyFacadeTest <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/6 下午4:34
 * @version: 1.0.0
 */

public class APICflEasyFacadeTest extends BaseTest {


//    protected APICflEasyFacade apiCflEasyFacade = RemoteFacadeProxyFactory
//            .getService(APICflEasyFacade.class, "nc-cashier-hessian");

    @Autowired
    private APICflEasyFacade apiCflEasyFacade;

    //通用参数
    private static String token = "FF1C283938B829C6F7DEDCC4B707F914D8565E1FD2A9007DFED496FD819B33AE";
    private static String userIp = "127.0.0.1";
    private static String merchantNo = "10040057338";
    private static String version = "1.0";

    //卡6项+取款密码
    private static String cardNo = "6259650890455659111";
    private static String idNo = "341021199003189753";
    private static String owner = "王志俊";
    private static String phoneNo = "15011530233";
    private static String cvv = "543";
    private static String avlidDate = "0325";
    private static String bankPWD = "123456";

    //用户信息
    private static String userNo = "du100001";
    private static String userType = "USER_ID";

    //业务方参数
    private static String bizType = "";
    private static boolean checkProduct = false;
    private static String payScene = "YJZF-WAP-USUAL";
    private static String mcc = "7993";

    //绑卡id、支付记录id
    private static String bindId = "2048883";
    private static String recordId = "69886";

    @Test
    public void testFirstPay() {
        APICflEasyFirstRequestDTO requestDTO = new APICflEasyFirstRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setUserIp(userIp);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);
        requestDTO.setCardNo(cardNo);
        requestDTO.setOwner(owner);
        requestDTO.setIdNo(idNo);
        requestDTO.setPhoneNo(phoneNo);
        requestDTO.setCvv(cvv);
        requestDTO.setValidDate(avlidDate);
        requestDTO.setUserNo(userNo);
        requestDTO.setUserType(userType);
        requestDTO.setPeriod("6");
        System.out.println("入参==="+requestDTO);
        APICflEasyPaymentResponseDTO responseDTO = apiCflEasyFacade.firstPayRequest(requestDTO);
        System.out.println("返回==="+responseDTO);




    }


    @Test
    public void testBindPay() {
        APICflEasyBindRequestDTO requestDTO = new APICflEasyBindRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setUserIp(userIp);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);

        requestDTO.setBindId(bindId);
        requestDTO.setUserNo(userNo);
        requestDTO.setUserType(userType);
        requestDTO.setPeriod("6");

        System.out.println("入参==="+requestDTO);
        APICflEasyPaymentResponseDTO responseDTO = apiCflEasyFacade.bindPayRequest(requestDTO);
        System.out.println("返回==="+responseDTO);
    }

    @Test
    public void testSmsSend() {
        APICflEasySmsSendRequestDTO requestDTO = new APICflEasySmsSendRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);

//        requestDTO.setOwner(owner);
//        requestDTO.setIdNo(idNo);
//        requestDTO.setPhoneNo(phoneNo);
        requestDTO.setCvv(cvv);
        requestDTO.setAvlidDate(avlidDate);
//        requestDTO.setBankPWD(bankPWD);
        requestDTO.setRecordId(recordId);
        System.out.println("入参==="+requestDTO);
        APIBasicResponseDTO responseDTO = apiCflEasyFacade.smsSend(requestDTO);
        System.out.println("返回==="+responseDTO);
    }

    @Test
    public void testSynConfirm() {
        APICflEasyConfirmPayRequestDTO requestDTO = new APICflEasyConfirmPayRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);

//        requestDTO.setOwner(owner);
//        requestDTO.setIdNo(idNo);
//        requestDTO.setPhoneNo(phoneNo);
//        requestDTO.setCvv(cvv);
//        requestDTO.setAvlidDate(avlidDate);
//        requestDTO.setBankPWD(bankPWD);
        requestDTO.setRecordId(recordId);

        requestDTO.setVerifyCode("798014");
        System.out.println("入参==="+requestDTO);
        APICflEasyConfirmPayResponseDTO responseDTO = apiCflEasyFacade.confirmPay(requestDTO);
        System.out.println("返回==="+responseDTO);
    }

    @Test
    public void testQueryBankInfo() {
        APICflEasyBankInfoRequestDTO requestDTO = new APICflEasyBankInfoRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);
        APICflEasyBankInfoResponseDTO responseDTO = apiCflEasyFacade.querySupportBankInfo(requestDTO);
        System.out.println(responseDTO);
    }
}