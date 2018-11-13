/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.nccashier.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.nccashier.biz.APICflEasyBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.SynTypeEnum;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.service.APICflEasyService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.cwh.enumtype.BankCardType;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PaymentProcessEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.enumtype.CardType;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 类名称: APICflEasyBizImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/4 下午2:32
 * @version: 1.0.0
 */
@Service
public class APICflEasyBizImpl extends APIBaseBiz implements APICflEasyBiz {

    private static final Logger LOGGER = LoggerFactory.getLogger(APICflEasyBizImpl.class);

    @Autowired
    private APICflEasyService apiCflEasyService;
    @Autowired
    private NewOrderHandleService newOrderHandleService;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private APICashierYJZFService apiCashierYJZFService;
    @Autowired
    private CashierBankCardService cashierBankCardService;
    @Autowired
    private CwhService cwhService;


    /** 分期易产品信息 */
    private static ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.ZF_FQY, PayTypeEnum.ZF_FQY_API);


    @Override
    public APICflEasyPaymentResponseDTO firstPayRequest(APICflEasyFirstRequestDTO requestDTO) {
        APICflEasyPaymentResponseDTO responseDTO = new APICflEasyPaymentResponseDTO();
        try {
            // 参数校验
            firstValidateParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            // 校验卡类型、银行、金额、期数
            validBankInfoForFirstpay(requestDTO, orderInfo);
            // 产品开通校验
            MerchantInNetConfigResult merchantInNetConfig = apiCflEasyService.verifyProductOpen(orderInfo.getMerchantAccountCode(), orderInfo.getTransactionType());
            // 同人校验
            ValidateSamePersionLimitDTO limitDTO = getValidateSamePersionLimitDTO(requestDTO);
            apiCashierYJZFService.validateSamePersionLimit(limitDTO, orderInfo);
            // 获取外部用户
            CashierUserInfo cashierUser = apiCashierYJZFService.buildMemberUser(requestDTO.getUserNo(), requestDTO.getUserType(), orderInfo.getMerchantAccountCode());
            // 创建或获取paymentRequest
            PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(requestDTO.getUserIp(), null, cashierUser);
            PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig, productLevel, extInfo);
            // 首次下单支付
            responseDTO = apiCflEasyService.firstPayRequest(requestDTO, paymentRequest, cashierUser);
        }catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO,requestDTO);
        return responseDTO;
    }


    /**
     * 首次支付参数校验
     */
    private void firstValidateParam(APICflEasyFirstRequestDTO requestDTO){
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
                    Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[apiCflEasyFirstRequest],token=" + requestDTO.getToken() + "]");
    }

    /**
     * 首次支付校验卡类型、银行、期数和金额
     * @param requestDTO
     */
    private void validBankInfoForFirstpay(APICflEasyFirstRequestDTO requestDTO, OrderDetailInfoModel orderInfo) {
        CardBinDTO cardBinDTO = null;
        try {
            cardBinDTO = cashierBankCardService.getCardBinInfo(requestDTO.getCardNo());
        }catch (Throwable t) {
            // 吃掉异常，不影响正常交易
            LOGGER.error("查询卡bin异常", t);
            return;
        }
        // 如果没查到信息，就直接通过，让底层去做校验
        if(cardBinDTO == null || cardBinDTO.getBankEnum() == null || cardBinDTO.getCardType() ==null) {
            return;
        }
        // 校验卡类型
        if(cardBinDTO.getCardType() != CardType.CREDIT) {
            throw new CashierBusinessException(Errors.CFL_EASY_CARD_TYPE_ERROR.getCode(),
                    Errors.CFL_EASY_CARD_TYPE_ERROR.getMsg());
        }
        // 校验银行
        Map<String, String> cflEasyBankInfoMaps = CommonUtil.getCflEasyBankInfoMaps();
        // 如果获取异常，则让底层做校验
        if(MapUtils.isEmpty(cflEasyBankInfoMaps)) {
            return;
        }
        String bankInfoString = cflEasyBankInfoMaps.get(cardBinDTO.getBankEnum().name());
        if(StringUtils.isBlank(bankInfoString)) {
            throw new CashierBusinessException(Errors.CFL_EASY_BANK_CODE_NOT_SUPPORT.getCode(),
                    Errors.CFL_EASY_BANK_CODE_NOT_SUPPORT.getMsg());
        }
        // 校验期数
        CflEasyBankInfo cflEasyBankInfo = JSONObject.parseObject(bankInfoString, CflEasyBankInfo.class);
        String[] periods = cflEasyBankInfo.getSupportPeriods().split(",");
        List<String> periodsList = new ArrayList<String>(Arrays.asList(periods));
        if(!periodsList.contains(requestDTO.getPeriod())) {
            throw new CashierBusinessException(Errors.CFL_EASY_PERIOD_NOT_SUPPORT.getCode(),
                    Errors.CFL_EASY_PERIOD_NOT_SUPPORT.getMsg());
        }
        // 校验金额
        String[] amounts = cflEasyBankInfo.getLimitAmounts().split(",");
        Amount amountLow = new Amount(amounts[0]);
        Amount amountHigh = new Amount(amounts[1]);
        Amount reqAmount = new Amount(orderInfo.getOrderAmount());
        if(!((amountLow.isLesserThan(reqAmount) && reqAmount.isLesserThan(amountHigh))
                || amountLow.isGreaterThan(reqAmount) && reqAmount.isGreaterThan(amountHigh))) {
            throw new CashierBusinessException(Errors.CFL_EASY_AMOUNT_NOT_SUPPORT.getCode(),
                    Errors.CFL_EASY_AMOUNT_NOT_SUPPORT.getMsg());
        }
    }

    /**
     * 组装同人校验入参
     */
    private ValidateSamePersionLimitDTO getValidateSamePersionLimitDTO(APICflEasyFirstRequestDTO requestDTO) {
        ValidateSamePersionLimitDTO limitDTO = new ValidateSamePersionLimitDTO();
        limitDTO.setIdNo(requestDTO.getIdNo());
        limitDTO.setOwner(requestDTO.getOwner());
        limitDTO.setUserNo(requestDTO.getUserNo());
        limitDTO.setUserType(requestDTO.getUserType());
        return limitDTO;
    }

    /**
     * 组装订单扩展信息
     */
    private PaymentRequestExtInfo buildPaymentRequestExtInfo(String userIp, String bindId, CashierUserInfo user) {
        PaymentRequestExtInfo extInfo = new PaymentRequestExtInfo();
        extInfo.setUserIp(userIp);
        extInfo.setBindId(bindId);
        extInfo.setCashierUser(user);
        return extInfo;
    }


    @Override
    public APICflEasyPaymentResponseDTO bindPayRequest(APICflEasyBindRequestDTO requestDTO) {
        APICflEasyPaymentResponseDTO responseDTO = new APICflEasyPaymentResponseDTO();
        try {
            // 参数校验
            bindValidateParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            // 校验卡类型，银行，期数，金额
            validBankInfoForBindPay(requestDTO, orderInfo);
            // 产品开通校验
            MerchantInNetConfigResult merchantInNetConfig = apiCflEasyService.verifyProductOpen(orderInfo.getMerchantAccountCode(), orderInfo.getTransactionType());
            // 获取外部用户
            CashierUserInfo cashierUser = apiCashierYJZFService.buildMemberUser(requestDTO.getUserNo(), requestDTO.getUserType(), orderInfo.getMerchantAccountCode());
            // 创建或获取paymentRequest
            PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(requestDTO.getUserIp(), requestDTO.getBindId(), cashierUser);
            PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig, productLevel, extInfo);
            responseDTO = apiCflEasyService.bindPayRequest(requestDTO, paymentRequest, cashierUser);
        }catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    private void bindValidateParam(APICflEasyBindRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
                    Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[apiCflEasyBindRequest],token=" + requestDTO.getToken() + "]");
    }

    /**
     * 绑卡支付，校验卡类型，银行，期数，金额
     */
    private void validBankInfoForBindPay(APICflEasyBindRequestDTO requestDTO, OrderDetailInfoModel orderInfo) {
        BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(Long.valueOf(requestDTO.getBindId()));
        // 校验卡类型
        if(bindCardDTO == null || bindCardDTO.getBankCardType() == null || bindCardDTO.getBankCode()== null) {
            throw new CashierBusinessException(Errors.CFL_EASY_BIND_ID_NOT_SUPPORT.getCode(),
                    Errors.CFL_EASY_BIND_ID_NOT_SUPPORT.getMsg());
        }
        if(bindCardDTO.getBankCardType() != BankCardType.CREDITCARD) {
            throw new CashierBusinessException(Errors.CFL_EASY_CARD_TYPE_ERROR.getCode(),
                    Errors.CFL_EASY_CARD_TYPE_ERROR.getMsg());
        }
        // 校验银行
        Map<String, String> cflEasyBankInfoMaps = CommonUtil.getCflEasyBankInfoMaps();
        // 如果获取异常，则让底层做校验
        if(MapUtils.isEmpty(cflEasyBankInfoMaps)) {
            return;
        }
        String bankInfoString = cflEasyBankInfoMaps.get(bindCardDTO.getBankCode());
        if(StringUtils.isBlank(bankInfoString)) {
            throw new CashierBusinessException(Errors.CFL_EASY_BANK_CODE_NOT_SUPPORT.getCode(),
                    Errors.CFL_EASY_BANK_CODE_NOT_SUPPORT.getMsg());
        }
        // 校验期数
        CflEasyBankInfo cflEasyBankInfo = JSONObject.parseObject(bankInfoString, CflEasyBankInfo.class);
        String[] periods = cflEasyBankInfo.getSupportPeriods().split(",");
        List<String> periodsList = new ArrayList<String>(Arrays.asList(periods));
        if(!periodsList.contains(requestDTO.getPeriod())) {
            throw new CashierBusinessException(Errors.CFL_EASY_PERIOD_NOT_SUPPORT.getCode(),
                    Errors.CFL_EASY_PERIOD_NOT_SUPPORT.getMsg());
        }
        // 校验金额
        String[] amounts = cflEasyBankInfo.getLimitAmounts().split(",");
        Amount amountLow = new Amount(amounts[0]);
        Amount amountHigh = new Amount(amounts[1]);
        Amount reqAmount = new Amount(orderInfo.getOrderAmount());
        if(!((amountLow.isLesserThan(reqAmount) && reqAmount.isLesserThan(amountHigh))
                || amountLow.isGreaterThan(reqAmount) && reqAmount.isGreaterThan(amountHigh))) {
            throw new CashierBusinessException(Errors.CFL_EASY_AMOUNT_NOT_SUPPORT.getCode(),
                    Errors.CFL_EASY_AMOUNT_NOT_SUPPORT.getMsg());
        }
    }

    @Override
    public APIBasicResponseDTO smsSend(APICflEasySmsSendRequestDTO requestDTO) {
        APIBasicResponseDTO responseDTO = new APIBasicResponseDTO();
        try {
            // 参数校验
            smsSendValidateParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            // 获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), true);
            // 获取paymentRecord
            PaymentRecord paymentRecord = apiCflEasyService.queryPaymentRecord(orderInfo, requestDTO.getRecordId(), PaymentProcessEnum.PAYMENT_PROCESS_SEND_SMS.getValue());
            //检查支付流程
            boolean needClearCardInfo = apiCflEasyService.paymentProcessInfoCheck(requestDTO.getCardInfoDTO(), paymentRecord, PaymentProcessEnum.PAYMENT_PROCESS_SEND_SMS.getValue(), null);
            if(needClearCardInfo){
                requestDTO.cleanCardInfo();
            }
            responseDTO = apiCflEasyService.smsSend(requestDTO.getCardInfoDTO(), paymentRequest, paymentRecord);
        }catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    private void smsSendValidateParam(APICflEasySmsSendRequestDTO requestDTO){
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
                    Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[preauthSmsSend],token=" + requestDTO.getToken() + "]");
    }

    @Override
    public APICflEasyConfirmPayResponseDTO confirmPay(APICflEasyConfirmPayRequestDTO requestDTO) {
        APICflEasyConfirmPayResponseDTO responseDTO = new APICflEasyConfirmPayResponseDTO();
        try {
            // 参数校验
            validateConfirmPayParam(requestDTO);
            // 反查订单，并校验签名商编和订单referer
            OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(), requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
            // 获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), true);
            // 获取paymentRecord
            PaymentRecord paymentRecord = apiCflEasyService.queryPaymentRecord(orderInfo, requestDTO.getRecordId(), PaymentProcessEnum.PAYMENT_PROCESS_CONFIRM_PAY.getValue());
            //检查支付流程
            boolean needClearCardInfo = apiCflEasyService.paymentProcessInfoCheck(requestDTO.getCardInfoDTO(), paymentRecord, PaymentProcessEnum.PAYMENT_PROCESS_CONFIRM_PAY.getValue(), requestDTO.getVerifyCode());
            if(needClearCardInfo){
                requestDTO.cleanCardInfo();
            }
            responseDTO = apiCflEasyService.confirmPay(requestDTO.getCardInfoDTO(),requestDTO.getVerifyCode(), paymentRequest, paymentRecord);
        }catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    public void validateConfirmPayParam(APICflEasyConfirmPayRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[分期易API同步确认支付|paymentConfirm],token=" + requestDTO.getToken() + "]");

    }

    @Override
    public APICflEasyBankInfoResponseDTO querySupportBankInfo(APICflEasyBankInfoRequestDTO requestDTO) {
        APICflEasyBankInfoResponseDTO responseDTO = new APICflEasyBankInfoResponseDTO();
        try {
            // 参数校验
            validQueryBankPayParam(requestDTO);
            Map<String, String> cflEasyBankInfoMaps = CommonUtil.getCflEasyBankInfoMaps();
            if(MapUtils.isEmpty(cflEasyBankInfoMaps)) {
                throw new CashierBusinessException(Errors.QUERY_BANK_LIST_ERROR.getCode(),
                        Errors.QUERY_BANK_LIST_ERROR.getMsg());
            }
            List<CflEasyBankInfo> bankInfoList = transfromBankList(cflEasyBankInfoMaps);
            if(CollectionUtils.isEmpty(bankInfoList)) {
                throw new CashierBusinessException(Errors.QUERY_BANK_LIST_ERROR.getCode(),
                        Errors.QUERY_BANK_LIST_ERROR.getMsg());
            }
            responseDTO.setCflEasyBankInfoList(bankInfoList);
        }catch (Throwable t) {
            String bizType = (requestDTO == null ? null : requestDTO.getBizType());
            handleException(bizType, responseDTO, t);
        }
        supplyResponse(responseDTO, requestDTO);
        return responseDTO;
    }

    public void validQueryBankPayParam(APICflEasyBankInfoRequestDTO requestDTO) {
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
        requestDTO.validate();
        // 商编的转化 —— 将OPR：等前缀去掉
        requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
        NcCashierLoggerFactory.TAG_LOCAL.set("[一键支付API确认支付|paymentConfirm],token=" + requestDTO.getToken() + "]");

    }


    private List<CflEasyBankInfo> transfromBankList(Map<String, String> cflEasyBankInfoMaps) {
        List<CflEasyBankInfo> cflEasyBankInfos = new ArrayList<CflEasyBankInfo>();

            for(Map.Entry<String, String> entry : cflEasyBankInfoMaps.entrySet()) {
                try {
                    String bankInfoString = entry.getValue();
                    CflEasyBankInfo cflEasyBankInfo = JSONObject.parseObject(bankInfoString, CflEasyBankInfo.class);
                    cflEasyBankInfos.add(cflEasyBankInfo);
                }catch (Throwable t) {
                    LOGGER.error("解析银行卡列表信息异常", t);
                    continue;
                }
            }
        return cflEasyBankInfos;
    }
}