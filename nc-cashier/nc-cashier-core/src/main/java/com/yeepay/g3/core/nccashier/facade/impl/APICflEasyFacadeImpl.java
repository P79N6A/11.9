/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.APICflEasyBiz;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.APICflEasyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类名称: APICflEasyFacadeImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午2:29
 * @version: 1.0.0
 */
@Service("apiCflEasyFacade")
public class APICflEasyFacadeImpl implements APICflEasyFacade {

    @Autowired
    private APICflEasyBiz apiCflEasyBiz;

    @Override
    public APICflEasyPaymentResponseDTO firstPayRequest(APICflEasyFirstRequestDTO requestDTO) {
        return apiCflEasyBiz.firstPayRequest(requestDTO);
    }

    @Override
    public APICflEasyPaymentResponseDTO bindPayRequest(APICflEasyBindRequestDTO requestDTO) {
        return apiCflEasyBiz.bindPayRequest(requestDTO);
    }

    @Override
    public APIBasicResponseDTO smsSend(APICflEasySmsSendRequestDTO requestDTO) {
        return apiCflEasyBiz.smsSend(requestDTO);
    }

    @Override
    public APICflEasyConfirmPayResponseDTO confirmPay(APICflEasyConfirmPayRequestDTO requestDTO) {
        return apiCflEasyBiz.confirmPay(requestDTO);
    }

    @Override
    public APICflEasyBankInfoResponseDTO querySupportBankInfo(APICflEasyBankInfoRequestDTO requestDTO) {
        return apiCflEasyBiz.querySupportBankInfo(requestDTO);
    }
}