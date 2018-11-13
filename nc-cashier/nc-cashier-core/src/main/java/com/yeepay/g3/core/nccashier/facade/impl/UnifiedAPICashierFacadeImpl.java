package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.UnifiedAPICashierBiz;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.UnifiedAPICashierFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnifiedAPICashierFacadeImpl implements UnifiedAPICashierFacade {

    @Autowired
    private UnifiedAPICashierBiz unifiedAPICashierBiz;

    @Override
    public UnifiedAPICashierResponseDTO pay(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        return unifiedAPICashierBiz.pay(apiCashierRequestDTO);
    }

    @Override
    public ApiBindPayPaymentResponseDTO requestPayment(ApiBindPayPaymentRequestDTO param) {
        return unifiedAPICashierBiz.requestPayment(param);
    }

    @Override
    public ApiBindPaySendSmsResponseDTO requestSmsSend(ApiBindPaySendSmsRequestDTO param) {
        return unifiedAPICashierBiz.requestSmsSend(param);
    }

    @Override
    public ApiBindPayConfirmResponseDTO confirmPayment(ApiBindPayConfirmRequestDTO param) {
        return unifiedAPICashierBiz.confirmPayment(param);
    }
}
