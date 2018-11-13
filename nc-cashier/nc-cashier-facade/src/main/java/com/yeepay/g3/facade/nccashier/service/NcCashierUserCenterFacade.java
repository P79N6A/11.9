package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * Description 调取关于用户中心接口的业务
 * PackageName: com.yeepay.g3.facade.nccashier.service
 *
 * @author pengfei.chen
 * @since 16/12/30 14:31
 */
public interface NcCashierUserCenterFacade {

    public MerchantAuthorityResponseDTO merchantAuthority(MerchantAuthorityRequestDTO requestDTO);

    public ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSms(ShareCardAuthoritySendSmsRequestDTO requestDTO);

    public ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirm(ShareCardAuthoritySmsConfirmRequestDTO requestDTO);
}
