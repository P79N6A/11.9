package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.InstallmentBanksResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;

/**
 * 担保分期biz
 *
 */
public interface GuaranteeInstallmentBiz {

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

    /**
     * 担保分期-获得支持银行卡期数
     * @param requestId
     * @return
     */
    InstallmentRouteResponseDTO getSupportBankAndPeriods(long requestId);
}
