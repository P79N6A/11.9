package com.yeepay.g3.core.nccashier.log;

import com.yeepay.g3.core.nccashier.interceptors.MethodInvokeLog;
import com.yeepay.riskcontrol.facade.v2.RcSyncPayReqDto;

/**
 * 对log日志打印的敏感信息进行加密处理
 * 
 * @author peile.fan 2015年7月8日下午4:20:06
 */
public class LogInfoEncryptUtil {

	private static final MethodInvokeLog methodInvokeLog = new MethodInvokeLog();

	public static <T> String getLogString(T object) {
		if (object == null) {
			return null;
		}
		if (object.getClass().isArray()) {
			return getArrayLogString((Object[]) object);
		} else {
			return methodInvokeLog.getObjectString(object);
		}
	}

	public static String logString(RcSyncPayReqDto reqRisk) {
		return getLogString(reqRisk);
	}

	private static String getArrayLogString(Object[] object) {
		StringBuffer sb = new StringBuffer();
		Object[] arguments = object;
		int i = 1;
		for (Object arg : arguments) {
			sb.append("[arg" + i + ":" + getLogString(arg) + "] ");
			i++;
		}
		return sb.toString();
	}
}
