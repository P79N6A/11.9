package test;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendFanRouteFacade;

public class TestFanRouteMain {

    public static void main(String[] ags){
        query();
    }

    public static void query() {
        FrontendFanRouteFacade transferFacade = getRemoteFacade("http://127.0.0.1:8080/fe/hessian/", FrontendFanRouteFacade.class);
        FanRouteAddRequestDTO requestDTO = new FanRouteAddRequestDTO();
        requestDTO.setCustomerNum("111144");
        requestDTO.setReportId("22222");
        requestDTO.setSubCustomerNum("33333");
        try {
            FanRouteAddResponseDTO query = transferFacade.addFanRouteInfo(requestDTO);
            System.out.println(JSON.toJSONString(query));
        }catch (Exception e){
            e.printStackTrace();
        }

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
