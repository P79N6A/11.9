package com.yeepay.g3.core.payprocessor.util.log;

import java.util.HashMap;
import java.util.Map;

/**
 * 对log日志打印的敏感信息进行加密处理
 */
public class LogInfoEncryptUtil {
	private static final Map<Class, LogString> logStringMap = new HashMap<Class, LogString>();

	private static final MethodInvokeLog methodInvokeLog = new MethodInvokeLog();

	public static <T> String getLogString(T object) {
		if (object == null) {
			return null;
		}
		if (object.getClass().isArray()) {
			return getArrayLogString((Object[]) object);
		} else {
			return getSingleObjectLogString(object);
		}

	}

	private static String getArrayLogString(Object[] object) {
		StringBuffer sb = new StringBuffer();
		Object[] arguments = object;
		int i = 1;
		for (Object arg : arguments) {
			sb.append("[arg" + i + ":" + getSingleObjectLogString(arg) + "] ");
			i++;
		}
		return sb.toString();
	}

	private static <T> String getSingleObjectLogString(T object) {
		if (object == null) {
			return null;
		}
		LogString<T> logString = logStringMap.get(object.getClass());
		if (null == logString) {
			return methodInvokeLog.getObjectString(object);
		}
		return logString.getLogString(object);
	}

}
