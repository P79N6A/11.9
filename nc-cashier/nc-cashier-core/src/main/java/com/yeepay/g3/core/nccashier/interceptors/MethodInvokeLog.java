package com.yeepay.g3.core.nccashier.interceptors;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import com.yeepay.g3.core.nccashier.log.LogInfoEncryptUtil;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.util.HiddenCode;
import com.yeepay.g3.utils.common.log.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Felix on 1/15/16.
 */

@Component
public class MethodInvokeLog {

	private Logger logger = NcCashierLoggerFactory.getLogger(this.getClass());

	public void exceptionLog(Throwable e, String method)
			throws CashierBusinessException, IllegalArgumentException {
		if (e instanceof CashierBusinessException) {
			logger.warn("[业务异常] - [" + method + "] - errorCode:"
					+ ((CashierBusinessException) e).getDefineCode() + ", errorMsg:"
					+ e.getMessage(), e);
			throw (CashierBusinessException) e;
		} else if (e instanceof IllegalArgumentException) {
			logger.warn("[业务异常] - [" + method + "]", e);
			throw (IllegalArgumentException) e;
		} else {
			logger.error("[系统异常] - [" + method + "]", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}

	/**
	 * 格式化参数信息
	 *
	 * @param arguments 参数
	 * @return
	 */
	protected String formatArguments(Object[] arguments) {
		StringBuffer sb = new StringBuffer();
		if (arguments == null) {
			sb.append("null");
		} else {
			int i = 1;
			for (Object arg : arguments) {
				sb.append("[arg" + i + ":" + LogInfoEncryptUtil.getLogString(arg) + "] ");
				i++;
			}
		}
		return sb.toString();
	}

	public String getObjectString(Object o) {
		if (null == o)
			return null;
		if (isMobileNo(o)) {
			return HiddenCode.hiddenMobile(o.toString());
		}
		if (isBankCardNo(o)) {
			return HiddenCode.hiddenBankCardNO(o.toString());
		}
		if (isOverrideToString(o)) {
			return o.toString();
		}
		return ToStringBuilder.reflectionToString(o);

	}

	private boolean isOverrideToString(Object o) {
		if (o == null) {
			return false;
		}
		try {
			Method method = o.getClass().getMethod("toString", new Class[]{});
			return !method.getDeclaringClass().getName().equals("java.lang.Object");
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	private boolean isBankCardNo(Object o) {
		String str = String.valueOf(o);
		return str.matches("\\d{16}|\\d{19}");
	}

	public static boolean isMobileNo(Object o) {
		String str = String.valueOf(o);
		return str.matches("(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}");
	}
}
