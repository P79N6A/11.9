package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.Exception.FrontendBizException;
import com.yeepay.g3.core.frontend.biz.NotifyBiz;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendReNotifyDaemonFacade;
import com.yeepay.g3.utils.common.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author chronos.
 * @createDate 16/8/31.
 */
@Service("frontendReNotifyDaemonFacade")
public class FrontendReNotifyDaemonFacadeImpl implements FrontendReNotifyDaemonFacade {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontendReNotifyDaemonFacadeImpl.class);

//    private static final String DATE_FORMAT = "yyyyMMddHHmm";

    @Autowired
    private NotifyBiz notifyBiz;

    @Override
    public void reNotifyWX(Integer minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end, -minutes);
            notifyBiz.notifyMqByDate(start,end, PlatformType.WECHAT.name());
        } catch (FrontendBizException fe){
            logger.warn("[业务异常]",fe);
        } catch (Throwable th){
            logger.error("[系统异常]",th);
        }
    }
    
    @Override
    public void reNotifyZFB(Integer minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end, -minutes);
            notifyBiz.notifyMqByDate(start,end, PlatformType.ALIPAY.name());
        } catch (FrontendBizException fe){
            logger.warn("[业务异常]",fe);
        } catch (Throwable th){
            logger.error("[系统异常]",th);
        }
    }
    
    @Override
    public void reNotifyUPOP(Integer minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end, -minutes);
            notifyBiz.notifyMqByDate(start,end, PlatformType.OPEN_UPOP.name());
        } catch (FrontendBizException fe){
            logger.warn("[业务异常]",fe);
        } catch (Throwable th){
            logger.error("[系统异常]",th);
        }
    }
    
    @Override
    public void reNotifyJD(Integer minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end, -minutes);
            notifyBiz.notifyMqByDate(start,end, PlatformType.JD.name());
        } catch (FrontendBizException fe){
            logger.warn("[业务异常]",fe);
        } catch (Throwable th){
            logger.error("[系统异常]",th);
        }
    }

    @Override
    public void reNotifyQQ(Integer minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end, -minutes);
            notifyBiz.notifyMqByDate(start,end, PlatformType.QQ.name());
        } catch (FrontendBizException fe){
            logger.warn("[业务异常]",fe);
        } catch (Throwable th){
            logger.error("[系统异常]",th);
        }
    }
}
