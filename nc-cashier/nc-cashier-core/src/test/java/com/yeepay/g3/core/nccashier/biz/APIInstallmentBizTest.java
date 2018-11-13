package com.yeepay.g3.core.nccashier.biz;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.client.HessianProxyFactory;
import com.ibm.db2.jcc.am.t;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentComfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.service.APIInstallmentFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class APIInstallmentBizTest extends BaseTest{
	
	private static Logger logger = LoggerFactory.getLogger(APIInstallmentBizTest.class);
	
	private static final String MERCHANT_NO_7800 = "10040007800";
	
	private static final String CUR_VERSION = "1.0";
	
	private static final String USER_IP = "127.0.0.1";
	
	private static final String CARD_NO_WANNENGDE = "6226388000000095";
	
	private static final String PHONE_NO_WANNEDE = "18100000000";
	
	private static final String SIGN_RELATION_ID = "10000031";
	
	private static final String VERIFY_CODE_WANNENGDE = "111111";
	
	private static final String USER_NO = "zml";
	
	private static final String USER_TYPE = "USER_ID";
	
	@Resource
	private APIInstallmentBiz apiInstallmentBiz;
	
	public static APIInstallmentRequestDTO nonSignCardN0_request(){
		APIInstallmentRequestDTO requestDTO = new APIInstallmentRequestDTO();
		requestDTO.setToken("0D207A9DB4EE36C94265EF5219D648A92412D8DA95B358B78A4FB044BBF21B53");
		requestDTO.setMerchantNo("10040007800");
		requestDTO.setUserIp(USER_IP);
		requestDTO.setCardNo("6222280015502385");
		requestDTO.setNumber("3");
		requestDTO.setVersion("1.0");
		return requestDTO;
	}
	
	public static APIInstallmentRequestDTO signCardN0_request(){
		APIInstallmentRequestDTO requestDTO = new APIInstallmentRequestDTO();
		requestDTO.setToken("757D538B3278B90C6A211E62D03E1B67DE97E69ED95BF226ED445A0FA060B4BC");
		requestDTO.setMerchantNo(MERCHANT_NO_7800);
		requestDTO.setUserIp(USER_IP);
		requestDTO.setCardNo(CARD_NO_WANNENGDE);
		requestDTO.setNumber("6");
		requestDTO.setVersion(CUR_VERSION);
		return requestDTO;
	}
	
	public static APIInstallmentRequestDTO signCardId_request() {
		APIInstallmentRequestDTO requestDTO = new APIInstallmentRequestDTO();
		requestDTO.setToken("0D207A9DB4EE36C94265EF5219D648A9CECBCF12DBE10E9591B3F590B430FEEB");
		requestDTO.setMerchantNo(MERCHANT_NO_7800);
		requestDTO.setUserIp(USER_IP);
		requestDTO.setCardNo(CARD_NO_WANNENGDE);
		requestDTO.setSignRelationId(SIGN_RELATION_ID);
		requestDTO.setNumber("6");
		requestDTO.setVersion(CUR_VERSION);
		return requestDTO;
	}
	
	public static APIInstallmentRequestDTO signCardId_nonPhoneNo(){
		APIInstallmentRequestDTO requestDTO = new APIInstallmentRequestDTO();
		requestDTO.setToken("757D538B3278B90C6A211E62D03E1B678E736FE84099E6746C5B9E5DC2A5B564");
		requestDTO.setMerchantNo(MERCHANT_NO_7800);
		requestDTO.setUserIp(USER_IP);
		requestDTO.setCardNo(CARD_NO_WANNENGDE);
		requestDTO.setSignRelationId(SIGN_RELATION_ID);
		requestDTO.setNumber("12");
		requestDTO.setVersion(CUR_VERSION);
		return requestDTO;
	}
	
	public static APIInstallmentRequestDTO signCardNo_externalUser(){
		APIInstallmentRequestDTO requestDTO = new APIInstallmentRequestDTO();
		requestDTO.setToken("CC7F933186A95360B5BAF137BCF84F3D122A0CAF71E12B88F9E60E6D463FFA9B");
		requestDTO.setMerchantNo(MERCHANT_NO_7800);
		requestDTO.setUserIp(USER_IP);
//		requestDTO.setCardNo(CARD_NO_WANNENGDE);
		requestDTO.setCardNo("4062522863921466");
//		requestDTO.setUserNo(USER_NO);
//		requestDTO.setUserType(USER_TYPE);
//		requestDTO.setSignRelationId(SIGN_RELATION_ID);
		requestDTO.setNumber("0");
		requestDTO.setVersion(CUR_VERSION);
		return requestDTO;
	}
	
	@Test
	public void requestNonSignCardN0Test() {
		// 输入卡号，非签约卡
		// APIInstallmentRequestDTO requestDTO = nonSignCardN0_request();
		// 输入卡号，签约卡
//		 APIInstallmentRequestDTO requestDTO = signCardN0_request();
		// 输入签约关系ID，签约卡
//		APIInstallmentRequestDTO requestDTO = signCardId_request();
		// 输入签约关系ID，签约卡，无手机号
//		APIInstallmentRequestDTO requestDTO =  signCardNo();
		APIInstallmentRequestDTO requestDTO = signCardNo_externalUser();
		APIBasicResponseDTO response = apiInstallmentBiz.request(requestDTO);
		logger.info("测试结果={}", response);
    }
	
	// ----------------- 发短验证 --------------------------
	public static APIInstallmentSmsRequestDTO sms_resend(){
		APIInstallmentSmsRequestDTO requestDTO = new APIInstallmentSmsRequestDTO();
		requestDTO.setMerchantNo(MERCHANT_NO_7800);
		requestDTO.setRecordId("40304");
		requestDTO.setToken("54A0B4DB24ABC1F21CC61FBC200C2A1004D4B22D18DB7959CF8EAF36D7FB1829");
		requestDTO.setVersion(CUR_VERSION);
		return requestDTO;
	}
	
	@Test
	public void smsSendTest(){
		APIInstallmentSmsRequestDTO requestDTO = sms_resend();
		APIBasicResponseDTO response = apiInstallmentBiz.smsSend(requestDTO);
		logger.info("测试结果={}", response);
	}
	
	// ----------------- 确认支付 --------------------------
	
	public static APIInstallmentComfirmRequestDTO confirm(){
		APIInstallmentComfirmRequestDTO requestDTO = new APIInstallmentComfirmRequestDTO();
		requestDTO.setMerchantNo(MERCHANT_NO_7800);
		requestDTO.setRecordId("40380");
		requestDTO.setToken("04A4804D67CAC90F86597E4D37CA97B3D0C421682E4B0F4C6B9654C10CDCED11");
		requestDTO.setVerifyCode(VERIFY_CODE_WANNENGDE);
		requestDTO.setVersion(CUR_VERSION);
		return requestDTO;
	}
	
	@Test
	public void confirmPayTest(){ 
//		APIInstallmentComfirmRequestDTO requestDTO = confirm();
//		APIBasicResponseDTO response = apiInstallmentBiz.confirmPay(requestDTO);
//		logger.info("测试结果={}", response);
		System.out.println(AESUtil.aesDecrypt("1u4C6ypWEcW5fdqoetVajQ%3D%3D"));;
	}
	
	class DoWork implements Runnable {

        @Override
        public void run() {
        		confirmPayTest();
        }

    }

    @Test
    public void test() {
        DoWork dw = new DoWork();
        Thread t1 = new Thread(dw);
        t1.start();
        try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        Thread t2 = new Thread(dw);
        t2.start();
        try {
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	
	// --------------- 查询接口 -----------------------
	
	@Test
	public void installmentInfoQueryTest(){
		InstallmentInfoRequestDTO requestDTO = new InstallmentInfoRequestDTO();
		requestDTO.setMerchantNo(MERCHANT_NO_7800);
		requestDTO.setVersion(CUR_VERSION);
		InstallmentInfoResponseDTO response = apiInstallmentBiz.queryInstallmentRateInfos(requestDTO);
		logger.info("测试结果={}", com.alibaba.fastjson.JSON.toJSONString(response));
	}
	
	@Test
	public void queryInstallment(){
//		SignRelationQueryRequestDTO requestDTO = new SignRelationQueryRequestDTO();
////		requestDTO.setMerchantNo(MERCHANT_NO_7800);
//		requestDTO.setVersion(CUR_VERSION);
//		requestDTO.setUserNo("zml");
//		requestDTO.setUserType(IdentityType.USER_ID.name());
//		SignRelationQueryResponseDTO response = apiInstallmentBiz.querySignRelationList(requestDTO);
//		logger.info("查询签约关系列表，得={}", response);
		String userNo = AESUtil.aesDecrypt("YVW5fRkAYYckxjJQwIXj0A==");
		System.out.println(userNo);
	}
	
	
	// ------- 远程调用 -----------
//	public static void main(String[] args){
//		HessianProxyFactory factory = new HessianProxyFactory();
//		String url = "http://10.151.30.8:8009/nc-cashier-hessian/hessian/APIInstallmentFacade";
//		APIInstallmentRequestDTO requestDTO = new APIInstallmentRequestDTO();
//		requestDTO.setToken("866B6FDB8D151DF3F701441B815CD3FB92FBCCA07DA14D1A32040339E432E9F1");
//		requestDTO.setMerchantNo(MERCHANT_NO_7800);
//		requestDTO.setUserIp(USER_IP);
//		requestDTO.setVersion(CUR_VERSION);
//		requestDTO.setCardNo("5239590003027978");
//		requestDTO.setNumber("6");
//		try {
//			APIInstallmentFacade apiInstallmentFacade = (APIInstallmentFacade) factory.create(APIInstallmentFacade.class, url);
//			logger.info("测试结果={}", com.alibaba.fastjson.JSON.toJSONString(apiInstallmentFacade.request(requestDTO)));
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		String text = "";
//		System.out.println(JSON.toJSONString(text));
//		
//	}
	
	public static final Map<String, String> hm = new HashMap<String, String>();
	static{
		hm.put("A", "A");
	}
	
	public static void main(String[] args){
		
	}
}
