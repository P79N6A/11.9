package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.APIYJZFBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.service.APICashierYJZFService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PaymentProcessEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class APIYJZFBizImpl extends APIBaseBiz implements APIYJZFBiz {

    @Autowired
    private APICashierYJZFService apiCashierYJZFService;
    @Autowired
    private NewOrderHandleService newOrderHandleService;
    @Autowired
    private PaymentRequestService paymentRequestService;

    /** 一键支付的三级产品信息 */
    private static ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.NCPAY, PayTypeEnum.BANK_PAY_WAP);

    @Override
    public APIYJZFFirstPaymentResponseDTO firstPayRequest(APIYJZFFirstPaymentRequestDTO requestDTO) {
        APIYJZFFirstPaymentResponseDTO responseDTO = new APIYJZFFirstPaymentResponseDTO();
        try {
            // 参数校验
            apiCashierYJZFService.validateFirstPaymentParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            // 产品开通校验
            MerchantInNetConfigResult merchantInNetConfig = apiCashierYJZFService.verifyProductOpen(orderInfo.getMerchantAccountCode(), requestDTO.isCheckProductOpen(), orderInfo.getTransactionType());
            // 同人限制校验
            ValidateSamePersionLimitDTO limitDTO = getValidateSamePersionLimitDTO(requestDTO);
            apiCashierYJZFService.validateSamePersionLimit(limitDTO, orderInfo);
            // 获取外部用户
            CashierUserInfo cashierUser = apiCashierYJZFService.buildMemberUser(requestDTO.getUserNo(), requestDTO.getUserType(), orderInfo.getMerchantAccountCode());
            // 创建或获取paymentRequest
            PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(requestDTO.getUserIp(), requestDTO.getPayScene(), requestDTO.getMcc(), null,cashierUser);
            PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig, productLevel, extInfo);
            // 进行首次支付下单
            responseDTO = apiCashierYJZFService.firstPayRequest(requestDTO, paymentRequest, cashierUser);
        } catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    private ValidateSamePersionLimitDTO getValidateSamePersionLimitDTO(APIYJZFFirstPaymentRequestDTO requestDTO) {
        ValidateSamePersionLimitDTO limitDTO = new ValidateSamePersionLimitDTO();
        limitDTO.setIdNo(requestDTO.getIdNo());
        limitDTO.setOwner(requestDTO.getOwner());
        limitDTO.setUserNo(requestDTO.getUserNo());
        limitDTO.setUserType(requestDTO.getUserType());
        return limitDTO;
    }

    @Override
    public APIYJZFBindPaymentResponseDTO bindPayRequest(APIYJZFBindPaymentRequestDTO requestDTO) {
        APIYJZFBindPaymentResponseDTO responseDTO = new APIYJZFBindPaymentResponseDTO();
        try {
            // 参数校验
            apiCashierYJZFService.validateBindPaymentParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            // 产品开通校验
            MerchantInNetConfigResult merchantInNetConfig = apiCashierYJZFService.verifyProductOpen(orderInfo.getMerchantAccountCode(), requestDTO.isCheckProductOpen(), orderInfo.getTransactionType());
            // 获取外部用户
            CashierUserInfo cashierUser = apiCashierYJZFService.buildMemberUser(requestDTO.getUserNo(), requestDTO.getUserType(), orderInfo.getMerchantAccountCode());
            // 创建或获取paymentRequest
            PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(requestDTO.getUserIp(), requestDTO.getPayScene(), requestDTO.getMcc(), requestDTO.getBindId(), cashierUser);
            PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig, productLevel, extInfo);
            // 进行二次支付下单
            responseDTO = apiCashierYJZFService.bindPayRequest(requestDTO, paymentRequest, cashierUser);
        } catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    @Override
    public APIBasicResponseDTO sendSMS(APIYJZFSendSmsRequestDTO requestDTO) {
        APIBasicResponseDTO responseDTO = new APIBasicResponseDTO();
        try {
            // 参数校验
            apiCashierYJZFService.validateSendSMSParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            //获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), true);
            //获取PaymentRecord
            PaymentRecord paymentRecord = apiCashierYJZFService.queryPaymentRecord(orderInfo, requestDTO.getRecordId(), PaymentProcessEnum.PAYMENT_PROCESS_SEND_SMS.getValue());
            //检查支付流程
            boolean needClearCardInfo = apiCashierYJZFService.paymentProcessInfoCheck(requestDTO.getCardInfoDTO(), paymentRecord, PaymentProcessEnum.PAYMENT_PROCESS_SEND_SMS.getValue(), null);
            if(needClearCardInfo){
                requestDTO.cleanCardInfo();
            }
            // 进行请求发短验
            responseDTO = apiCashierYJZFService.sendSMS(requestDTO, paymentRequest, paymentRecord);
        } catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    @Override
    public APIYJZFConfirmPayResponseDTO paymentConfirm(APIYJZFConfirmPayRequestDTO requestDTO) {
        APIYJZFConfirmPayResponseDTO responseDTO = new APIYJZFConfirmPayResponseDTO();
        try {
            // 参数校验
            apiCashierYJZFService.validateConfirmPayParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            //获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), true);
            //获取PaymentRecord
            PaymentRecord paymentRecord = apiCashierYJZFService.queryPaymentRecord(orderInfo, requestDTO.getRecordId(), PaymentProcessEnum.PAYMENT_PROCESS_CONFIRM_PAY.getValue());
            //检查支付流程
            boolean needClearCardInfo = apiCashierYJZFService.paymentProcessInfoCheck(requestDTO.getCardInfoDTO(), paymentRecord, PaymentProcessEnum.PAYMENT_PROCESS_CONFIRM_PAY.getValue(), requestDTO.getVerifyCode());
            if(needClearCardInfo){
                requestDTO.cleanCardInfo();
            }
            // 进行请求确认支付
            responseDTO = apiCashierYJZFService.confirmPay(requestDTO, paymentRequest, paymentRecord);
        } catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    private PaymentRequestExtInfo buildPaymentRequestExtInfo(String userIp, String payScene, String mcc, String bindId, CashierUserInfo user) {
        PaymentRequestExtInfo extInfo = new PaymentRequestExtInfo();
        extInfo.setUserIp(userIp);
        extInfo.setBindId(bindId);
        extInfo.setPayScene(payScene);
        extInfo.setMcc(mcc);
        extInfo.setCashierUser(user);
        return extInfo;
    }
}
