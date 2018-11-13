package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.GuaranteeInstallmentBiz;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;
import com.yeepay.g3.facade.nccashier.service.GuaranteeInstallmentFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuaranteeInstallmentFacadeImpl implements GuaranteeInstallmentFacade {

    @Autowired
    private GuaranteeInstallmentBiz guaranteeInstallmentBiz;

    @Override
    public GuaranteeInstallmentPrePayResponseDTO prePay(GuaranteeInstallmentPrePayRequestDTO requestDTO) {
        return guaranteeInstallmentBiz.prePay(requestDTO);
    }

    @Override
    public GuaranteeInstallmentPaymentResponseDTO requestPayment(GuaranteeInstallmentPaymentRequestDTO requestDTO) {
        return guaranteeInstallmentBiz.requestPayment(requestDTO);
    }

    @Override
    public InstallmentRouteResponseDTO getSupportBankAndPeriods(long requestId) {
        return guaranteeInstallmentBiz.getSupportBankAndPeriods(requestId);
    }
}
