package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayResponseDTO;

import java.math.BigDecimal;

/**
 * 担保分期service
 */
public interface GuaranteeInstallmentService {

    /**
     * 封装参数，调用PP的担保分期预路由接口
     * @param paymentRequest
     * @param requestDTO
     * @return
     */
    NcGuaranteeCflPrePayResponseDTO callGuaranteeInstallmentPrePay(PaymentRequest paymentRequest, GuaranteeInstallmentPrePayRequestDTO requestDTO);

    /**
     * 预路由返回后，计算并组装所需信息
     *
     * @param requestDTO
     * @param paymentRequest
     * @param ncGuaranteeCflPrePayResponseDTO
     * @return
     */
    GuaranteeInstallmentPrePayResponseDTO handleGuaranteeInstallmentInfo(GuaranteeInstallmentPrePayRequestDTO requestDTO, PaymentRequest paymentRequest, NcGuaranteeCflPrePayResponseDTO ncGuaranteeCflPrePayResponseDTO);

    /**
     * 创建paymentRecord，或获取符合要求的已有record
     *
     * @param paymentRequest
     * @param requestDTO
     * @param cardInfo
     * @return
     */
    PaymentRecord createRecordWhenUnexist(PaymentRequest paymentRequest, GuaranteeInstallmentPaymentRequestDTO requestDTO, CardInfo cardInfo);

        /**
         * 封装参数，调用PP的担保分期支付下单接口，并保存支付订单号等
         *
         * @param paymentRequest
         * @param paymentRecord
         * @param requestDTO
         * @return
         */
    NcGuaranteeCflPayResponseDTO callGuaranteeInstallmentPayment(PaymentRequest paymentRequest, PaymentRecord paymentRecord, GuaranteeInstallmentPaymentRequestDTO requestDTO);


    /**
     * 支付下单返回后，更新record，并且处理返回信息
     * @param ncGuaranteeCflPayResponseDTO
     * @param paymentRecord
     * @return
     */
    GuaranteeInstallmentPaymentResponseDTO updateRecordAndReturn(NcGuaranteeCflPayResponseDTO ncGuaranteeCflPayResponseDTO, PaymentRecord paymentRecord);
    /**
     * 获取对应requestId所支持的银行和期数
     * @param orderAmount
     * @param responseDTO
     * @return
     */
    void getSupportBankAndPeriods(BigDecimal orderAmount, InstallmentRouteResponseDTO responseDTO);

}
