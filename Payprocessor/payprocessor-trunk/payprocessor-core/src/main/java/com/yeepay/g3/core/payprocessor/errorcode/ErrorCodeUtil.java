package com.yeepay.g3.core.payprocessor.errorcode;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.foundation.dto.DefaultErrorCode;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.foundation.dto.YeepayCommonException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.foundation.util.ErrorCodeUtility;

public class ErrorCodeUtil {

	private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(ErrorCodeUtil.class);

	/**
	 * 错误码系统间转换
	 * 
	 * @param systemCode
	 * @param errorCode
	 * @param errorMsg
	 * @param defaultErrorCode
	 * @return
	 */
	public static PayBizException mapErrorCode(String systemCode, String errorCode, String errorMsg,
			String defaultErrorCode) {
		DefaultErrorCode _defaultErrorCode = new DefaultErrorCode();
		_defaultErrorCode.setDefaultSystemCode(ErrorCodeSource.PP.getSysCode());
		_defaultErrorCode.setDefaultErrorCode(defaultErrorCode);
		_defaultErrorCode.setDefaultErrorMsg(errorMsg);

		YeepayCommonException yeepayCommonException = null;
		try {
			yeepayCommonException = ErrorCodeUtility.mapErrorCode(systemCode, errorCode, errorMsg, _defaultErrorCode);
		} catch (Exception e) {
			logger.error("[系统异常] - [ErrorCodeUtility.mapErrorCode]-未能从错误码配置系统中找到对应错误关系:sysCode:",
					ErrorCodeSource.PP.getSysCode() + errorCode, e);
		}
		if (yeepayCommonException != null) {
			return new PayBizException(yeepayCommonException.getErrorCode(), yeepayCommonException.getErrorMsg());
		} else {
			return new PayBizException(defaultErrorCode, errorMsg);

		}
	}

	public static ErrorMeta translateCode(String source, String errorCode, String errorMessage, String defaultErrorCode) {
		if (StringUtils.isNotEmpty(errorCode)) {
			DefaultErrorCode _defaultErrorCode = new DefaultErrorCode();
			_defaultErrorCode.setDefaultSystemCode(ErrorCodeSource.PP.getSysCode());
			_defaultErrorCode.setDefaultErrorCode(defaultErrorCode);
			_defaultErrorCode.setDefaultErrorMsg(errorMessage);
			try {
				ErrorMeta errorMeta = ErrorCodeUtility.mapErrorMeta(source, errorCode, errorMessage, _defaultErrorCode);
				if (errorMeta != null) {
					return errorMeta;
				}
			} catch (Exception e) {
				logger.error("[Outer]-[系统异常] - [ErrorCodeUtility.mapErrorMeta]-未能从错误码配置系统中找到转换关系:sysCode:"
						+ ToStringBuilder.reflectionToString((defaultErrorCode)));
			}
		}
		return null;
	}

	/**
	 * 获取错误码信息
	 * 
	 * @param errorCode
	 * @return
	 */
	public static String retrieveErrorCodeMsg(String errorCode) {
		String errorMsg = null;
		try {
			errorMsg = ErrorCodeUtility.retrieveErrorCodeMsg(ErrorCodeSource.PP.getSysCode(), errorCode);
		} catch (Exception e) {
			logger.error("[系统异常] - [ErrorCodeUtility.retrieveErrorCodeMsg]-未能从错误码配置系统中找到对应错误信息:sysCode:",
					ErrorCodeSource.PP.getSysCode() + errorCode, e);
		}
		if (StringUtils.isBlank(errorMsg)) {
			errorMsg = "系统异常";
		}
		return errorMsg;

	}

}
