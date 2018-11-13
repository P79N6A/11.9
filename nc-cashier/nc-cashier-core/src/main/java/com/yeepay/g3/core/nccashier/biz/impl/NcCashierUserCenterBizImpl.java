package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.NcCashierUserCenterBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.SmsSendStatusEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.cwh.param.BaseInfo;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.biz.impl
 *
 * @author pengfei.chen
 * @since 16/12/30 14:44
 */
@Service("ncCashierUserCenterBiz")
public class NcCashierUserCenterBizImpl extends NcCashierBaseBizImpl implements NcCashierUserCenterBiz {
    Logger logger = LoggerFactory.getLogger(NcCashierUserCenterBizImpl.class) ;
    @Override
    public MerchantAuthorityResponseDTO merchantAuthority(MerchantAuthorityRequestDTO requestDTO) {
        MerchantAuthorityResponseDTO merchantAuthorityResponseDTO = new MerchantAuthorityResponseDTO();
        try {
            if(requestDTO == null || requestDTO.getRequestId() == null){
                throw CommonUtil.handleException(Errors.INPUT_PARAM_NULL);
            }
            PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
            NcCashierLoggerFactory.TAG_LOCAL.set("[共享卡授权请求|merchantAuthority] - [requestId=" + requestDTO.getRequestId() + "]");
            //检验是否需要去请求用户中心去授权
            BaseInfo baseInfo = ncCashierUserCenterService.validateNeedAuthorityRequest(paymentRequest,requestDTO.getCusType());
            if(baseInfo!=null){
                ncCashierUserCenterService.authorityCreateOrderAndSendSms(paymentRequest,baseInfo,merchantAuthorityResponseDTO);
                buildMerchantAuthorityResponse( baseInfo, merchantAuthorityResponseDTO);
            }else {
                throw CommonUtil.handleException(Errors.USER_CENTER_EXCEPTION);
            }
        }catch (Throwable e){
            logger.error("商户授权失败,requestId:{}", requestDTO.getRequestId(), e);
            handleException(merchantAuthorityResponseDTO, e);
        }
        return merchantAuthorityResponseDTO;
    }

    @Override
    public ShareCardAuthoritySendSmsResponseDTO authoritySendSms(ShareCardAuthoritySendSmsRequestDTO requestDTO) {
        ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSmsResponseDTO = new ShareCardAuthoritySendSmsResponseDTO();
        try {
            if (requestDTO == null || requestDTO.getRequestId() == null) {
                throw CommonUtil.handleException(Errors.INPUT_PARAM_NULL);
            }
            NcCashierLoggerFactory.TAG_LOCAL.set("[共享卡授权发送短信|authoritySendSms] - [requestId=" + requestDTO.getRequestId() + "]");
            ncCashierUserCenterService.shareCardAuthoritySendSms(requestDTO,shareCardAuthoritySendSmsResponseDTO);
        }catch (Throwable e){
            shareCardAuthoritySendSmsResponseDTO.setSmsSendStatus(SmsSendStatusEnum.SEND_FAILED.name());
            handleException(shareCardAuthoritySendSmsResponseDTO,e);
        }
        return shareCardAuthoritySendSmsResponseDTO;
    }

    @Override
    public ShareCardAuthoritySmsConfirmResponseDTO authoritySmsConfirm(ShareCardAuthoritySmsConfirmRequestDTO requestDTO){
        ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirmResponseDTO = new ShareCardAuthoritySmsConfirmResponseDTO();
        try {
            if (requestDTO == null || requestDTO.getRequestId() == null || StringUtils.isBlank(requestDTO.getValidateCode())) {
                throw CommonUtil.handleException(Errors.INPUT_PARAM_NULL);
            }
            NcCashierLoggerFactory.TAG_LOCAL.set("[共享卡授权确认短信|authoritySmsConfirm] - [requestId=" + requestDTO.getRequestId() + "]");
            //请求短信确认
            ncCashierUserCenterService.shareCardAuthoritySmsConfirm(requestDTO, shareCardAuthoritySmsConfirmResponseDTO);
            shareCardAuthoritySmsConfirmResponseDTO.setSmsValidateStatus(SmsSendStatusEnum.SMS_CONFIRM_SUCCESS.name());

        }catch (Throwable e){
            logger.error("授权短信确认失败,requestId:{}",requestDTO.getRequestId(),e);
            shareCardAuthoritySmsConfirmResponseDTO.setSmsValidateStatus(SmsSendStatusEnum.SMS_CONFIRM_FAILED.name());
            handleException(shareCardAuthoritySmsConfirmResponseDTO,e);
        }
        return shareCardAuthoritySmsConfirmResponseDTO;
    }

    private void buildMerchantAuthorityResponse(BaseInfo baseInfo,MerchantAuthorityResponseDTO merchantAuthorityResponseDTO){
        String idcard  = baseInfo.getIdcard();
        if(StringUtils.isNotBlank(idcard)){
            merchantAuthorityResponseDTO.setIdCardNoLater(idcard.substring(idcard.length()-4));
        }
        String phone = baseInfo.getBankMobile();
        if(StringUtils.isBlank(phone)){
            phone = baseInfo.getYbMobile();
        }

        if(StringUtils.isNotBlank(phone)){
            merchantAuthorityResponseDTO.setPhoneLater(HiddenCode.hiddenMobile(phone));
        }
        merchantAuthorityResponseDTO.setSmsSendStatus(SmsSendStatusEnum.SEND_SUCCESS.name());

    }

}
