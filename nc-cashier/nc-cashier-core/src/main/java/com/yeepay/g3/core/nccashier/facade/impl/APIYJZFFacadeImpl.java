package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.APIYJZFBiz;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.APIYJZFFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class APIYJZFFacadeImpl implements APIYJZFFacade {

    @Autowired
    private APIYJZFBiz apiyjzfBiz;

    @Override
    public APIYJZFFirstPaymentResponseDTO firstPayRequest(APIYJZFFirstPaymentRequestDTO requestDTO) {
        return apiyjzfBiz.firstPayRequest(requestDTO);
    }

    @Override
    public APIYJZFBindPaymentResponseDTO bindPayRequest(APIYJZFBindPaymentRequestDTO requestDTO) {
        return apiyjzfBiz.bindPayRequest(requestDTO);
    }

    @Override
    public APIBasicResponseDTO sendSMS(APIYJZFSendSmsRequestDTO requestDTO) {
        return apiyjzfBiz.sendSMS(requestDTO);
    }

    @Override
    public APIYJZFConfirmPayResponseDTO paymentConfirm(APIYJZFConfirmPayRequestDTO requestDTO) {
        return apiyjzfBiz.paymentConfirm(requestDTO);
    }
}
