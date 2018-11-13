package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.GuaranteeInstallmentBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.GuaranteeInstallmentService;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuaranteeInstallmentBizImpl extends NcCashierBaseBizImpl implements GuaranteeInstallmentBiz {


    @Autowired
    private GuaranteeInstallmentService guaranteeInstallmentService;

    @Override
    public GuaranteeInstallmentPrePayResponseDTO prePay(GuaranteeInstallmentPrePayRequestDTO requestDTO) {
        GuaranteeInstallmentPrePayResponseDTO prePayResponseDTO = new GuaranteeInstallmentPrePayResponseDTO();
        try {
            //1，参数校验
            BeanValidator.validate(requestDTO);
            NcCashierLoggerFactory.TAG_LOCAL.set("[担保分期预支付|prePay]—[requestId=" + requestDTO.getRequestId() + "]");
            //2，获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
            //3，封装参数，调用PP
            NcGuaranteeCflPrePayResponseDTO ncGuaranteeCflPrePayResponseDTO = guaranteeInstallmentService.callGuaranteeInstallmentPrePay(paymentRequest, requestDTO);
            //4，计算并组装所需信息
            prePayResponseDTO = guaranteeInstallmentService.handleGuaranteeInstallmentInfo(requestDTO, paymentRequest, ncGuaranteeCflPrePayResponseDTO);
        }catch (Throwable t){
            handleException(prePayResponseDTO, t);
        }
        return prePayResponseDTO;
    }

    @Override
    public GuaranteeInstallmentPaymentResponseDTO requestPayment(GuaranteeInstallmentPaymentRequestDTO requestDTO) {
        GuaranteeInstallmentPaymentResponseDTO paymentResponseDTO = new GuaranteeInstallmentPaymentResponseDTO();
        try {
            //1，参数校验
            BeanValidator.validate(requestDTO);
            NcCashierLoggerFactory.TAG_LOCAL.set("[担保分期请求支付|requestPayment]—[requestId=" + requestDTO.getRequestId() + "]");
            //2，获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
            //3，获取卡信息
            CardInfo cardInfo = cashierBankCardService.getNonNullCardBin(requestDTO.getCardNo());
            //4，创建或获取paymentRecord
            PaymentRecord paymentRecord = guaranteeInstallmentService.createRecordWhenUnexist(paymentRequest, requestDTO, cardInfo);
            //5，封装参数，调用PP
            NcGuaranteeCflPayResponseDTO ncGuaranteeCflPayResponseDTO = guaranteeInstallmentService.callGuaranteeInstallmentPayment(paymentRequest, paymentRecord, requestDTO);
            //6，更新record，并封装返回结果
            paymentResponseDTO = guaranteeInstallmentService.updateRecordAndReturn(ncGuaranteeCflPayResponseDTO, paymentRecord);
        }catch (Throwable t){
            handleException(paymentResponseDTO, t);
        }
        return paymentResponseDTO;
    }

    @Override
    public InstallmentRouteResponseDTO getSupportBankAndPeriods(long requestId) {
        InstallmentRouteResponseDTO responseDTO = new InstallmentRouteResponseDTO();
        try {
            // 校验入参
            validateGuranteeParam(requestId);
            // 订单校验
            PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestId);
            // 获取签约卡列表
            guaranteeInstallmentService.getSupportBankAndPeriods(paymentRequest.getOrderAmount(),responseDTO);
        } catch (Throwable t) {
            handleException(responseDTO, t);
        }
        return responseDTO;
    }

    /**
     * 校验担保分期入参，并将paymentRequestId加到threadLocal中
     *
     * @param requestId
     */
    private void validateGuranteeParam(long requestId) {
        if (requestId <= 0) {
            throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
        }
        NcCashierLoggerFactory.TAG_LOCAL.set("[gurantee|getSupportBankAndPeriods]—[requestId=" + requestId + "]");
    }

}
