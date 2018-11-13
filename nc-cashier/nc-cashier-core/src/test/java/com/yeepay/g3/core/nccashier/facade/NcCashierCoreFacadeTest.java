/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.validation.constraints.AssertTrue;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.dubbo.common.utils.Log;
import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.UserProceeService;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierBindPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierFirstPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.nccashier.service.NcCashierCoreFacade;
import com.yeepay.g3.facade.nccashier.util.NullObject;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import static org.junit.Assert.*;

/**
 * @author zhen.tan
 *
 */
public class NcCashierCoreFacadeTest extends BaseTest {
	
	private static final Logger Log = LoggerFactory.getLogger(NcCashierCoreFacadeTest.class);

	@Resource
	private NcCashierCoreFacade ncCashierCoreFacade;

	@Resource
	private UserProceeService userProceeService;


	@Resource
	private PaymentRequestService paymentRequestService;


	@Resource
	private PaymentProcessService paymentProcessService;



	private UserAccount saveUserAccount(String tokenId, long requestId) {

		PaymentRequest request = paymentRequestService.findPayRequestById(requestId);
		if (request != null) {
			UserAccount userAccount = userProceeService.getUserAccountInfo(tokenId);
			if (userAccount == null) {
				userAccount = new UserAccount();
				userAccount.setTokenId(tokenId);
				userAccount.setUserIp("");
				userAccount.setUserUa("");
				userAccount.setPaymentRequestId(requestId);
				// uaccount.setParamShowInfo(rInfo);
				userAccount.setMerchantNo(request.getMerchantNo());
				userAccount.setTradeSysOrderId(request.getTradeSysOrderId());
				userAccount.setTradeSysNo(request.getTradeSysNo());
				userAccount.setOrderOrderId(request.getOrderOrderId());
				userAccount.setOrderSysNo(request.getOrderSysNo());
				userAccount.setCreateTime(new Date());
				userAccount.setUpdateTime(new Date());
				userProceeService.saveUserAccount(userAccount);
			}

			return userAccount;
		} else {
			return null;
		}

	}

	@Test
	public void testBindPayCreatePayment() {
		 String tokenId = UUID.randomUUID().toString();
//		String tokenId = "c49c018d-55aa-4125-83bf-677cab3c65dd";
		long requestId = 5885;
		UserAccount userAccount = saveUserAccount(tokenId, requestId);
		if (userAccount != null) {
			CashierPaymentRequestDTO paymentRequest = new CashierPaymentRequestDTO();
			paymentRequest.setOrderType(NCCashierOrderTypeEnum.BIND);
			paymentRequest.setRequestId(requestId);
			paymentRequest.setTokenId(tokenId);
//			paymentRequest.setRecordId(8780);
			paymentRequest.setBindId(2017813);

			CashierPaymentResponseDTO response = ncCashierCoreFacade.createPayment(paymentRequest);
			System.out.println(response);
			Assert.assertTrue(response != null
					&& response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		} else {
			Assert.assertTrue(false);
		}
	}

	@Test
	@Ignore
	public void testFirstPayCreatePayment() {
		String tokenId = UUID.randomUUID().toString();
//		String tokenId = "9f5f907c-fe47-44e0-8f2e-13a2b19609b0";
		long requestId = 5746;
		long recordId =5934;
		UserAccount userAccount = saveUserAccount(tokenId, requestId);
		if (userAccount != null) {
			CashierPaymentRequestDTO paymentRequest = new CashierPaymentRequestDTO();
			paymentRequest.setOrderType(NCCashierOrderTypeEnum.FIRST);
			paymentRequest.setRequestId(requestId);
//			paymentRequest.setRecordId(recordId);
			paymentRequest.setTokenId(tokenId);
			
			CardInfoDTO cardInfo = new CardInfoDTO();
			cardInfo.setCardno("6225758316888816");
			cardInfo.setPhone("15911024890");
			cardInfo.setBankCode("ICBC");
			cardInfo.setCardType("DEBIT");
			cardInfo.setName("谭振星");
			cardInfo.setIdno("430224198803246513");
			paymentRequest.setCardInfo(cardInfo);

			CashierPaymentResponseDTO response = ncCashierCoreFacade.createPayment(paymentRequest);
			System.out.println(response);
			Assert.assertTrue(response != null
					&& response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		} else {
			Assert.assertTrue(false);
		}
	}

	@Test
	@Ignore
	public void testFirstPayRepeatCreatePayment() {
		String tokenId = "9f5f907c-fe47-44e0-8f2e-13a2b19609b0";
		long requestId = 2735;
		long recordId =5934;
		UserAccount userAccount = saveUserAccount(tokenId, requestId);
		if (userAccount != null) {
			CashierPaymentRequestDTO paymentRequest = new CashierPaymentRequestDTO();
			paymentRequest.setOrderType(NCCashierOrderTypeEnum.FIRST);
			// paymentRequest.setRequestId(requestId);
			paymentRequest.setTokenId(tokenId);
			paymentRequest.setRecordId(recordId);
			CardInfoDTO cardInfo = new CardInfoDTO();
			cardInfo.setCardno("6222020200108256211");
			cardInfo.setPhone("15911024890");
			cardInfo.setBankCode("ICBC");
			cardInfo.setCardType("DEBIT");
			cardInfo.setName("谭振星");
			cardInfo.setIdno("430224198803246513");
			paymentRequest.setCardInfo(cardInfo);

			CashierPaymentResponseDTO response = ncCashierCoreFacade.createPayment(paymentRequest);
			System.out.println(response);
			Assert.assertTrue(response != null
					&& response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		} else {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void testQueryResult() {
		long requestId = 13548;
		long recordId =17665;
		CashierQueryRequestDTO queryRequestDto = new CashierQueryRequestDTO();
		queryRequestDto.setRecordId(recordId);
		queryRequestDto.setRequestId(requestId);
		CashierQueryResponseDTO response = ncCashierCoreFacade.queryPayResult(queryRequestDto);
		System.out.println(response);
		Assert.assertTrue(
				response != null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
	}

	@Test
	@Ignore
	public void sendSmsTest() {
		long requestId = 5852;
		long recordId =9114;
		String tokenId = "e93afc46-a240-45e5-8e9a-374c9f70d976";
		CashierSmsSendRequestDTO smsRequest = new CashierSmsSendRequestDTO();
		NeedBankCardDTO needItem = new NeedBankCardDTO();
		needItem.setIdno("411011199001012030");
		needItem.setPhoneNo("18310243160");
		boolean isNull = NullObject.objectIsNull(needItem);
		if(!isNull){
			smsRequest.setNeedBankCardDTO(needItem);
		}
		
		smsRequest.setRecordId(recordId);
		smsRequest.setRequestId(requestId);
		smsRequest.setReqSmsSendTypeEnum(ReqSmsSendTypeEnum.YEEPAY);
		smsRequest.setTokenId(tokenId);
		CashierSmsSendResponseDTO response = ncCashierCoreFacade.sendSms(smsRequest);
		System.out.println(JSON.toJSONString(response));
	}

	@Test
	@Ignore
	public void testFirstPay() {
		CashierFirstPayRequestDTO firstPayRequest = new CashierFirstPayRequestDTO();
		Long paymentRecordId = 5678L;
		Long paymentRequestId = 2548L;
		CardInfoDTO cardInfo = new CardInfoDTO();
		PaymentRecord paymentRecord =
				paymentProcessService.findRecordByPaymentRecordId(String.valueOf(paymentRecordId));
		cardInfo.setBankCode(paymentRecord.getBankCode());
		cardInfo.setCardno(paymentRecord.getCardNo());
		cardInfo.setCardType(paymentRecord.getCardType());
		cardInfo.setIdno(paymentRecord.getIdCard());
		cardInfo.setName("樊培乐");
		cardInfo.setPhone("18610141201");
		firstPayRequest.setCardInfo(cardInfo);
		firstPayRequest.setTokenId("e41ed694-0c45-4020-9a77-2d0822d5fee3");
		firstPayRequest.setVerifycode("527486");
		firstPayRequest.setPaymentRecordId(paymentRecordId);
		firstPayRequest.setPaymentRequestId(paymentRequestId);
		CashierPayResponseDTO response = ncCashierCoreFacade.firstPay(firstPayRequest);
		System.out.println(response);
	}

	@Test
	@Ignore
	public void testBindPay() {
		Long paymentRecordId = 5740L;
		Long paymentRequestId = 2593L;
		PaymentRecord paymentRecord =
				paymentProcessService.findRecordByPaymentRecordId(String.valueOf(paymentRecordId));

		CashierPaymentRequestDTO createRequest = new CashierPaymentRequestDTO();
		createRequest.setBindId(Long.parseLong(paymentRecord.getBindId()));
		createRequest.setCardInfo(null);
		createRequest.setOrderType(NCCashierOrderTypeEnum.BIND);
		createRequest.setRecordId(paymentRecordId);
		createRequest.setRequestId(paymentRequestId);
		createRequest.setTokenId("e41ed694-0c45-4020-9a77-2d0822d5fee3");
		CashierPaymentResponseDTO createResponse = ncCashierCoreFacade.createPayment(createRequest);

		NeedBankCardDTO needBankCardDTO = createResponse.getNeedBankCardDto();

		CashierBindPayRequestDTO payresult = new CashierBindPayRequestDTO();
		payresult.setTokenId("e41ed694-0c45-4020-9a77-2d0822d5fee3");
		payresult.setVerifycode("657928");
		payresult.setPaymentRecordId(paymentRecordId);
		payresult.setPaymentRequestId(paymentRequestId);
		payresult.setBindId(paymentRecord.getBindId());
		payresult.setNeedBankCardDTO(needBankCardDTO);


		CashierPayResponseDTO response = ncCashierCoreFacade.bindPay(payresult);
		System.out.println(response);
	}
//	@Ignore
	@Test
	public void createPaymentTest(){
		CashierPaymentRequestDTO request = null;
//		CashierPaymentResponseDTO response = ncCashierCoreFacade.createPayment(request);
		
		request = new CashierPaymentRequestDTO();
		request.setBindId(2024163);//2017813
		request.setCardInfo(null);
		request.setOrderType(NCCashierOrderTypeEnum.BIND);
		request.setRecordId(0);
		request.setRequestId(46117l);
		request.setTokenId("7c981546-53b4-4c57-8f94-55c62af01303");
		CashierPaymentResponseDTO response = ncCashierCoreFacade.createPayment(request);
		System.out.println(response.toString());
	}
	
	@Test
	public void installmentPayResultQueryTest(){
		// requestId:13106 & recordId:17124 
		CashierQueryRequestDTO request = new CashierQueryRequestDTO();
		request.setRecordId(17124);
		request.setRequestId(13106);
		request.setRepeatQuery(true);
		CashierQueryResponseDTO response = ncCashierCoreFacade.queryPayResult(request);
		Log.info("installmentPayResultQueryTest response:{}", response);
		assertNotNull(response);
		assertEquals(ProcessStatusEnum.SUCCESS, response.getProcessStatusEnum());
	}
}
