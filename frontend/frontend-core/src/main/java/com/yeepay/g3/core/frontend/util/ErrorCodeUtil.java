package com.yeepay.g3.core.frontend.util;

import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.errorcode.ErrorCodeSource;
import com.yeepay.g3.facade.foundation.dto.DefaultErrorCode;
import com.yeepay.g3.facade.foundation.dto.YeepayCommonException;
import com.yeepay.g3.utils.foundation.util.ErrorCodeUtility;

public class ErrorCodeUtil {
	
	/**
	 * 错误码系统间转换
	 * @param systemCode
	 * @param errorCode
	 * @param errorMsg
	 * @param defaultErrorCode
	 * @return
	 */
	public static FrontendBizException mapErrorCode(String systemCode, String errorCode,
			String errorMsg, String defaultErrorCode){
		DefaultErrorCode _defaultErrorCode = new DefaultErrorCode();
		_defaultErrorCode.setDefaultSystemCode(ErrorCodeSource.FRONTEND.getSysCode());
		_defaultErrorCode.setDefaultErrorCode(defaultErrorCode);
		_defaultErrorCode.setDefaultErrorMsg(errorMsg);
		YeepayCommonException yeepayCommonException = ErrorCodeUtility.mapErrorCode(systemCode, errorCode, errorMsg, _defaultErrorCode);
		return new FrontendBizException(yeepayCommonException.getErrorCode(), yeepayCommonException.getErrorMsg());
	}
	
	/**
	 * 获取错误码信息
	 * @param errorCode
	 * @return
	 */
	public static String retrieveErrorCodeMsg(String errorCode){
		return ErrorCodeUtility.retrieveErrorCodeMsg(ErrorCodeSource.FRONTEND.getSysCode(), errorCode);
	}

}
