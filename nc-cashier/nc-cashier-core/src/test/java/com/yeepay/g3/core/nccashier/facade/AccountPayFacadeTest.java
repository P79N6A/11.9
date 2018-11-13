package com.yeepay.g3.core.nccashier.facade;

import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;
import com.yeepay.g3.facade.nccashier.service.AccountPayFacade;

public class AccountPayFacadeTest extends BaseTest{
	
	@Autowired
	private AccountPayFacade accountPayFacade;
	
	// =============================== 商户信息校验接口 ======================================
	@Test
	public void testValidateMerchantInfoNullParam(){
		AccountPayValidateRequestDTO req = null;
		AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
		System.out.println("testValidateMerchantInfoNullParam，response=" + res);
	}

	@Test
	public void testValidateMerchantInfoEmptyAllFields(){
		AccountPayValidateRequestDTO req = new AccountPayValidateRequestDTO();
		AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
		System.out.println("testValidateMerchantInfoEmptyAllFields，response=" + res);
	}
	
	@Test
	public void testValidateMerchantInfoEmptyUserAccount(){
		AccountPayValidateRequestDTO req = new AccountPayValidateRequestDTO();
		req.setUserAccount("wmt_test02");
		System.out.println("testValidateMerchantInfoEmptyUserAccount，request=" + req);
		AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
		System.out.println("testValidateMerchantInfoEmptyUserAccount，response=" + res);
	}
	
	@Test
	public void testValidateMerchantInfoErrorUserAccount(){
		AccountPayValidateRequestDTO req = new AccountPayValidateRequestDTO();
		req.setUserAccount("zml");
		req.setTradePassword("12345qwert");
		System.out.println("testValidateMerchantInfoErrorUserAccount，request=" + req);
		AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
		System.out.println("testValidateMerchantInfoErrorUserAccount，response=" + res);
	}
	
	@Test
	public void testValidateMerchantInfoNonAuthority(){
		AccountPayValidateRequestDTO req = new AccountPayValidateRequestDTO();
		req.setUserAccount("wmt_test02");
		req.setTradePassword("12345qwert");
		System.out.println("testValidateMerchantInfoNonAuthority，request=" + req);
		AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
		System.out.println("testValidateMerchantInfoNonAuthority，response=" + res);
	}
	
	@Test
	public void testValidateMerchantInfoErrorPassword(){
		AccountPayValidateRequestDTO req = new AccountPayValidateRequestDTO();
		req.setUserAccount("finaltest_lihui@126.com");
		req.setTradePassword("123456234");
		System.out.println("testValidateMerchantInfoAuthority，request=" + req);
		AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
		System.out.println("testValidateMerchantInfoAuthority，response=" + res);
	}
	
	@Test
	public void testValidateMerchantInfoFrozenAccount(){
		String[] passwords = {"123456", "1234567", "12345678", "123456789", "12345678090"};
		AccountPayValidateRequestDTO req = new AccountPayValidateRequestDTO();
		req.setUserAccount("finaltest_lihui@126.com");
		int i=1;
		for(String password: passwords){
			req.setTradePassword(password);
			System.out.println("testValidateMerchantInfoFrozenAccount，i=" + i + ", request=" + req);
			AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
			System.out.println("testValidateMerchantInfoFrozenAccount，i=" + i + ", response=" + res);
		}
		
	}
	
	@Test
	public void testValidateMerchantInfoCorrect(){
		AccountPayValidateRequestDTO req = new AccountPayValidateRequestDTO();
		req.setUserAccount("finaltest_lihui@126.com");
		req.setTradePassword("1234qwer");
		System.out.println("testValidateMerchantInfoCorrect，request=" + req);
		AccountPayValidateResponseDTO res = accountPayFacade.validateMerchantAccount(req);
		System.out.println("testValidateMerchantInfoCorrect，response=" + res);
	}
	
	

	@Test
	public void testRequestAccountPay(){
		CashierAccountPayRequestDTO req = new CashierAccountPayRequestDTO();
		req.setUserAccount("finaltest_lihui@126.com");
		req.setDebitCustomerNo("10040007708");
		req.setRequestId(20456L);
		req.setRecordId(27890L);
		req.setTokenId("86537116-809e-4fb7-90c8-42f6cf679a5b");
		System.out.println("testRequestAccountPay，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testRequestAccountPay，response=" + res);
	}
	
	// =============================== 商户账户支付接口 ======================================
	
	@Test
	public void testPayNullParam(){
		CashierAccountPayRequestDTO req = null;
		System.out.println("testPayNullParam，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testPayNullParam，response=" + res);
	}
	
	@Test
	public void testPayEmptyAllFields(){
		CashierAccountPayRequestDTO req = new CashierAccountPayRequestDTO();
		System.out.println("testPayEmptyAllFields，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testPayEmptyAllFields，response=" + res);
	}
	
	@Test
	public void testPayNonExsitPayRequest(){
		CashierAccountPayRequestDTO req = new CashierAccountPayRequestDTO();
		req.setTokenId("4a9b8e76-d491-478d-b63d-84a833db5d38");
		req.setRequestId(20702l);
		req.setUserAccount("zml");
		req.setDebitCustomerNo("10040007800");
		System.out.println("testPayNonExsitPayRequest，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testPayNonExsitPayRequest，response=" + res);
	}
	
	
	@Test
	public void testPayExpireTimePayRequest(){
		CashierAccountPayRequestDTO req = new CashierAccountPayRequestDTO();
		req.setTokenId("4a9b8e76-d491-478d-b63d-84a833db5d38");
		req.setRequestId(20611l);
		req.setUserAccount("zml");
		req.setDebitCustomerNo("10040007800");
		System.out.println("testPayExpireTimePayRequest，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testPayExpireTimePayRequest，response=" + res);
	}
	
	
	@Test
	public void testPaySUCCESSPayRequest(){
		CashierAccountPayRequestDTO req = new CashierAccountPayRequestDTO();
		req.setTokenId("4a9b8e76-d491-478d-b63d-84a833db5d38");
		req.setRequestId(20707l);
		req.setUserAccount("zml");
		req.setDebitCustomerNo("10040007800");
		System.out.println("testPaySUCCESSPayRequest，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testPaySUCCESSPayRequest，response=" + res);
	}
	
	/**
	 * 首次支付
	 */
	@Test
	public void testPayEmptyPayRecordId(){
		CashierAccountPayRequestDTO req = new CashierAccountPayRequestDTO();
		req.setTokenId("939a8df8-99e5-4b3c-af4a-6516e5ddc906");
		req.setRequestId(20709l);
		req.setUserAccount("zml");
		req.setDebitCustomerNo("10040007800");
		req.setRecordId(null);
		System.out.println("testPayEmptyPayRecordId，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testPayEmptyPayRecordId，response=" + res);
	}
	
	@Test
	public void testPaySECONDPAYRecordId(){
		CashierAccountPayRequestDTO req = new CashierAccountPayRequestDTO();
		// 网银-账户支付
		req.setTokenId("747d09fb-bfac-4f7a-8c92-3f901795fa61");
		req.setRequestId(20712l);
		req.setUserAccount("zml");
		req.setDebitCustomerNo("10040007800");
		req.setRecordId(28332l);
		System.out.println("testPaySECONDPAYRecordId，request=" + req);
		CashierAccountPayResponseDTO res = accountPayFacade.pay(req);
		System.out.println("testPaySECONDPAYRecordId，response=" + res);
	}

	
}
