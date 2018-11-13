package com.yeepay.g3.core.nccashier.facade;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.CardOwnerConfirmResDTO;
import com.yeepay.g3.facade.nccashier.service.CashierCardOwnerFacade;

public class CashierCardOwnerFacadeTest extends BaseTest {
	@Autowired
	private CashierCardOwnerFacade cashierCardOwnerFacade;
	
	@Ignore
	@Test
	public void testSetCardOwner() {
		long requestId = 11380l;
		String bindId = "2022059";
		cashierCardOwnerFacade.setCardOwner(bindId,null, null,requestId);
	}
	@Ignore
	@Test
	public void testUnbind() {
		long requestId = 11380l;
		cashierCardOwnerFacade.unbindCard(requestId, "101611236049039597");
	}

}
