package com.yeepay.g3.core.frontend.util.log;

import org.springframework.core.NamedThreadLocal;
import com.yeepay.g3.utils.common.log.Logger;

public class FeLoggerFactory {

	/**
	 * 获取Logger 实例
	 * @param clazz
	 * @return
     */
	public static Logger getLogger(Class<?> clazz){
		return new FeLogger(clazz);
	}
	
	public static final NamedThreadLocal<String> TAG_LOCAL = new NamedThreadLocal<String>("TAG_THREAD_LOCAL");

}
