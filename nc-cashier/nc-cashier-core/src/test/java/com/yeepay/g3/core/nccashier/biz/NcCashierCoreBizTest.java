/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.UserProceeService;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NewOrderRequestResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;

/**
 * @author zhen.tan
 *
 */
public class NcCashierCoreBizTest extends BaseTest {

	@Resource
	private NcCashierCoreBiz ncCashierCoreBiz;
	
	@Resource
	private UserProceeService userProceeService;
	
	@Resource
	private PaymentRequestService paymentRequestService;
	
	@Resource
	private CashierRouteBiz cashierRouteBiz;
	
	@Resource
	private MerchantCashierCustomizedBiz merchantCashierCustomizedBiz;
	@Test
	public void testQueryMerchantCashierCustomizedLayoutSelectInfo(){
		String merchantNo = "10040007800";
		merchantCashierCustomizedBiz.queryMerchantCashierCustomizedLayoutSelectInfo(merchantNo);
	}
	@Test
	public void testQueryMerchantCashierCustomizedFile(){
		String fileId = "10040007000";
		String fileType = "less";
		merchantCashierCustomizedBiz.queryMerchantCashierCustomizedFile(fileId, fileType);
	}
	private UserAccount saveUserAccount(String tokenId,long requestId){
		
		PaymentRequest request = paymentRequestService.findPayRequestById(requestId);
		if(request != null){
			UserAccount userAccount = userProceeService.getUserAccountInfo(tokenId);
			if(userAccount == null){
				userAccount = new UserAccount();
				userAccount.setTokenId(tokenId);
				userAccount.setUserIp("");
				userAccount.setUserUa("");
				userAccount.setPaymentRequestId(requestId);
	//			uaccount.setParamShowInfo(rInfo);
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
		}else{
			return null;
		}
		
	}
	
	@Test
	public void testBindPayCreatePayment(){
//		String tokenId = UUID.randomUUID().toString();
		String tokenId ="85f9acf9-6cd1-4687-8c0c-f9cc0c099fa8";
		long requestId = 2058;
		UserAccount userAccount = saveUserAccount(tokenId,requestId);
		if(userAccount != null){
			CashierPaymentRequestDTO paymentRequest = new CashierPaymentRequestDTO();
			paymentRequest.setOrderType(NCCashierOrderTypeEnum.BIND);
			paymentRequest.setRequestId(requestId);
			paymentRequest.setTokenId(tokenId);
			paymentRequest.setRecordId(5008);
//			CardInfoDTO cardInfo = new CardInfoDTO();
//			cardInfo.setCardno("6222020200108256211");
//			cardInfo.setPhone("15911024890");
//			cardInfo.setBankCode("ICBC");
//			cardInfo.setCardType("DEBIT");
//			cardInfo.setName("谭振");
//			cardInfo.setIdno("430224198803246513");
//			paymentRequest.setCardInfo(cardInfo);
			paymentRequest.setBindId(2017875);
			
			CashierPaymentResponseDTO response = ncCashierCoreBiz.createPayment(paymentRequest);
			System.out.println(response);
			Assert.assertTrue(response !=null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		}else{
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testFirstPayCreatePayment(){
		String tokenId = UUID.randomUUID().toString();
		long requestId = 1911;
		UserAccount userAccount = saveUserAccount(tokenId,requestId);
		if(userAccount != null){
			CashierPaymentRequestDTO paymentRequest = new CashierPaymentRequestDTO();
			paymentRequest.setOrderType(NCCashierOrderTypeEnum.FIRST);
			paymentRequest.setRequestId(requestId);
			paymentRequest.setTokenId(tokenId);
			CardInfoDTO cardInfo = new CardInfoDTO();
			cardInfo.setCardno("6222020200108256211");
			cardInfo.setPhone("15911024890");
			cardInfo.setBankCode("ICBC");
			cardInfo.setCardType("DEBIT");
			cardInfo.setName("谭振");
			cardInfo.setIdno("430224198803246513");
			paymentRequest.setCardInfo(cardInfo);
			CashierPaymentResponseDTO response = ncCashierCoreBiz.createPayment(paymentRequest);
			System.out.println(response);
			Assert.assertTrue(response !=null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		}else{
			Assert.assertTrue(false);
		}
	}
		
	@Test
	public void testFirstPayRepeatCreatePayment(){
		String tokenId = "32618ac3-eaad-4dc7-b547-aa37463bb7cd";
		//5908cc0d-5d24-4a85-8f74-7e97373d2111
		long requestId = 1911;
		long recordId = 4932;
		UserAccount userAccount = saveUserAccount(tokenId,requestId);
		if(userAccount != null){
			CashierPaymentRequestDTO paymentRequest = new CashierPaymentRequestDTO();
			paymentRequest.setOrderType(NCCashierOrderTypeEnum.FIRST);
//			paymentRequest.setRequestId(requestId);
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
			CashierPaymentResponseDTO response = ncCashierCoreBiz.createPayment(paymentRequest);
			System.out.println(response);
			Assert.assertTrue(response !=null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		}else{
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testQueryResult(){
		long requestId = 1911;
		long recordId = 4932;
		CashierQueryRequestDTO queryRequestDto = new CashierQueryRequestDTO();
		queryRequestDto.setRecordId(recordId);
		queryRequestDto.setRequestId(requestId);
		CashierQueryResponseDTO response = ncCashierCoreBiz.queryPayResult(queryRequestDto);
		System.out.println(response);
		Assert.assertTrue(response !=null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
	}
	
	@Test
	public void testOrder(){
		NewOrderRequestResponseDTO order = cashierRouteBiz.newOrderRequest("0FB5183137B4B17E9D52A1F9E7F6BDC74E4999AB1CC1EC6F39BF2F7EE3E1BAF2", 0);
		System.out.println("order=" + order);
	}
}
