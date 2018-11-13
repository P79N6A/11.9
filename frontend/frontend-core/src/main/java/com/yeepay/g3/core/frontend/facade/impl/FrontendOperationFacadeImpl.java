package com.yeepay.g3.core.frontend.facade.impl;

import com.yeepay.g3.core.frontend.biz.NotifyBiz;
import com.yeepay.g3.core.frontend.biz.QueryBiz;
import com.yeepay.g3.core.frontend.biz.RefundManageBiz;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.frontend.dto.FeOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendOperationFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chronos.h
 * @createDate 16/8/12.
 */
@Service
public class FrontendOperationFacadeImpl implements FrontendOperationFacade {
    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FeLogger.class);

    @Resource
    private QueryBiz queryBiz;

    @Resource
    private NotifyBiz notifyBiz;

    @Resource
    private RefundManageBiz refundManageBiz;

    @Override
    public FeOperationResponseDTO repairOrders(FeOperationRequestDTO orderNos) {
        FeLoggerFactory.TAG_LOCAL.set("[批量补单]");
        try {
            return queryBiz.repairOrders(orderNos);
        } finally {
            FeLoggerFactory.TAG_LOCAL.remove();
        }
    }

    @Override
    public FeOperationResponseDTO reNotifyOrders(FeOperationRequestDTO orderNos) {
        FeLoggerFactory.TAG_LOCAL.set("[批量补发通知]");
        try {
            return notifyBiz.notifyOrders(orderNos);
        } finally {
            FeLoggerFactory.TAG_LOCAL.remove();
        }
    }

    @Override
    public FeOperationResponseDTO refundOrders(FeOperationRequestDTO requestDTO) {
        FeLoggerFactory.TAG_LOCAL.set("[批量补发退款] - ");
        try {
            return refundManageBiz.refundOrders(requestDTO);
        } finally {
            FeLoggerFactory.TAG_LOCAL.remove();
        }
    }

}
