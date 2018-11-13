package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface HisPaymentRequestService {

    /**
     * 通过唯一索引查询订单
     * @param orderSystem
     * @param orderNo
     * @return
     */
    PaymentRequest queryBySystemAndOrderNo(String orderSystem, String orderNo);


}
