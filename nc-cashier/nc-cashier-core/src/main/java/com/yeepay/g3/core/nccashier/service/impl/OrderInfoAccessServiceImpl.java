package com.yeepay.g3.core.nccashier.service.impl;

import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.gateway.service.adapter.OrderInfoAccessAdapter;
import com.yeepay.g3.core.nccashier.service.OrderInfoAccessService;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.adapterservice.impl
 * 
 * @author pengfei.chen
 * @since 17/4/12 15:58
 */
@Service("orderInfoAccessAdapterService")
public class OrderInfoAccessServiceImpl extends ApplicationObjectSupport implements OrderInfoAccessService {

    @Override
    public OrderDetailInfoModel getOrderDetailInfoModel(String token,OrderSysConfigDTO orderSysConfigDTO) {
        Map<String, OrderInfoAccessAdapter> map = getApplicationContext().getBeansOfType(OrderInfoAccessAdapter.class);
        OrderDetailInfoModel orderDetailInfoModel = null;
        for (OrderInfoAccessAdapter orderInfoAccess : map.values()) {
            if(orderInfoAccess.isSupport(orderSysConfigDTO.getAccessCode())){
                orderDetailInfoModel = orderInfoAccess.getOrderDetailInfoModel(token,orderSysConfigDTO);
                orderDetailInfoModel.setPaySysCode(PaymentSysCode.PAY_PROCCESOR);
            }
        }
        return orderDetailInfoModel;
    }
}
