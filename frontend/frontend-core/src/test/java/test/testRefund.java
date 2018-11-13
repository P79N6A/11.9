package test;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.enumtype.RefundType;
import com.yeepay.g3.facade.frontend.facade.FrontendRefundFacade;
import com.yeepay.g3.utils.common.StringUtils;

public class testRefund {
	
	public static void main(String[] args) {
		HessianProxyFactory factory = new HessianProxyFactory();
		FrontendRefundFacade frontendRefundFacade;
		try {
			frontendRefundFacade = (FrontendRefundFacade) factory.create(FrontendRefundFacade.class, 
					"http://10.151.32.27:30027/frontend-hessian/hessian/FrontendRefundFacade");
			FrontendRefundRequestDTO frontendRefundRequestDTO = new FrontendRefundRequestDTO();
			frontendRefundRequestDTO.setRequestSystem("PAYPROCESSOR");
			frontendRefundRequestDTO.setCustomerNumber("10040028946");
			frontendRefundRequestDTO.setRequestId("ACTIVESCAN1706211123461575964");
			frontendRefundRequestDTO.setPlatformType(PlatformType.WECHAT);
			frontendRefundRequestDTO.setRefundType(RefundType.REPEATPAY);
			FrontendRefundResponseDTO frontendRefundResponseDTO = frontendRefundFacade.refund(frontendRefundRequestDTO);
			System.out.println(frontendRefundResponseDTO.toString());
			if(StringUtils.isBlank(frontendRefundResponseDTO.getResponseCode())){
				System.out.println("退款成功");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
