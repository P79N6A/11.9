package com.yeepay.g3.core.nccashier.interceptors;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.yeepay.g3.core.nccashier.log.LogInfoEncryptUtil;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * facade接口调用的拦截器， 1、实现入参出参的日志打印 2、实现接口方法级别的接口调用时间
 * @author fanpl
 */
public class InnerFacadeInvokeLogInterceptor extends MethodInvokeLog implements MethodInterceptor {
	private static Logger logger =
			NcCashierLoggerFactory.getLogger(InnerFacadeInvokeLogInterceptor.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long startTime = System.currentTimeMillis();
		Method method = invocation.getMethod();
		String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
		try {
			logger.info("[innersys] - [入参] - [" + methodName + "] - "
					+ LogInfoEncryptUtil.getLogString(invocation.getArguments()) + "]");
			Object result = invocation.proceed();
			logger.info("[innersys] - [返回结果] - [" + methodName + "] - " + getObjectString(result));
			return result;
		} catch (Throwable e) {
			logger.warn("[innersys] - [异常] - [" + methodName + "] - "
					+ e.getClass().getSimpleName()
					+ ":" + e.getMessage());
			throw e;
		} finally {
			long time = (System.currentTimeMillis() - startTime);
			logger.info("[innersys] - [执行时间] - [" + methodName + "] - 耗时[" + time + "]毫秒");
			NcCashierLoggerFactory.TAG_LOCAL.remove();
		}
	}
}
