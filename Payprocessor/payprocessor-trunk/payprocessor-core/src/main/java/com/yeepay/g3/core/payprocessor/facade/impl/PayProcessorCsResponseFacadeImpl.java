package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.RefundBiz;
import com.yeepay.g3.facade.payprocessor.facade.PayProcessorCsResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/11/15.
 */
@Service
public class PayProcessorCsResponseFacadeImpl implements PayProcessorCsResponseFacade {

    @Autowired
    private RefundBiz refundBiz;

    @Override
    public String receiveResponse(Map<String, Object> object) {
        return refundBiz.receiveResponse(object);
    }
}
