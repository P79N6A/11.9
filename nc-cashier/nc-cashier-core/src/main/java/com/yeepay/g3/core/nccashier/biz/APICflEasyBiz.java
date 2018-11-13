/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * 类名称: APICflEasyBiz <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午2:32
 * @version: 1.0.0
 */
public interface APICflEasyBiz {

    /**
     * 分期易首次支付下单
     * @param requestDTO
     * @return
     */
    APICflEasyPaymentResponseDTO firstPayRequest(APICflEasyFirstRequestDTO requestDTO);

    /**
     * 分期易绑卡支付下单
     * @param requestDTO
     * @return
     */
    APICflEasyPaymentResponseDTO bindPayRequest(APICflEasyBindRequestDTO requestDTO);

    /**
     * 分期易发短信
     * @param requestDTO
     * @return
     */
    APIBasicResponseDTO smsSend(APICflEasySmsSendRequestDTO requestDTO);

    /**
     * 分期易确认支付
     * @param requestDTO
     * @return
     */
    APICflEasyConfirmPayResponseDTO confirmPay(APICflEasyConfirmPayRequestDTO requestDTO);

    /**
     * 分期易查询支持的银行
     * @param requestDTO
     * @return
     */
    APICflEasyBankInfoResponseDTO querySupportBankInfo(APICflEasyBankInfoRequestDTO requestDTO);
}
