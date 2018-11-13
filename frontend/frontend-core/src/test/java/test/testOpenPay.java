package test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.frontend.dto.PayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PayResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.facade.FrontendPayFacade;
import com.yeepay.g3.utils.common.StringUtils;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class testOpenPay {
	
	public static void main(String[] args) {
		PayRequestDTO requestDTO = new PayRequestDTO();
		requestDTO.setRequestId("request"+System.currentTimeMillis());   //需要唯一
		requestDTO.setRequestSystem("PAYPROCESSOR");
//		requestDTO.setCustomerNumber("10012426766");
		requestDTO.setCustomerNumber("10040007800");
		requestDTO.setGoodsDescription("test");
		requestDTO.setOutTradeNo("out"+System.currentTimeMillis());
		requestDTO.setPayerIp("1.1.1.1");
		requestDTO.setPlatformType(PlatformType.WECHAT);
		requestDTO.setOrderType(OrderType.ACTIVESCAN);
		requestDTO.setTotalAmount(new BigDecimal("0.01"));
		requestDTO.setPayBusinessType(PayBusinessType.DC);
		requestDTO.setOrderSystem("DS");
		requestDTO.setPageCallBack("http://www.yeepay.com");
		requestDTO.setCustomerName("test");
		requestDTO.setCustomerLevel("V");
		requestDTO.setPaymentProduct("WAP");
		requestDTO.setOpenId("och7GjvYlkgK9kZSDa1bKQsEKRBs");
		requestDTO.setAppId("wx9e13bd68a8f1921e");
		requestDTO.setPayInterface("ICBCGC_ICBC_JD_ZF_5011");
		requestDTO.setBankTotalCost(new BigDecimal("0.01"));
		requestDTO.setBasicProductCode("3011001001001");
		requestDTO.setRetailProductCode("3011001003001A");
//		requestDTO.setDealUniqueSerialNo("dealUniqueSerialNo"+System.currentTimeMillis());
		requestDTO.setDealUniqueSerialNo("dealUniqueSerialNo1505720008104");
//		requestDTO.setPayEmpowerNo("289887651524406366");
//		requestDTO.setMerchantStoreNo("test");
//		requestDTO.setMerchantTerminalId("49000002");
//		requestDTO.setOrderExpireMin(1);
		Map<String, String> extParam = new HashMap<String, String>();
		String specifyChannelCodes = "AAAAAA|BBBBBB|CCCCCC";
		extParam.put("specifyChannelCodes", specifyChannelCodes);
//		requestDTO.setExtParam(extParam);

		HessianProxyFactory factory = new HessianProxyFactory();
		FrontendPayFacade frontendPayFacade = null;
		try {
			frontendPayFacade = (FrontendPayFacade) factory.create(FrontendPayFacade.class,
					"http://10.151.32.27:30028/frontend-hessian/hessian/FrontendPayFacade");
//			frontendPayFacade = (FrontendPayFacade) factory.create(FrontendPayFacade.class,
//					"http://10.132.2.22:30028/frontend-hessian/hessian/FrontendPayFacade");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println(requestDTO.toString());
		PayResponseDTO payResponseDTO = frontendPayFacade.openPay(requestDTO);
		System.out.println(payResponseDTO.toString());
		if(StringUtils.isBlank(payResponseDTO.getResponseCode())){
			System.out.println("请求支付成功");
		}
	}

}
