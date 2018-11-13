/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.*;

/**
 * 类名称: PayCflEasyFacade <br>
 * 类描述: <br>
 * 分期易接口
 * @author: zhijun.wang
 * @since: 18/9/1 下午12:17
 * @version: 1.0.0
 */
public interface PayCflEasyFacade {

    /**
     * 分期易下单接口
     * @param ncCflEasyRequestDTO
     * @return
     */
    NcCflEasyResponseDTO createPayment(NcCflEasyRequestDTO ncCflEasyRequestDTO);

    /**
     * 分期易发短信接口
     * @param ncCflEasySmsRequestDTO
     * @return
     */
    NcCflEasySmsResponseDTO sendSms(NcCflEasySmsRequestDTO ncCflEasySmsRequestDTO);

    /**
     * 分期易同步确认支付接口
     * @param ncCflEasyConfirmRequestDTO
     * @return
     */
    PayRecordResponseDTO synConfirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO);

    /**
     * 分期易异步确认支付接口
     * @param ncCflEasyConfirmRequestDTO
     * @return
     */
    NcCflEasyConfirmResponseDTO confirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO);
}
