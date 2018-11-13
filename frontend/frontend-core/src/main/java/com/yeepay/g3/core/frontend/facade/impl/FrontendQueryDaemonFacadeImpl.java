package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.biz.QueryBiz;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendQueryDaemonFacade;
import com.yeepay.g3.utils.common.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service("frontendQueryDaemonFacade")
public class FrontendQueryDaemonFacadeImpl implements FrontendQueryDaemonFacade{
	
	private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendQueryDaemonFacadeImpl.class);
	
	@Autowired
	private QueryBiz queryBiz;
	
	@Override
	public void queryBankOrderWX(int minutes) {
		try{
			FeLoggerFactory.TAG_LOCAL.set("[定时补单WX]");
			logger.info("minutes: " + minutes);
			Date endDate = new Date();
			Date beginDate = DateUtils.addMinute(endDate, -minutes);
			queryBiz.queryBankOrder(beginDate, endDate, PlatformType.WECHAT.name());
		} catch(Throwable ta){
			logger.error("[定时补单异常]", ta);
		} finally {
	    	FeLoggerFactory.TAG_LOCAL.set(null);
	    }
	}
	
	@Override
	public void queryBankOrderZFB(int minutes) {
		try{
			FeLoggerFactory.TAG_LOCAL.set("[定时补单ZFB]");
			logger.info("minutes: " + minutes);
			Date endDate = new Date();
			Date beginDate = DateUtils.addMinute(endDate, -minutes);
			queryBiz.queryBankOrder(beginDate, endDate, PlatformType.ALIPAY.name());
		} catch(Throwable ta){
			logger.error("[定时补单异常]", ta);
		} finally {
	    	FeLoggerFactory.TAG_LOCAL.set(null);
	    }
	}
	
	@Override
	public void queryBankOrderUPOP(int minutes) {
		try{
			FeLoggerFactory.TAG_LOCAL.set("[定时补单UPOP]");
			logger.info("minutes: " + minutes);
			Date endDate = new Date();
			Date beginDate = DateUtils.addMinute(endDate, -minutes);
			queryBiz.queryBankOrder(beginDate, endDate, PlatformType.OPEN_UPOP.name());
		} catch(Throwable ta){
			logger.error("[定时补单异常]", ta);
		} finally {
	    	FeLoggerFactory.TAG_LOCAL.set(null);
	    }
	}
	
	@Override
	public void queryBankOrderJD(int minutes) {
		try{
			FeLoggerFactory.TAG_LOCAL.set("[定时补单JD]");
			logger.info("minutes: " + minutes);
			Date endDate = new Date();
			Date beginDate = DateUtils.addMinute(endDate, -minutes);
			queryBiz.queryBankOrder(beginDate, endDate, PlatformType.JD.name());
		} catch(Throwable ta){
			logger.error("[定时补单异常]", ta);
		} finally {
	    	FeLoggerFactory.TAG_LOCAL.set(null);
	    }
	}

	@Override
	public void queryBankOrderQQ(int minutes) {
		try{
			FeLoggerFactory.TAG_LOCAL.set("[定时补单QQ]");
			logger.info("minutes: " + minutes);
			Date endDate = new Date();
			Date beginDate = DateUtils.addMinute(endDate, -minutes);
			queryBiz.queryBankOrder(beginDate, endDate, PlatformType.QQ.name());
		} catch(Throwable ta){
			logger.error("[定时补单异常]", ta);
		} finally {
			FeLoggerFactory.TAG_LOCAL.set(null);
		}
	}

	@Override
	public void queryBankOrderWX(String start, String end) {
		try{
			FeLoggerFactory.TAG_LOCAL.set("[指定时段补单WX]");
			logger.info("queryBankOrderWX() start:{},end:{} ",start,end);
			Date startDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(start);
			Date endDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(end);
			queryBiz.queryBankOrder(startDate, endDate, PlatformType.WECHAT.name());
		} catch(Throwable ta){
			logger.error("[指定时段补单异常]", ta);
		} finally {
			FeLoggerFactory.TAG_LOCAL.set(null);
		}
	}

	@Override
	public void queryBankOrderZFB(String start, String end) {
		try{
			FeLoggerFactory.TAG_LOCAL.set("[指定时段补单ZFB]");
			logger.info("queryBankOrderZFB() start:{},end:{} ",start,end);
			Date startDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(start);
			Date endDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(end);
			queryBiz.queryBankOrder(startDate, endDate, PlatformType.WECHAT.name());
		} catch(Throwable ta){
			logger.error("[指定时段补单异常]", ta);
		} finally {
			FeLoggerFactory.TAG_LOCAL.set(null);
		}
	}
}
