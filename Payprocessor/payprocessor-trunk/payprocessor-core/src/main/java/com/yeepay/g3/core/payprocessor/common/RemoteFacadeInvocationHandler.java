package com.yeepay.g3.core.payprocessor.common;

import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.core.payprocessor.util.log.LogInfoEncryptUtil;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 远程调用
 * @author
 */
public class RemoteFacadeInvocationHandler implements InvocationHandler {

    private static final Logger logger = PayLoggerFactory
			.getLogger(RemoteFacadeInvocationHandler.class);

    /**
     * 目标对象
     */

    private Object target;

    /**
     * 外部系统
     */
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
				+ LogInfoEncryptUtil.getLogString(args));
        try {
			Object result = AopUtils.invokeJoinpointUsingReflection(target, method, args);
			logger.info("[outer] - [返回结果] - [" + sysName + "] - [" + methodName + "] - "
					+ LogInfoEncryptUtil.getLogString(result));
            return result;
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
