package com.yeepay.g3.core.payprocessor.external.service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;

/**
 * 退款中心服务
 * @author chronos.
 * @createDate 2016/11/10.
 */
public interface RefundCenterService {
    /**
     * 发送到退款中心
     * @param payment
     * @param payRecord
     * @param reverseRecord
     * @return
     */
    boolean processRefund(PaymentRequest payment, PayRecord payRecord, ReverseRecord reverseRecord);

}
