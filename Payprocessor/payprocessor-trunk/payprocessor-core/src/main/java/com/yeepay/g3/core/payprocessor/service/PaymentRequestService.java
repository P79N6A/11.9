package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentRequestService {

    /**
     * 通过唯一索引查询订单
     * @param orderSystem
     * @param orderNo
     * @return
     */
    PaymentRequest queryBySystemAndOrderNo(String orderSystem, String orderNo);

    /**
     * 创建订单
     * @param payment
     */
    void createPaymentRequest(PaymentRequest payment);

    /**
     * 根据主键查询
     * @param requestId
     * @return
     */
    PaymentRequest selectByPrimaryKey(Long requestId);

	int updateRequestToReverse(Long requestId, String orgStatus);

    /**
     * 查询成功但未通知的订单
     * @param startDate
     * @param endDate
     * @return
     */
    List<PaymentRequest> queryUnNotifyPayment(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 更新订单类型和订单状态
     * @param requestId
     * @param payType 订单类型
     * @param status 订单状态
     * @param prePayType 原订单类型
     * @param preStatus 原订单状态
     * @return
     */
    int updatePayTypeAndStatus(Long requestId, String payType, String status, String prePayType, String preStatus);

    /**
     * 更新主单通知业务方状态
     */
    int updatePaymentOrderSystemStatus(Long requestId, String orderSystemStatus);
}
