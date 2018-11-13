package com.yeepay.g3.core.nccashier.service;


import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * API收银台-绑卡支付-业务逻辑接口
 * Created by ruiyang.du on 2017/6/28.
 */
public interface APICashierBindCardPayService {


    /**
     * 绑卡支付，请求下单并获取补充项
     * @param needItemRequestDTO
     * @return
     */
    ApiBindPayPaymentResponseDTO requestPayment(ApiBindPayPaymentRequestDTO needItemRequestDTO);

    /**
     * 绑卡支付，请求发短验
     * @param param
     * @return
     */
    ApiBindPaySendSmsResponseDTO requestSmsSend(ApiBindPaySendSmsRequestDTO param);

    /**
     * 绑卡支付，校验短验，确认支付
     * @param param
     * @return
     */
    ApiBindPayConfirmResponseDTO confirmPayment(ApiBindPayConfirmRequestDTO param);
}
