package com.yeepay.g3.core.nccashier.interceptors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.aop.support.AopUtils;

import com.yeepay.g3.core.nccashier.log.LogInfoEncryptUtil;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.utils.common.exception.YeepayBizException;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * Created by Felix on 1/15/16.
 */
public class RemoteFacadeInvocationHandler extends MethodInvokeLog implements InvocationHandler {

	private static final Logger logger =
			NcCashierLoggerFactory.getLogger(RemoteFacadeInvocationHandler.class);

	/**
	 * 目标对象
	 */

	private Object target;

	private String sysName;

	/**
	 * 构造方法
	 *
	 * @param target 目标对象
	 */
	public RemoteFacadeInvocationHandler(Object target, String sysName) {
		this.target = target;
		this.sysName = sysName;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long startTime = System.currentTimeMillis();
		String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
		logger.info("[outer] - [入参] - [" + sysName + "] - [" + methodName + "] - "
				+ formatArguments(args));
		try {
			Object result = AopUtils.invokeJoinpointUsingReflection(target, method, args);
			logger.info("[outer] - [返回结果] - [" + sysName + "] - [" + methodName + "] - "
					+ LogInfoEncryptUtil.getLogString(result));
			return result;
		} catch (YeepayBizException e) {
			logger.warn("[outer] - [业务异常] - [" + methodName + "] - " + e.getDefineCode() + ":"
					+ e.getMessage());
			throw e;
		} catch (Throwable e) {
			logger.warn("[outer] - [异常] - [" + sysName + "] - [" + methodName + "]", e);
			throw e;
		} finally {
			long time = (System.currentTimeMillis() - startTime);
			logger.info("[outer] - [执行时间] - [" + sysName + "] - [" + methodName + "] - 耗时[" + time
					+ "]毫秒");
		}
	}

}
