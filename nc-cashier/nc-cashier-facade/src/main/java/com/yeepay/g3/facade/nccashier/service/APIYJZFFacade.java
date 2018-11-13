package com.yeepay.g3.facade.nccashier.service;


import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * API收银台一键支付Facade，首次支付下单、二次支付下单、请求发短验、确认支付
 */
public interface APIYJZFFacade {

    /**
     * 首次支付，请求下单
     * @param requestDTO
     * @return
     */
    APIYJZFFirstPaymentResponseDTO firstPayRequest(APIYJZFFirstPaymentRequestDTO requestDTO);

    /**
     * 二次支付，请求下单
     * @param requestDTO
     * @return
     */
    APIYJZFBindPaymentResponseDTO bindPayRequest(APIYJZFBindPaymentRequestDTO requestDTO);

    /**
     * 请求发验证码
     * @param requestDTO
     * @return
     */
    APIBasicResponseDTO sendSMS(APIYJZFSendSmsRequestDTO requestDTO);

    /**
     * 确认支付
     * @param requestDTO
     * @return
     */
    APIYJZFConfirmPayResponseDTO paymentConfirm(APIYJZFConfirmPayRequestDTO requestDTO);
}
