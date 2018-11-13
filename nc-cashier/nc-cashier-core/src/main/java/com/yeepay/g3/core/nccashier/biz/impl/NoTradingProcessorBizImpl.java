package com.yeepay.g3.core.nccashier.biz.impl;

import java.util.Map;
import java.util.UUID;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.nccashier.biz.BindCardPayBiz;
import com.yeepay.g3.core.nccashier.biz.NoTradingProcessorBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.NoTradingProcessorService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.ExtendInfoFromPayRequest;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindConfirmResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPAuthBindSMSResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPCardBinResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryBindCardOpenStatusResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NOPQueryOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nop.dto.AuthBindCardConfirmRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardConfirmResponseDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardResponseDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardSmsRequestDTO;
import com.yeepay.g3.facade.nop.dto.AuthBindCardSmsResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryCardBinRequestDTO;
import com.yeepay.g3.facade.nop.dto.QueryCardbinResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryOrderRequestDTO;
import com.yeepay.g3.facade.nop.dto.QueryOrderResponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryProductStatusReponseDTO;
import com.yeepay.g3.facade.nop.dto.QueryProductStatusRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class NoTradingProcessorBizImpl extends NcCashierBaseBizImpl implements NoTradingProcessorBiz {

	private static final String MERCHANT_APP_KEY_PREFIX = "OPR:";
	@Autowired
	private NoTradingProcessorService nopService;

	
	
	@Autowired
	private BindCardPayBiz bindCardPayBiz;
	@Override
	public NOPCardBinResponseDTO getNopCardBinInfo(NOPCardBinRequestDTO cardBinRequestDTO) {
		NOPCardBinResponseDTO response = new NOPCardBinResponseDTO();
		try {
			// 参数校验
			BeanValidator.validate(cardBinRequestDTO);
			// 调用nop卡bin接口
			NcCashierLoggerFactory.TAG_LOCAL.set("[查询nop卡bin信息|getNopCardBinInfo],商编="+cardBinRequestDTO.getMerchantNo()+",卡号="+ HiddenCode.hiddenBankCardNO(cardBinRequestDTO.getCardNo()));
			QueryCardBinRequestDTO nopRequest = buidlNopCardBinInfoRequestDTO(cardBinRequestDTO);
			QueryCardbinResponseDTO nopResponse = nopService.queryCardBin(nopRequest);
			response.setBankCode(nopResponse.getBankCode());
			response.setBankName(nopResponse.getBankName());
			response.setCardType(nopResponse.getBankCardType());
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	private QueryCardBinRequestDTO buidlNopCardBinInfoRequestDTO(NOPCardBinRequestDTO cardBinRequestDTO) {
		QueryCardBinRequestDTO nopRequest = new QueryCardBinRequestDTO();
		// TODO 不确定这一块有没有问题
		nopRequest.setBankCardNo(cardBinRequestDTO.getCardNo());
		nopRequest.setMerchantNo(cardBinRequestDTO.getMerchantNo());
		nopRequest.setParentMerchantNo(cardBinRequestDTO.getMerchantNo());
		String biz = null;
		
		if (cardBinRequestDTO.getBindPayRequest()!= null && cardBinRequestDTO.getBindPayRequest().getTokenId() != null) {
			PaymentRequest payRequest = paymentRequestService
					.findPayRequestById(cardBinRequestDTO.getBindPayRequest().getRequestId());
			biz = payRequest.getOrderSysNo();
		} else {
			biz = (String) CommonUtil.getNcCashierBindCardBizConfig(cardBinRequestDTO.getBizType()).get("BIZTYPE_NCPAY");
		}
		nopRequest.setBiz(StringUtils.isNotBlank(biz) ? Long.valueOf(biz) : 20l);
		nopRequest.setCardType(StringUtils.isNotBlank(cardBinRequestDTO.getCardType()) ? cardBinRequestDTO.getCardType():"DC");
		return nopRequest;
	}

	@Override
	public NOPAuthBindResponseDTO authBindCardRequest(NOPAuthBindRequestDTO authBindRequestDTO) {
		NOPAuthBindResponseDTO response = new NOPAuthBindResponseDTO();
		try {
			// 参数校验
			BeanValidator.validate(authBindRequestDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[鉴权绑卡请求|authBindCardRequest],商编="+authBindRequestDTO.getMerchantNo()+",merchantFlowId="+ authBindRequestDTO.getMerchantFlowId());
			// 封装NOP鉴权绑卡请求接口入参
			AuthBindCardRequestDTO nopAuthBindCardRequest = buildNopAuthBindCardRequestDTO(authBindRequestDTO);
			// 调用nop鉴权绑卡请求接口
			AuthBindCardResponseDTO nopResponse = nopService.authBindCardRequest(nopAuthBindCardRequest);
			response.setMerchantNo(nopResponse.getMerchantNo());
			response.setMerchantFlowId(nopResponse.getMerchantFlowId());
			response.setBindId(nopResponse.getBindId());
			response.setNopOrderId(nopResponse.getNopOrderId());
			response.setRequestFlowId(nopAuthBindCardRequest.getRequestFlowId());
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	private AuthBindCardRequestDTO buildNopAuthBindCardRequestDTO(NOPAuthBindRequestDTO authBindRequestDTO) {
		AuthBindCardRequestDTO nopAuthBindCardRequest = new AuthBindCardRequestDTO();
		nopAuthBindCardRequest.setParentMerchantNo(authBindRequestDTO.getMerchantNo());
		nopAuthBindCardRequest.setMerchantNo(authBindRequestDTO.getMerchantNo());
		nopAuthBindCardRequest.setMerchantFlowId(authBindRequestDTO.getMerchantFlowId());
		nopAuthBindCardRequest.setAppKey(MERCHANT_APP_KEY_PREFIX+authBindRequestDTO.getMerchantNo());
		String biz = null;
		String bizCode = null;
		String bizFlowId = null;
		String bindType = null;
		if (authBindRequestDTO.getBindPayRequest() != null && authBindRequestDTO.getBindPayRequest().getTokenId() != null) {
			bindType = "CONSUME";
			// modify by meiling.zhuang: 绑卡支付调用nop，传从订单系统反查而得的营销产品码
			if(authBindRequestDTO.getBindPayRequest().getRequestId()==null || authBindRequestDTO.getBindPayRequest().getRequestId()==0){
				throw new CashierBusinessException(Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg()+", requestId为空");
			}
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(authBindRequestDTO.getBindPayRequest().getRequestId());
			ExtendInfoFromPayRequest extendInfo = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
			nopAuthBindCardRequest.setRetailProductCode(extendInfo.getSaleProductCode()); 
		} else {
			bindType = "AUTH";
			nopAuthBindCardRequest.setRetailProductCode("DSBZB"); 
		}
		bizFlowId = StringUtils.isNotBlank(authBindRequestDTO.getBizFlowId()) ? authBindRequestDTO.getBizFlowId() : UUID.randomUUID().toString();
		Map<String, Object> bizMap = CommonUtil.getNcCashierBindCardBizConfig(StringUtils.isBlank(authBindRequestDTO.getBizType())?"DS":authBindRequestDTO.getBizType());
		biz = (String) bizMap.get("BIZTYPE_NCAUTH");
		bizCode = (String) bizMap.get("BIZTYPE_NCPAY");
		nopAuthBindCardRequest.setBasicProductCode(
				CommonUtil.getBasicProductCode(Constant.BIND_CARD_BASIC_PRODUCT_CODE_KEY, biz));
//		nopAuthBindCardRequest.setRetailProductCode("DSBZB"); 
		nopAuthBindCardRequest.setBiz(biz);
		nopAuthBindCardRequest.setBizCode(Long.valueOf(bizCode));
		nopAuthBindCardRequest.setBizFlowId(bizFlowId);
		nopAuthBindCardRequest.setBindType(bindType);
		nopAuthBindCardRequest.setRequestSystem(Constant.NOP_REQUEST_SYSTEM_NC_CASHIER);
		// 请求方请求号，每次鉴权绑卡就重新生成
		nopAuthBindCardRequest.setRequestFlowId(UUID.randomUUID().toString());
		nopAuthBindCardRequest.setUserNo(authBindRequestDTO.getUserNo());
		nopAuthBindCardRequest.setUserType(authBindRequestDTO.getUserType());
		nopAuthBindCardRequest.setBankCardNo(authBindRequestDTO.getBankCardNo());
		nopAuthBindCardRequest.setUserName(authBindRequestDTO.getUserName());
		nopAuthBindCardRequest.setIdCardNo(authBindRequestDTO.getIdCardNo());
		nopAuthBindCardRequest.setPhone(authBindRequestDTO.getPhone());
		if (StringUtils.isNotBlank(authBindRequestDTO.getCvv2())
				&& StringUtils.isNotBlank(authBindRequestDTO.getValidthru())) {
			nopAuthBindCardRequest.setCvv2(authBindRequestDTO.getCvv2());
			nopAuthBindCardRequest.setValidthru(authBindRequestDTO.getValidthru());
			// CREDIT_SIX(63) 卡+cvv+有效期+手机号+身份证+姓名；
			nopAuthBindCardRequest.setAuthType("CREDIT_SIX");
		} else {
			// COMMON_FOUR(15) 普通4项 卡+身份证+姓名+手机号
			nopAuthBindCardRequest.setAuthType("COMMON_FOUR");
		}
		// 风控字段该如何上送
		//nopAuthBindCardRequest.setRiskParamExt("");
		// 订单有效时间
		// nopAuthBindCardRequest.setOrderValidate();
		nopAuthBindCardRequest.setIsSMS(true);
		nopAuthBindCardRequest.setSmsSender("BANK_YEEPAY");
		nopAuthBindCardRequest.setCardType(StringUtils.isNotBlank(authBindRequestDTO.getCardType()) ? authBindRequestDTO.getCardType() : "DC");
		nopAuthBindCardRequest.setBindCallBackUrl(authBindRequestDTO.getBindCallBackUrl());
		return nopAuthBindCardRequest;
	}

	@Override
	public NOPAuthBindConfirmResponseDTO authBindCardConfirm(NOPAuthBindConfirmRequestDTO authBindConfirmRequest) {
		NOPAuthBindConfirmResponseDTO response = new NOPAuthBindConfirmResponseDTO();
		try {
			// 参数校验
			BeanValidator.validate(authBindConfirmRequest);
			NcCashierLoggerFactory.TAG_LOCAL.set("[鉴权绑卡确认|authBindCardRequest],商编="+authBindConfirmRequest.getMerchantNo()+",merchantFlowId="+ authBindConfirmRequest.getMerchantFlowId());
			AuthBindCardConfirmRequestDTO nopAuthBindConfirmRequest = buildNopAuthBindCardConfirmRequestDTO(
					authBindConfirmRequest);
			AuthBindCardConfirmResponseDTO nopAuthBindConfirmResponse = nopService
					.authBindCardConfirm(nopAuthBindConfirmRequest);
			if (authBindConfirmRequest.getBindPayRequest() != null && authBindConfirmRequest.getBindPayRequest().getTokenId() != null) {
				if ("NOP00000".equals(nopAuthBindConfirmResponse.getCode()) 
				&& StringUtils.isNotBlank(nopAuthBindConfirmResponse.getBindId())) {
					FirstBindCardPayRequestDTO bindPayRequestDTO= new FirstBindCardPayRequestDTO();
					buildBindCardPayRequest(authBindConfirmRequest, nopAuthBindConfirmResponse, bindPayRequestDTO);
					FirstBindCardPayResponseDTO bindPayResponseDTO = bindCardPayBiz.firstPay(bindPayRequestDTO);
					if(bindPayResponseDTO != null && StringUtils.isNotBlank(bindPayResponseDTO.getReturnCode())){
						throw new CashierBusinessException(bindPayResponseDTO.getReturnCode(), bindPayResponseDTO.getReturnMsg());
					}
					response.setBindPayResponse(bindPayResponseDTO);
				}
			}
			response.setMerchantNo(nopAuthBindConfirmResponse.getMerchantNo());
			response.setMerchantFlowId(nopAuthBindConfirmResponse.getMerchantFlowId());
			response.setRequestFlowId(nopAuthBindConfirmResponse.getRequestFlowId());
			response.setNopOrderId(nopAuthBindConfirmResponse.getNopOrderId());
			response.setBindId(nopAuthBindConfirmResponse.getBindId());
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	private void buildBindCardPayRequest(NOPAuthBindConfirmRequestDTO authBindConfirmRequest,
			AuthBindCardConfirmResponseDTO nopAuthBindConfirmResponse, FirstBindCardPayRequestDTO bindPayRequestDTO) {
		bindPayRequestDTO.setBindId(Long.valueOf(nopAuthBindConfirmResponse.getBindId()));
		bindPayRequestDTO.setRequestId(authBindConfirmRequest.getBindPayRequest().getRequestId());
		bindPayRequestDTO.setTokenId(authBindConfirmRequest.getBindPayRequest().getTokenId());
		NeedBankCardDTO needBankCardDTO = new NeedBankCardDTO();
		needBankCardDTO.setOwner(authBindConfirmRequest.getUserName());
		needBankCardDTO.setIdno(authBindConfirmRequest.getIdCardNo());
		needBankCardDTO.setPhoneNo(authBindConfirmRequest.getPhone());
		needBankCardDTO.setCardno(authBindConfirmRequest.getBankCardNo());
		needBankCardDTO.setAvlidDate(authBindConfirmRequest.getValidthru());
		needBankCardDTO.setCvv(authBindConfirmRequest.getCvv2());
		bindPayRequestDTO.setNeedBankCardDTO(needBankCardDTO);
	}

	private AuthBindCardConfirmRequestDTO buildNopAuthBindCardConfirmRequestDTO(
			NOPAuthBindConfirmRequestDTO authBindConfirmRequest) {
		AuthBindCardConfirmRequestDTO nopAuthBindConfirmRequest = new AuthBindCardConfirmRequestDTO();
		nopAuthBindConfirmRequest.setParentMerchantNo(authBindConfirmRequest.getMerchantNo());
		nopAuthBindConfirmRequest.setMerchantNo(authBindConfirmRequest.getMerchantNo());
		nopAuthBindConfirmRequest.setSmsCode(authBindConfirmRequest.getSmsCode());
		nopAuthBindConfirmRequest.setMerchantFlowId(authBindConfirmRequest.getMerchantFlowId());
		nopAuthBindConfirmRequest.setRequestSystem(Constant.NOP_REQUEST_SYSTEM_NC_CASHIER);
		nopAuthBindConfirmRequest.setRequestFlowId(authBindConfirmRequest.getRequestFlowId());
		return nopAuthBindConfirmRequest;
	}

	@Override
	public NOPAuthBindSMSResponseDTO authBindCardReSendSMS(NOPAuthBindSMSRequestDTO authBindSMSRequest) {
		NOPAuthBindSMSResponseDTO response = new NOPAuthBindSMSResponseDTO();
		try {
			// 参数校验
			BeanValidator.validate(authBindSMSRequest);
			NcCashierLoggerFactory.TAG_LOCAL.set("[鉴权绑卡重发短验|authBindCardReSendSMS],商编="+authBindSMSRequest.getMerchantNo()+",merchantFlowId="+ authBindSMSRequest.getMerchantFlowId());
			AuthBindCardSmsRequestDTO nopRequest = new AuthBindCardSmsRequestDTO();
			nopRequest.setMerchantNo(authBindSMSRequest.getMerchantNo());
			nopRequest.setParentMerchantNo(authBindSMSRequest.getMerchantNo());
			nopRequest.setMerchantFlowId(authBindSMSRequest.getMerchantFlowId());
			nopRequest.setRequestSystem(Constant.NOP_REQUEST_SYSTEM_NC_CASHIER);
			nopRequest.setRequestFlowId(authBindSMSRequest.getRequestFlowId());
			AuthBindCardSmsResponseDTO nopResponse = nopService.authBindCardSms(nopRequest);
			response.setMerchantNo(nopResponse.getMerchantNo());
			response.setRequestFlowId(nopResponse.getRequestFlowId());
			response.setNopOrderId(nopResponse.getNopOrderId());
			response.setRequestFlowId(nopResponse.getRequestFlowId());
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	@Override
	public NOPQueryOrderResponseDTO queryNopOrderStatus(NOPQueryOrderRequestDTO queryOrderRequest) {
		NOPQueryOrderResponseDTO response = new NOPQueryOrderResponseDTO();
		try {
			// 参数校验
			BeanValidator.validate(queryOrderRequest);
			NcCashierLoggerFactory.TAG_LOCAL.set("[查询nop订单状态|queryNopOrderStatus],商编="+queryOrderRequest.getMerchantNo()+",merchantFlowId="+ queryOrderRequest.getMerchantFlowId());
			QueryOrderRequestDTO nopRequest = new QueryOrderRequestDTO();
			nopRequest.setMerchantNo(queryOrderRequest.getMerchantNo());
			nopRequest.setMerchantFlowId(queryOrderRequest.getMerchantFlowId());
			nopRequest.setRequestSystem(Constant.NOP_REQUEST_SYSTEM_NC_CASHIER);
			nopRequest.setRequestFlowId(queryOrderRequest.getRequestFlowId());
			QueryOrderResponseDTO nopResponse = nopService.queryOrder(nopRequest);
			response.setOrderStatus(nopResponse.getOrderStatus());
			response.setMerchantNo(nopResponse.getMerchantNo());
			response.setMerchantFlowId(nopRequest.getMerchantFlowId());
			response.setRequestFlowId(nopResponse.getRequestFlowId());
			response.setNopOrderId(nopResponse.getNopOrderId());
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	@Override
	public NOPQueryBindCardOpenStatusResponseDTO getNopBindCardOpenStatus(String merchantNo) {
		NOPQueryBindCardOpenStatusResponseDTO response = new NOPQueryBindCardOpenStatusResponseDTO();
		try {
			QueryProductStatusRequestDTO nopRequest = new QueryProductStatusRequestDTO();
			nopRequest.setParentMerchantNo(merchantNo);
			nopRequest.setMerchantNo(merchantNo);
			QueryProductStatusReponseDTO nopResponse = nopService.queryProductStatus(nopRequest);
			// ENABLE 开通
			// DISABLE 未开通
			response.setStatus(nopResponse.getProductStatus());
			response.setMerchantNo(merchantNo);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	private String getBizSystem(String orderSysNo) {
		String bizCode = null;
		Map<String, String> switchSamePerson = CommonUtil.getSysConfigFrom3G(Constant.CARDINFO_LIMIT_BIZ + orderSysNo);
		if (MapUtils.isNotEmpty(switchSamePerson)) {
			bizCode = switchSamePerson.get(orderSysNo);
			if (StringUtils.isBlank(bizCode)) {
				logger.info("绑卡时业务方编码未配置");
			}
		}
		return bizCode;
	}
}
