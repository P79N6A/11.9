/**
 * 
 */
package com.yeepay.g3.core.nccashier.facade;

import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierQueryResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.service.CashierBusinessFacade;
import com.yeepay.g3.facade.nccashier.service.NcCashierCoreFacade;

/**
 * @author zhen.tan
 *
 */
public class CashierBusinessFacadeTest{

//	@Resource
//	private CashierBusinessFacade cashierBusinessFacade;
	
	@Test
	public void testSupply(){
		String tradeSysOrderId ="2432965279";
		String tradeSysNo = "2GTRADE";
		HessianProxyFactory bean = new HessianProxyFactory();
//		
		CashierBusinessFacade cashierBusinessFacade;
		try {
			cashierBusinessFacade = (CashierBusinessFacade)bean.create(CashierBusinessFacade.class,
					"http://59.151.25.126:6144//nc-cashier-hessian/hessian/CashierBusinessFacade");
			OrderNoticeDTO noticeDTO = cashierBusinessFacade.supplementPaymentOrder(tradeSysOrderId, tradeSysNo);
			Assert.assertNotNull(noticeDTO);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryResult() {
		long requestId = 14746022;
		long recordId =14194409;
		CashierQueryRequestDTO queryRequestDto = new CashierQueryRequestDTO();
		queryRequestDto.setRecordId(recordId);
		queryRequestDto.setRequestId(requestId);
		queryRequestDto.setRepeatQuery(true);
		HessianProxyFactory bean = new HessianProxyFactory();
//		
		NcCashierCoreFacade ncCashierCoreFacade;
		try {
			ncCashierCoreFacade = (NcCashierCoreFacade)bean.create(NcCashierCoreFacade.class,
					"http://59.151.25.126:6144/nc-cashier-hessian/hessian/NcCashierCoreFacade");
			CashierQueryResponseDTO response = ncCashierCoreFacade.queryPayResult(queryRequestDto);
			System.out.println(response);
			Assert.assertTrue(response != null && response.getProcessStatusEnum() == ProcessStatusEnum.SUCCESS);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
}
