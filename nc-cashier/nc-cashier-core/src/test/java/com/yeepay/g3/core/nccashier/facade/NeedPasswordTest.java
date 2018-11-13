package com.yeepay.g3.core.nccashier.facade;

import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierResponseDTO;
import com.yeepay.g3.facade.nccashier.service.UnifiedAPICashierFacade;

public class NeedPasswordTest {

    public static void main(String[] args) {
        UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
        unifiedAPICaspayhierRequestDTO.setPayTool("MSCANPAY");
        unifiedAPICaspayhierRequestDTO.setPayType("WECHAT");
        unifiedAPICaspayhierRequestDTO.setToken("C7B97749D25C75AA3B899BA29F758376F91C040933A10C4DFA3BA51E25224D56");
        unifiedAPICaspayhierRequestDTO.setMerchantNo("10040058439");
        unifiedAPICaspayhierRequestDTO.setMerchantStoreNo("12332222");
        unifiedAPICaspayhierRequestDTO.setMerchantTerminalId("USER_ID");
        unifiedAPICaspayhierRequestDTO.setPayEmpowerNo("123");
        unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
        unifiedAPICaspayhierRequestDTO.setVersion("1.0");
//        Map<String, String> extParam = new HashMap<String, String>();
//        extParam.put("specifyChannelCodes", "specifyChannelCodes");
//        extParam.put("reportId", "aaaaaa");
//        unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));


        UnifiedAPICashierFacade unifiedAPICashierFacade = getRemoteFacade("http://172.18.162.225:8077/nc/hessian/", UnifiedAPICashierFacade.class);

        System.out.println("入参=" + JSONObject.toJSONString(unifiedAPICaspayhierRequestDTO));
        UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
       System.out.println("返回结果=" + JSONObject.toJSONString(pay));
    }

    public static <T> T getRemoteFacade(String urlPerfix, Class<T> facade) {
        try {
            HessianProxyFactory factory = new HessianProxyFactory();
            String url = urlPerfix + facade.getSimpleName();
            System.out.println(url);
            return (T) factory.create(facade, url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
