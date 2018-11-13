package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;

/**
 * 担保分期facade
 *
 */
public interface GuaranteeInstallmentFacade {

    /**
     * 担保分期-预支付
     * @param requestDTO
     * @return
     */
    GuaranteeInstallmentPrePayResponseDTO prePay(GuaranteeInstallmentPrePayRequestDTO requestDTO);

    /**
     * 担保分期-请求支付
     * @param requestDTO
     * @return
     */
    GuaranteeInstallmentPaymentResponseDTO requestPayment(GuaranteeInstallmentPaymentRequestDTO requestDTO);


    InstallmentRouteResponseDTO getSupportBankAndPeriods(long requestId);


}
