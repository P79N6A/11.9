package com.yeepay.g3.core.frontend.util.log;

/**
 * 获取对应DTO的日志信息, 主要用于日志掩码功能
 */
public interface LogString<T> {
	/**
	 *
	 * @return
	 */
	String getLogString(T object);
}
