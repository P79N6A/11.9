package com.yeepay.g3.facade.payprocessor.facade;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.riskcontrol.facade.DoorgodFacade;
import com.yeepay.riskcontrol.facade.RcSyncRspDto;
import com.yeepay.riskcontrol.facade.v2.RcSyncEfPayReqDto;

import java.net.MalformedURLException;

/**
 * @author chronos.
 * @createDate 16/8/9.
 */
public class BaseFacadeTest {
    private static final String LOGGER_INFO = "[info]:";
    public static <T> T getRemoteFacade(Class<T> facade,Boolean isQa) {
        HessianProxyFactory factory=new HessianProxyFactory();
        String host="http://127.0.0.1:8080";
        if (isQa)
            host="http://10.151.30.43:8001";
        String url=host+"/payprocessor-hessian/hessian/"+facade.getSimpleName();
        info(url);
        factory.setOverloadEnabled(true);
        try {
            return (T)factory.create(facade, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static <T> T getRemoteFacade(Class<T> facade,String url){
        HessianProxyFactory factory=new HessianProxyFactory();
        try {
            return (T)factory.create(facade, url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static void info(Object org){
        if (org != null) {
            out(LOGGER_INFO, org.toString());
        } else {
            out(LOGGER_INFO, "org is null");
        }
    }

    public static void out(String level,String str){
        System.out.println( level + str);
    }

    public static <T> T getRemoteFacade(Class<T> facade){
        HessianProxyFactory factory=new HessianProxyFactory();
        String url = "http://172.21.0.53:8180/riskcontrol-service/hessian/" + facade.getSimpleName();
        factory.setOverloadEnabled(true);
        try {
            return (T)factory.create(facade, url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static void main(String args[]){
        DoorgodFacade facade = getRemoteFacade(DoorgodFacade.class);
        RcSyncEfPayReqDto rs = new RcSyncEfPayReqDto();
        RcSyncRspDto dto = facade.intercept(rs);
        System.out.println(dto.toString());
    }
}
