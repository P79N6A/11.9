package com.yeepay.g3.core.nccashier.facade;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.APIYJZFFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class APIYJZFFacadeTest extends BaseTest {

    @Autowired
    private APIYJZFFacade apiyjzfFacade;

    //通用参数
    private static String token = "BF61B1634B53900886E0D10778537F6C062317F6D266C75B98BFC16F78167FF1";
    private static String userIp = "127.0.0.1";
    private static String merchantNo = "10040020578";
    private static String version = "1.0";

    //卡6项+取款密码
    private static String cardNo = "";
    private static String idNo = "";
    private static String owner = "杜瑞洋";
    private static String phoneNo = "15010050371";
    private static String cvv = "167";
    private static String avlidDate = "0822";
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
    private static String bindId = "2046776";
    private static String recordId = "50368";

    @Test
    public void testFirstPayRequest(){
        APIYJZFFirstPaymentRequestDTO requestDTO = new APIYJZFFirstPaymentRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setUserIp(userIp);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);
        requestDTO.setCardNo(cardNo);
        requestDTO.setOwner(owner);
        requestDTO.setIdNo(idNo);
        requestDTO.setPhoneNo(phoneNo);
        requestDTO.setCvv(cvv);
        requestDTO.setAvlidDate(avlidDate);
        requestDTO.setUserNo(userNo);
        requestDTO.setUserType(userType);
        requestDTO.setCheckProductOpen(checkProduct);
        requestDTO.setPayScene(payScene);
        requestDTO.setMcc(mcc);
        System.out.println("入参==="+requestDTO);
        APIYJZFFirstPaymentResponseDTO responseDTO = apiyjzfFacade.firstPayRequest(requestDTO);
        System.out.println("返回==="+responseDTO);
    }

    @Test
    public void testBindPayRequest(){
        APIYJZFBindPaymentRequestDTO requestDTO = new APIYJZFBindPaymentRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setUserIp(userIp);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);

        requestDTO.setBindId(bindId);
        requestDTO.setUserNo(userNo);
        requestDTO.setUserType(userType);

        requestDTO.setCheckProductOpen(checkProduct);
        requestDTO.setPayScene(payScene);
        requestDTO.setMcc(mcc);
        System.out.println("入参==="+requestDTO);
        APIYJZFBindPaymentResponseDTO responseDTO = apiyjzfFacade.bindPayRequest(requestDTO);
        System.out.println("返回==="+responseDTO);
    }

    @Test
    public void testSMSSend(){
        APIYJZFSendSmsRequestDTO requestDTO = new APIYJZFSendSmsRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);

        requestDTO.setOwner(owner);
        requestDTO.setIdNo(idNo);
        requestDTO.setPhoneNo(phoneNo);
        requestDTO.setCvv(cvv);
        requestDTO.setAvlidDate(avlidDate);
        requestDTO.setBankPWD(bankPWD);
        requestDTO.setRecordId(recordId);
        System.out.println("入参==="+requestDTO);
        APIBasicResponseDTO responseDTO = apiyjzfFacade.sendSMS(requestDTO);
        System.out.println("返回==="+responseDTO);
    }

    @Test
    public void testConfirmPay(){
        APIYJZFConfirmPayRequestDTO requestDTO = new APIYJZFConfirmPayRequestDTO();
        requestDTO.setToken(token);
        requestDTO.setMerchantNo(merchantNo);
        requestDTO.setVersion(version);

        requestDTO.setOwner(owner);
        requestDTO.setIdNo(idNo);
        requestDTO.setPhoneNo(phoneNo);
        requestDTO.setCvv(cvv);
        requestDTO.setAvlidDate(avlidDate);
        requestDTO.setBankPWD(bankPWD);
        requestDTO.setRecordId(recordId);

        requestDTO.setVerifyCode("798014");
        System.out.println("入参==="+requestDTO);
        APIYJZFConfirmPayResponseDTO responseDTO = apiyjzfFacade.paymentConfirm(requestDTO);
        System.out.println("返回==="+responseDTO);
    }
}
