package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.cwh.param.BaseInfo;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.ncmember.dto.*;

import java.util.List;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service
 *
 * @author pengfei.chen
 * @since 16/12/30 15:17
 */
public interface NcCashierUserCenterService {
    public MerchantConfigRespDTO registInfoFromUserCenter(String merchantNo);

    public GetSharableRespDTO queryShareBindCardList(GetSharableReqDTO getSharableRespDTO);

    public List<BaseInfo> filterShareBindCard(List<BaseInfo> list,PaymentRequest paymentRequest, String cusType);

    public RequestAuthorityRespDTO shareCardAuthCreateOrder(RequestAuthorityReqDTO requestAuthorityReqDTO);

    public void shareCardAuthoritySendSms(ShareCardAuthoritySendSmsRequestDTO shareCardAuthoritySendSmsRequestDTO,ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSmsResponseDTO);

    public void shareCardAuthoritySmsConfirm(ShareCardAuthoritySmsConfirmRequestDTO shareCardAuthoritySmsConfirmRequestDTO,ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirmResponseDTO);

    public void sortBindCardByMerchant(List<BindCardDTO> bindCardDTOList,List<BaseInfo> baseInfoList );

    public BaseInfo validateNeedAuthorityRequest(PaymentRequest paymentRequest,String cusType);

    public void authorityCreateOrderAndSendSms(PaymentRequest paymentRequest,BaseInfo baseInfo,MerchantAuthorityResponseDTO merchantAuthorityResponseDTO);
}
