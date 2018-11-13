package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.facade.payprocessor.dto.QueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryResponseDTO;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chronos.
 * @createDate 2016/11/15.
 */
public class PayProcessorQueryFacadeTest extends BaseTest {

    @Autowired
    PayProcessorQueryFacade queryFacade ;

//    PayProcessorQueryFacade queryFacade = RemoteServiceFactory.getService(PayProcessorQueryFacade.class, ExternalSystem.PP.getSysName());

//    PayProcessorQueryFacade queryFacade = RemoteServiceFactory.getService(
//            "http://10.151.32.27:30013/payprocessor-hessian" ,
//            RemotingProtocol.HESSIAN , PayProcessorQueryFacade.class);


//    PayProcessorQueryFacade queryFacade = RemoteServiceFactory.getService(
//            "http://10.132.2.22:30013/payprocessor-hessian" ,
//            RemotingProtocol.HTTPINVOKER , PayProcessorQueryFacade.class);

    @Test
    public void query() throws Exception {
        QueryRequestDTO requestDTO = new QueryRequestDTO();
        requestDTO.setOrderSystem("YJZF");
        requestDTO.setOrderNo("order11195");
        System.out.println(ToStringBuilder.reflectionToString(requestDTO));
        QueryResponseDTO responseDTO = queryFacade.query(requestDTO);
        System.out.println(responseDTO.toString());
    }

    @Test
    public void test() {
        System.out.println("init success");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryHisOrder() {
        QueryRequestDTO requestDTO = new QueryRequestDTO();
        requestDTO.setOrderNo("201612072010002");
        requestDTO.setOrderSystem("YJZF");
        QueryResponseDTO responseDTO = queryFacade.queryHisOrder(requestDTO);
        System.out.println(responseDTO.toString());
    }
}