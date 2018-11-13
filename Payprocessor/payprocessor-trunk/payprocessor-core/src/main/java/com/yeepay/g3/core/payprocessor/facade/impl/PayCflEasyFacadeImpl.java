/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.facade.impl;

import com.yeepay.g3.core.payprocessor.biz.NcPayCflEasyBiz;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.facade.PayCflEasyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类名称: PayCflEasyFacadeImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午2:54
 * @version: 1.0.0
 */
@Service("PayCflEasyFacade")
public class PayCflEasyFacadeImpl implements PayCflEasyFacade {

    @Autowired
    private NcPayCflEasyBiz ncPayCflEasyBiz;

    @Override
    public NcCflEasyResponseDTO createPayment(NcCflEasyRequestDTO ncCflEasyRequestDTO) {
        return ncPayCflEasyBiz.createPayment(ncCflEasyRequestDTO);
    }

    @Override
    public NcCflEasySmsResponseDTO sendSms(NcCflEasySmsRequestDTO ncCflEasySmsRequestDTO) {
        return ncPayCflEasyBiz.sendSms(ncCflEasySmsRequestDTO);
    }

    @Override
    public PayRecordResponseDTO synConfirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO) {
        return ncPayCflEasyBiz.synConfirmPay(ncCflEasyConfirmRequestDTO);
    }

    @Override
    public NcCflEasyConfirmResponseDTO confirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO) {
        return ncPayCflEasyBiz.confirmPay(ncCflEasyConfirmRequestDTO);
    }
}