package com.yeepay.g3.core.nccashier.log;

/**
 * 获取对应DTO的日志信息, 主要用于日志掩码功能
 * 
 * @author Created by Felix on 1/18/16.
 */
public interface LogString<T> {
	
	String getLogString(T object);
}
