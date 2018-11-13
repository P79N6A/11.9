package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.OrderInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.OrderInfoQueryRequestDTO;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.service
 *
 * @author pengfei.chen
 * @since 17/4/7 14:37
 */
public interface NcCashierOrderInfoFacade {

    public OrderInfoDTO queryOrderInfoByToken(OrderInfoQueryRequestDTO orderInfoQueryRequestDTO);
}
