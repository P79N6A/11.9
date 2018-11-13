
package com.yeepay.g3.app.nccashier.wap.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.app.nccashier.wap.utils.LogInfoEncryptUtil;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * aciton拦截器，参数校验、日志打印，异常处理等。
 * 
 * @author：peile.fan
 * @since：2016年5月20日 下午1:44:42
 * @version:
 */
public class ActionInterceptor implements MethodInterceptor {
	private Logger logger = LoggerFactory.getLogger(ActionInterceptor.class);

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long startTime = System.currentTimeMillis();
		Method method = invocation.getMethod();
		String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
		Object[] args = invocation.getArguments();
		Map<String,String> outParamMap = null;
		
		for (Object obj : args) {
			if (obj instanceof HttpServletRequest) {
				HttpServletRequest request = (HttpServletRequest) obj;
				Map<String,Object> paramMap = request.getParameterMap();
				outParamMap = LogInfoEncryptUtil.sensitiveInfoConvert(paramMap);
				break;
			}
		}
			
		Object result = null;
		try {
			logger.info("[nc-cashier-action] - [入参] - [" + methodName + "] - [" + JSONObject.toJSONString(outParamMap) + "]");
			result = invocation.proceed();
		} catch (Throwable e) {
			logger.error("http请求拦截器异常 e:{}", e);
		} finally {
			long time = (System.currentTimeMillis() - startTime);
			logger.info("[nc-cashier-action] - [执行时间] - [" + methodName + "] - 耗时[" + time + "]毫秒");
		}
		return result;
	}

}
