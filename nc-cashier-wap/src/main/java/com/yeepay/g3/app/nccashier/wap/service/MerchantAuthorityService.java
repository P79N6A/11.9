package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.facade.nccashier.dto.MerchantAuthorityResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ShareCardAuthoritySendSmsResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ShareCardAuthoritySmsConfirmResponseDTO;

/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.service
 *
 * @author pengfei.chen
 * @since 17/1/4 14:45
 */
public interface MerchantAuthorityService {

    public MerchantAuthorityResponseDTO merchantAuthorityRequest(Long requestId);

    public ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSms(Long requestId);

    public ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirm(Long requestId,String validateCode);

}
