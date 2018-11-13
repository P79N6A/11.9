package com.yeepay.g3.core.frontend.util.log;

import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.facade.bankchannel.dto.OpenPayRequestDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.yeepay.g3.facade.trade.bankinterface.nocard.result.VerifyResultDTO;


/**
 * 对log日志打印的敏感信息进行加密处理
 */
public class LogInfoEncryptUtil {
	private static final Map<Class, LogString> logStringMap = new HashMap<Class, LogString>();

	private static final MethodInvokeLog methodInvokeLog = new MethodInvokeLog();


	static {

		logStringMap.put(VerifyResultDTO.class, new LogString() {
			@Override
			public String getLogString(Object object) {
				return ToStringBuilder.reflectionToString(object);
			}
		});

		//added by dongbo.jiao 20180322 to mask senstive msg
		logStringMap.put(OpenPayRequestDTO.class, new LogString() {
			@Override
			public String getLogString(Object object) {
				return ReflectionToStringBuilder.toStringExclude(object,"userIdCard","userTrueName");
			}
		});

	}


	public static <T> String getLogString(T object) {
		if (object == null) {
			return null;
		}
		if(object.getClass().isArray()) {
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
            sb.append("[arg" + i + ":" + getSingleObjectLogString(arg)+"] ");
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
	
	// added by zhijun.wang 2017-06-29
	public static <T> String getLogStringforBankNotify(T object) {
		if (object == null) {
			return null;
		}
		if(object.getClass().isArray()) {
			return getArrayLogString((Object[]) object);
		} else {
			return getSingleObjectLogStringforBankNotify(object);
		}
	}
	
	// added by zhijun.wang 2017-06-29
	private static <T> String getSingleObjectLogStringforBankNotify(T object) {
		if (object == null) {
			return null;
		}
		LogString<T> logString = logStringMap.get(object.getClass());
		if (null == logString) {
			return methodInvokeLog.getObjectStringforBankNotify(object);
		}
		return logString.getLogString(object);
	}

}
