package com.yeepay.g3.core.frontend.service;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.facade.frontend.dto.BasicRequestDTO;

/**
 * 风控服务类
 * @author chronos.
 * @createDate 16/9/18.
 */
public interface RiskControlService {

    /**
     * 下单同步风控接口
     */
    void syncControl(BasicRequestDTO requestDTO, PayOrder payOrder);

    /**
     * 支付完成异步风控接口
     * @param payOrder
     */
    void asyncControl(PayOrder payOrder);
}
