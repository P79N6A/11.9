package com.yeepay.g3.core.nccashier.facade;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.service.NcCashierUserAccessFacade;

public class NcCashierUserAccessFacadeTest extends BaseTest {
	@Autowired
	private NcCashierUserAccessFacade ncCashierUserAccessFacade;

	@Test
	public void testRequestBaseInfo() {
		RequestInfoDTO Info =
				ncCashierUserAccessFacade.requestBaseInfo("960e211e-056a-4d33-833d-3e0f3ad55183");
		System.out.println(JSON.toJSONString(Info));
	}
}
