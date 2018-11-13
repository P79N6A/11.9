package com.yeepay.g3.core.nccashier.gateway.service.adapter;

import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service
 * 交易信息接入适配器
 * @author pengfei.chen
 * @since 17/4/12 11:19
 */
public interface OrderInfoAccessAdapter {

    /**
     * 适配出订单标准出参信息
     * @param orderNo
     * @param bizType 
     * @return
     */
    public OrderDetailInfoModel getOrderDetailInfoModel(String orderNo, OrderSysConfigDTO orderSysConfigDTO);

    /**
     * 根据业务方匹配是否支持此适配器
     * @param accessCode
     * @return
     */
    public boolean isSupport(String accessCode);

}
