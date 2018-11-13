package com.yeepay.g3.core.nccashier.facade.impl;


import com.yeepay.g3.core.nccashier.biz.NcCashierUserCenterBiz;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.NcCashierUserCenterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.facade.impl
 *
 * @author pengfei.chen
 * @since 16/12/30 14:35
 */
@Service("ncCashierUserCenterFacade")
public class NcCashierUserCenterFacadeImpl implements NcCashierUserCenterFacade {
    @Autowired
    private NcCashierUserCenterBiz ncCashierUserCenterBiz;

    @Override
    public MerchantAuthorityResponseDTO merchantAuthority(MerchantAuthorityRequestDTO requestDTO) {
        return ncCashierUserCenterBiz.merchantAuthority(requestDTO);
    }

    @Override
    public ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSms(ShareCardAuthoritySendSmsRequestDTO requestDTO) {
        return ncCashierUserCenterBiz.authoritySendSms(requestDTO);
    }

    @Override
    public ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirm(ShareCardAuthoritySmsConfirmRequestDTO requestDTO) {
        return ncCashierUserCenterBiz.authoritySmsConfirm(requestDTO);
    }
}
