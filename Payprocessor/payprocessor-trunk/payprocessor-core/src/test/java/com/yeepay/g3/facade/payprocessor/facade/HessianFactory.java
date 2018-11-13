package com.yeepay.g3.facade.payprocessor.facade;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * 远程调用工具类
 * 
 * @author Johnson
 * @version Saturday March 2nd, 2013
 */
public class HessianFactory {

	private static ConcurrentMap<String, Object> serviceProxyMap = new ConcurrentHashMap<String, Object>();
	private static ConcurrentMap<Class<?>, String> serviceUrlMap = new ConcurrentHashMap<Class<?>, String>();

	/**
	 * 注册默认服务地址
	 */
	static {
		String local = null;
		try {
			local = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		serviceUrlMap.put(PayOrderFacade.class,
				"http://10.149.149.3:30014/payprocessor-hessian/hessian/PayOrderFacade");
		serviceUrlMap.put(PayProcessorQueryFacade.class,
				"http://10.149.149.3:30014/payprocessor-hessian/hessian/PayProcessorQueryFacade");
		// serviceUrlMap.put(PayOrderFacade.class,
		// "http://10.151.32.27:30013/payprocessor-hessian/soa/hessian/"+PayOrderFacade.class.getName());
	}

	/**
	 * 创建服务对象
	 * 
	 * @param <T>
	 * @param serviceUrl
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T createService(String serviceUrl, Class<T> clazz) {
		HessianProxyFactory bean = new HessianProxyFactory();
		bean.setDebug(true);
		try {
			return (T) bean.create(clazz, serviceUrl);
		} catch (MalformedURLException e) {
			Logger.getLogger(HessianFactory.class).error(e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * 添加服务地址
	 * 
	 * @param clazz
	 * @param serviceUrl
	 */
	public static void put(Class<?> clazz, String serviceUrl) {
		if (serviceUrlMap.containsKey(clazz)) {
			return;
		}
		serviceUrlMap.put(clazz, serviceUrl);
	}

	/**
	 * 获取服务对象
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getService(Class<T> clazz) {
		String serviceUrl = serviceUrlMap.get(clazz);
		if (serviceUrl == null) {
			return null;
		}
		Object service = serviceProxyMap.get(serviceUrl);
		if (service == null) {
			service = HessianFactory.createService(serviceUrl, clazz);
			serviceProxyMap.put(serviceUrl, service);
		}
		return (T) service;
	}
}
