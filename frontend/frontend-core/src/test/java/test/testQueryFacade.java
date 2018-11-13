package test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.facade.FrontendQueryFacade;
import com.yeepay.g3.utils.common.StringUtils;

import java.net.MalformedURLException;

/**
 * 查单接口
 * @author wangmeimei
 *
 */
public class testQueryFacade {
	
	public static void main(String[] args) {
		HessianProxyFactory factory = new HessianProxyFactory();
		FrontendQueryFacade frontendQueryFacade;
		try {
			frontendQueryFacade = (FrontendQueryFacade) factory.create(FrontendQueryFacade.class, 
					"http://10.151.32.44:30027/frontend-hessian/hessian/FrontendQueryFacade");
			FrontendQueryRequestDTO frontendQueryRequestDTO = new FrontendQueryRequestDTO();
			frontendQueryRequestDTO.setRequestId("PASSIVESCAN1706211105416763305");
			frontendQueryRequestDTO.setRequestSystem("PAYPROCESSOR");
			frontendQueryRequestDTO.setPlatformType(PlatformType.WECHAT);
			FrontendQueryResponseDTO frontendQueryResponseDTO = frontendQueryFacade.queryOrderInfo(frontendQueryRequestDTO);
			System.out.println(frontendQueryResponseDTO.toString());
			if(StringUtils.isBlank(frontendQueryResponseDTO.getResponseCode())){
				System.out.println("查询成功");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
