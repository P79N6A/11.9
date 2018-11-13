/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.biz.impl;

import com.yeepay.g3.core.payprocessor.biz.NcPayCflEasyBiz;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.ncpay.dto.CflEasyResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.CflEasySmsResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import org.springframework.stereotype.Service;

/**
 * 类名称: NcPayCflEasyBizImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午3:00
 * @version: 1.0.0
 */
@Service
public class NcPayCflEasyBizImpl extends BaseBizImpl implements NcPayCflEasyBiz {

    private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(NcPayCflEasyBizImpl.class);

    @Override
    public NcCflEasyResponseDTO createPayment(NcCflEasyRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[分期易请求下单] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
        NcCflEasyResponseDTO responseDTO = new NcCflEasyResponseDTO();
        PayRecord record = null;
        try {
            PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
            record = buildPayRecord(requestDTO, payment);
            createPayRecord(record);
            if(record.isCombinedPay()) {
                createCombPayRecord(requestDTO, record, payment);
            }
            CflEasyResponseDTO ncResponse = ncPayCflEasyService.createPayment(requestDTO, record);
            updatePayRecordByNcPayResponse(record.getRecordNo(), ncResponse.getPayOrderId());
            buildCreatePaymentResponse(ncResponse, record, responseDTO);
        } catch (Throwable th) {
            logger.error("[业务异常]", th);
            setErrorInfo(responseDTO, th);
            updateErrorInfoToRecord(responseDTO, record == null ? null : record.getRecordNo());
        } finally {
            setBasicResponseArgs(requestDTO, responseDTO);
            PayLoggerFactory.TAG_LOCAL.remove();
        }
        return responseDTO;
    }


    private void updatePayRecordByNcPayResponse(String recordNo, String paymentNO) {
        payRecordService.updatePaymentNo(recordNo, paymentNO, TrxStatusEnum.DOING.name());
    }

    private void buildCreatePaymentResponse(CflEasyResponseDTO ncResponse, PayRecord record, NcCflEasyResponseDTO responseDTO) {
        responseDTO.setRecordNo(record.getRecordNo());
        responseDTO.setNeedItem(ncResponse.getNeedItem());
        responseDTO.setSmsType(ncResponse.getSmsType());
        responseDTO.setPayerInterestRate(ncResponse.getPayerInterestRate());
    }

    @Override
    public NcCflEasySmsResponseDTO sendSms(NcCflEasySmsRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[分期易发短信] - [recordNo = " + requestDTO.getRecordNo() + "]");
        NcCflEasySmsResponseDTO responseDTO = new NcCflEasySmsResponseDTO();
        try {
            // 校验支付订单
            String ncpayPaymentNo = payRecordService.queryNcPaymentNo(requestDTO.getRecordNo());
            CflEasySmsResponseDTO ncResponse = ncPayCflEasyService.sendSms(requestDTO, ncpayPaymentNo);
            bulidSmsResponse(responseDTO, requestDTO.getRecordNo(), ncResponse);
        } catch (Throwable e) {
            logger.error("[业务异常]", e);
            setErrorInfo(responseDTO, e);
        } finally {
            PayLoggerFactory.TAG_LOCAL.remove();
        }
        return responseDTO;
    }

    private void bulidSmsResponse(NcCflEasySmsResponseDTO responseDTO, String recordNo, CflEasySmsResponseDTO ncResponse) {
        responseDTO.setRecordNo(recordNo);
        responseDTO.setSmsCode(ncResponse.getSmsCode());
        responseDTO.setSmsSendType(ncResponse.getSmsSendType());
    }

    @Override
    public PayRecordResponseDTO synConfirmPay(NcCflEasyConfirmRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[分期易确认支付] - [recordNo = " + requestDTO.getRecordNo() + "]");
        PayRecordResponseDTO responseDTO = new PayRecordResponseDTO();
        try {
            // 查询支付子单
            PayRecord record = payRecordService.queryRecordById(requestDTO.getRecordNo());
            // 调用ncpay同步确认支付接口
            PayQueryResponseDTO ncResponse = ncPayCflEasyService.synConfirmPay(requestDTO, record.getPaymentNo());
            // 处理结果
            handlePayQueryResult(ncResponse, record, responseDTO);
        }catch (Throwable e) {
            logger.error("[业务异常]", e);
            setErrorInfo(responseDTO, e);
        } finally {
            PayLoggerFactory.TAG_LOCAL.remove();
        }
        return responseDTO;
    }

    @Override
    public NcCflEasyConfirmResponseDTO confirmPay(NcCflEasyConfirmRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[分期易异步确认支付] - [recordNo = " + requestDTO.getRecordNo() + "]");
            NcCflEasyConfirmResponseDTO responseDTO = new NcCflEasyConfirmResponseDTO();
        try {
            // 查询支付子单
            PayRecord record = payRecordService.queryRecordById(requestDTO.getRecordNo());
            // 调用ncpay异步确认支付接口
            PayConfirmResponseDTO ncpayConfirmResopnse = ncPayCflEasyService.confirmPay(requestDTO, record.getPaymentNo());
            bulidConfirmResponse(responseDTO, requestDTO.getRecordNo(), ncpayConfirmResopnse);
        }catch (Throwable e) {
            logger.error("[业务异常]", e);
            setErrorInfo(responseDTO, e);
        }finally {
            PayLoggerFactory.TAG_LOCAL.remove();
        }
        return responseDTO;
    }

    private void bulidConfirmResponse(NcCflEasyConfirmResponseDTO responseDTO, String recordNo,
                                      PayConfirmResponseDTO ncPayResponse) {
        responseDTO.setRecordNo(recordNo);
        responseDTO.setSmsStatus(ncPayResponse.getSmsStatus());
    }

}