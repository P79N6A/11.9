package com.yeepay.g3.facade.payprocessor.facade;

import java.util.Map;

/**
 * 支付处理器接收清算中心回调接口
 * @author chronos.
 * @createDate 2016/11/15.
 */
public interface PayProcessorCsResponseFacade {

    /**
     * 接收清算中心回调接口
     * @param object
     * @return
     */
    String receiveResponse(Map<String, Object> object);
}
