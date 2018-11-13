package com.yeepay.g3.core.payprocessor.util.log;

import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class PayLogger implements Logger{

	public static final String MSG_CONNECTOR = " - ";
	private Logger logger;
	
	private Class<?> clazz;
	
	public PayLogger(Class<?> clazz){
		this.logger = LoggerFactory.getLogger(clazz);
		this.clazz = clazz;
	}
	
	@Override
	public void debug(String arg0) {
		if(!isDebugEnabled()){
			return;
		}
		logger.debug(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
	}

	@Override
	public void debug(String arg0, Object... arg1) {
		if(!isDebugEnabled()){
			return;
		}
		arg0 = this.format(arg0, arg1);
		if(arg1[arg1.length-1] instanceof Throwable){
			logger.debug(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, (Throwable)arg1[arg1.length-1]);
		}
		else{
			logger.debug(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
		}
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		if(!isDebugEnabled()){
			return;
		}
		logger.debug(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, arg1);
	}

	@Override
	public void error(final String arg0) {
		if(!isErrorEnabled()){
			return;
		}
		logger.error(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
	}

	@Override
	public void error(String arg0, Object... arg1) {
		if(!isErrorEnabled()){
			return;
		}
		final String msg = this.format(arg0, arg1);
		if(arg1[arg1.length-1] instanceof Throwable){
			logger.error(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + msg, (Throwable)arg1[arg1.length-1]);
		}
		else{
			logger.error(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + msg);
		}
	}

	@Override
	public void error(String arg0, Throwable arg1) {
		if(!isErrorEnabled()){
			return;
		}
		final String msg = this.format(arg0, new Object[]{arg1});
		logger.error(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + msg, arg1);
	}

	@Override
	public void info(String arg0) {
		if(!isInfoEnabled()){
			return;
		}
		logger.info(PayLoggerFactory.TAG_LOCAL.get() + MSG_CONNECTOR + arg0);
	}

	@Override
	public void info(String arg0, Object... arg1) {
		if(!isInfoEnabled()){
			return;
		}
		arg0 = this.format(arg0, arg1);
		if(arg1[arg1.length-1] instanceof Throwable){
			logger.info(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, (Throwable)arg1[arg1.length-1]);
		}
		else{
			logger.info(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
		}
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		if(!isInfoEnabled()){
			return;
		}
		logger.info(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, arg1);
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
	
	public boolean isTraceEnabled(){
		return true;
	}

	@Override
	public void trace(String arg0) {
		if(!isTraceEnabled()){
			return;
		}
		logger.trace(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
	}

	@Override
	public void trace(String arg0, Object... arg1) {
		if(!isTraceEnabled()){
			return;
		}
		arg0 = this.format(arg0, arg1);
		if(arg1[arg1.length-1] instanceof Throwable){
			logger.trace(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, (Throwable)arg1[arg1.length-1]);
		}
		else{
			logger.trace(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
		}
	}

	@Override
	public void trace(String arg0, Throwable arg1) {
		if(!isTraceEnabled()){
			return;
		}
		logger.trace(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, arg1);
	}

	@Override
	public void warn(String arg0) {
		if(!isWarnEnabled()){
			return;
		}
		logger.warn(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
	}

	@Override
	public void warn(String arg0, Object... arg1) {
		if(!isWarnEnabled()){
			return;
		}
		arg0 = this.format(arg0, arg1);
		if(arg1[arg1.length-1] instanceof Throwable){
			logger.warn(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, (Throwable)arg1[arg1.length-1]);
		}
		else{
			logger.warn(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0);
		}
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		if(!isWarnEnabled()){
			return;
		}
		logger.warn(PayLoggerFactory.TAG_LOCAL.get()+ MSG_CONNECTOR + arg0, arg1);
	}

	/**
	 * 堆栈信息
	 * @return，0=class，1=file，2=line，3=method
	 */
	private String[] getLocation(Throwable tr){
		StackTraceElement[] trace = tr.getStackTrace();
		for(StackTraceElement t : trace){
			if(t.getClassName().equals(clazz.getName())){
				return new String[]{t.getClassName(), t.getFileName(), String.valueOf(t.getLineNumber()), t.getMethodName()};
			}
		}
		return new String[4];
	}
	
	private String format(String arg0, Object[] arg1){
		if(arg0==null||arg0.isEmpty()||arg1==null||arg1.length==0){
			return arg0;
		}
		for(int i = 0; i!=arg1.length; i++){
			if(arg1[i]==null){
				continue;
			}
			if(arg1[i] instanceof String){
			}
			else if(arg1[i] instanceof Throwable){
				Throwable t = (Throwable)arg1[i];
				String msg = t.toString();
				StringBuffer traceLog = new StringBuffer();
				StackTraceElement[] trace = t.getStackTrace();
				for (StackTraceElement traceElement : trace) {
					traceLog.append("\nat " + traceElement);
				}
				msg = msg+"\n\r"+ traceLog.toString();
				if(null==msg || msg.trim().isEmpty()){
					arg1[i]=(t.getClass().getName());
				}
				else{
					arg1[i]=msg;
				}
			}
			else{
				arg1[i]=JSONUtils.toJsonString(arg1[i]);
			}
		}
		return java.text.MessageFormat.format(arg0, arg1);
	}

	@Override
	public void saveToDB(Object arg0) {
		
	}

	@Override
	public void saveToDB(String arg0) {
		
	}

}
