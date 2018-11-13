package com.yeepay.g3.core.frontend.util;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.errorcode.ErrorCode;

import java.net.MalformedURLException;
import java.util.List;


/**
 * 工具类
 */
public class CommonUtils {

	
	/**
	 * 获取指定的facadeService
	 * @param  c
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getFacadeService(Class c,String url){
		HessianProxyFactory factory = new HessianProxyFactory();  
        try {  
        	Object obj= factory.create(c, url);
        	return obj;
        } catch (Exception e) {  
        	return null;
        }  	
	}

	/**
	 * 获取远程服务,
	 * @param facade
	 * @param <T>
	 * @return
	 */
	public static <T> T getRemoteFacade(Class<T> facade, String url, Integer connectTimeout, Integer readeTimeOut){
		HessianProxyFactory factory=new HessianProxyFactory();
		factory.setOverloadEnabled(true);
		factory.setConnectTimeout(connectTimeout);
		factory.setReadTimeout(readeTimeOut);
		try {
			return (T)factory.create(facade, url);
		} catch (MalformedURLException e) {
			throw new FrontendBizException(ErrorCode.F0001000);
		}
	}
	
	/**
	 * 判断是否包含业务方
	 * @param requestSystem
	 * @return
	 */
	public static boolean containsRequestSystem(String requestSystem){
		List<String> list = ConstantUtils.getRequestSystemList();
		if(list != null && list.contains(requestSystem)){
			return true;
		}
		return false;
	}
	
}
