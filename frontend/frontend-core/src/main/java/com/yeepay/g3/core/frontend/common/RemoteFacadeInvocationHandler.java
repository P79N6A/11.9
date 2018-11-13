package com.yeepay.g3.core.frontend.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.aop.support.AopUtils;

import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.core.frontend.util.log.LogInfoEncryptUtil;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 远程调用
 * @author
 */
public class RemoteFacadeInvocationHandler implements InvocationHandler {

    private static final Logger logger = FeLoggerFactory
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
        StringBuffer logBuffer = new StringBuffer("[outer] - [入参] - [").append(sysName).append("] - [").
                append(methodName).append("] - ").append(LogInfoEncryptUtil.getLogString(args));
        try {
			Object result = AopUtils.invokeJoinpointUsingReflection(target, method, args);
            logBuffer.append(" --- [返回结果] - ").append(LogInfoEncryptUtil.getLogString(result));
            return result;
        } catch (Throwable e) {
			logger.warn("[outer] - [异常] - [" + sysName + "] - [" + methodName + "]", e);
            throw e;
        } finally {
            long time = (System.currentTimeMillis() - startTime);
            logBuffer.append(" --- [执行时间] - ").append("耗时[").append(time).append("]毫秒");
			logger.info(logBuffer.toString());
        }
    }

}
