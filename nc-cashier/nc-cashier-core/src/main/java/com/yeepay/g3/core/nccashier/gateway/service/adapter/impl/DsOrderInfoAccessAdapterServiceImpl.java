package com.yeepay.g3.core.nccashier.gateway.service.adapter.impl;

import com.yeepay.g3.core.nccashier.gateway.service.OrderProcessorService;
import com.yeepay.g3.core.nccashier.gateway.service.adapter.OrderInfoAccessAdapter;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service.impl
 * 大算交易信息接入适配器实现
 * @author pengfei.chen
 * @since 17/4/12 11:45
 */
@Service
public class DsOrderInfoAccessAdapterServiceImpl extends NcCashierBaseService implements OrderInfoAccessAdapter {

    @Resource
    private OrderProcessorService orderProcessorService;
    @Override
    public OrderDetailInfoModel getOrderDetailInfoModel(String orderNo,OrderSysConfigDTO orderSysConfigDTO) {
        return orderProcessorService.getOrderDetailInfo(orderNo);
    }

    @Override
    public boolean isSupport(String accessCode) {
        return Constant.DS_SYS_INTERFACE.equals(accessCode);
    }
}
