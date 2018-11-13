package com.yeepay.g3.core.nccashier.log;

import org.springframework.core.NamedThreadLocal;

import com.yeepay.g3.utils.common.log.Logger;

public class NcCashierLoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		return new NcCashierLogger(clazz);
	}

	public static final NamedThreadLocal<String> TAG_LOCAL =
			new NamedThreadLocal<String>("TAG_THREAD_LOCAL");

}
