package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.PreauthBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.service.PreauthService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PreauthBindConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.utils.common.StringUtils;

@Service("preauthBiz")
public class PreauthBizImpl extends NcCashierBaseBizImpl implements PreauthBiz {

	@Resource
	private PreauthService preauthService;
	@Resource
	private OrderPaymentService orderPaymentService;
	@Override
	public CashierPaymentResponseDTO preAuthOrderRequest(CashierPaymentRequestDTO paymentRequestDto) {
		CashierPaymentResponseDTO response = new CashierPaymentResponseDTO();
		try {
			basicValidatePaymentRequest(paymentRequestDto, response);
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(paymentRequestDto.getRequestId());
			PaymentRecord paymentRecord = preauthService.getRecord4PreauthRequest(paymentRequest, paymentRequestDto);
			PaymentResponseDTO paymentResponseDTO = preauthService.preAuthOrderRequest(paymentRequest, paymentRecord, paymentRequestDto);
			buildPreauthOrderResponse(paymentRequestDto, response, paymentRequest, paymentResponseDTO);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}
	@Override
	public CashierPaymentResponseDTO preAuthBindOrderRequest(CashierPaymentRequestDTO paymentRequestDto) {
		CashierPaymentResponseDTO response = new CashierPaymentResponseDTO();
		try {
			basicValidatePaymentRequest(paymentRequestDto, response);
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(paymentRequestDto.getRequestId());
			PaymentRecord paymentRecord = preauthService.getRecord4PreauthBindRequest(paymentRequest, paymentRequestDto, true);
			PaymentResponseDTO paymentResponseDTO = preauthService.preAuthOrderRequest(paymentRequest, paymentRecord, paymentRequestDto);
			buildPreauthOrderResponse(paymentRequestDto, response, paymentRequest, paymentResponseDTO);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}
	@Override
	public PayConfirmBaseResponseDTO preAuthOrderConfirm(PayConfirmBaseRequestDTO requestDTO) {
		PayConfirmBaseResponseDTO response = new PayConfirmBaseResponseDTO();
		PaymentRecord paymentRecord = null;
		try {
			validatePreauthConfirmRequestDTO(requestDTO,response);
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			paymentRecord = preauthService.getRecord4PreauthConfirm(paymentRequest, requestDTO.getRecordId(), null, NCCashierOrderTypeEnum.FIRST);
			preauthService.preAuthOrderConfirm(paymentRecord, requestDTO.getVerifyCode(), null);
			buidlPPConfirmResponse(response);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}
	@Override
	public PayConfirmBaseResponseDTO preAuthBindOrderConfirm(PreauthBindConfirmRequestDTO requestDTO) {
		PayConfirmBaseResponseDTO response = new PayConfirmBaseResponseDTO();
		PaymentRecord paymentRecord = null;
		try {
			validatePreauthBindConfirmRequestDTO(requestDTO,response);
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			paymentRecord = preauthService.getRecord4PreauthConfirm(paymentRequest, requestDTO.getRecordId(), null, NCCashierOrderTypeEnum.BIND);
			Long cardId = preauthService.saveTmpCard(paymentRecord, requestDTO.getCardInfoDTO(), paymentRequest, true);
			preauthService.preAuthOrderConfirm(paymentRecord, requestDTO.getVerifyCode(), cardId);
			buidlPPConfirmResponse(response);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}
	@Override
	public CashierSmsSendResponseDTO preAuthSendSms(CashierSmsSendRequestDTO smsRequest) {
		CashierSmsSendResponseDTO response = new CashierSmsSendResponseDTO();
		Long tmpCardId = 0l;
		try {
			basicValidateSmsRequest(smsRequest, response);
			NeedBankCardDTO needBankCardDTO = smsRequest.getNeedBankCardDTO(); 
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(smsRequest.getRequestId());
			PaymentRecord paymentRecord = preauthService.getRecord4PreauthSendSms(paymentRequest,smsRequest.getRecordId(), null);
			if(paymentRecord.getNeedItem() != 0 && null != needBankCardDTO){
				CardInfoDTO cardInfoDTO = buildCardInfoDTO(needBankCardDTO);
				tmpCardId = preauthService.saveTmpCard(paymentRecord, cardInfoDTO, paymentRequest, true);
			}
			preauthService.preauthSmsSend(paymentRecord, smsRequest, tmpCardId);
			response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}
	private void buildPreauthOrderResponse(CashierPaymentRequestDTO paymentRequestDto,
			CashierPaymentResponseDTO response, PaymentRequest paymentRequest, PaymentResponseDTO paymentResponseDTO) {
		ReqSmsSendTypeEnum smsType = CommonUtil.transferBindPaySMSType(paymentResponseDTO.getSmsType());
		response.setReqSmsSendTypeEnum(smsType);
		if(paymentResponseDTO.getNeedItem() != 0){
			NeedBankCardDTO bindCardAndNeedSupplement = orderPaymentService.getBindCardAndNeedSupplement(paymentRequestDto.getBindId(),paymentResponseDTO.getNeedItem(),paymentRequest);
			response.setNeedBankCardDto(bindCardAndNeedSupplement);
		}
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}
	
	private void buidlPPConfirmResponse(PayConfirmBaseResponseDTO response) {
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}
	private void validatePreauthConfirmRequestDTO(PayConfirmBaseRequestDTO requestDTO,PayConfirmBaseResponseDTO response) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "请求参数为空");
		}
		requestDTO.validate();
		response.setTokenId(requestDTO.getTokenId());
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[preauthConfirm],预授权确认requestId=" + requestDTO.getRequestId() + ",预授权确认recordId=" + requestDTO.getRecordId() + "]");
	}
	private void validatePreauthBindConfirmRequestDTO(PreauthBindConfirmRequestDTO requestDTO, PayConfirmBaseResponseDTO response) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "请求参数为空");
		}
		requestDTO.validate();
		response.setTokenId(requestDTO.getTokenId());
		NcCashierLoggerFactory.TAG_LOCAL.set("[preauthBindConfirm],预授权绑卡确认requestId=" + requestDTO.getRequestId()
				+ ",预授权绑卡确认recordId=" + requestDTO.getRecordId() + "]");
	}

	private void basicValidatePaymentRequest(CashierPaymentRequestDTO paymentRequestDto,
			CashierPaymentResponseDTO response) {
		BeanValidator.validate(paymentRequestDto);
		response.setTokenId(paymentRequestDto.getTokenId());
		NcCashierLoggerFactory.TAG_LOCAL.set("[preAuthOrderRequest],预授权请求requestId=" + paymentRequestDto.getRequestId()
				+ ",预授权请求recordId=" + paymentRequestDto.getRecordId() + "");
	}
	private void basicValidateSmsRequest(CashierSmsSendRequestDTO smsRequest,
			CashierSmsSendResponseDTO response) {
		BeanValidator.validate(smsRequest);
		response.setTokenId(smsRequest.getTokenId());
		NcCashierLoggerFactory.TAG_LOCAL.set("[preauthSendSms],支付请求ID=" + smsRequest.getRequestId()
				+ ",支付记录ID=" + smsRequest.getRecordId() + "]");
	}
	public NeedBankCardDTO getBindCardAndNeedSupplement(long bindId, int needItem, PaymentRequest paymentRequest) {
		NeedBankCardDTO bkdto = new NeedBankCardDTO();
		BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(bindId);
		bkdto.setPhoneNo(bindCardDTO.getBankMobile());//发短验用
		bkdto.setYpMobile(bindCardDTO.getYbMobile());//发短验用
		//ncpay返回的补充项 获取绑卡补充项并设置绑卡限制值
		NeedSurportDTO needSurportDTO = setCardNeedSupplement(needItem, paymentRequest, bkdto);
		bkdto.setNeedSurportDTO(needSurportDTO);
		return bkdto;
	}
	private NeedSurportDTO setCardNeedSupplement(int needItem, PaymentRequest paymentRequest, NeedBankCardDTO bkdto){
		//NCPAY返回的当前卡所需补充项不为空
		NeedSurportDTO needSurportDTO = null;
		if (needItem != 0) {
			// 获取绑卡限制值  后续设置NeedBankCardDTO返回值，将必填且有值得项塞进去
			BindLimitInfoResDTO samePersonInfo = bankCardLimitInfoService.getLimitInfo4bind(paymentRequest);
			NCPayParamMode nCPayParamMode = new NCPayParamMode(needItem);
			logger.info("ncpay所需补充项={}", nCPayParamMode);
			needSurportDTO = new NeedSurportDTO();
			if (nCPayParamMode.needAvlidDate()) {
				needSurportDTO.setAvlidDateIsNeed(true);
			}
			if (nCPayParamMode.needBankMobilePhone()) {
				needSurportDTO.setPhoneNoIsNeed(true);
			}//密码
			if (nCPayParamMode.needBankPWD()) {
				needSurportDTO.setBankPWDIsNeed(true);
			}//CVV
			if (nCPayParamMode.needCvv()) {
				needSurportDTO.setCvvIsNeed(true);
			}//证件号
			if (nCPayParamMode.needIdCardNumber()) {
				needSurportDTO.setIdnoIsNeed(true);
			}//证件类型
			if (nCPayParamMode.needIdCardType()) {
				needSurportDTO.setIdCardTypeIsNeed(true);
			}
			if (nCPayParamMode.needNumber()) {
				needSurportDTO.setCardnoIsNeed(true);
			}//姓名
			if (nCPayParamMode.needUserName()) {
				needSurportDTO.setOwnerIsNeed(true);
			}
			if (nCPayParamMode.needYeepayMobilePhone()) {
				needSurportDTO.setYpMobileIsNeed(true);
			}
			//若同人限制值有值则页面不显示补充项信息
			if (samePersonInfo != null
					&& !Constant.MERCHANT_LIMIT_TYPE.equals(samePersonInfo
							.getBindCardLimitType())
					&& StringUtils.isNotBlank(samePersonInfo
							.getIdentityNoLimit())
					&& StringUtils
							.isNotBlank(samePersonInfo.getUserNameLimit())) {
				if (nCPayParamMode.needUserName()) {
					needSurportDTO.setOwnerIsNeed(false);
				}
				if (nCPayParamMode.needIdCardNumber()) {
					needSurportDTO.setIdnoIsNeed(false);
				}
			}
			bkdto.setNeedSurportDTO(needSurportDTO);
		}
		return needSurportDTO;
	}
	private CardInfoDTO buildCardInfoDTO(NeedBankCardDTO needBankCardDTO) {
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		cardInfoDTO.setCardno(needBankCardDTO.getCardno());
		cardInfoDTO.setValid(needBankCardDTO.getAvlidDate());
		cardInfoDTO.setName(needBankCardDTO.getOwner());
		cardInfoDTO.setCvv2(needBankCardDTO.getCvv());
		cardInfoDTO.setIdno(needBankCardDTO.getIdno());
		cardInfoDTO.setPhone(needBankCardDTO.getPhoneNo());
		return cardInfoDTO;
	}
}
