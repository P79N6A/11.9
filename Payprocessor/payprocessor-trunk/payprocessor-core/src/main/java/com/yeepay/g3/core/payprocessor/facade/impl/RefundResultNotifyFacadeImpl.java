package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.RefundBiz;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.refund.RefundResultNotifyFacade;
import com.yeepay.g3.facade.refund.dto.RefundResultNotifyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 退款中心完成回调接口
 * @author chronos.
 * @createDate 16/8/5.
 */
@Service("refundResultNotifyFacade")
public class RefundResultNotifyFacadeImpl implements RefundResultNotifyFacade {

    private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(RefundResultNotifyFacadeImpl.class);

    @Autowired
    private RefundBiz refundBiz;

    @Override
    public void refundResultNotify(RefundResultNotifyDTO refundResultNotifyDTO) {
        refundBiz.receiveCenterNotify(refundResultNotifyDTO);
    }
}
