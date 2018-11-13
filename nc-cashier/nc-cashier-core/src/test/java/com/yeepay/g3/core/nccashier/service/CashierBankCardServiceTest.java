package com.yeepay.g3.core.nccashier.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.BankCardReponseDTO;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;


public class CashierBankCardServiceTest extends BaseTest{
	
	private static Logger Log = LoggerFactory.getLogger(CashierBankCardServiceTest.class);

	@Resource
	private CashierBankCardService cashierBankCardService;
	
	public void testGetPayType() {
	}

	@Test
	public void testGetBankCardInfo() {
		long requestId = 5529;
		BankCardReponseDTO response = new BankCardReponseDTO();
		response.setRequestId(requestId);
		cashierBankCardService.getBankCardInfo(requestId, response,Constant.BNAK_RULE_CUSTYPE_SALE);
		Log.info("testGetBankCardInfo response:{}", response);
	}
	
	
	public static void main(String[] args){
		String identityId = "SyvMH/f6hyaeX0tP2JVjekWWyIxO1pHF4/j9ZBQugt4=";
		identityId = AESUtil.aesDecrypt(identityId);
		Log.info(identityId);
		String cardNo = "7XSi1ZtLBdF04svuyXlA8sYP5Xac2dI3mQlHLw4CWrU=";
		cardNo = AESUtil.aesDecrypt(cardNo);
		Log.info("cardNo is {}", cardNo);
		String userName = "etPmAgcBFdZvSI+zXJ/+cg==";
		userName = AESUtil.aesDecrypt(userName);
		Log.info("userName is {}", userName);
		String id = "9KGXIBVMznS/4+7UmxEl+Yo2LvHfeoYDGvprsdlI2fM=";
		id = AESUtil.aesDecrypt(id);
		Log.info("id is {}", id);
		String bankMobile = "1plZpcAcqibs+MMrqoxpXw==";
		bankMobile = AESUtil.aesDecrypt(bankMobile);
		Log.info("bankMobile is {}", bankMobile);
		String ybMobile = "1plZpcAcqibs+MMrqoxpXw==";
		ybMobile = AESUtil.aesDecrypt(ybMobile);
		Log.info("ybMobile is {}", ybMobile);
		
		String owner2 = "KMTXlaNdKUM5OtCL7qUPPg==";
		owner2 = AESUtil.aesDecrypt(owner2);
		Log.info("owner2 is {}", owner2);
	}

}
