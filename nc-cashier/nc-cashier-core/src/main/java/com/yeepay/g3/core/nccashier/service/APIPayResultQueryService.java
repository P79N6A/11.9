package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.facade.nccashier.dto.APIPayResultQueryResponseDTO;

/**
 * @program: nc-cashier-parent
 * @description: 支付结果查询Service
 * @author: jimin.zhou
 * @create: 2018-07-17 17:17
 **/
public interface APIPayResultQueryService {

    PaymentRecord getRecordByIdUseRedis(long recordId);

    APIPayResultQueryResponseDTO gerPayResultByQueryPP(PaymentRecord record);

    void saveRedisWhenQuerySucess(PaymentRecord record);


}
