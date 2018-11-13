package test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.frontend.dto.PrePayRequestDTO;
import com.yeepay.g3.facade.frontend.dto.PrePayResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.facade.FrontendPayFacade;
import com.yeepay.g3.utils.common.StringUtils;

public class testPrePay {
	
	public static void main(String[] args) {
		PrePayRequestDTO prePayRequestDTO = new PrePayRequestDTO();
		prePayRequestDTO.setBasicProductCode("3011001001001");
		prePayRequestDTO.setRetailProductCode("3011001003001A");
		prePayRequestDTO.setCustomerNumber("10040007800");
		prePayRequestDTO.setDealUniqueSerialNo("dealUniqueSerialNo"+System.currentTimeMillis());
		prePayRequestDTO.setPayBusinessType(PayBusinessType.DC);
		prePayRequestDTO.setTotalAmount(new BigDecimal("0.01"));
//		prePayRequestDTO.setAppId("wx2dabfd4bb5026f0e");
		prePayRequestDTO.setPlatformType("WECHAT");
//		prePayRequestDTO.setIndustryCode("");
//		prePayRequestDTO.setCustomerLevel("V");
		Map<String, String> extParam = new HashMap<String, String>();
		String specifyChannelCodes = "AAAAAA|BBBBBB|CCCCCC";
		extParam.put("specifyChannelCodes", specifyChannelCodes);
		prePayRequestDTO.setExtParam(extParam);


		HessianProxyFactory factory = new HessianProxyFactory();
		FrontendPayFacade frontendPayFacade = null;
		try {
			frontendPayFacade = (FrontendPayFacade) factory.create(FrontendPayFacade.class, "http://10.151.32.27:30028/frontend-hessian/hessian/FrontendPayFacade");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println(prePayRequestDTO.toString());
		PrePayResponseDTO prePayResponseDTO = frontendPayFacade.prePayJsapi(prePayRequestDTO);
		System.out.println(prePayResponseDTO.toString());
		if(StringUtils.isBlank(prePayResponseDTO.getResponseCode())){
			System.out.println("预路由成功");
		}
	}

}
