package com.yeepay.g3.core.payprocessor.external.service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;

/**
 * 退款对接清算中心服务
 * @author chronos.
 * @createDate 2016/11/10.
 */
public interface CsProcessService {

    /**
     * 发送到清算中心
     * @param payment
     * @param payRecord
     * @return
     */
    boolean processCsRefund(PaymentRequest payment, PayRecord payRecord);

    /**
     * 判断清算中心是否处理成功
     * @param recordNo
     * @return
     */
    boolean queryCsRecord(String recordNo);

}
