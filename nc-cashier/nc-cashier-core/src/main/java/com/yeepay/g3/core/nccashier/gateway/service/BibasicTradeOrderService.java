package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.facade.pccashier.pay.dto.CashierQueryResponseDTO;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.gateway.service
 *
 * @author pengfei.chen
 * @since 17/1/17 16:35
 */
public interface BibasicTradeOrderService {

    /**
     * 从二代交易获取订单详情
     * @param orderNo
     * @return
     */
    public CashierQueryResponseDTO queryOrderDetailFromBibasic(String orderNo);
}
