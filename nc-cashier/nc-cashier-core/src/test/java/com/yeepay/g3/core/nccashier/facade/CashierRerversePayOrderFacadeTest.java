package com.yeepay.g3.core.nccashier.facade;

import java.net.MalformedURLException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.nccashier.service.CashierBusinessFacade;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class CashierRerversePayOrderFacadeTest extends BaseTest {
	
	private static final Logger Log = LoggerFactory.getLogger(CashierRerversePayOrderFacadeTest.class);
	
	@Autowired
	private CashierBusinessFacade cashierBusinessFacade;
	
	
	public static void main(String[] args) {
		
		ReverseRequestDTO reverseRequestDTO = new ReverseRequestDTO();
		reverseRequestDTO.setMerchantNo("10013682657");
		reverseRequestDTO.setBizOrderNum("2566852413");
		reverseRequestDTO.setBizType(2l);
		reverseRequestDTO.setBizReverseNum("2566852413");
		reverseRequestDTO.setRemark("重复支付退款");
		
		HessianProxyFactory bean = new HessianProxyFactory();
		try {
			CashierBusinessFacade cashierBusinessFacadeInnerTest = (CashierBusinessFacade)bean.create(CashierBusinessFacade.class,
					"http://59.151.25.126:6144/nc-cashier-hessian/hessian/CashierBusinessFacade");
			ReverseResponseDTO reverseResponseDTO = cashierBusinessFacadeInnerTest.reversePayOrder(reverseRequestDTO);
			System.out.println(ToStringBuilder.reflectionToString(reverseResponseDTO));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void testRerversePayOrder(){
		ReverseResponseDTO reverseResponseDTO = null;
		ReverseRequestDTO reverseRequestDTO = new ReverseRequestDTO();
		reverseRequestDTO.setMerchantNo("10040018749");
		reverseRequestDTO.setBizOrderNum("411606015283691681");
		reverseRequestDTO.setBizType(9l);
		reverseRequestDTO.setBizReverseNum("411606015283691681");
		reverseResponseDTO = cashierBusinessFacade.reversePayOrder(reverseRequestDTO);
		System.out.println(reverseResponseDTO.toString());
	}
	
	@Test
	public void testReversePayOrder(){
		ReverseRequestDTO request = null;
		request = new ReverseRequestDTO();
		
		//请求订单不存在
		request.setMerchantNo("10040020578");
		request.setBizOrderNum("01615577461880661");
		request.setBizType(7l);
		request.setBizReverseNum("01615577461880661");
		ReverseResponseDTO response = cashierBusinessFacade.reversePayOrder(request);
	}
	
	@Test
	public void installmentReverseTest(){
		ReverseRequestDTO request = new ReverseRequestDTO();
		request.setMerchantNo("10040018287");
		request.setBizOrderNum("DD1482743765964");
		request.setBizType(7l);
		request.setReverseInterfaceType("FE_CFL");
		request.setBizReverseNum("DD1482743765964");
		ReverseResponseDTO response = cashierBusinessFacade.reversePayOrder(request);
		Log.info("installmentReverseTest response:{}", response);
	}
}
