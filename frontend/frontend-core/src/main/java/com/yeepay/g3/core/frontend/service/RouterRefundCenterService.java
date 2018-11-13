package com.yeepay.g3.core.frontend.service;

import com.yeepay.g3.core.frontend.entity.PayOrder;

/**
 * @author chronos.
 * @createDate 16/8/5.
 */
public interface RouterRefundCenterService {

    /**
     * 先查询退款中心是否存在该退款请求(防止重复请求)
     * 不存在:提交退款到退款中心,同时更新退款状态
     * 存在:更新退款状态
     * @param order
     * @return
     */
    boolean submitRefundRequest(PayOrder order);
}
