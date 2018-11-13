package com.yeepay.g3.facade.nccashier.service;


import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * 统一API收银台接口（聚合支付主被扫、微信公众号、支付宝生活号、绑卡支付）
 * Created by ruiyang.du on 2017/6/28.
 */
public interface UnifiedAPICashierFacade {

    /**
     * 聚合及被扫支付，支付请求
     * @param apiCashierRequestDTO
     * @return
     */
    UnifiedAPICashierResponseDTO pay(UnifiedAPICashierRequestDTO apiCashierRequestDTO);

    /**
     * 绑卡支付，请求下单并获取补充项
     * @param param
     * @return
     */
    ApiBindPayPaymentResponseDTO requestPayment(ApiBindPayPaymentRequestDTO param);

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
