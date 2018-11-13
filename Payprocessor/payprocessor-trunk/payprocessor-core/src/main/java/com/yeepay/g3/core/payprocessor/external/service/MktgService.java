/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.external.service;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.facade.mktg.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.BasicRequestDTO;

/**
 * 类名称: MktgService <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/6/19 上午10:11
 * @version: 1.0.0
 */
public interface MktgService {


    /**
     * 预冻结
     * @param requestDTO
     * @param payRecord
     * @param combPayRecord
     * @param paymentRequest
     */
    void deposit(BasicRequestDTO requestDTO, PayRecord payRecord, CombPayRecord combPayRecord, PaymentRequest paymentRequest);

    /**
     * 扣款
     * @param payRecord
     * @param combPayRecord
     */
    void payment(PayRecord payRecord, CombPayRecord combPayRecord, PaymentRequest paymentRequest);
}
