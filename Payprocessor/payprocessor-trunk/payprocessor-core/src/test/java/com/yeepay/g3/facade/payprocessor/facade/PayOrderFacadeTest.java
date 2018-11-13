
package com.yeepay.g3.facade.payprocessor.facade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.CashierVersion;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;

/**
 * @author peile.fan
 *
 */
public class PayOrderFacadeTest extends BaseTest {

//	@Autowired
//	private PayOrderFacade payOrderFacade;

//	protected  PayOrderFacade payOrderFacade = RemoteFacadeProxyFactory.getService(PayOrderFacade.class, ExternalSystem.PP);

//	PayOrderFacade payOrderFacade = RemoteServiceFactory.getService(
//            "http://10.151.32.27:30013/payprocessor-hessian/" ,
//            RemotingProtocol.HTTPINVOKER , PayOrderFacade.class);

//	PayManageFacade payManageFacade = RemoteServiceFactory.getService(
//			"http://10.132.2.22:30013/payprocessor-hessian/" ,
//			RemotingProtocol.HTTPINVOKER , PayManageFacade.class);

//	PayOrderFacade payOrderFacade = RemoteServiceFactory.getService(
//			"http://10.132.2.22:30013/payprocessor-hessian/" ,
//			RemotingProtocol.HESSIAN , PayOrderFacade.class);

	PayOrderFacade payOrderFacade = RemoteServiceFactory.getService(
			"http://payprocessor-hessian.zfcpx.k8s.yp/payprocessor-hessian/" ,
			RemotingProtocol.HESSIAN , PayOrderFacade.class);

//
	@Test
	public void testNcRequest() {
		NcPayOrderRequestDTO requestDTO = new NcPayOrderRequestDTO();
		requestDTO.setAmount(new BigDecimal("1.00"));
		requestDTO.setBizType(9L);
//		requestDTO.setCardInfoId(1944357L);
		requestDTO.setCardInfoType(CardInfoTypeEnum.TEMP);
		requestDTO.setCashierVersion(CashierVersion.WEB);
		requestDTO.setCustomerName("NC测试");
		requestDTO.setCustomerNumber("10040003895");
		requestDTO.setDealUniqueSerialNo("1502699169722445");
		requestDTO.setGoodsInfo(
				"userua\":\"Mozilla/5.0 (Linux; Android 6.0; HUAWEI P8max Build/HUAWEIDAV-703L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrows\",\"tooluserip\":\"223.104.5.195\",terminalinfo\":\"{\"terminalId\":\"135819814\",\"terminalType\":\"OTHER\"}");
		requestDTO.setIndustryCode("7993");
		requestDTO.setMemberNO("501235936166");
		requestDTO.setMemberType(MemberTypeEnum.JOINLY);
		requestDTO.setOrderNo("orderNo" + System.currentTimeMillis());
		requestDTO.setOrderSystem("DS");
		requestDTO.setOutTradeNo("outTraderNo:1502699169722");
		requestDTO.setPayerIp("172.18.166.69");
		requestDTO.setPayProduct("NCPAY");
		requestDTO.setPayScene("WAP");
		requestDTO.setProductName("zhangxh自测");
		requestDTO.setRequestSysId("150269916972244");
		requestDTO.setRequestSystem("nccashier");
		requestDTO.setUserFee(new BigDecimal("0.01"));
		requestDTO.setPayOrderType(PayOrderType.SALE);
		requestDTO.setRetailProductCode("3011001003001A");
		requestDTO.setBasicProductCode("3011001006003");

		BankCardInfoDTO cardInfoDTO = new BankCardInfoDTO();
		cardInfoDTO.setBankCode("CMBCHINA");
		cardInfoDTO.setBankName("招商银行");
		cardInfoDTO.setBankMobile("15011530233");
		cardInfoDTO.setCardNo("6214830158565928");
		cardInfoDTO.setCardType("DEBIT");
		cardInfoDTO.setIdCard("341021199003189753");
		cardInfoDTO.setIdCardType("ID");
		cardInfoDTO.setOwner("王志俊");
		requestDTO.setBankCardInfoDTO(cardInfoDTO);

		NcPayOrderResponseDTO response = payOrderFacade.ncRequest(requestDTO);
		System.out.println(JSON.toJSONString(response));
	}

	@Test
	public void testOpenPay() {
		OpenPayRequestDTO openPayRequestDTO = new OpenPayRequestDTO();
		openPayRequestDTO.setOpenId("1");
		openPayRequestDTO.setPayScene("1");
				openPayRequestDTO.setPayBusinessType(PayBusinessType.DC);
		openPayRequestDTO.setPlatformType(PlatformType.WECHAT);
		OpenPayResponseDTO openPayResponseDTO = payOrderFacade.openRequest(openPayRequestDTO);
	}

	@Test
	public void testOpenPrePay(){
		OpenPrePayRequestDTO openPrePayRequestDTO = new OpenPrePayRequestDTO();
		openPrePayRequestDTO.setAppId("wx123456789");
		openPrePayRequestDTO.setCustomerNumber("10040007800");
		openPrePayRequestDTO.setBasicProductCode("3011001001001");
		openPrePayRequestDTO.setDealUniqueSerialNo(System.currentTimeMillis()+"");
		openPrePayRequestDTO.setIndustryCode("7993");
		openPrePayRequestDTO.setPayBusinessType(PayBusinessType.DC);
		openPrePayRequestDTO.setRequestSystem("NCCASHIER");
		openPrePayRequestDTO.setRetailProductCode("3011001003001A");
		openPrePayRequestDTO.setBasicProductCode("3011001006003");
		openPrePayRequestDTO.setCustomerLevel("V");
		openPrePayRequestDTO.setTotalAmount(new BigDecimal("10"));
		openPrePayRequestDTO.setPlatformType("ALIPAY");
		Map<String, String> extParam = new HashMap<String, String>();
		String specifyChannelCodes = "dddddd|eeeeee|ffffff";
		extParam.put("specifyChannelCodes", specifyChannelCodes);
		openPrePayRequestDTO.setExtParam(extParam);
		OpenPrePayResponseDTO response = payOrderFacade.openPrePay(openPrePayRequestDTO);
		System.out.println(JSON.toJSONString(response));
	}

	@Test
	public void testAccountPay() {
	    AccountPayRequestDTO accountPayRequestDTO = new AccountPayRequestDTO();
	    accountPayRequestDTO.setCashierVersion(CashierVersion.H5);
	    accountPayRequestDTO.setRequestSysId(UUID.randomUUID().toString());
	    accountPayRequestDTO.setRequestSystem("nccashier");
	    accountPayRequestDTO.setOrderSystem("BC");
	    accountPayRequestDTO.setOutTradeNo(UUID.randomUUID().toString());
	    accountPayRequestDTO.setProductName("测试商品");
	    accountPayRequestDTO.setPayProduct("ACCOUNTPAY");
	    accountPayRequestDTO.setOrderNo(UUID.randomUUID().toString());
	    accountPayRequestDTO.setDealUniqueSerialNo(UUID.randomUUID().toString());
	    accountPayRequestDTO.setPayOrderType(PayOrderType.ACCOUNT);
	    accountPayRequestDTO.setAmount(new BigDecimal(100));
	    accountPayRequestDTO.setUserFee(new BigDecimal("0.01"));
	    accountPayRequestDTO.setCustomerName("测试商编");
	    accountPayRequestDTO.setCustomerNumber("10040008070");
	    accountPayRequestDTO.setDebitCustomerNo("10040008081");
	    accountPayRequestDTO.setDebitUserName("123456");
	    accountPayRequestDTO.setRetailProductCode("123456");
	    accountPayRequestDTO.setBasicProductCode("123456");
	    Map<String , String> extInfo = new HashMap<String, String>();
	    extInfo.put("historyType", "ADJUST");
	    extInfo.put("bizType", "ADJUST");
	    accountPayRequestDTO.setExtInfo(extInfo);
	  
	    AccountPayResponseDTO responseDTO = payOrderFacade.accountPay(accountPayRequestDTO);
	    System.out.println(responseDTO);
	    
	    try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}


	@Test
	public void testNcRequestWithComb() {
		NcPayOrderRequestDTO requestDTO = new NcPayOrderRequestDTO();
		requestDTO.setAmount(new BigDecimal("20.00"));
		requestDTO.setBizType(9L);
//		requestDTO.setCardInfoId(1944357L);
		requestDTO.setCardInfoType(CardInfoTypeEnum.TEMP);
		requestDTO.setCashierVersion(CashierVersion.WEB);
		requestDTO.setCustomerName("2222222");
		requestDTO.setCustomerNumber("44444444");
		requestDTO.setDealUniqueSerialNo("deal" + System.currentTimeMillis());
		requestDTO.setGoodsInfo(
				"userua\":\"Mozilla/5.0 (Linux; Android 6.0; HUAWEI P8max Build/HUAWEIDAV-703L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrows\",\"tooluserip\":\"223.104.5.195\",terminalinfo\":\"{\"terminalId\":\"135819814\",\"terminalType\":\"OTHER\"}");
		requestDTO.setIndustryCode("7993");
		requestDTO.setMemberNO("501235936166");
		requestDTO.setMemberType(MemberTypeEnum.JOINLY);
		requestDTO.setOrderNo("orderNo" + System.currentTimeMillis());
		requestDTO.setOrderSystem("DS");
		requestDTO.setOutTradeNo("outTraderNo:1502699169723");
		requestDTO.setPayerIp("172.18.166.69");
		requestDTO.setPayProduct("NCPAY");
		requestDTO.setPayScene("WAP");
		requestDTO.setProductName("zhangxh自测");
		requestDTO.setRequestSysId("1502699169722");
		requestDTO.setRequestSystem("nccashier");
		requestDTO.setUserFee(new BigDecimal("0.01"));
		requestDTO.setPayOrderType(PayOrderType.SALE);
		requestDTO.setRetailProductCode("3011001003001A");
		requestDTO.setBasicProductCode("3011001006003");

		BankCardInfoDTO cardInfoDTO = new BankCardInfoDTO();
		cardInfoDTO.setBankCode("CCB");// CMBCHINA
		cardInfoDTO.setBankName("中国建设银行");// 招商银行
		cardInfoDTO.setBankMobile("15011530233");
		cardInfoDTO.setCardNo("6227000012540185327");// 6214830158565928
		cardInfoDTO.setCardType("DEBIT");
		cardInfoDTO.setIdCard("341021199003189753");
		cardInfoDTO.setIdCardType("ID");
		cardInfoDTO.setOwner("王志俊");
		requestDTO.setBankCardInfoDTO(cardInfoDTO);

		CombRequestDTO combRequestDTO = new CombRequestDTO();
		combRequestDTO.setPayOrderType("MKTG");
		combRequestDTO.setMarketingNo("1001201806220000000164");
		requestDTO.setCombRequestDTO(combRequestDTO);

		NcPayOrderResponseDTO response = payOrderFacade.ncRequest(requestDTO);
		System.out.println("响应参数：" + response);
	}


	@Test
	public void testAccountPayComb() {
		AccountPayRequestDTO accountPayRequestDTO = new AccountPayRequestDTO();
		accountPayRequestDTO.setCashierVersion(CashierVersion.H5);
		accountPayRequestDTO.setRequestSysId(UUID.randomUUID().toString());
		accountPayRequestDTO.setRequestSystem("nccashier");
		accountPayRequestDTO.setOrderSystem("BC");
		accountPayRequestDTO.setOutTradeNo(UUID.randomUUID().toString());
		accountPayRequestDTO.setProductName("测试商品");
		accountPayRequestDTO.setPayProduct("YJZF");
		accountPayRequestDTO.setOrderNo(UUID.randomUUID().toString());
		accountPayRequestDTO.setDealUniqueSerialNo(UUID.randomUUID().toString());
		accountPayRequestDTO.setPayOrderType(PayOrderType.ACCOUNT);
		accountPayRequestDTO.setAmount(new BigDecimal(100));
		accountPayRequestDTO.setUserFee(new BigDecimal("0.01"));
		accountPayRequestDTO.setCustomerName("测试商编");
		accountPayRequestDTO.setCustomerNumber("10040008070");
		accountPayRequestDTO.setDebitCustomerNo("10040008081");
		accountPayRequestDTO.setDebitUserName("123456");
		accountPayRequestDTO.setRetailProductCode("123456");
		accountPayRequestDTO.setBasicProductCode("123456");
		Map<String , String> extInfo = new HashMap<String, String>();
		extInfo.put("historyType", "ADJUST");
		extInfo.put("bizType", "ADJUST");
		accountPayRequestDTO.setExtInfo(extInfo);

		CombRequestDTO combRequestDTO = new CombRequestDTO();
		combRequestDTO.setPayOrderType("MKTG");
		combRequestDTO.setMarketingNo("1001201806220000000164");
		accountPayRequestDTO.setCombRequestDTO(combRequestDTO);

		AccountSyncPayResponseDTO responseDTO = payOrderFacade.accountSyncPay(accountPayRequestDTO);
		System.out.println(responseDTO);
	}


	@Test
	public void testPersonalMemberSyncPay() {
		PersonalMemberSyncPayRequestDTO requestDTO = new PersonalMemberSyncPayRequestDTO();
		requestDTO.setMemberNo("212468327627");
		requestDTO.setRequestSysId(UUID.randomUUID().toString());
		requestDTO.setRequestSystem("nccashier");
		requestDTO.setOrderSystem("BC");
		requestDTO.setOrderNo("orderNo" + System.currentTimeMillis());
		requestDTO.setOutTradeNo(UUID.randomUUID().toString());
		requestDTO.setAmount(new BigDecimal(0.5));
		requestDTO.setPayOrderType(PayOrderType.MEMBER_PAY);
		requestDTO.setRetailProductCode("3011001003001A");
		requestDTO.setBasicProductCode("3011001006003");
		requestDTO.setPayerIp("172.18.166.69");
		requestDTO.setPayProduct("GRHY");
		requestDTO.setCustomerNumber("10040020578");
		requestDTO.setCustomerName("啥");
		requestDTO.setDealUniqueSerialNo("deal" + System.currentTimeMillis());
		requestDTO.setProductName("test");

		CombRequestDTO combRequestDTO = new CombRequestDTO();
		combRequestDTO.setPayOrderType("MKTG");
		combRequestDTO.setMarketingNo("111");

		PersonalMemberSyncPayResponseDTO responseDTO = payOrderFacade.personalMemberSyncPay(requestDTO);
		System.out.println(responseDTO);

	}
	
	
}
