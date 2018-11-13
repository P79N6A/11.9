package com.yeepay.g3.app.nccashier.wap.service.impl;

import com.yeepay.g3.app.nccashier.wap.service.MerchantAuthorityService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.service.impl
 *
 * @author pengfei.chen
 * @since 17/1/4 14:45
 */
@Service("merchantAuthorityService")
public class MerchantAuthorityServiceImpl implements MerchantAuthorityService {

    @Autowired
    private NcCashierService ncCashierService;

    @Override
    public MerchantAuthorityResponseDTO merchantAuthorityRequest(Long requestId) {
        MerchantAuthorityRequestDTO merchantAuthorityRequestDTO = new MerchantAuthorityRequestDTO();
        merchantAuthorityRequestDTO.setRequestId(requestId);
        merchantAuthorityRequestDTO.setCusType(Constant.BNAK_RULE_CUSTYPE_SALE);
        return ncCashierService.merchantAuthorityRequest(merchantAuthorityRequestDTO);
    }

    @Override
    public ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSms(Long requestId) {
        ShareCardAuthoritySendSmsRequestDTO shareCardAuthoritySendSmsRequestDTO = new ShareCardAuthoritySendSmsRequestDTO();
        shareCardAuthoritySendSmsRequestDTO.setRequestId(requestId);
        return ncCashierService.shareCardAuthoritySendSms(shareCardAuthoritySendSmsRequestDTO);
    }

    @Override
    public ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirm(Long requestId,String validateCode) {
        ShareCardAuthoritySmsConfirmRequestDTO shareCardAuthoritySmsConfirmRequestDTO = new ShareCardAuthoritySmsConfirmRequestDTO();
        shareCardAuthoritySmsConfirmRequestDTO.setRequestId(requestId);
        shareCardAuthoritySmsConfirmRequestDTO.setValidateCode(validateCode);
        shareCardAuthoritySmsConfirmRequestDTO.setCusType(Constant.BNAK_RULE_CUSTYPE_SALE);
        ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirmResponseDTO = ncCashierService.shareCardAuthoritySmsConfirm(shareCardAuthoritySmsConfirmRequestDTO);
        return shareCardAuthoritySmsConfirmResponseDTO;
    }
}
