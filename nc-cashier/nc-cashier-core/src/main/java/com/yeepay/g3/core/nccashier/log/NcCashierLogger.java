package com.yeepay.g3.core.nccashier.log;

import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class NcCashierLogger implements Logger {

	private Logger logger;

	private Class<?> clazz;

	public NcCashierLogger(Class<?> clazz) {
		this.logger = LoggerFactory.getLogger(clazz);
		this.clazz = clazz;
	}

	@Override
	public void debug(String arg0) {
		if (!isDebugEnabled()) {
			return;
		}
		logger.debug(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0);
	}

	@Override
	public void debug(String arg0, Object... arg1) {
		if (!isDebugEnabled()) {
			return;
		}
		logger.debug(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);

	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		if (!isDebugEnabled()) {
			return;
		}
		logger.debug(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);
	}

	@Override
	public void error(final String arg0) {
		if (!isErrorEnabled()) {
			return;
		}
		logger.error(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0);
	}

	@Override
	public void error(String arg0, Object... arg1) {
		if (!isErrorEnabled()) {
			return;
		}
		logger.error(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);
	}

	@Override
	public void error(String arg0, Throwable arg1) {
		if (!isErrorEnabled()) {
			return;
		}
		logger.error(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);
	}

	@Override
	public void info(String arg0) {
		if (!isInfoEnabled()) {
			return;
		}
		logger.info(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0);
	}

	@Override
	public void info(String arg0, Object... arg1) {
		if (!isInfoEnabled()) {
			return;
		}
		logger.info(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);

	}

	@Override
	public void info(String arg0, Throwable arg1) {
		if (!isInfoEnabled()) {
			return;
		}
		logger.info(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public void trace(String arg0) {
		if (!isTraceEnabled()) {
			return;
		}
		logger.trace(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0);
	}

	@Override
	public void trace(String arg0, Object... arg1) {
		if (!isTraceEnabled()) {
			return;
		}
		logger.trace(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);

	}

	@Override
	public void trace(String arg0, Throwable arg1) {
		if (!isTraceEnabled()) {
			return;
		}
		logger.trace(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);
	}

	@Override
	public void warn(String arg0) {
		if (!isWarnEnabled()) {
			return;
		}
		logger.warn(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0);
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		if (!isWarnEnabled()) {
			return;
		}
		logger.warn(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		if (!isWarnEnabled()) {
			return;
		}
		logger.warn(NcCashierLoggerFactory.TAG_LOCAL.get() + "-" + arg0, arg1);
	}

	@Override
	public void saveToDB(Object arg0) {

	}

	@Override
	public void saveToDB(String arg0) {

	}
}
