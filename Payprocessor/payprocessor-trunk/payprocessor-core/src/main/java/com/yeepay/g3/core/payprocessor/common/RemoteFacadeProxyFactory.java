package com.yeepay.g3.core.payprocessor.common;

import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.util.ncmock.MockRemoteServiceFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;

/**
 * 远程调用Facade动态代理工厂, 主要用于包装外部facade调用日志监控.
 * 
 * @author
 */
public class RemoteFacadeProxyFactory {

	/**
	 * @param clz
	 *            外部Facade接口class
	 * @param system
	 *            日志中将显示的系统
	 * @param <T>
	 * @return 如果获取到外部服务则返回该服务的实例.
	 */
	public static <T> T getService(Class<T> clz, ExternalSystem system) {
		Assert.notNull(clz);
		Assert.notNull(system, "system must be specified");
		Assert.isTrue(clz.isInterface(), "clz must be interface");
		T target = MockRemoteServiceFactory.getService(clz);
		RemoteFacadeInvocationHandler invocationHandler = new RemoteFacadeInvocationHandler(target,
				system.getSysName());
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				target.getClass().getInterfaces(), invocationHandler);
	}

}
