package com.yeepay.g3.core.nccashier.biz;


import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * 统一API收银台业务组合接口
 * Created by ruiyang.du on 2017/6/28.
 */
public interface UnifiedAPICashierBiz {

    /**
     * 聚合及被扫支付，支付请求
     * @param apiCashierRequestDTO
     * @return
     */
    UnifiedAPICashierResponseDTO pay(UnifiedAPICashierRequestDTO apiCashierRequestDTO);

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
