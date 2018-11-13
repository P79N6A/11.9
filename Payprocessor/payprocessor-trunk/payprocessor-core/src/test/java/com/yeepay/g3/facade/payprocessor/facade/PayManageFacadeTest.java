
package com.yeepay.g3.facade.payprocessor.facade;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SignCardIdEnum;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.facade.cwh.facade.PayTmpCardCwhFacade;
import com.yeepay.g3.facade.cwh.param.PayTmpCardDTO;
import com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @author peile.fan
 *
 */
public class PayManageFacadeTest extends BaseTest {

//	@Autowired
//	private PayManageFacade payManageFacade;

	protected  PayManageFacade payManageFacade = RemoteFacadeProxyFactory.getService(PayManageFacade.class, ExternalSystem.PP);

	protected PayTmpCardCwhFacade payTmpCardCwhFacade = RemoteFacadeProxyFactory.getService(PayTmpCardCwhFacade.class,
			ExternalSystem.NCCWH);

	@Test
	public void testSendSms() {
		NcSmsRequestDTO requestDTO = new NcSmsRequestDTO();
		requestDTO.setRecordNo("SALE1707031619366227911");
		requestDTO.setSmsSendType(ReqSmsSendTypeEnum.YEEPAY);
		NcSmsResponseDTO smsResponse = payManageFacade.sendSms(requestDTO);
		System.out.println(JSON.toJSONString(smsResponse));
	}

	@Test
	public void testConfirmPay() {
		NcPayConfirmRequestDTO requestDTO = new NcPayConfirmRequestDTO();
		requestDTO.setRecordNo("SALE1806211106161564432");
//		requestDTO.setSmsCode("302714");
		NcPayConfirmResponseDTO confirmResponse = payManageFacade.confirmPay(requestDTO);
		System.out.println(JSON.toJSONString(confirmResponse));
	}

	@Test
	public void testSynConfirmPay() {
		NcPayConfirmRequestDTO requestDTO = new NcPayConfirmRequestDTO();
		PayTmpCardDTO tmpCard = new PayTmpCardDTO();
//		tmpCard.setCardNo("6225758340891885");
//		tmpCard.setCardCvn2("152");
//		tmpCard.setCardExpireDate("1219");
//		long tmpCardId = payTmpCardCwhFacade.add(tmpCard);
//		System.out.println("tmpCardId=" + tmpCardId);
		requestDTO.setRecordNo("SALE1806261611569638755");
//		requestDTO.setSmsCode("129354");
		//requestDTO.setTmpCardId(tmpCardId);
		PayRecordResponseDTO responseDTO = payManageFacade.synConfirmPay(requestDTO);
		System.out.println(JSON.toJSONString("返回结果" + responseDTO));
	}

	@Test
	public void testCflOpenAndPay() {
		NcPayCflOpenRequestDTO requestDTO = new NcPayCflOpenRequestDTO();
		requestDTO.setCflCount(6);
		requestDTO.setCflRate(new BigDecimal("0.005"));
		requestDTO.setMerchantFeeSubsidy(new BigDecimal("0.002"));
		requestDTO.setMerchantAmountSubsidy(new BigDecimal("5"));
		requestDTO.setOutTradeNo("Out99" + System.currentTimeMillis());
		requestDTO.setDealUniqueSerialNo("Deal99" + System.currentTimeMillis());
		requestDTO.setPayProduct("NCPAY");
		requestDTO.setPayOrderType(PayOrderType.BK_CFL);
		requestDTO.setOrderSystem("ordersystem");
		requestDTO.setRequestSystem("nccashier");
		requestDTO.setRequestSysId("1502699169722");
		requestDTO.setOrderNo("order99" + System.currentTimeMillis());
		requestDTO.setAmount(new BigDecimal("500.00"));
		requestDTO.setUserFee(new BigDecimal("0.01"));
		requestDTO.setCustomerNumber("test");
		requestDTO.setCustomerName("NC测试");
		requestDTO.setProductName("zhangxh自测");
		requestDTO.setPayerIp("172.18.166.69");
		requestDTO.setIndustryCode("7993");
		requestDTO.setGoodsInfo(
				"userua\":\"Mozilla/5.0 (Linux; Android 6.0; HUAWEI P8max Build/HUAWEIDAV-703L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrows\",\"tooluserip\":\"223.104.5.195\",terminalinfo\":\"{\"terminalId\":\"135819814\",\"terminalType\":\"OTHER\"}");
		requestDTO.setBizType(5L);
		requestDTO.setMemberType(MemberTypeEnum.JOINLY);
		requestDTO.setMemberNO("501235936166");
		requestDTO.setSignCardIdType(SignCardIdEnum.SIGN_INFO);
		requestDTO.setSignCardId(123456789L);
		requestDTO.setCardNo("6226388000000095");
		requestDTO.setPageCallBack("http://www.baidu.com");
		requestDTO.setPayScene("WAP");
		requestDTO.setRetailProductCode("3011001003001A");
		requestDTO.setBasicProductCode("3011001006003");

		NcPayCflOpenResponseDTO responseDTO = payManageFacade.ncpayCflOpenAndPay(requestDTO);
	}

	@Test
	public void testCflRequest() {
		NcPayCflOrderRequestDTO requestDTO = new NcPayCflOrderRequestDTO();
		requestDTO.setCflCount(6);
		requestDTO.setCflRate(new BigDecimal("0.005"));
		requestDTO.setMerchantFeeSubsidy(new BigDecimal("0.002"));
		requestDTO.setMerchantAmountSubsidy(new BigDecimal("5"));
		requestDTO.setOutTradeNo("Out99" + System.currentTimeMillis());
		requestDTO.setDealUniqueSerialNo("Deal99" + System.currentTimeMillis());
		requestDTO.setPayProduct("NCPAY");
		requestDTO.setPayOrderType(PayOrderType.BK_CFL);
		requestDTO.setOrderSystem("DS");
		requestDTO.setRequestSystem("nccashier");
		requestDTO.setRequestSysId("1502699169722");
		requestDTO.setOrderNo("order99" + System.currentTimeMillis());
		requestDTO.setAmount(new BigDecimal("500.00"));
		requestDTO.setUserFee(new BigDecimal("0.01"));
		requestDTO.setCustomerNumber("test");
		requestDTO.setCustomerName("test");
		requestDTO.setProductName("zhangxh自测");
		requestDTO.setPayerIp("172.18.166.69");
		requestDTO.setIndustryCode("7993");
//		requestDTO.setGoodsInfo(
//				"userua\":\"Mozilla/5.0 (Linux; Android 6.0; HUAWEI P8max Build/HUAWEIDAV-703L) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrows\",\"tooluserip\":\"223.104.5.195\",terminalinfo\":\"{\"terminalId\":\"135819814\",\"terminalType\":\"OTHER\"}");
		requestDTO.setBizType(9L);
		requestDTO.setMemberType(MemberTypeEnum.JOINLY);
		requestDTO.setMemberNO("501235936166");
		requestDTO.setSignCardIdType(SignCardIdEnum.SIGN_INFO);
		requestDTO.setSignCardId(200000043L);
		requestDTO.setPayScene("WAP");
		requestDTO.setRetailProductCode("3011001003001A");
		requestDTO.setBasicProductCode("3011001006003");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("registTime", "2016-09-09 12:12:12");
		jsonObject.put("registId", "testtest");
		jsonObject.put("userRegisterMobile", "15011530233");
		jsonObject.put("terminalId", "135819814");
		jsonObject.put("terminalType", "OTHER");
		jsonObject.put("userIp", "223.104.5.195");
		jsonObject.put("os", "1");
		jsonObject.put("transPhoneType", "1");
		jsonObject.put("goodsName", "测试商品");
		jsonObject.put("customerId", "123456789");
		jsonObject.put("merchantNoSec", "1111111");
		jsonObject.put("goodsCodeSec", "22222");
		requestDTO.setGoodsInfo(jsonObject.toJSONString());

		NcPayCflOrderResponseDTO responseDTO = payManageFacade.ncpayCflRequest(requestDTO);
	}


	@Test
	public void testCflSendSms() {
		NcPayCflSmsRequestDTO requestDTO = new NcPayCflSmsRequestDTO();
		requestDTO.setRecordNo("BK_CFL1801051450331715321");
		requestDTO.setMobileNo("18100000000");

		NcPayCflSmsResponseDTO responseDTO = payManageFacade.ncpayCflSendSms(requestDTO);
	}

	@Test
	public void testCflConfirm() {
		NcPayCflConfirmRequestDTO requestDTO = new NcPayCflConfirmRequestDTO();
		requestDTO.setRecordNo("BK_CFL1710261755583490378");
		requestDTO.setSmsCode("111111");

		NcPayCflConfirmResponseDTO responseDTO = payManageFacade.ncpayCflConfirmPay(requestDTO);
	}

	@Test
	public void testCflSynConfirm() {
		NcPayCflSynConfirmRequestDTO requestDTO = new NcPayCflSynConfirmRequestDTO();
//		PayTmpCardDTO tmpCard = new PayTmpCardDTO();
//		tmpCard.setCardNo("6225758340891885");
//		tmpCard.setCardCvn2("152");
//		tmpCard.setCardExpireDate("1219");
//		long tmpCardId = payTmpCardCwhFacade.add(tmpCard);
//		System.out.println("tmpCardId=" + tmpCardId);
		requestDTO.setRecordNo("BK_CFL1801051450331715321");
		requestDTO.setSmsCode("111111");
		//requestDTO.setTmpCardId(tmpCardId);
//		PayRecordResponseDTO responseDTO = payManageFacade.ncpayCflSynConfirmPay(requestDTO);
	}
}
