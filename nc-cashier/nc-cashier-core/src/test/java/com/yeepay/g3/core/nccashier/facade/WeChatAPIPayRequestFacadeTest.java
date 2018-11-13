/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.UserProceeService;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.service.WeChatPayRequestFacade;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * @author zhen.tan
 *
 */
public class WeChatAPIPayRequestFacadeTest extends BaseTest {
	
	private static final Logger Log = LoggerFactory.getLogger(WeChatAPIPayRequestFacadeTest.class);


	@Resource
	private UserProceeService userProceeService;
	@Resource
	private PaymentRequestService paymentRequestService;
	@Resource
	private WeChatPayRequestFacade  weChatPayRequestFacade;
	
	@Resource
	private PayProcessorService payProcessorService;

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
	public void testCreateOrder() {
		 String tokenId = UUID.randomUUID().toString();
		long requestId = 13166l;
		UserAccount userAccount = saveUserAccount(tokenId, requestId);
		if (userAccount != null) {
			WeChatPayResponseDTO response = null;
			WeChatPayRequestDTO request = new WeChatPayRequestDTO();
			request.setPayType("CFL");
			request.setRecordId(0l);
			request.setRequestId(requestId);
			request.setTokenId(tokenId);
			try{
			 response = weChatPayRequestFacade.pay(request);
			}catch(Throwable e){
				e.printStackTrace();
			}
			System.out.println(response);
			Assert.assertTrue(response != null
					&& response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		} else {
			Assert.assertTrue(false);
		}
	}
	
	@Ignore
	@Test
	public void testInstallmentCreateOrder() {
		String tokenId = UUID.randomUUID().toString();
		long requestId = 13106;
		UserAccount userAccount = saveUserAccount(tokenId, requestId);
		if (userAccount != null) {
			WeChatPayResponseDTO response = null;
			WeChatPayRequestDTO request = new WeChatPayRequestDTO();
			request.setPayType(PayTypeEnum.CFL.name());
			request.setRecordId(userAccount.getPaymentRecordId()==null?0:Long.valueOf(userAccount.getPaymentRecordId()));
			request.setRequestId(requestId);
			request.setTokenId(tokenId);
			try {
				response = weChatPayRequestFacade.pay(request);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			Log.info("testInstallmentCreateOrder response:{}", response);
			Assert.assertTrue(response != null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		} else {
			Assert.assertTrue(false);
		}
	}


//	@Test
//	@Ignore
//	public void testQueryResult() {
//		long requestId = 2735;
//		long recordId =5934;
//		CashierQueryRequestDTO queryRequestDto = new CashierQueryRequestDTO();
//		queryRequestDto.setRecordId(recordId);
//		queryRequestDto.setRequestId(requestId);
//		CashierQueryResponseDTO response = ncCashierCoreFacade.queryPayResult(queryRequestDto);
//		System.out.println(response);
//		Assert.assertTrue(
//				response != null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
//	}
	
	@Test
	public void testOpenPayTest(){
		OpenPayRequestDTO request = new OpenPayRequestDTO();
		request.setAmount(new BigDecimal(0.01));
//		request.setPayerAccountNo("819270895@qq.com");
		request.setAppId("2017080207997311");
		request.setPageCallBack("http://10.151.30.8:8008/nc-cashier-wap//wap/query/result?token=4193afb5-5959-4b8e-9a99-76d695cca6ee");
		request.setPayBusinessType(PayBusinessType.DC);
		request.setPlatformType(PlatformType.ALIPAY);
		request.setCustomerLevel("V");
		request.setRetailProductCode("DSBZB");
		request.setBasicProductCode("ZF_SHH");
		request.setOutTradeNo("OPR:10040020578501677147287805");
		request.setDealUniqueSerialNo("1001201708150000000000032341");
		request.setCashierType("SY_H5");
		request.setPayProduct("ZFB_SHH");
		request.setPayOrderType(PayOrderType.LN);
		request.setOrderSystem("DS");
		request.setOrderNo("1001201708150000000000032341");
		request.setRequestSystem("nccashier");
		request.setRequestSysId("23868");
		request.setCustomerNumber("10040020578");
		request.setCustomerName("test@yeepay.com");
		request.setProductName("111");
		request.setPayerIp("172.18.160.120");
//		request.setExpireDate(new Date());
		request.setIndustryCode("7993");
		request.setRiskProduction("DS");
		request.setPayInterface("JHKY_CCB_JD_ZF_6016");
		try{
			OpenPrePayRequestDTO preRequestDTO = new OpenPrePayRequestDTO();
			preRequestDTO.setAppId("2017080207997311");
			preRequestDTO.setBasicProductCode("ZH_SHH");
			preRequestDTO.setCustomerLevel("V");
			preRequestDTO.setCustomerNumber("10040020578");
			preRequestDTO.setDealUniqueSerialNo("1001201708150000000000032341");
			preRequestDTO.setIndustryCode("7993");
			preRequestDTO.setPayBusinessType(PayBusinessType.DC);
			preRequestDTO.setRequestSystem("nccashier");
			preRequestDTO.setRetailProductCode("DSBZB");
			preRequestDTO.setTotalAmount(new BigDecimal(0.01));
			preRequestDTO.setPlatformType(PlatformType.ALIPAY.name());
			OpenPrePayResponseDTO prePayResponse = payProcessorService.payOrderPrePay(preRequestDTO);
			request.setBankTotalCost(prePayResponse.getBankTotalCost());
			request.setReportMerchantNo(prePayResponse.getReportMerchantNo());
			OpenPayResponseDTO response = payProcessorService.openPayRequest(request);
//			System.out.println(prePayResponse);
			System.out.println(response);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

}
