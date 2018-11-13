/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package com.yeepay.g3.core.payprocessor.external.service.impl;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.external.service.NcPayCflEasyService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.ncpay.dto.*;
import com.yeepay.g3.facade.ncpay.enumtype.OrderTypeEnum;
import com.yeepay.g3.facade.ncpay.exception.PaymentException;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasyConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasyRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcCflEasySmsRequestDTO;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 类名称: NcPayCflEasyServiceImpl <br>
 * 类描述: <br>
 *
 * @author: zhijun.wang
 * @since: 18/9/1 下午3:25
 * @version: 1.0.0
 */
@Service
public class NcPayCflEasyServiceImpl extends AbstractService implements NcPayCflEasyService {

    private static final Logger logger = PayLoggerFactory.getLogger(NcPayCflEasyServiceImpl.class);

    @Override
    public CflEasyResponseDTO createPayment(NcCflEasyRequestDTO requestDTO, PayRecord record) {
        CflEasyResponseDTO response = null;
        try {
            response = ncPayCflEasyFacade.createPayment(buildCreatePaymentRequest(requestDTO, record));
        }catch (PaymentException e) {
            throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
                    ErrorCode.P9003081);
        }catch (Throwable t) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        if(response == null) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        return response;
    }

    /**
     * 组装ncpay下单请求参数
     */
    private CflEasyRequestDTO buildCreatePaymentRequest(NcCflEasyRequestDTO requestDTO, PayRecord record) {
        CflEasyRequestDTO param = new CflEasyRequestDTO();
        param.setBizType(requestDTO.getBizType());
        param.setBizOrderNum(record.getRecordNo());
        param.setOrderType(OrderTypeEnum.CFL_EASY);
        param.setBizOrderDate(record.getCreateTime());
        param.setMerchantNo(requestDTO.getCustomerNumber());
        param.setMerchantName(requestDTO.getCustomerName());
        param.setProductName(requestDTO.getProductName());
        param.setRequestNo(requestDTO.getOutTradeNo());
        param.setMemberType(requestDTO.getMemberType());
        param.setMemberNO(requestDTO.getMemberNO());
        if(record.isCombinedPay()) {
            param.setOrderAmount(record.getFirstPayAmount());
        }else {
            param.setOrderAmount(requestDTO.getAmount());
        }
        param.setCardInfoType(requestDTO.getCardInfoType());
        param.setCardInfoId(requestDTO.getCardInfoId());
        param.setGoodsInfo(requestDTO.getGoodsInfo());
        param.setToolInfo(requestDTO.getToolsInfo());
        param.setTerminalId(requestDTO.getPayScene());
        param.setRequestSystem(ConstantUtils.NC_PAY_REQUEST_SYS);
        param.setPayToolId(requestDTO.getPayTool());
        param.setDealUniqueSerialNo(requestDTO.getDealUniqueSerialNo());
        if (requestDTO.getUserFee() != null) {
            param.setPayerFee(requestDTO.getUserFee());
        } else {
            param.setPayerFee(new BigDecimal(0));
        }
        param.setRetailProductCode(record.getRetailProductCode());
        param.setBasicProductCode(record.getBasicProductCode());
        // 添加卡信息
        if(requestDTO.getBankCardInfoDTO() != null) {
            param.setCardInfoDTO(composeCardInfo(requestDTO.getBankCardInfoDTO()));
        }
        param.setCflCount(requestDTO.getCflCount());
        return param;
    }

    @Override
    public CflEasySmsResponseDTO sendSms(NcCflEasySmsRequestDTO requestDTO, String ncpayPaymentNo) {
        CflEasySmsResponseDTO response = null;
        try {
            response = ncPayCflEasyFacade.sendMessage(builidSmsRequest(requestDTO, ncpayPaymentNo));
        }catch (PaymentException e) {
            throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
                    ErrorCode.P9003082);
        }catch (Throwable t) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        if(response == null) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        return response;
    }

    /**
     * 组装ncpay发短信入参
     */
    private CflEasySmsRequestDTO builidSmsRequest(NcCflEasySmsRequestDTO requestDTO, String paymentNo) {
        CflEasySmsRequestDTO request = new CflEasySmsRequestDTO();
        request.setPayOrderId(paymentNo);
        request.setSmsSendType(requestDTO.getSmsSendType());
        request.setTmpCardId(requestDTO.getTmpCardId());
        if(requestDTO.getBankCardInfoDTO() != null) {
            request.setCardInfoDTO(composeCardInfo(requestDTO.getBankCardInfoDTO()));
        }
        return request;
    }

    @Override
    public PayQueryResponseDTO synConfirmPay(NcCflEasyConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
        PayQueryResponseDTO payQueryResponseDTO = null;
        try {
            payQueryResponseDTO = ncPayCflEasyFacade.synConfirmPay(bulidConfirmPayDTO(requestDTO, ncpayPaymentNo));
        } catch (PaymentException e) {
            throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
                    ErrorCode.P9003083);
        } catch (Throwable e) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        return payQueryResponseDTO;
    }

    /**
     * 组装ncpay确认支付入参
     */
    private CflEasyConfirmRequestDTO bulidConfirmPayDTO(NcCflEasyConfirmRequestDTO requestDTO, String paymentNo) {
        CflEasyConfirmRequestDTO request = new CflEasyConfirmRequestDTO();
        request.setPayOrderId(paymentNo);
        request.setSmsCode(requestDTO.getSmsCode());
        request.setTmpCardId(requestDTO.getTmpCardId());
        if(requestDTO.getBankCardInfoDTO() != null) {
            request.setCardInfoDTO(composeCardInfo(requestDTO.getBankCardInfoDTO()));
        }
        return request;
    }

    @Override
    public PayConfirmResponseDTO confirmPay(NcCflEasyConfirmRequestDTO requestDTO, String ncpayPaymentNo) {
        PayConfirmResponseDTO payConfirmResponseDTO = null;
        try {
            payConfirmResponseDTO = ncPayCflEasyFacade.confirmPay(bulidConfirmPayDTO(requestDTO, ncpayPaymentNo));
        } catch (PaymentException e) {
            throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.NCPAY.getSysCode(), e.getDefineCode(), e.getMessage(),
                    ErrorCode.P9003083);
        } catch (Throwable e) {
            throw new PayBizException(ErrorCode.P9001000);
        }
        return payConfirmResponseDTO;
    }
}