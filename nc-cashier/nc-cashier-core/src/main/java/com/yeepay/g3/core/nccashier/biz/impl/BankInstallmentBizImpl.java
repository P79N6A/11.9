package com.yeepay.g3.core.nccashier.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yeepay.g3.core.nccashier.biz.BankInstallmentBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.InstallmentPayTypeEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.CashierBankCardService;
import com.yeepay.g3.core.nccashier.service.InstallmentService;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.SignCardService;
import com.yeepay.g3.core.nccashier.vo.BankInfo;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.core.nccashier.vo.InstallmentBankInfo;
import com.yeepay.g3.core.nccashier.vo.InstallmentInfoNeeded;
import com.yeepay.g3.core.nccashier.vo.InstallmentRateInfo;
import com.yeepay.g3.core.nccashier.vo.SignRelationInfo;
import com.yeepay.g3.core.nccashier.vo.UrlInfo;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CardNoOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentBankInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentBanksResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentSmsRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationIdOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;

@Component("bankInstallmentBiz")
public class BankInstallmentBizImpl extends NcCashierBaseBizImpl implements BankInstallmentBiz {

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private SignCardService signCardService;

	@Resource
	private InstallmentService installmentService;

	@Resource
	private CashierBankCardService cashierBankCardService;

	@Resource
	private OrderPaymentService orderPaymentService;

	@Override
	public InstallmentRouteResponseDTO routePayWay(long requestId) {
		InstallmentRouteResponseDTO responseDTO = new InstallmentRouteResponseDTO();
		try {
			// 校验入参
			validateRoutePayWayParam(requestId);
			// 订单校验
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestId);
			// 获取签约卡列表
			filterSignCardList(paymentRequest, responseDTO);
		} catch (Throwable t) {
			handleException(responseDTO, t);
		}
		return responseDTO;
	}

	private void filterSignCardList(PaymentRequest paymentRequest, InstallmentRouteResponseDTO response) {
		List<SignRelationInfo> signRelationList = installmentService.getSignCardList(paymentRequest);
		InstallmentBanksResponseDTO bankList = cashierBankCardService
				.filterSupportInstallmentBankList(paymentRequest.getMerchantNo(), paymentRequest.getOrderAmount());
		response.setUsableBankList(bankList.getUsableBankList());
		if (CollectionUtils.isNotEmpty(signRelationList)) {
			List<InstallmentBankInfoDTO> allBankList = new ArrayList<InstallmentBankInfoDTO>();
			if (CollectionUtils.isNotEmpty(bankList.getUsableBankList())) {
				allBankList.addAll(bankList.getUsableBankList());
			}
			if (CollectionUtils.isNotEmpty(bankList.getUnusableBankList())) {
				allBankList.addAll(bankList.getUnusableBankList());
			}
			List<SignRelationDTO> usableSignRelationList = new ArrayList<SignRelationDTO>();
			List<SignRelationDTO> unusableSignRelationList = new ArrayList<SignRelationDTO>();
			for (SignRelationInfo signRelation : signRelationList) {
				SignRelationDTO signRelationDTO = signRelation.transferToSignRelationDTO();
				boolean bankOfSignRelationSupport = false;
				for (InstallmentBankInfoDTO bank : allBankList) {
					if (bank.getBank().getBankCode()
							.equals(signRelation.getSignCardInfo().getCardInfo().getBank().getBankCode())) {
						bankOfSignRelationSupport = true;
						if ("UNUSABLE".equals(bank.getStatus())) {
							signRelationDTO.setRemark(bank.getRemark());
							unusableSignRelationList.add(signRelationDTO);
						} else {
							signRelationDTO.setNumsAndRates(bank.getNumsAndRates());
							usableSignRelationList.add(signRelationDTO);
						}
						break;
					}
				}
				if (!bankOfSignRelationSupport) {
					signRelationDTO.setRemark("不支持此卡");
					unusableSignRelationList.add(signRelationDTO);
				}
			}
			response.setUsableSignRelationList(usableSignRelationList);
			response.setUnusableSignRelationList(unusableSignRelationList);
		}
	}

	/**
	 * 校验银行卡分期入参，并将paymentRequestId加到threadLocal中
	 * 
	 * @param requestId
	 */
	private void validateRoutePayWayParam(long requestId) {
		if (requestId <= 0) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "requestId未传");
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[路由|routerPayWay]—[requestId=" + requestId + "]");
	}

	@Override
	public InstallmentFeeInfoResponseDTO getInstallmentFeeInfo(InstallmentFeeInfoRequestDTO requestDTO) {
		InstallmentFeeInfoResponseDTO response = new InstallmentFeeInfoResponseDTO();
		try {
			validateInstallmentFeeInfoRequestDTO(requestDTO);
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			installmentService.calculateInstallmentAmount(paymentRequest, requestDTO, response);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	private void validateInstallmentFeeInfoRequestDTO(InstallmentFeeInfoRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "请求参数为空");
		}
		requestDTO.validate();
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[获取手续费信息|getInstallmentFeeInfo]—[requestId=" + requestDTO.getRequestId() + "]");
	}

	@Override
	public CardNoOrderResponseDTO orderByCardNo(CardNoOrderRequestDTO cardNoOrderRequestDTO) {
		CardNoOrderResponseDTO response = new CardNoOrderResponseDTO();
		try {
			validateCardNoOrderRequestDTO(cardNoOrderRequestDTO);
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(cardNoOrderRequestDTO.getRequestId());
			InstallmentBankInfo currentBankInfo = validateCardNo(cardNoOrderRequestDTO, paymentRequest);
			InstallmentInfoNeeded request = createRecordWhenUnexsit(cardNoOrderRequestDTO, paymentRequest,
					currentBankInfo);
			order(request, response);
		} catch (Throwable t) {
			if (t instanceof CashierBusinessException) {
				if (Errors.REPEAT_ORDER.getCode().equals(((CashierBusinessException) t).getDefineCode())) {
					return response;
				}
			}
			handleException(response, t);
		}
		return response;
	}

	private void order(InstallmentInfoNeeded request, CardNoOrderResponseDTO response) {
		if (InstallmentPayTypeEnum.FIRST.equals(request.getPayType())) {
			UrlInfo urlInfo = installmentService.openAndPay(request);
			response.setUrlInfo(urlInfo.buildUrlInfoDTO());
		} else {
			installmentService.signedCardOrder(request);
		}
	}

	private InstallmentInfoNeeded createRecordWhenUnexsit(CardNoOrderRequestDTO cardNoOrderRequestDTO,
			PaymentRequest paymentRequest, InstallmentBankInfo currentBankInfo) {
		InstallmentInfoNeeded request = new InstallmentInfoNeeded();
		request.setToken(cardNoOrderRequestDTO.getTokenId());
		request.setCurrentBankInfo(currentBankInfo);
		installmentService.judgeSignStatusOfCardNo(cardNoOrderRequestDTO.getCardNo(), paymentRequest.getCashierUser(),
				request);
		PaymentRecord paymentRecord = installmentService.getRecordToOrder(cardNoOrderRequestDTO.getCardNo(), request,
				paymentRequest);
		request.setPaymentRecord(paymentRecord);
		request.setPaymentRequest(paymentRequest);

		return request;
	}

	private InstallmentBankInfo validateCardNo(CardNoOrderRequestDTO cardNoOrderRequestDTO,
			PaymentRequest paymentRequest) {
		CardInfo cardBin = cashierBankCardService.getNonNullCardBin(cardNoOrderRequestDTO.getCardNo());
		if (!cardBin.getBank().getBankCode().equals(cardNoOrderRequestDTO.getBankCode())) {
			throw new CashierBusinessException(Errors.BANK_AND_CARD_INFO_MATCH);
		}
		List<InstallmentBankInfo> bankList = cashierBankCardService.getSupportBankList(paymentRequest.getMerchantNo());
		for (InstallmentBankInfo bank : bankList) {
			if (bank.getBankInfo().getBankCode().equals(cardBin.getBank().getBankCode())) {
				if (paymentRequest.getOrderAmount().compareTo(new BigDecimal(bank.getBankInfo().getMinLimit())) < 0) {
					throw new CashierBusinessException(Errors.LESS_THAN_MIN_LIMIT);
				}
				bank.judgeTargetRateInfoByPeriod(cardNoOrderRequestDTO.getPeriod());
				return bank;
			}
		}
		throw new CashierBusinessException(Errors.SUPPORT_BANK_FAILED);

	}

	private void validateCardNoOrderRequestDTO(CardNoOrderRequestDTO cardNoOrderRequestDTO) {
		if (cardNoOrderRequestDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "请求参数为空");
		}
		cardNoOrderRequestDTO.validate();
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[卡号下单|orderByCardNo]—[requestId=" + cardNoOrderRequestDTO.getRequestId() + "]");

	}

	@Override
	public BasicResponseDTO orderBySignRelationId(SignRelationIdOrderRequestDTO requestDTO) {
		BasicResponseDTO response = new BasicResponseDTO();
		try {
			validateSignRelationIdOrderRequestDTO(requestDTO);
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			InstallmentInfoNeeded request = validateSignRelationId(requestDTO, paymentRequest);
			getOrCreatePaymentRecord(request, paymentRequest, requestDTO.getTokenId());
			installmentService.signedCardOrder(request);
		} catch (Throwable t) {
			if (t instanceof CashierBusinessException) {
				if (Errors.REPEAT_ORDER.getCode().equals(((CashierBusinessException) t).getDefineCode())) {
					return response;
				}
			}
			handleException(response, t);
		}
		return response;
	}

	private void getOrCreatePaymentRecord(InstallmentInfoNeeded request, PaymentRequest paymentRequest, String token) {
		PaymentRecord paymentRecord = installmentService.getRecordToOrder(null, request, paymentRequest);
		request.setPaymentRecord(paymentRecord);
		request.setPaymentRequest(paymentRequest);
	}

	private InstallmentInfoNeeded validateSignRelationId(SignRelationIdOrderRequestDTO requestDTO,
			PaymentRequest paymentRequest) {
		InstallmentInfoNeeded request = new InstallmentInfoNeeded();
		request.setToken(requestDTO.getTokenId());
		String bankCode = installmentService.isSignRelationIdIllegal(requestDTO.getSignRelationId() + "",
				paymentRequest.getCashierUser(), request);
		InstallmentRateInfo targetInstallmentRateInfo = isBankAndPeriodLegal(bankCode, requestDTO.getBankCode(),
				requestDTO.getPeriod(), paymentRequest);
		InstallmentBankInfo installmentBankInfo = new InstallmentBankInfo();
		BankInfo bank = new BankInfo();
		bank.setBankCode(bankCode);
		installmentBankInfo.setBankInfo(bank);
		installmentBankInfo.setTargetInstallmentRateInfo(targetInstallmentRateInfo);
		request.setCurrentBankInfo(installmentBankInfo);
		return request;
	}

	private InstallmentRateInfo isBankAndPeriodLegal(String targetBankCode, String oriBankCode, String period,
			PaymentRequest paymentRequest) {
		if (!targetBankCode.equals(oriBankCode)) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION);
		}
		List<InstallmentBankInfo> bankList = cashierBankCardService.getSupportBankList(paymentRequest.getMerchantNo());
		for (InstallmentBankInfo bank : bankList) {
			if (bank.getBankInfo().getBankCode().equals(targetBankCode)) {
				if (paymentRequest.getOrderAmount().compareTo(new BigDecimal(bank.getBankInfo().getMinLimit())) < 0) {
					throw new CashierBusinessException(Errors.LESS_THAN_MIN_LIMIT);
				}
				bank.judgeTargetRateInfoByPeriod(period);
				return bank.getTargetInstallmentRateInfo();
			}
		}
		throw new CashierBusinessException(Errors.SUPPORT_BANK_FAILED);
	}

	private void validateSignRelationIdOrderRequestDTO(SignRelationIdOrderRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "请求参数为空");
		}
		requestDTO.validate();
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[签约关系ID下单|orderBySignRelationId]—[requestId=" + requestDTO.getRequestId() + "]");
	}

	@Override
	public BasicResponseDTO sendSms(InstallmentSmsRequestDTO requestDTO) {
		BasicResponseDTO response = new BasicResponseDTO();
		try {
			validateSendSmsRequestDTO(requestDTO);
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			PaymentRecord paymentRecord = installmentService.exsitValidRecordToSms(paymentRequest,
					requestDTO.getRecordId() + "");
			CombinedPaymentDTO combinedPaymentDTO = orderPaymentService.buildCombinedPaymentDTO(paymentRequest,
					paymentRecord);
			installmentService.smsSend(combinedPaymentDTO);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	private void validateSendSmsRequestDTO(InstallmentSmsRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "请求参数为空");
		}
		requestDTO.validate();
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[sendSms],支付请求ID=" + requestDTO.getRequestId() + ",支付记录ID=" + requestDTO.getRecordId() + "]");
	}

	@Override
	public BasicResponseDTO confirmPay(InstallmentConfirmRequestDTO requestDTO) {
		BasicResponseDTO response = new BasicResponseDTO();
		PaymentRecord paymentRecord = null;
		try {
			validateInstallmentConfirmRequestDTO(requestDTO);
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			paymentRecord = installmentService.getRecordToConfirm(requestDTO.getRecordId() + "", paymentRequest);
			installmentService.comfirmPay(paymentRecord, requestDTO.getVerifyCode());
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}


	private void validateInstallmentConfirmRequestDTO(InstallmentConfirmRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), "请求参数为空");
		}
		requestDTO.validate();
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[confirmPay],支付请求ID=" + requestDTO.getRequestId() + ",支付记录ID=" + requestDTO.getRecordId() + "]");
	}

}
