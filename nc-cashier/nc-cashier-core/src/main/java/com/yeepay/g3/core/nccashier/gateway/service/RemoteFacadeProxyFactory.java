package com.yeepay.g3.core.nccashier.gateway.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.util.Assert;

import com.yeepay.g3.core.nccashier.interceptors.RemoteFacadeInvocationHandler;
import com.yeepay.g3.util.ncmock.MockRemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

/**
 * 远程调用Facade动态代理工厂, 主要用于实现外部facade调用日志监控. Created by Felix on 1/15/16.
 */
public class RemoteFacadeProxyFactory {
	public static <T> T getService(Class<T> clz, String sysName) {
		Assert.notNull(clz);
		Assert.isTrue(clz.isInterface(), "clz must be interface");
		T target = MockRemoteServiceFactory.getService(clz);
		RemoteFacadeInvocationHandler invocationHandler =
				new RemoteFacadeInvocationHandler(target, sysName);
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target
				.getClass().getInterfaces(), invocationHandler);
	}
	
	
	public static <T> T getService(String serviceUrl, RemotingProtocol protocol,Class<T> clz, String sysName) {
		Assert.notNull(clz);
		Assert.isTrue(clz.isInterface(), "clz must be interface");
		T target = MockRemoteServiceFactory.getService(serviceUrl,protocol,clz);
		RemoteFacadeInvocationHandler invocationHandler =
				new RemoteFacadeInvocationHandler(target, sysName);
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), target
				.getClass().getInterfaces(), invocationHandler);
	}

}
