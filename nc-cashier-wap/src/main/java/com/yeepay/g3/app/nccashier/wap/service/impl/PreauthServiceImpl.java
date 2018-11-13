package com.yeepay.g3.app.nccashier.wap.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.PreauthService;
import com.yeepay.g3.app.nccashier.wap.vo.CardBinInfoResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardInfoVO;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PreauthBindConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Service("preauthService")
public class PreauthServiceImpl implements PreauthService {
	private static final Logger logger = LoggerFactory.getLogger(PreauthServiceImpl.class);
	@Resource
	private NcCashierService ncCashierService;

	@Override
	public ReqSmsSendTypeEnum preAuthOrderRequest(String token, RequestInfoDTO info, CardInfoVO cardInfo,
			CardBinInfoResponseVO cardBinInfo) {
		ReqSmsSendTypeEnum reqSmsSendTypeEnum = null;
		CashierPaymentRequestDTO requestDTO = buildPreauthRequest(token, info, cardInfo, cardBinInfo);
		logger.info("[monitor],event:nccashierwap_preauth_CreateOrder_request,requestId:{},merchantNo:{}",
				info.getPaymentRequestId(), info.getMerchantNo());
		CashierPaymentResponseDTO response = ncCashierService.preauthOrderRequest(requestDTO);
		reqSmsSendTypeEnum = response.getReqSmsSendTypeEnum();
		return reqSmsSendTypeEnum;
	}

	@Override
	public void preAuthOrderConfirm(String token, RequestInfoDTO info, String verifyCode) {
		PayConfirmBaseRequestDTO requestDTO = new PayConfirmBaseRequestDTO();
		requestDTO.setTokenId(token);
		requestDTO.setRecordId(info.getPaymentRecordId());
		requestDTO.setRequestId(info.getPaymentRequestId());
		requestDTO.setVerifyCode(verifyCode);
		logger.info("[monitor],event:nccashierwap_preauth_ConfirmOrder_request,requestId:{},merchantNo:{}",
				info.getPaymentRequestId(), info.getMerchantNo());
		ncCashierService.preauthOrderConfirm(requestDTO);
	}

	@Override
	public CashierPaymentResponseDTO preAuthBindOrderRequest(String token, String bindId, RequestInfoDTO info) {
		CashierPaymentRequestDTO requestDTO = new CashierPaymentRequestDTO();
		requestDTO.setOrderType(NCCashierOrderTypeEnum.BIND);
		requestDTO.setRequestId(info.getPaymentRequestId());
		requestDTO.setRecordId(info.getPaymentRecordId() == null ? 0 : info.getPaymentRecordId());
		requestDTO.setTokenId(token);
		requestDTO.setBindId(Long.parseLong(bindId));

		logger.info("[monitor],event:nccashierwap_preauthBindOrder_request,requestId:{},merchantNo:{}",
				info.getPaymentRequestId(), info.getMerchantNo());
		return ncCashierService.preauthBindOrderRequest(requestDTO);
	}
	@Override
	public void preAuthBindOrderConfirm(String token, RequestInfoDTO info, String verifyCode, CardInfoVO cardInfo) {
		PreauthBindConfirmRequestDTO requestDTO = new PreauthBindConfirmRequestDTO();
		requestDTO.setTokenId(token);
		requestDTO.setRecordId(info.getPaymentRecordId());
		requestDTO.setRequestId(info.getPaymentRequestId());
		CardInfoDTO cardInfoDTO = buildCardInfoDTO(cardInfo, null);
		requestDTO.setCardInfoDTO(cardInfoDTO);
		requestDTO.setVerifyCode(verifyCode);
		logger.info("[monitor],event:nccashierwap_preauthBindOrder_confirm,requestId:{},merchantNo:{}",
				info.getPaymentRequestId(), info.getMerchantNo());
		ncCashierService.preauthBindOrderConfirm(requestDTO);
	}

	private NeedBankCardDTO buildNeedBankCardDTO(CardInfoVO cardInfo) {
		NeedBankCardDTO needBankCardDTO = new NeedBankCardDTO();
        needBankCardDTO.setAvlidDate(cardInfo.getAvlidDate());
        needBankCardDTO.setOwner(cardInfo.getOwner());
        needBankCardDTO.setCvv(cardInfo.getCvv());
        needBankCardDTO.setCardno(cardInfo.getCardNo());
        needBankCardDTO.setIdno(cardInfo.getIdNo());
        needBankCardDTO.setPhoneNo(cardInfo.getPhoneNo());
		return needBankCardDTO;
	}
	@Override
	public void preAuthSendSMS(String token, RequestInfoDTO info, CardInfoVO cardInfo,ReqSmsSendTypeEnum smsType) {
		CashierSmsSendRequestDTO requestDTO = new CashierSmsSendRequestDTO();
		requestDTO.setReqSmsSendTypeEnum(smsType);
		requestDTO.setRecordId(info.getPaymentRecordId());
		requestDTO.setTokenId(token);
		requestDTO.setRequestId(info.getPaymentRequestId());
		NeedBankCardDTO needBankCardDTO = buildNeedBankCardDTO(cardInfo);
		requestDTO.setNeedBankCardDTO(needBankCardDTO);
		logger.info("[monitor],event:nccashierwap_preAuthSendSMS_request,requestId:{},merchantNo:{}",
				info.getPaymentRequestId(), info.getMerchantNo());
		ncCashierService.preAuthSendSms(requestDTO);
	}

	private CashierPaymentRequestDTO buildPreauthRequest(String token, RequestInfoDTO info, CardInfoVO cardInfo,
			CardBinInfoResponseVO cardBinInfo) {
		CashierPaymentRequestDTO requestDTO = new CashierPaymentRequestDTO();
		CardInfoDTO cardInfoDto = buildCardInfoDTO(cardInfo, cardBinInfo);
		requestDTO.setCardInfo(cardInfoDto);
		requestDTO.setRequestId(info.getPaymentRequestId());
		requestDTO.setRecordId(info.getPaymentRecordId() == null ? 0 : info.getPaymentRecordId());
		requestDTO.setTokenId(token);
		requestDTO.setOrderType(NCCashierOrderTypeEnum.FIRST);
		return requestDTO;
	}

	private CardInfoDTO buildCardInfoDTO(CardInfoVO cardInfo, CardBinInfoResponseVO cardBinInfo) {
		CardInfoDTO cardInfoDto = new CardInfoDTO();
		if(null != cardInfo){
			cardInfoDto.setCardno(cardInfo.getCardNo());
			cardInfoDto.setValid(cardInfo.getAvlidDate());
			cardInfoDto.setCvv2(cardInfo.getCvv());
			cardInfoDto.setIdno(cardInfo.getIdNo());
			cardInfoDto.setName(cardInfo.getOwner());
			cardInfoDto.setPhone(cardInfo.getPhoneNo());
		}
		if(null != cardBinInfo){
			cardInfoDto.setBankCode(cardBinInfo.getBankCode());
			cardInfoDto.setBankName(cardBinInfo.getBankName());
			cardInfoDto.setCardType(cardBinInfo.getCardType());
		}
		return cardInfoDto;
	}

	
}
