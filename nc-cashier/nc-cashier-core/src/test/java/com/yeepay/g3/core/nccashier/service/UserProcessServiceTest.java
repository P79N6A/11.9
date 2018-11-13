package com.yeepay.g3.core.nccashier.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.entity.UserAccount;

public class UserProcessServiceTest extends BaseTest {
	
	@Resource
	private UserProceeService userProcessService;

	@Test
	public void test() {
		String tokenId = "c43e2226-a478-4bf7-9cce-c41f2c1873cc";
		userProcessService.getAndUpdatePaymentRecordId(tokenId, "1111");
		UserAccount userAccount = userProcessService.getUserAccountInfo(tokenId);
		System.out.println(userAccount);
		assertEquals("1111", userAccount.getPaymentRecordId());
	}

}
