package com.yeepay.g3.core.nccashier.facade;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.service.UnifiedAPICashierFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class UnifiedAPICashierFacadeTest extends BaseTest{
	
	@Autowired
	private UnifiedAPICashierFacade unifiedAPICashierFacade;


	private String token = "298DDCB18642E3D4C4AE44C47071A28AB6446BC4B57E036280801F133904D879";
	private String merchantN0 = "10040020578";

	/**
	 * 微信-主扫-支付
	 */
	@Test
	public void testActiveScanWechat(){
		UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
		unifiedAPICaspayhierRequestDTO.setPayTool("SCCANPAY");
		unifiedAPICaspayhierRequestDTO.setPayType("ALIPAY");
		unifiedAPICaspayhierRequestDTO.setToken(token);
		unifiedAPICaspayhierRequestDTO.setMerchantNo(merchantN0);
		unifiedAPICaspayhierRequestDTO.setVersion("1.0");
		unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
		Map<String,String> extParam = new HashMap<String, String>();
		extParam.put("specifyChannelCodes","specifyChannelCodes");
		unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
		System.out.println("入参=" + JSON.toJSONString(unifiedAPICaspayhierRequestDTO));
		UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 微信-被扫-支付
	 */
	@Test
	public void testPassiveScanWechat(){
		UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
		unifiedAPICaspayhierRequestDTO.setPayTool("MSCANPAY");
		unifiedAPICaspayhierRequestDTO.setPayType("UPOP");
		unifiedAPICaspayhierRequestDTO.setToken(token);
		unifiedAPICaspayhierRequestDTO.setMerchantNo(merchantN0);
		unifiedAPICaspayhierRequestDTO.setMerchantStoreNo("12332222");
		unifiedAPICaspayhierRequestDTO.setMerchantTerminalId("USER_ID");
		unifiedAPICaspayhierRequestDTO.setPayEmpowerNo("123");
		unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
		unifiedAPICaspayhierRequestDTO.setVersion("1.0");
		Map<String,String> extParam = new HashMap<String, String>();
		extParam.put("specifyChannelCodes","specifyChannelCodes");
		unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
		System.out.println("入参=" + unifiedAPICaspayhierRequestDTO);
		UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 微信公众号-支付
	 */
	@Test
	public void testWechatOpenId(){
		UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
		unifiedAPICaspayhierRequestDTO.setPayTool("WECHAT_OPENID");
		unifiedAPICaspayhierRequestDTO.setPayType("WECHAT");
		unifiedAPICaspayhierRequestDTO.setToken(token);
		unifiedAPICaspayhierRequestDTO.setMerchantNo(merchantN0);
		unifiedAPICaspayhierRequestDTO.setAppId("wx9e13bd68a8f1921e");
		unifiedAPICaspayhierRequestDTO.setOpenId("obwCas9Gaw3cKBw3Wg");
		unifiedAPICaspayhierRequestDTO.setVersion("1.0");
		unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
		Map<String,String> extParam = new HashMap<String, String>();
		extParam.put("specifyChannelCodes","specifyChannelCodes");
		unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
		System.out.println("入参=" + unifiedAPICaspayhierRequestDTO);
		UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 支付宝生活号-支付
	 */
	@Test
	public void testAliLifeNo(){
		UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
		unifiedAPICaspayhierRequestDTO.setPayTool(PayTool.ZFB_SHH.name());
		unifiedAPICaspayhierRequestDTO.setPayType("ALIPAY");
		unifiedAPICaspayhierRequestDTO.setToken(token);
		unifiedAPICaspayhierRequestDTO.setMerchantNo(merchantN0);
		unifiedAPICaspayhierRequestDTO.setAppId("2017080207997311");
		unifiedAPICaspayhierRequestDTO.setOpenId("2088002364008751");
		unifiedAPICaspayhierRequestDTO.setVersion("1.0");
		unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
		Map<String,String> extParam = new HashMap<String, String>();
		extParam.put("specifyChannelCodes","specifyChannelCodes");
		unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
		System.out.println("入参=" + unifiedAPICaspayhierRequestDTO);
		UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 绑卡支付-请求获取补充项
	 */
	@Test
	public void testRequestPayment(){
		ApiBindPayPaymentRequestDTO needItemRequestDTO = new ApiBindPayPaymentRequestDTO();
		needItemRequestDTO.setToken(token);
		needItemRequestDTO.setMerchantNo(merchantN0);
		needItemRequestDTO.setBindId("2024610");
		needItemRequestDTO.setUserNo("duruiyang123");
		needItemRequestDTO.setUserType("USER_ID");
		needItemRequestDTO.setVersion("1.0");
		needItemRequestDTO.setUserIp("127.0.0.1");
		System.out.println("入参=" + needItemRequestDTO);
		ApiBindPayPaymentResponseDTO pay = unifiedAPICashierFacade.requestPayment(needItemRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 绑卡支付-请求发短验
	 */
	@Test
	public void testRequestSmsSend(){
		ApiBindPaySendSmsRequestDTO requestSmsRequest = new ApiBindPaySendSmsRequestDTO();
		requestSmsRequest.setToken(token);
		requestSmsRequest.setVersion("1.0");
		System.out.println("入参=" + requestSmsRequest);
		ApiBindPaySendSmsResponseDTO pay = unifiedAPICashierFacade.requestSmsSend(requestSmsRequest);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 绑卡支付-请求确认支付
	 */
	@Test
	public void testConfirmPayment(){
		ApiBindPayConfirmRequestDTO confirmPayRequestDTO = new ApiBindPayConfirmRequestDTO();
		confirmPayRequestDTO.setToken(token);
		confirmPayRequestDTO.setVersion("1.0");
		confirmPayRequestDTO.setVerifyCode("823594");
		System.out.println("入参=" + confirmPayRequestDTO);
		ApiBindPayConfirmResponseDTO pay = unifiedAPICashierFacade.confirmPayment(confirmPayRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 微信-SDK-支付
	 */
	@Test
	public void testWechatSDK(){
		UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
		unifiedAPICaspayhierRequestDTO.setPayTool("EWALLET");
		unifiedAPICaspayhierRequestDTO.setPayType("WECHAT");
		unifiedAPICaspayhierRequestDTO.setToken(token);
		unifiedAPICaspayhierRequestDTO.setMerchantNo(merchantN0);
		unifiedAPICaspayhierRequestDTO.setVersion("1.0");
		unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
		Map<String,String> extParam = new HashMap<String, String>();
		/*extParam.put("specifyChannelCodes","specifyChannelCodes");*/
		unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
		System.out.println("入参=" + JSON.toJSONString(unifiedAPICaspayhierRequestDTO));
		UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	/**
	 * 微信-小程序-支付
	 */
	@Test
	public void testWechatMiniProgram(){
		UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
		unifiedAPICaspayhierRequestDTO.setPayTool(PayTool.XCX_OFFLINE_ZF.name());
		unifiedAPICaspayhierRequestDTO.setPayType("WECHAT");
		unifiedAPICaspayhierRequestDTO.setToken(token);
		unifiedAPICaspayhierRequestDTO.setMerchantNo(merchantN0);
		unifiedAPICaspayhierRequestDTO.setVersion("1.0");
		unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
		unifiedAPICaspayhierRequestDTO.setAppId("wx759cf58f8c34056c");
		unifiedAPICaspayhierRequestDTO.setOpenId("ossLG5VICiU-cV2QJzELe_nKUyuQ");
		Map<String,String> extParam = new HashMap<String, String>();
		unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
		System.out.println("入参=" + JSON.toJSONString(unifiedAPICaspayhierRequestDTO));
		UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
		System.out.println("返回结果=" + pay);
	}

	public static void main(String[] args) {
		UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
		unifiedAPICaspayhierRequestDTO.setPayTool("SCCANPAY");
		unifiedAPICaspayhierRequestDTO.setPayType("ALIPAY");
		unifiedAPICaspayhierRequestDTO.setToken("token");
		unifiedAPICaspayhierRequestDTO.setMerchantNo("merchantNo");
		unifiedAPICaspayhierRequestDTO.setVersion("1.0");
		unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
		Map<String,String> extParam = new HashMap<String, String>();
		extParam.put("specifyChannelCodes","specifyChannelCodes");
		unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
		System.out.println("入参=" + JSON.toJSONString(unifiedAPICaspayhierRequestDTO));
	}
}
