package com.yeepay.g3.core.nccashier.gateway.service.impl;

import javax.ws.rs.HttpMethod;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.NcPayService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.IntelligentNetResultFrontCallbackResInfo;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.cwh.param.ExternalUserDTO;
import com.yeepay.g3.facade.nccashier.dto.CardBinResDTO;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayQueryRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayResultTaskRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.QueryPreRouteRedirectRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.QueryPreRouteRedirectResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.RequestPaymentParam;
import com.yeepay.g3.facade.ncpay.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.SmsSendRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.SmsSendResponseDTO;
import com.yeepay.g3.facade.ncpay.exception.PaymentException;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * Created by xiewei on 15-10-20.
 */
@Service("ncPayService")
public class NcPayServiceImpl extends NcCashierBaseService implements NcPayService {
	protected static final Logger logger = NcCashierLoggerFactory.getLogger(NcPayServiceImpl.class);

	/**
	 * 获取外部用户
	 */
	@Override
	public ExternalUserDTO getExternalUser(String merchantAccount, String identityId,
			IdentityType identityType, String userName, String userIdentityNo)
					throws CashierBusinessException {
		ExternalUserDTO dto = externalUserCwhFacade.getUser(merchantAccount, identityId,
				identityType, userName, userIdentityNo);
		return dto;
	}

	/**
	 * 创建支付订单
	 */
	@Override
	public PaymentResponseDTO requestPayment(RequestPaymentParam param)
			throws CashierBusinessException, IllegalArgumentException {
		PaymentResponseDTO respDTO = null;
		try {
			respDTO = paymentManagerWrapperFacade.requestPayment(param);
		} catch (PaymentException e) {
			throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), e.getDefineCode(), e.getMessage());
		} catch (Exception e) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (null == respDTO) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		return respDTO;
	}

	/**
	 * 发短验
	 */
	@Override
	public SmsSendResponseDTO verifyAndSendSms(SmsSendRequestDTO requestDTO) throws CashierBusinessException {

		SmsSendResponseDTO smsSendResponseDTO = null;
		try {
			smsSendResponseDTO = paymentManageFacade.verifyAndSendSms(requestDTO);
		} catch (PaymentException e) {
			logger.warn("verifyAndSendSms异常", e);
			throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), e.getDefineCode(), e.getMessage());
		} catch (Throwable e) {
			logger.error("verifyAndSendSms异常", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (smsSendResponseDTO == null) {
			throw CommonUtil.handleException(Errors.SMS_SEND_FRILED);
		}
		return smsSendResponseDTO;
	}

	/**
	 * 确认支付
	 */
	@Override
	public PayConfirmResponseDTO confirmPay(PayConfirmRequestDTO confirmDTO)
			throws CashierBusinessException {
		PayConfirmResponseDTO payConfirmResponseDTO = null;
		try {
			payConfirmResponseDTO = paymentManageFacade.confirmPay(confirmDTO);
		} catch (PaymentException e) {// PaymentException YeepayBizException
			throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), e.getDefineCode(), e.getMessage());
		} catch (Throwable e) {
			logger.error("confirmPay异常", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		return payConfirmResponseDTO;
	}

	/**
	 * 支付订单查询
	 */
	@Override
	public PayQueryResponseDTO queryPaymentOrder(String paymentRecordId)
			throws CashierBusinessException {
		PayQueryRequestDTO queryDTO = new PayQueryRequestDTO();
		queryDTO.setPayOrderId(paymentRecordId);
		PayQueryResponseDTO payQueryResponseDTO = null;
		try {
			payQueryResponseDTO = paymentManageFacade.queryPaymentOrder(queryDTO);
		} catch (PaymentException e) {
			throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), e.getDefineCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("queryPaymentOrder异常", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (payQueryResponseDTO == null) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		return payQueryResponseDTO;
	}

	@Override
	public ReverseResponseDTO reversePayOrder(ReverseRequestDTO reverseRequestDTO)
			throws CashierBusinessException {
		ReverseResponseDTO reverseResponseDTO = null;
		try {
			reverseResponseDTO = ncPayncReverseFacade.reversePayOrder(reverseRequestDTO);
		} catch (PaymentException e) {
			logger.warn("reversePayOrder异常", e);
			throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), e.getDefineCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("reversePayOrder异常", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (reverseResponseDTO == null) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		return reverseResponseDTO;
	}

	@Override
	public PayQueryResponseDTO supplementPaymentOrder(String paymentOrderNo)
			throws CashierBusinessException {
		PayQueryResponseDTO payQueryResponseDTO = null;
		try {
			payQueryResponseDTO = paymentManageFacade.supplementPaymentOrder(paymentOrderNo);
		} catch (PaymentException e) {
			logger.warn("supplementPaymentOrder异常", e);
			throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), e.getDefineCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("supplementPaymentOrder异常", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		return payQueryResponseDTO;
	}


	public CardBinResDTO getBankCardByCardNo(String cardNo) {
		try {
			CardBinDTO cardBinDTO = configCommonFacade.queryCardInfo(cardNo);

			if (cardBinDTO == null) {
				return null;
			} else {
				CardBinResDTO cardBinResDTO = new CardBinResDTO();
				cardBinResDTO.setBankCode(cardBinDTO.getBankEnum().name());
				cardBinResDTO.setBankName(cardBinDTO.getBankEnum().getName());
				cardBinResDTO.setCardType(cardBinDTO.getCardType().name());
				return cardBinResDTO;
			}

		} catch (Exception e) {
			logger.error("getBankCardByCardNo异常", e);
			return null;
		}
	}

	@Override
	public boolean updateTaskStatus(PayResultTaskRequestDTO payResultTaskRequestDTO) {
		boolean isUpdateSuccess = false;
		try {
			isUpdateSuccess = ncPayResultTaskFacade.updateTaskStatus(payResultTaskRequestDTO);
		} catch (Exception e) {
			logger.error("mq确认回调ncpay失败", e);
		}
		return isUpdateSuccess;
	}

	@Override
	public IntelligentNetResultFrontCallbackResInfo queryPreRouteRedirectInfo(String paymentNo) {
		QueryPreRouteRedirectRequestDTO requestDTO = new QueryPreRouteRedirectRequestDTO();
		requestDTO.setPaymentNo(paymentNo);
		QueryPreRouteRedirectResponseDTO response = null;
		try {
			response = paymentManageFacade.queryPreRouteRedirectInfo(requestDTO);
		} catch (PaymentException paymentException) {
			logger.warn("调用ncpay查询前端回调地址异常, paymentNo=" + paymentNo, paymentException);
			// 预路由支付订单状态处理中
			if ("500150".equals(paymentException.getDefineCode())) {
				throw new CashierBusinessException(Errors.ORDER_STATUS_UNKNOWN);
			} else {
				throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), paymentException.getDefineCode(),
						paymentException.getMessage());
			}
		}
		if (response != null) {
			return buildIntelligentNetResultFrontCallbackResInfo(response);
		}
		throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
	}

	
	
	private IntelligentNetResultFrontCallbackResInfo buildIntelligentNetResultFrontCallbackResInfo(
			QueryPreRouteRedirectResponseDTO response) {
		IntelligentNetResultFrontCallbackResInfo resInfo = new IntelligentNetResultFrontCallbackResInfo();
		resInfo.setPaymentNo(response.getPaymentNo());
		resInfo.setRedirectMehod(
				response.getRedirectMethod() == null ? HttpMethod.GET : response.getRedirectMethod().name());
		resInfo.setRedirectUrl(response.getRedirectUrl());
		resInfo.setBizOrderNo(response.getBizOrderNo());
		resInfo.setBizType(response.getBizType());
		resInfo.setConfirmTime(response.getConfirmTime());
		resInfo.setMerchantNo(response.getMerchantNo());
		resInfo.setOrderAmount(response.getOrderAmount());
		resInfo.setRealAmount(response.getRealAmount());
		resInfo.setRequestNo(response.getRequestNo());
		resInfo.setState(response.getState());
		return resInfo;
	}
}
