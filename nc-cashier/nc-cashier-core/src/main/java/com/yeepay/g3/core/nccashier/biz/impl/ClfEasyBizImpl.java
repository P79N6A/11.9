package com.yeepay.g3.core.nccashier.biz.impl;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-10-18 10:36
 **/

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.biz.ClfEasyBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.enumtype.SynTypeEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.ClfEasyService;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.core.nccashier.service.APICflEasyService;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APICflEasyConfirmPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardBinResDTO;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankDTO;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankInfo;
import com.yeepay.g3.facade.nccashier.dto.CflEasyBankReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentPeriodAndPaymentInfo;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyConfirmPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasyPreRouterRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.CflEasySupportBankRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.clfeasy.ClfEasyBaseRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PaymentProcessEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SMSSendTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-18 10:36
 **/

@Component
public class ClfEasyBizImpl extends NcCashierBaseBizImpl implements ClfEasyBiz {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ClfEasyBizImpl.class);

    @Autowired
    private APICflEasyService apiCflEasyService;
    
    @Autowired
    private ClfEasyService clfEasyService;
    
    @Autowired
    private PaymentRequestService paymentRequestService;

    @Autowired
    private OrderPaymentService orderPaymentService;
    
    private void smsSendValidateParam(ClfEasyBaseRequestDTO requestDTO){
        basicValidateParam(requestDTO);
        ((CflEasySmsSendRequestDTO)requestDTO).validate();
    }

    private void confirmPayValidateParam(ClfEasyBaseRequestDTO requestDTO){
        basicValidateParam(requestDTO);
        ((CflEasyConfirmPayRequestDTO)requestDTO).validate();
    }


    private void basicValidateParam(ClfEasyBaseRequestDTO requestDTO){
        if (requestDTO == null) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
                    Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
        }
    }


    @Override
    public BasicResponseDTO smsSend(CflEasySmsSendRequestDTO requestDTO) {
        BasicResponseDTO responseDTO = new BasicResponseDTO();
        try {
            //基本校验
            smsSendValidateParam(requestDTO);
            NcCashierLoggerFactory.TAG_LOCAL.set("[分期易|smsSend]—[requestId=" + requestDTO.getRequestId() + "]");
            // 获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
            // 获取paymentRecord
            PaymentRecord paymentRecord = clfEasyService.getPaymentRecord(paymentRequest, requestDTO.getRecordId().toString(), PayTool.ZF_FQY, OrderAction.SEND_SMS,null);
            //检查支付流程
            boolean needClearCardInfo = apiCflEasyService.paymentProcessInfoCheck(requestDTO.getCardInfoDTO(), paymentRecord, PaymentProcessEnum.PAYMENT_PROCESS_SEND_SMS.getValue(), null);
            if(needClearCardInfo){
                requestDTO.cleanCardInfo();
            }
            APIBasicResponseDTO apiBasicResponseDTO = apiCflEasyService.smsSend(requestDTO.getCardInfoDTO(), paymentRequest, paymentRecord);
            buildBaicResponseDTOfromApi(apiBasicResponseDTO,responseDTO);
        }catch (Throwable t){
            handleException(responseDTO, t);
        }
        return responseDTO;

    }



    @Override
    public BasicResponseDTO confirmPay(CflEasyConfirmPayRequestDTO requestDTO) {
        BasicResponseDTO responseDTO = new BasicResponseDTO();
        try {
            //基本校验
            confirmPayValidateParam(requestDTO);
            NcCashierLoggerFactory.TAG_LOCAL.set("[分期易|confirmPay]—[requestId=" + requestDTO.getRequestId() + "]");
            // 获取paymentRequest
            PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
            // 获取paymentRecord
            PaymentRecord paymentRecord = clfEasyService.getPaymentRecord(paymentRequest, requestDTO.getRecordId().toString(), PayTool.ZF_FQY, OrderAction.CONFIRM_PAY,null);
            //确认支付
			clfEasyService.confirmPay(requestDTO.getCardInfoDTO(),requestDTO.getVerifycode(), paymentRequest, paymentRecord, SynTypeEnum.ASYN);
        } catch (Throwable t) {
            handleException(responseDTO, t);
        }
        return responseDTO;
    }

	private void validateSupportCflEasyBankParam(CflEasySupportBankRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
	}
	
	@Override
	public CflEasyBankReponseDTO getSupportCflEasyBankInfo(CflEasySupportBankRequestDTO requestDTO) {
		CflEasyBankReponseDTO responseDTO = new CflEasyBankReponseDTO();
		try {
			validateSupportCflEasyBankParam(requestDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[分期易|supporyBank]—[requestId=" + requestDTO.getRequestId()
					+ ", token=" + requestDTO.getToken() + "]");
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			Map<String, String> cflEasyBankInfoMaps = CommonUtil.getCflEasyBankInfoMaps();
			if (MapUtils.isEmpty(cflEasyBankInfoMaps)) {
				throw new CashierBusinessException(Errors.QUERY_BANK_LIST_ERROR);
			}
			transferBankInfo(responseDTO, cflEasyBankInfoMaps, paymentRequest.getOrderAmount());
		} catch (Throwable t) {
			handleException(responseDTO, t);
		}
		return responseDTO;
	}

	private CflEasyBankInfo transformBank(String bankInfoString) {
		if (StringUtils.isBlank(bankInfoString)) {
			return null;
		}
		try {
			return JSONObject.parseObject(bankInfoString, CflEasyBankInfo.class);
		} catch (Throwable t) {
			LOGGER.error("解析银行卡列表信息异常", t);
		}
		return null;
	}
	
	private List<CflEasyBankInfo> transfromBankList(Map<String, String> cflEasyBankInfoMaps) {
		List<CflEasyBankInfo> cflEasyBankInfos = new ArrayList<CflEasyBankInfo>();
		for (Map.Entry<String, String> entry : cflEasyBankInfoMaps.entrySet()) {
			try {
				String bankInfoString = entry.getValue();
				CflEasyBankInfo cflEasyBankInfo = JSONObject.parseObject(bankInfoString, CflEasyBankInfo.class);
				cflEasyBankInfos.add(cflEasyBankInfo);
			} catch (Throwable t) {
				LOGGER.error("解析银行卡列表信息异常", t);
				continue;
			}
		}
		return cflEasyBankInfos;
	}

	private void transferBankInfo(CflEasyBankReponseDTO bankListResponse, Map<String, String> cflEasyBankInfoMaps,
			BigDecimal orderAmount) {
		List<CflEasyBankInfo> cflEasyBankInfos = transfromBankList(cflEasyBankInfoMaps);
		if (CollectionUtils.isEmpty(cflEasyBankInfos)) {
			throw new CashierBusinessException(Errors.QUERY_BANK_LIST_ERROR);
		}
		List<CflEasyBankDTO> cflEasyBankList = new ArrayList<CflEasyBankDTO>();
		for (CflEasyBankInfo cflEasyBankInfo : cflEasyBankInfos) {
			if (StringUtils.isNotBlank(cflEasyBankInfo.getLimitAmounts())) {
				String amountLimits[] = cflEasyBankInfo.getLimitAmounts().split(",");
				if (amountLimits != null && amountLimits.length > 0) {
					if (StringUtils.isNotBlank(amountLimits[0])
							&& new BigDecimal(amountLimits[0]).compareTo(orderAmount) > 0) {
						// 最低限额大于订单金额
						continue;
					}
					if (StringUtils.isNotBlank(amountLimits[1])
							&& new BigDecimal(amountLimits[1]).compareTo(orderAmount) < 0) {
						// 最大限额低于订单金额
						continue;
					}
				}
			}
			if (StringUtils.isNotBlank(cflEasyBankInfo.getSupportPeriods())) {
				String periods[] = cflEasyBankInfo.getSupportPeriods().split(",");
				if (periods == null || periods.length == 0) {
					// 所配的分期期数为空
					continue;
				}
				CflEasyBankDTO cflEasyBankDTO = new CflEasyBankDTO(cflEasyBankInfo.getBankCode(), cflEasyBankInfo.getBankName());
				List<InstallmentPeriodAndPaymentInfo> periodAndPaymentInfos = new ArrayList<InstallmentPeriodAndPaymentInfo>();
				for (String period : periods) {
					// TODO 向上取整
					BigDecimal terminalAmount = orderAmount.divide(new BigDecimal(period), 2, BigDecimal.ROUND_HALF_UP);
					InstallmentPeriodAndPaymentInfo installmentPeriodAndPaymentInfo = new InstallmentPeriodAndPaymentInfo();
					installmentPeriodAndPaymentInfo.setTerminalPayment(terminalAmount);
					installmentPeriodAndPaymentInfo.setFirstPayment(terminalAmount);
					installmentPeriodAndPaymentInfo.setPeriod(period);
					periodAndPaymentInfos.add(installmentPeriodAndPaymentInfo);
				}
				cflEasyBankDTO.setPeriodAndPaymentInfos(periodAndPaymentInfos);
				cflEasyBankList.add(cflEasyBankDTO);
			}
		}
		bankListResponse.setOrderAmount(orderAmount);
		bankListResponse.setCflEasyBankList(cflEasyBankList);
	}

	private void validateCflEasyOrderRequestDTO(CflEasyOrderRequestDTO requestDTO){
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
	}
	
	private void validateCflEasyPreRouterRequestDTO(CflEasyPreRouterRequestDTO requestDTO){
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
	}
	
	private CardInfo checkCardInfo(CardInfoDTO cardInfo, String period, PaymentRequest paymentRequest){
		CardInfo cardBin = checkCardInfo(cardInfo.getCardno(), cardInfo.getBankCode(), period, paymentRequest);
		cardInfo.setCardType(cardBin.getCardType().name());
		cardInfo.setBankName(cardBin.getBank().getBankName());
		// TODO 预路由的时候拿到的就不要这么校验了，到时候将预路由拿到的存储到缓存，或者也不校验，直接给pp
		cardInfo.cardItemIsFull();
		return cardBin;
	}
	
	private CardInfo checkCardInfo(String cardNo, String bankCode, 
			String period, PaymentRequest paymentRequest) {
		checkBankAndPeriod(paymentRequest.getOrderAmount(), bankCode, period);
		CardInfo cardBin = cashierBankCardService.getNonNullCardBin(cardNo);
		if (!cardBin.getBank().getBankCode().equals(bankCode)) {
			throw new CashierBusinessException(Errors.BANK_AND_CARD_INFO_MATCH);
		}
		return cardBin;
	}
	
	private boolean withinAmountLimit(String amountLimit, BigDecimal orderAmount, boolean throwE) {
		if (StringUtils.isNotBlank(amountLimit)) {
			String amountLimits[] = amountLimit.split(",");
			if (amountLimits != null && amountLimits.length > 0) {
				if (StringUtils.isNotBlank(amountLimits[0])
						&& new BigDecimal(amountLimits[0]).compareTo(orderAmount) > 0) {
					// 最低限额大于订单金额
					if (throwE) {
						throw new CashierBusinessException(Errors.LESS_THAN_MIN_LIMIT);
					} else {
						return false;
					}
				}
				if (StringUtils.isNotBlank(amountLimits[1])
						&& new BigDecimal(amountLimits[1]).compareTo(orderAmount) < 0) {
					// 最大限额低于订单金额
					if (throwE) {
						throw new CashierBusinessException(Errors.LARGER_THAN_MAX_LIMIT);
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void checkBankAndPeriod(BigDecimal orderAmount, String bankCode, String periodByUser) {
		Map<String, String> cflEasyBankInfoMaps = CommonUtil.getCflEasyBankInfoMaps();
		if (MapUtils.isEmpty(cflEasyBankInfoMaps)) {
			throw new CashierBusinessException(Errors.QUERY_BANK_LIST_ERROR);
		}
		// 校验银行
		CflEasyBankInfo cflEasyBankInfo = transformBank(cflEasyBankInfoMaps.get(bankCode));
		if (cflEasyBankInfo == null || StringUtils.isBlank(cflEasyBankInfo.getSupportPeriods())) {
			throw new CashierBusinessException(Errors.SUPPORT_BANK_FAILED);
		}
		// 校验期数
		String[] periodList = cflEasyBankInfo.getSupportPeriods().split(",");
		if (periodList == null || periodList.length == 0) {
			throw new CashierBusinessException(Errors.SUPPORT_BANK_FAILED);
		}
		for (String period : periodList) {
			if (StringUtils.isNotBlank(period) && period.equals(periodByUser)) {
				// 校验限额
				withinAmountLimit(cflEasyBankInfo.getLimitAmounts(), orderAmount, true);
				return;
			}
		}
		throw new CashierBusinessException(Errors.PERIOR_NOT_SUPPPORT);
	}
	
	@Override
	public CflEasyOrderResponseDTO preRouter(CflEasyPreRouterRequestDTO requestDTO){
		CflEasyOrderResponseDTO responseDTO = new CflEasyOrderResponseDTO();
		try {
			validateCflEasyPreRouterRequestDTO(requestDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[分期易|preRouter]—[requestId=" + requestDTO.getRequestId()
					+ ", token=" + requestDTO.getToken() + "]");
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			CardInfo cardInfo = checkCardInfo(requestDTO.getCardNo(), requestDTO.getBankCode(), requestDTO.getPeriod()+"", paymentRequest);
			// 调用pp预路由接口 TODO 
			preRouteResponse(responseDTO, requestDTO, needCardSupport(cardInfo), cardInfo, null);
		} catch (Throwable t) {
			handleException(responseDTO, t);
		}
		return responseDTO;
	}
	
	/**
	 * 首先先默认为五项必填 -- 后续+绑卡和预路由接口，来决策应该填哪些信息
	 * 
	 * @return
	 */
	private NeedSurportDTO needCardSupport(CardInfo cardInfo){
		NeedSurportDTO needSurportDTO = new NeedSurportDTO();
		if(CardTypeEnum.CREDIT == cardInfo.getCardType()){
			needSurportDTO.setAvlidDateIsNeed(true);
			needSurportDTO.setCvvIsNeed(true);
		}
		needSurportDTO.setIdnoIsNeed(true);
		needSurportDTO.setOwnerIsNeed(true);
		needSurportDTO.setPhoneNoIsNeed(true);
		return needSurportDTO;
	}
	
	@Override
	public CflEasyOrderResponseDTO order(CflEasyOrderRequestDTO requestDTO) {
		CflEasyOrderResponseDTO responseDTO = new CflEasyOrderResponseDTO();
		try {
			validateCflEasyOrderRequestDTO(requestDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[分期易|order]—[requestId=" + requestDTO.getRequestId()
					+ ", token=" + requestDTO.getToken() + "]");
			PaymentRequest paymentRequest = paymentRequestService
					.findPaymentRequestByRequestId(requestDTO.getRequestId());
			CardInfo cardInfo = checkCardInfo(requestDTO.getCardInfo(), requestDTO.getPeriod()+"", paymentRequest);
	        if (requestDTO.getCardInfo() != null) {
	        		// 保存临时卡
	        		long tmpId = orderPaymentService.addPayTmpCard(requestDTO.getCardInfo());
	            cardInfo.setTmpId(tmpId+"");
	        }
			PaymentRecord paymentRecord = clfEasyService.getRecordToOrder(cardInfo, requestDTO.getPeriod()+"", requestDTO.getToken(), paymentRequest);
			NeedSurportDTO needSurportDTO = clfEasyService.order(paymentRequest, paymentRecord, requestDTO.getCardInfo());
			orderResponse(responseDTO, requestDTO, needSurportDTO, cardInfo, paymentRecord.getSmsVerifyType());
		} catch (Throwable t) {
			handleException(responseDTO, t);
		}
		return responseDTO;
	}
	
	private void preRouteResponse(CflEasyOrderResponseDTO responseDTO, CflEasyPreRouterRequestDTO requestDTO,
			NeedSurportDTO needItem, CardInfo cardInfo, String smsType) {
		responseDTO.setNeedItem(needItem);
		if (StringUtils.isNotBlank(smsType) && !SMSSendTypeEnum.NONE.name().equals(smsType)) {
			responseDTO.setSmsType(SMSSendTypeEnum.YEEPAY.name());
		}
		CardBinResDTO cardBinRes = new CardBinResDTO();
		cardBinRes.setBankCode(requestDTO.getBankCode());
		cardBinRes.setBankName(cardInfo.getBank().getBankName());
		cardBinRes.setCardlater4(
				requestDTO.getCardNo().substring(
						requestDTO.getCardNo().length() - 4, 
						requestDTO.getCardNo().length()));
		cardBinRes.setCardType(cardInfo.getCardType() == null ? "" : cardInfo.getCardType().name());
		responseDTO.setCardBin(cardBinRes);
	}
	
	private void orderResponse(CflEasyOrderResponseDTO responseDTO, CflEasyOrderRequestDTO requestDTO,
			NeedSurportDTO needItem, CardInfo cardInfo, String smsType) {
		responseDTO.setNeedItem(needItem);
		if (StringUtils.isNotBlank(smsType) && !SMSSendTypeEnum.NONE.name().equals(smsType)) {
			responseDTO.setSmsType(SMSSendTypeEnum.YEEPAY.name());
		}
		CardBinResDTO cardBinRes = new CardBinResDTO();
		cardBinRes.setBankCode(requestDTO.getCardInfo().getBankCode());
		cardBinRes.setBankName(cardInfo.getBank().getBankName());
		cardBinRes.setCardlater4(
				requestDTO.getCardInfo().getCardno().substring(
						requestDTO.getCardInfo().getCardno().length() - 4, 
						requestDTO.getCardInfo().getCardno().length()));
		cardBinRes.setCardType(cardInfo.getCardType() == null ? "" : cardInfo.getCardType().name());
		responseDTO.setCardBin(cardBinRes);
	}
	
}
