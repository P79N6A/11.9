package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.biz.RefundManageBiz;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendRefundDaemonFacade;
import com.yeepay.g3.utils.common.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author chronos.
 * @createDate 16/8/4.
 */
@Service("frontendRefundDaemonFacade")
public class FrontEndRefundDaemonFacadeImpl implements FrontendRefundDaemonFacade {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FrontEndRefundDaemonFacadeImpl.class);

//    private static final String DATE_FORMAT = "yyyyMMddHHmm";

    @Autowired
    private RefundManageBiz refundManageBiz;

    @Override
    public void errorRefundWX(int minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end,-minutes);
            refundManageBiz.refundByDate(start,end,PlatformType.WECHAT.name());
        } catch (Throwable th){
            logger.error("[系统异常][退款处理失败]",th);
        }

    }
    
    @Override
    public void errorRefundZFB(int minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end,-minutes);
            refundManageBiz.refundByDate(start,end,PlatformType.ALIPAY.name());
        } catch (Throwable th){
            logger.error("[系统异常][退款处理失败]",th);
        }

    }
    
    @Override
    public void errorRefundUPOP(int minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end,-minutes);
            refundManageBiz.refundByDate(start,end,PlatformType.OPEN_UPOP.name());
        } catch (Throwable th){
            logger.error("[系统异常][退款处理失败]",th);
        }

    }
    
    @Override
    public void errorRefundJD(int minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end,-minutes);
            refundManageBiz.refundByDate(start,end,PlatformType.JD.name());
        } catch (Throwable th){
            logger.error("[系统异常][退款处理失败]",th);
        }

    }

    @Override
    public void errorRefundQQ(int minutes) {
        try {
            Date end = Calendar.getInstance().getTime();
            Date start = DateUtils.addMinute(end,-minutes);
            refundManageBiz.refundByDate(start,end,PlatformType.QQ.name());
        } catch (Throwable th){
            logger.error("[系统异常][退款处理失败]",th);
        }

    }

//    @Override
//    public void errorRefundByDate(String start, String end) {
//        try {
//            Date startDate = DateUtils.parseDate(start, DATE_FORMAT);
//            Date endDate = DateUtils.parseDate(end,DATE_FORMAT);
//            refundManageBiz.refundByDate(startDate,endDate);
//        } catch (ParseException e) {
//            logger.warn("[业务异常]",e);
//        } catch (Throwable th){
//            logger.error("[系统异常][退款处理失败]",th);
//        }
//    }

}
