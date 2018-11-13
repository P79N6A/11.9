package com.yeepay.g3.core.frontend.biz;

import com.yeepay.g3.facade.frontend.dto.FeOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationResponseDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FrontendRefundResponseDTO;

import java.util.Date;

/**
 * @author chronos.
 * @createDate 16/8/4.
 */
public interface RefundManageBiz {
    /**
     * 差错退款请求处理
     * @param dto
     * @return
     */
    FrontendRefundResponseDTO refundRequest(FrontendRefundRequestDTO dto);

    /**
     * 根据时间段查询退款请求
     * 发送到退款中心
     * 更新退款状态
     * @param start
     * @param end
     */
    void refundByDate(Date start, Date end, String platformType);

    /**
     * 单笔退款接口:根据订单ID进行退款,不走定时
     * @return
     */
    FeOperationResponseDTO refundOrders(FeOperationRequestDTO requestDTO);
    
}
