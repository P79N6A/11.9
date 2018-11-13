package com.yeepay.g3.core.frontend.errorcode;


/**
 * 提供对接错误码系统相关服务
 */
public interface ErrorCodeTranslator {

	String DEFAULT_ERROR_CODE = ErrorCode.F0001000;

	/**
	 * 获取错误码描述
	 * @param errorCode
	 * @return
	 */
	String getMessage(String errorCode);

	/**
	 * 错误码信息转义
	 * @param source
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 */
	String translateCode(ErrorCodeSource source, String errorCode, String errorMessage);
}
