/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.external.service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.facade.ncpay.dto.CflEasyResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.CflEasySmsResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasyConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasyRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasySmsRequestDTO;

/**
 * 类名称: NcPayCflEasyService <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午3:12
 * @version: 1.0.0
 */
public interface NcPayCflEasyService {

    /**
     * ncpay下单
     * @return
     */
    CflEasyResponseDTO createPayment(NcCflEasyRequestDTO requestDTO, PayRecord record);

    /**
     * ncpay发短信
     * @return
     */
    CflEasySmsResponseDTO sendSms(NcCflEasySmsRequestDTO requestDTO, String ncpayPaymentNo);

    /**
     * ncpay同步确认支付
     * @return
     */
    PayQueryResponseDTO synConfirmPay(NcCflEasyConfirmRequestDTO requestDTO, String ncpayPaymentNo);

    /**
     * ncpay异步确认支付
     * @param requestDTO
     * @param ncpayPaymentNo
     * @return
     */
    PayConfirmResponseDTO confirmPay(NcCflEasyConfirmRequestDTO requestDTO, String ncpayPaymentNo);
}
