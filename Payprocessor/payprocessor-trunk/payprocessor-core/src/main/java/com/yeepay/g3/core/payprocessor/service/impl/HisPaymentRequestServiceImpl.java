/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.service.impl;

import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.hisDao.HisPaymentRequestDao;
import com.yeepay.g3.core.payprocessor.service.HisPaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类名称: HisPaymentRequestServiceImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/24 下午4:43
 * @version: 1.0.0
 */
@Service
public class HisPaymentRequestServiceImpl implements HisPaymentRequestService {

    @Autowired
    private HisPaymentRequestDao hisPaymentRequestDao;

    @Override
    public PaymentRequest queryBySystemAndOrderNo(String orderSystem, String orderNo) {
        return hisPaymentRequestDao.queryBySystemAndOrderNo(orderSystem, orderNo);
    }
}