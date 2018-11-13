package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.biz
 *
 * @author pengfei.chen
 * @since 16/12/30 14:38
 */
public interface NcCashierUserCenterBiz {

    public MerchantAuthorityResponseDTO merchantAuthority(MerchantAuthorityRequestDTO requestDTO);

    public ShareCardAuthoritySendSmsResponseDTO authoritySendSms(ShareCardAuthoritySendSmsRequestDTO requestDTO);

    public ShareCardAuthoritySmsConfirmResponseDTO authoritySmsConfirm(ShareCardAuthoritySmsConfirmRequestDTO requestDTO);
}
