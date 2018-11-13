package com.yeepay.g3.core.nccashier.facade;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.interceptors.RemoteFacadeInvocationHandler;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierResponseDTO;
import com.yeepay.g3.facade.nccashier.service.UnifiedAPICashierFacade;
import com.yeepay.g3.util.ncmock.MockRemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static com.yeepay.g3.core.nccashier.facade.NeedPasswordTest.getRemoteFacade;

public class UnifiedAPICashierFacadeMainTest {

    static UnifiedAPICashierFacade unifiedAPICashierFacade = getRemoteFacade("http://localhost:8080/nc/hessian/", UnifiedAPICashierFacade.class);


    public static void main(String[] args) {
        UnifiedAPICashierRequestDTO unifiedAPICaspayhierRequestDTO = new UnifiedAPICashierRequestDTO();
        unifiedAPICaspayhierRequestDTO.setPayTool("MSCANPAY");
        unifiedAPICaspayhierRequestDTO.setPayType("WECHAT");
        unifiedAPICaspayhierRequestDTO.setToken("6140D1A6965117AE67DF015886A300F72EBA39CAB6563A4AD45DEBA5B035158F");
        unifiedAPICaspayhierRequestDTO.setMerchantNo("10040061750");
        unifiedAPICaspayhierRequestDTO.setVersion("1.0");
        unifiedAPICaspayhierRequestDTO.setUserIp("127.0.0.1");
        Map<String,String> extParam = new HashMap<String, String>();
        extParam.put("specifyChannelCodes","specifyChannelCodes");
        unifiedAPICaspayhierRequestDTO.setExtParamMap(JSON.toJSONString(extParam));
        System.out.println("入参=" + JSON.toJSONString(unifiedAPICaspayhierRequestDTO));
        UnifiedAPICashierResponseDTO pay = unifiedAPICashierFacade.pay(unifiedAPICaspayhierRequestDTO);
        System.out.println("返回结果=" + pay);

    }

    public static <T> T getService(String serviceUrl, RemotingProtocol protocol, Class<T> clz, String sysName) {
        Assert.notNull(clz);
        Assert.isTrue(clz.isInterface(), "clz must be interface");
        T target = MockRemoteServiceFactory.getService(serviceUrl,protocol,clz);
        RemoteFacadeInvocationHandler invocationHandler =
                new RemoteFacadeInvocationHandler(target, sysName);
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target
                .getClass().getInterfaces(), invocationHandler);
    }
}
