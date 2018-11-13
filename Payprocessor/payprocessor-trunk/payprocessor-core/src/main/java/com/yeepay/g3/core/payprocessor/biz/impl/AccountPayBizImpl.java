package com.yeepay.g3.core.payprocessor.biz.impl;


import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.biz.AccountPayBiz;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.AccountSyncPayResponseDTO;

@Service
public class AccountPayBizImpl extends BaseBizImpl implements AccountPayBiz {

    public static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(AccountPayBizImpl.class);

    @Override
    public AccountPayResponseDTO accountPay(AccountPayRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[账户支付异步请求] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
        AccountPayResponseDTO response = new AccountPayResponseDTO();
        PayRecord record = null;
        try {
            PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
            record = buildPayRecord(requestDTO, payment);
            createPayRecord(record);
            if(record.isCombinedPay()) {
                createCombPayRecord(requestDTO, record, payment);
            }
            if("CLOSE".equals(ConstantUtils.getNewAccountPay())) {
                response = accountPayService.pay(requestDTO, record);
            }else {
                response = accountPayService.enterprisePay(requestDTO, record);
            }
        } catch (Throwable th) {
            logger.error("[业务异常]", th);
            setErrorInfo(response, th);
            updateErrorInfoToRecord(response, record == null ? null : record.getRecordNo());
        } finally {
            setBasicResponseArgs(requestDTO, response);
            PayLoggerFactory.TAG_LOCAL.remove();
        }
        return response;
    }

    @Override
    public AccountSyncPayResponseDTO accountSyncPay(AccountPayRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[账户支付同步请求] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
        AccountSyncPayResponseDTO response = new AccountSyncPayResponseDTO();
        PayRecord record = null;
        try {
            PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
            record = buildPayRecord(requestDTO, payment);
            createPayRecord(record);
            if(record.isCombinedPay()) {
                createCombPayRecord(requestDTO, record, payment);
            }
            if("CLOSE".equals(ConstantUtils.getNewAccountPay())) {
                response = accountPayService.paySync(requestDTO, record);
            }else {
                response = accountPayService.enterprisePaySync(requestDTO, record);
            }

        } catch (Throwable th) {
            logger.error("[业务异常]", th);
            setErrorInfo(response, th);
            updateErrorInfoToRecord(response, record == null ? null : record.getRecordNo());
        } finally {
            setBasicResponseArgs(requestDTO, response);
            PayLoggerFactory.TAG_LOCAL.remove();
        }
        return response;
    }

}
