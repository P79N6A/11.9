package com.yeepay.g3.core.nccashier.facade;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.service.CashierSetsFacade;

public class Cashier3gSetFacedeTest extends BaseTest {
	@Autowired
	private CashierSetsFacade cashierSetsFacade;
	@Test
	@Ignore
	public void testGetSendSMSNo(){
		String sendSmsNo = cashierSetsFacade.getSendSMSNo();
		System.out.println(sendSmsNo);
	}
}
