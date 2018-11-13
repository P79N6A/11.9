package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;
import com.yeepay.g3.facade.refund.dto.RefundResultNotifyDTO;

import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
public interface RefundBiz {

    /**
     * 定时将退款发送到清算中心
     */
    void sendToCs();

    /**
     * 定时查询退款中心处理状态
     */
    void queryCsResult();

    /**
     * 定时将退款发送到退款中心
     */
    void sendToRefundCenter();

    /**
     * 退款中心回调处理
     * @param refundResultNotifyDTO
     */
    void receiveCenterNotify(RefundResultNotifyDTO refundResultNotifyDTO);

    /**
     * 接受清算中心回调后处理
     * @param object
     * @return
     */
    String receiveResponse(Map<String, Object> object);

    /**
     * 批量补发退款
     * @param requestDTO
     * @return
     */
    OperationResponseDTO batchRefund(OperationRequestDTO requestDTO);
}
