/**
 * 
 */
package test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.frontend.dto.BankNotifyRequestDTO;
import com.yeepay.g3.facade.frontend.dto.BankNotifyResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.facade.FrontendNotifyFacade;
import com.yeepay.g3.utils.common.StringUtils;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 银行子系统通知FE
 * @author TML
 *
 */
public class TestNotifyFacade {
	
	
	public static void main(String[] args) {
		
		String url = "http://10.151.32.27:30028/frontend-hessian/hessian/FrontendNotifyFacade";
	    HessianProxyFactory factory = new HessianProxyFactory();
	    FrontendNotifyFacade frontendNotifyFacade = null;
//	    FrontendOperationFacade frontendOperationFacade = null;
	    try {
//	    	frontendOperationFacade = (FrontendOperationFacade) factory.create(FrontendOperationFacade.class, url);
//	    	boolean notify = frontendOperationFacade.reNotifyByOrderNo("", PlatformType.NET.name());
	    	frontendNotifyFacade = (FrontendNotifyFacade) factory.create(FrontendNotifyFacade.class, url);
		    BankNotifyRequestDTO requestDTO = new BankNotifyRequestDTO();
			requestDTO.setRequestId("PASSIVESCAN1712201208052177964");
			requestDTO.setOrderNo("9900045110171220");    //支付请求接口会返回
			requestDTO.setCustomerNumber("10040007800");
			requestDTO.setOutTradeNo("OPR:10040007800515240152065374");
			requestDTO.setBankSuccessTime(new Timestamp(System.currentTimeMillis()));
			requestDTO.setPaySuccessTime(new Timestamp(System.currentTimeMillis()));
			requestDTO.setPayStatus(PayStatusEnum.SUCCESS);
			requestDTO.setPayBankcardType(PayBankcardType.CREDIT);
//			requestDTO.setTransactionId("9000018092170208");  
			requestDTO.setPayInterface("BJCCB_NET_OPEN_JHGC6610");
			requestDTO.setTotalAmount(new BigDecimal("0.01"));
			requestDTO.setBankTotalCost(new BigDecimal("0"));
			Map<String, String> extParam = new HashMap<String, String>();
			extParam.put("bizChannelId", "ABCDEFGhijk");
			requestDTO.setExtParam(extParam);
			BankNotifyResponseDTO responseDTO = frontendNotifyFacade.receiveBankNotify(requestDTO);
			System.out.println(responseDTO.toString());
			if(StringUtils.isBlank(responseDTO.getResponseCode())){
				System.out.println("通知成功");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
