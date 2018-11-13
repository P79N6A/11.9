package com.yeepay.g3.core.nccashier.facade;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.service.NcCashierUserCenterFacade;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.facade
 *
 * @author pengfei.chen
 * @since 17/1/4 16:37
 */
public class MerchantAuthorityFacadeTest extends BaseTest {

    @Autowired
    private NcCashierUserCenterFacade ncCashierUserCenterFacade;

    @Test
    public void merchantAuthorityTest(){
        MerchantAuthorityRequestDTO merchantAuthorityRequestDTO = new MerchantAuthorityRequestDTO();
        merchantAuthorityRequestDTO.setRequestId(13646l);
        MerchantAuthorityResponseDTO merchantAuthorityResponseDTO = ncCashierUserCenterFacade.merchantAuthority(merchantAuthorityRequestDTO);
        System.out.println(merchantAuthorityResponseDTO.getSmsSendStatus());
    }
    @Test
    public void shareSmsConfirmTest(){
        ShareCardAuthoritySmsConfirmRequestDTO shareCardAuthoritySmsConfirmRequestDTO = new ShareCardAuthoritySmsConfirmRequestDTO();
        shareCardAuthoritySmsConfirmRequestDTO.setRequestId(13646l);
        shareCardAuthoritySmsConfirmRequestDTO.setValidateCode("547918");
        ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirmResponseDTO = ncCashierUserCenterFacade.shareCardAuthoritySmsConfirm(shareCardAuthoritySmsConfirmRequestDTO);
        System.out.println(shareCardAuthoritySmsConfirmResponseDTO.getSmsValidateStatus());
    }

    @Test
    public void shareSendSmsTest(){
        ShareCardAuthoritySendSmsRequestDTO shareCardAuthoritySendSmsRequestDTO = new ShareCardAuthoritySendSmsRequestDTO();
        shareCardAuthoritySendSmsRequestDTO.setRequestId(13646l);
        ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSmsResponseDTO = ncCashierUserCenterFacade.shareCardAuthoritySendSms(shareCardAuthoritySendSmsRequestDTO);
        System.out.println(shareCardAuthoritySendSmsResponseDTO);
    }
}
