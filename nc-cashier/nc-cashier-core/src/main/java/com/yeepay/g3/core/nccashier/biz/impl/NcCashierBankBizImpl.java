/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yeepay.g3.core.nccashier.biz.NcCashierBankBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.CashierBankCardService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;
import com.yeepay.g3.utils.common.CollectionUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 
 * @author：zhen.tan
 * @since：2016年5月27日 上午9:31:25
 *
 */
@Service("ncCashierBankBiz")
public class NcCashierBankBizImpl extends NcCashierBaseBizImpl implements NcCashierBankBiz {


	@Resource
	private CashierBankCardService cashierBankCardService;
	
	@Resource
	private PaymentRequestService paymentRequestService;

	@Override
	public CardValidateResponseDTO getCardValidates(CardValidateRequestDTO requestDto) {
		CardValidateResponseDTO response = new CardValidateResponseDTO();
		try {
			basicValidateCardValidateRequest(requestDto, response);
			cashierBankCardService.getCardValidates(response, requestDto);
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}

	private void basicValidateCardValidateRequest(CardValidateRequestDTO requestDto,
			CardValidateResponseDTO response) {
		BeanValidator.validate(requestDto);
		response.setRequestId(requestDto.getRequestId());
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[getCardValidates],支付请求ID=" + requestDto.getRequestId());
	}

	public SupportBanksResponseDTO supportBankList(long requestId, String cusType) {
		SupportBanksResponseDTO response = new SupportBanksResponseDTO();
		try {
			basicValidateSupportBankRequest(requestId, response);
			cashierBankCardService.validateAndGetSupportBankList(requestId, response,cusType);
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}

	private void basicValidateSupportBankRequest(long requestId, SupportBanksResponseDTO response) {
		if (requestId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
		response.setRequestId(requestId);
		NcCashierLoggerFactory.TAG_LOCAL.set("[supportBankList],支付请求ID=" + requestId);
	}
	@Override
	public BankCardReponseDTO getBankCardInfo4Preauth(long requestId,String cusType) {
		BankCardReponseDTO bankCardReponseDTO = new BankCardReponseDTO();
		try {
			if (requestId <= 0) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
			}
			bankCardReponseDTO.setRequestId(requestId);
			NcCashierLoggerFactory.TAG_LOCAL.set("[getBankCardInfo4Preauth],支付请求ID=" + requestId);
			cashierBankCardService.getBankCardInfo4Preauth(requestId, bankCardReponseDTO, cusType);
		} catch (Throwable e) {
			handleException(bankCardReponseDTO, e);
		}
		return bankCardReponseDTO;
	}
	@Override
	public BankCardReponseDTO getBankCardInfo(long requestId,String cusType) {
		BankCardReponseDTO bankCardReponseDTO = new BankCardReponseDTO();
		try {
			if (requestId <= 0) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
			}
			bankCardReponseDTO.setRequestId(requestId);
			NcCashierLoggerFactory.TAG_LOCAL.set("[getBankCardInfo],支付请求ID=" + requestId);
			cashierBankCardService.getBankCardInfo(requestId, bankCardReponseDTO,cusType);
		} catch (Throwable e) {
			handleException(bankCardReponseDTO, e);
		}
		return bankCardReponseDTO;
	}

	@Override
	public BasicResponseDTO validatePassBindId(long requestId) {
		BasicResponseDTO basicResponseDTO = new BasicResponseDTO();
		try {
			if (requestId <= 0) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
			}
			NcCashierLoggerFactory.TAG_LOCAL.set("[validatePassBindId],支付请求ID=" + requestId);
			cashierBankCardService.validatePassBindId(requestId);
		} catch (Throwable e) {
			handleException(basicResponseDTO, e);
		}
		return basicResponseDTO;
	}

	@Override
	public PassBindIdDTO isPassBindId(long requestId) {
		PassBindIdDTO passBindIdDTO = new PassBindIdDTO();
		try {
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestId);
			if(StringUtils.isNotEmpty(paymentRequest.getBindId()))
				passBindIdDTO.setPassBindId(true);
			else
				passBindIdDTO.setPassBindId(false);
		} catch (Throwable e) {
			handleException(passBindIdDTO, e);
		}
		return passBindIdDTO;
	}

	@Override
	public CardBinInfoDTO getCardBinInfo(String cardno) {
		CardBinInfoDTO cardBinInfoDTO = new CardBinInfoDTO();
		try {
			if (StringUtils.isEmpty(cardno)) {
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "cardno为空");
			}
			CardBinDTO cardBinDTO = cashierBankCardService.getCardBinInfo(cardno);
			if(cardBinDTO.getBankEnum() == null || cardBinDTO.getCardType() ==null) {
				throw CommonUtil.handleException(Errors.INVALID_BANK_CARD_NO);
			}
			//将ncconfig的bankcode，转换成和通道符合的bankcode
			String origBankCode = cardBinDTO.getBankEnum().name();
			String bankCode = CommonUtil.standardBankCode(origBankCode);
			if(StringUtils.isNotEmpty(bankCode)){
				cardBinInfoDTO.setBank(bankCode);
			}else {
				cardBinInfoDTO.setBank(origBankCode);
			}
			cardBinInfoDTO.setName(cardBinDTO.getBankEnum().getName());
			cardBinInfoDTO.setCardno(cardno);
			if (cardBinDTO.getCardType() != null) {
				cardBinInfoDTO.setCardTypeEnum(CardTypeEnum.valueOf(cardBinDTO.getCardType().toString()));
			}
		} catch (Throwable e) {
			handleException(cardBinInfoDTO, e);
		}
		return cardBinInfoDTO;
	}

	@Override
	public InstallmentBanksResponseDTO getBankList(long requestId) {
		try {
			validateBankListParam(requestId);
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestId);
			InstallmentBanksResponseDTO bankList = cashierBankCardService
					.filterSupportInstallmentBankList(paymentRequest.getMerchantNo(), paymentRequest.getOrderAmount());
			if (bankList == null || (CollectionUtils.isEmpty(bankList.getUnusableBankList())
					&& CollectionUtils.isEmpty(bankList.getUsableBankList()))) {
				throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
			} 
			return bankList;
		} catch (Throwable t) {
			InstallmentBanksResponseDTO responseDTO = new InstallmentBanksResponseDTO();
			handleException(responseDTO, t);
			return responseDTO;
		}
	}
	
	private void validateBankListParam(long requestId){
		if (requestId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[getBankList],支付请求ID=" + requestId);
	}

}
