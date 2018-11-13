package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.bankcommunicate.nocard.enums.CardTypeEnum;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.enumtype.OrderSystemPreauthStatusEnum;
import com.yeepay.g3.core.nccashier.enumtype.PreauthCancelType;
import com.yeepay.g3.core.nccashier.enumtype.PreauthPayTypeEnum;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.cwh.enumtype.BankCardType;
import com.yeepay.g3.facade.cwh.enumtype.BindCardStatus;
import com.yeepay.g3.facade.cwh.enumtype.IdcardType;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.cwh.param.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteResDTO;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.dto.BasicResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;

@Service("preauthService")
public class PreauthServiceImpl extends NcCashierBaseService implements PreauthService {

	private static Logger logger = LoggerFactory.getLogger(PreauthServiceImpl.class);

	@Resource
	private CwhService cwhService;

	@Resource
	private BankCardLimitInfoService bankCardLimitInfoService;

	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private PayProcessorService payProcessorService;

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private CashierBindCardService cashierBindCardService;

	@Resource
	private MerchantVerificationService merchantVerificationService;

	@Autowired
	private APICashierYJZFService apiCashierYJZFService;

	@Autowired
	private CashierBankCardService cashierBankCardService;

	@Resource
	private NewOrderHandleService newOrderHandleService;

	/** 一键支付的三级产品信息 */
	private static ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.YSQ, PayTypeEnum.YSQ);

	/**
	 * 构建预授权完成、撤销、完成撤销的record比较条件实体
	 * 
	 * @param payOrderId
	 * @return
	 */
	private RecordCondition buildRecordCondition(String payOrderId) {
		RecordCondition compareCondition = new RecordCondition();
		compareCondition.setPayOrderId(payOrderId);
		compareCondition.setValidateStatus(false);
		return compareCondition;
	}

	/**
	 * 获取paymentRequest和paymentRecord
	 * 
	 * @param orderInfo
	 * @param payOrderId
	 * @return
	 */
	private CombinedPaymentDTO getPayRequestAndRecord(OrderDetailInfoModel orderInfo, String payOrderId) {
		// 查询paymentRequest
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);
		// 查找paymentRecord
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(paymentRequest.getTradeSysOrderId(),
				paymentRequest.getTradeSysNo(), buildRecordCondition(payOrderId));
		CombinedPaymentDTO combinedPaymentDTO = new CombinedPaymentDTO();
		combinedPaymentDTO.setPaymentRecord(paymentRecord);
		combinedPaymentDTO.setPaymentRequest(paymentRequest);
		return combinedPaymentDTO;
	}

	@Override
	public void preauthCancel(APIPreauthCancelRequestDTO requestDTO, APIPreauthResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo) {
		CombinedPaymentDTO combinedPaymentDTO = buildCombinedPaymentDTO(requestDTO.getPayOrderId(), responseDTO,
				orderInfo);
		String paymentOrderStatus = preauthCancelByCancelType(combinedPaymentDTO, PreauthCancelType.PREAUTHCANCEL);
		responseDTO.setStatus(paymentOrderStatus);
	}

	/**
	 * 调用下游支付系统完成预授权撤销、完成撤销，并返回支付系统支付子订单状态
	 * 
	 * @param combinedPaymentDTO
	 * @param cancelType
	 * @return
	 */
	private String preauthCancelByCancelType(CombinedPaymentDTO combinedPaymentDTO, PreauthCancelType cancelType) {
		PreAuthCancelRequestDTO cancelRequestDTO = buildPreAuthCancelRequestDTO(combinedPaymentDTO.getPaymentRequest(),
				combinedPaymentDTO.getPaymentRecord(), cancelType);
		// trxStatus：支付子订单状态，DOING：正在处理中，FAILED：撤销失败，SUCCESS：撤销成功
		// 待确认：分别是什么样的情况下处于以上三者状态
		PreAuthCancelResponseDTO response = payProcessorService.preauthCancel(cancelRequestDTO);
		return response.getTrxStatus() == null ? null : response.getTrxStatus().name();
	}

	/**
	 * 构造预授权完成撤销/撤销入参
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 * @param cancelType
	 * @return
	 */
	private PreAuthCancelRequestDTO buildPreAuthCancelRequestDTO(PaymentRequest paymentRequest,
			PaymentRecord paymentRecord, PreauthCancelType cancelType) {
		PreAuthCancelRequestDTO cancelRequestDTO = new PreAuthCancelRequestDTO();
		String goodsInfo = buildTradeRiskInfoByRequest(paymentRequest);
		cancelRequestDTO.setGoodsInfo(goodsInfo);
		cancelRequestDTO.setCancelType(cancelType.name());
		cancelRequestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
		cancelRequestDTO.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
		cancelRequestDTO.setOutTradeNo(paymentRequest.getMerchantOrderId());
		cancelRequestDTO.setPayerCardNo(paymentRecord.getCardNo());
		return cancelRequestDTO;
	}

	@Override
	public void preauthComplete(APIPreauthCompleteRequestDTO completeRequestDTO,
			APIPreauthCompleteResponseDTO responseDTO, OrderDetailInfoModel orderInfo) {
		// 获取paymentRequest和paymentRecord
		CombinedPaymentDTO combinedPaymentDTO = getPayRequestAndRecord(orderInfo, completeRequestDTO.getPayOrderId());
		// 调用支付系统预授权完成接口
		PreAuthCompleteResponseDTO preAuthCompleteResponseDTO = preauthComplete(combinedPaymentDTO,
				completeRequestDTO.getAmount());
		supplyPreauthCompleteRespDTO(preAuthCompleteResponseDTO, responseDTO);
	}

	private void supplyPreauthCompleteRespDTO(PreAuthCompleteResponseDTO preAuthCompleteResponseDTO,
			APIPreauthCompleteResponseDTO responseDTO) {
		String paymentOrderStatus = (preAuthCompleteResponseDTO.getTrxStatus() == null ? null
				: preAuthCompleteResponseDTO.getTrxStatus().name());
		responseDTO.setStatus(paymentOrderStatus);
		responseDTO.setCompletedOrderId(preAuthCompleteResponseDTO.getRecordNo());
		responseDTO.setFrpCode(preAuthCompleteResponseDTO.getFrpCode());
		responseDTO.setBankOrderNO(preAuthCompleteResponseDTO.getBankOrderNO());
		responseDTO.setBankTrxId(preAuthCompleteResponseDTO.getBankTrxId());
		responseDTO.setCost(preAuthCompleteResponseDTO.getCost());
		responseDTO.setBankPaySuccDate(preAuthCompleteResponseDTO.getBankPaySuccDate());
	}

	private PreAuthCompleteResponseDTO preauthComplete(CombinedPaymentDTO combinedPaymentDTO, BigDecimal amount) {
		PreAuthCompleteRequestDTO completeRequestDTO = buildPreAuthCompleteRequestDTO(
				combinedPaymentDTO.getPaymentRequest(), combinedPaymentDTO.getPaymentRecord(), amount);
		PreAuthCompleteResponseDTO response = payProcessorService.preauthComplete(completeRequestDTO);
		return response;
	}

	private PreAuthCompleteRequestDTO buildPreAuthCompleteRequestDTO(PaymentRequest paymentRequest,
			PaymentRecord paymentRecord, BigDecimal amount) {
		PreAuthCompleteRequestDTO completeRequestDTO = new PreAuthCompleteRequestDTO();
		completeRequestDTO.setOutTradeNo(paymentRecord.getMerchantOrderId());
		completeRequestDTO.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
		completeRequestDTO.setAmount(amount);
		completeRequestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
		return completeRequestDTO;
	}

	private CombinedPaymentDTO buildCombinedPaymentDTO(String payOrderId, APIPreauthResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo) {
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);
		CombinedPaymentDTO combinedPaymentDTO = new CombinedPaymentDTO();
		combinedPaymentDTO.setPaymentRequest(paymentRequest);
		PaymentRecord paymentRecord = new PaymentRecord(); // PaymentRecord新建
		paymentRecord.setPaymentOrderNo(payOrderId);
		combinedPaymentDTO.setPaymentRecord(paymentRecord);
		return combinedPaymentDTO;
	}

	@Override
	public void preauthCompleteCancel(APIPreauthCompleteCancelRequestDTO completeCancelRequestDTO,
			APIPreauthResponseDTO responseDTO, OrderDetailInfoModel orderInfo) {
		// 获取paymentRequest和paymentRecord
		CombinedPaymentDTO combinedPaymentDTO = buildCombinedPaymentDTO(completeCancelRequestDTO.getPayOrderId(),
				responseDTO, orderInfo);
		combinedPaymentDTO.getPaymentRecord().setCardNo(completeCancelRequestDTO.getCardNo());
		// 调用支付系统预授权完成撤销接口
		String paymentOrderStatus = preauthCancelByCancelType(combinedPaymentDTO,
				PreauthCancelType.PREAUTHCONFIRMCANCEL);
		responseDTO.setStatus(paymentOrderStatus);
	}

	
	@Override
	public void preAuthFirstRequestAPI(APIPreauthFirstRequestDTO requestDTO, APIPreauthPaymentResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo) {

		// 产品开通校验
		MerchantInNetConfigResult merchantInNetConfig = getMerchantInNetConfigResult(orderInfo);

		// 同人校验
		ValidateSamePersionLimitDTO limitDTO = getValidateSamePersionLimitDTO(requestDTO);
		apiCashierYJZFService.validateSamePersionLimit(limitDTO, orderInfo);

		// 获取外部用户
		CashierUserInfo cashierUserInfo = apiCashierYJZFService.buildMemberUser(requestDTO.getUserNo(),
				requestDTO.getUserType(), orderInfo.getMerchantAccountCode());
		PaymentRequestExtInfo paymentRequestExtInfo = getPaymentRequestExtInfo(requestDTO.getUserIp(), null, orderInfo,
				merchantInNetConfig, cashierUserInfo);

		// 创建或获取paymentRequest
		PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig,
				productLevel, paymentRequestExtInfo);

		// 只允许预授权下单
		if (!paymentRequestExtInfo.getPayScene().equals(paymentRequest.getBizModeCode())) {
			throw new CashierBusinessException(Errors.INSTALLMENT_PAY_EXCEPTION);
		}

		// 下单时调用此方法，需要获取卡类型、银行编码、银行名称
		CardInfo cardInfo = cashierBankCardService.getNonNullCardBin(requestDTO.getCardNo());

		CashierPaymentRequestDTO paymentRequestDto = new CashierPaymentRequestDTO();
		CardInfoDTO cardInfoDTO = requestDTO.getCardInfoDTO();
		cardInfoDTO.setCardType(cardInfo.getCardType().name());
		cardInfoDTO.setBankCode(cardInfo.getBank().getBankCode());
		cardInfoDTO.setBankName(cardInfo.getBank().getBankName());

		paymentRequestDto.setCardInfo(cardInfoDTO);
		paymentRequestDto.setOrderType(NCCashierOrderTypeEnum.FIRST);
		
		// 创建或获取paymentRecord
		PaymentRecord paymentRecord = getPaymentRecord4FirstAPI(requestDTO, cashierUserInfo, paymentRequest,
				paymentRequestDto);
		// 下单接口
		PaymentResponseDTO paymentResponseDTO = this.preAuthOrderRequest(paymentRequest, paymentRecord,
				paymentRequestDto);
		handlerSMSTypeAndNeedItem(paymentResponseDTO.getSmsType(), paymentResponseDTO.getNeedItem(), responseDTO);
	
		
		responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
		responseDTO.setPaymentRecordNo(paymentResponseDTO.getPayOrderId());
	}

	private ValidateSamePersionLimitDTO getValidateSamePersionLimitDTO(APIPreauthFirstRequestDTO requestDTO) {
		ValidateSamePersionLimitDTO limitDTO = new ValidateSamePersionLimitDTO();
		limitDTO.setIdNo(requestDTO.getIdNo());
		limitDTO.setOwner(requestDTO.getOwner());
		limitDTO.setUserNo(requestDTO.getUserNo());
		limitDTO.setUserType(requestDTO.getUserType());
		return limitDTO;
	}

	private MerchantInNetConfigResult getMerchantInNetConfigResult(OrderDetailInfoModel orderInfo) {
		VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
		requestParam.setMerchantNo(orderInfo.getMerchantAccountCode());
		requestParam.setProductLevel(productLevel);
		requestParam.setTransactionType(orderInfo.getTransactionType());
		return merchantVerificationService.verifyMerchantAuthority(requestParam);
	}

	private PaymentRecord getPaymentRecord4FirstAPI(APIPreauthFirstRequestDTO requestDTO,
			CashierUserInfo cashierUserInfo, PaymentRequest paymentRequest,
			CashierPaymentRequestDTO paymentRequestDto) {
		// 保存临时卡
		long tmpCardId = saveTmpCard(null, paymentRequestDto.getCardInfo(), paymentRequest, true);
		// 构建用户信息
		PersonHoldCard personHoldCard = buildPersonHoldCard(paymentRequestDto);
		// 构建condition
		RecordCondition condition = buildRecordCondition(requestDTO.getCardNo(), OrderAction.YSQ_ORDER,
				CashierVersionEnum.valueOf(paymentRequest.getCashierVersion()), null, NCCashierOrderTypeEnum.FIRST, 0L);
		// 创建paymentRecord
		PaymentRecord paymentRecord = paymentProcessService.createPaymentRecord(paymentRequest, condition,
				personHoldCard, CardInfoTypeEnum.TEMP.name(),
				cashierUserInfo != null ? cashierUserInfo.getExternalUserId() : null, null, String.valueOf(tmpCardId),
				null);

		paymentRequestDto.setRequestId(paymentRequest.getId());
		paymentRequestDto.setRecordId(paymentRecord.getId());
		return paymentRecord;
	}

	private PaymentRequestExtInfo getPaymentRequestExtInfo(String userIp, String bindId, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig, CashierUserInfo cashierUserInfo) {
		PaymentRequestExtInfo paymentRequestExtInfo = new PaymentRequestExtInfo();
		paymentRequestExtInfo.setAppId(orderInfo.getAppId());
		paymentRequestExtInfo.setCashierUser(cashierUserInfo);
		paymentRequestExtInfo.setMcc(merchantInNetConfig.getMcc());
		String payScene = merchantInNetConfig.getPaymentSceneMap().get(PayTool.YSQ);
		paymentRequestExtInfo.setPayScene(payScene);// YSQ-USUAL
		paymentRequestExtInfo.setUserIp(userIp);
		paymentRequestExtInfo.setBindId(bindId);
		return paymentRequestExtInfo;
	}

	@Override
	public void preAuthBindRequestAPI(APIPreauthBindRequestDTO requestDTO, APIPreauthPaymentResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo) {
		// 产品开通校验
		MerchantInNetConfigResult merchantInNetConfig = getMerchantInNetConfigResult(orderInfo);

		// 获取外部用户
		CashierUserInfo cashierUserInfo = apiCashierYJZFService.buildMemberUser(requestDTO.getUserNo(),
				requestDTO.getUserType(), requestDTO.getMerchantNo());
		PaymentRequestExtInfo paymentRequestExtInfo = getPaymentRequestExtInfo(requestDTO.getUserIp(),
				requestDTO.getBindId(), orderInfo, merchantInNetConfig, cashierUserInfo);

		// 创建或获取paymentRequest
		PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantInNetConfig,
				productLevel, paymentRequestExtInfo);

		// 只允许预授权下单
		if (!paymentRequestExtInfo.getPayScene().equals(paymentRequest.getBizModeCode())) {
			throw new CashierBusinessException(Errors.INSTALLMENT_PAY_EXCEPTION);
		}

		CashierPaymentRequestDTO paymentRequestDto = new CashierPaymentRequestDTO();
		paymentRequestDto.setOrderType(NCCashierOrderTypeEnum.BIND);
		paymentRequestDto.setBindId(Long.valueOf(requestDTO.getBindId()));
		
		PaymentRecord paymentRecord = this.getRecord4PreauthBindRequest(paymentRequest, paymentRequestDto, false);

		PaymentResponseDTO paymentResponseDTO = this.preAuthOrderRequest(paymentRequest, paymentRecord,
				paymentRequestDto);

		handlerSMSTypeAndNeedItem(paymentResponseDTO.getSmsType(), paymentResponseDTO.getNeedItem(), responseDTO);
		responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
		responseDTO.setPaymentRecordNo(paymentResponseDTO.getPayOrderId());
	}

	@Override
	public void preauthSmsSendAPI(APIPreauthSmsSendRequestDTO requestDTO, APIBasicResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo) {

		Long tmpCardId = 0L;
		// 查询支付请求
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);

		// 查询支付记录 做校验
		PaymentRecord paymentRecord = getRecord4PreauthSendSms(paymentRequest, requestDTO.getPaymentRecordNo());

		boolean needClearCardInfo = apiCashierYJZFService.paymentProcessInfoCheck(requestDTO.getCardInfoDTO(),
				paymentRecord, PaymentProcessEnum.PAYMENT_PROCESS_SEND_SMS.getValue(), null);
		if (needClearCardInfo) {
			requestDTO.cleanCardInfo();
		}

		// 存在补充项保存临时卡信息
		CardInfoDTO cardInfoDTO = requestDTO.getCardInfoDTO();
		if (paymentRecord.getNeedItem() != 0 && null != cardInfoDTO) {
			tmpCardId = this.saveTmpCard(paymentRecord, cardInfoDTO, paymentRequest, true);
		}

		CashierSmsSendRequestDTO request = new CashierSmsSendRequestDTO();
		// 如果为null则易宝发短验
		ReqSmsSendTypeEnum smsType = CommonUtil
				.transferBindPaySMSType(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()));
		if (smsType == null) {
			smsType = ReqSmsSendTypeEnum.YEEPAY;
		}
		request.setReqSmsSendTypeEnum(smsType);// 短验证

		this.preauthSmsSend(paymentRecord, request, tmpCardId);
		responseDTO.setRecordId(Long.toString(paymentRecord.getId()));
	}

	@Override
	public void preAuthOrderConfirmAPI(APIPreauthConfirmRequestDTO requestDTO, APIPreauthConfirmResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo) {

		// 查询支付请求
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);

		PaymentRecord paymentRecord = this.getRecord4PreauthConfirmAPI(paymentRequest, requestDTO.getPaymentRecordNo(), null);

		// 补充校验 如果发短验补充，不在补充
		boolean needClearCardInfo = apiCashierYJZFService.paymentProcessInfoCheck(requestDTO.getCardInfoDTO(),
				paymentRecord, PaymentProcessEnum.PAYMENT_PROCESS_CONFIRM_PAY.getValue(), requestDTO.getVerifyCode());
		if (needClearCardInfo) {
			requestDTO.cleanCardInfo();
		}

		Long tmpCardId = 0L;
		CardInfoDTO cardInfoDTO = requestDTO.getCardInfoDTO();
		if (paymentRecord.getNeedItem() != 0 && null != cardInfoDTO) {
			tmpCardId = this.saveTmpCard(paymentRecord, cardInfoDTO, paymentRequest, true);
		}

		// 确认支付
		preAuthOrderConfirm(paymentRecord, requestDTO.getVerifyCode(), tmpCardId);
		responseDTO.setRecordId(Long.toString(paymentRecord.getId()));

		// 首次支付支付成功后绑卡
		bindCard(paymentRecord, paymentRequest, responseDTO);
	}

	private void bindCard(PaymentRecord paymentRecord, PaymentRequest paymentRequest,
			APIPreauthConfirmResponseDTO responseDTO) {
		if (StringUtils.isBlank(paymentRequest.getIdentityId())
				&& StringUtils.isBlank(paymentRequest.getIdentityType())) {
			// 下单时未传入用户类型及用户号
			return;
		}
		String bindId = cashierBindCardService.bindCard(paymentRequest, paymentRecord);
		if (StringUtils.isNotBlank(bindId)) {
			responseDTO.setBindId(bindId);
		}
	}

	@Override
	public PaymentResponseDTO preAuthOrderRequest(PaymentRequest payRequest, PaymentRecord payRecord,
			CashierPaymentRequestDTO requestDto) {
		PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
		// 非init状态，且短验类型存储的有值，表明已调用过PP
		if (PayRecordStatusEnum.INIT != payRecord.getState() && StringUtils.isNotBlank(payRecord.getSmsVerifyType())
				&& StringUtils.isNotBlank(payRecord.getPaymentOrderNo())) {
			paymentResponseDTO.setSmsType(SmsSendTypeEnum.valueOf(payRecord.getSmsVerifyType()));
			paymentResponseDTO.setPayOrderId(payRecord.getPaymentOrderNo());
			paymentResponseDTO.setNeedItem(payRecord.getNeedItem());
			return paymentResponseDTO;
		}
		NcPayOrderRequestDTO orderRequestDto = buildNcPayOrderRequestDTO(payRequest, payRecord, requestDto);
		NcPayOrderResponseDTO orderResponseDto = payProcessorService.preauthRequest(orderRequestDto);
		// 把支付处理器的支付子订单号更新到无卡收银台的支付记录表中
		SmsSendTypeEnum targetSmsType = CommonUtil.transferPreAuthSMSType(orderResponseDto.getSmsType(), requestDto.getOrderType());
		paymentProcessService.updateRecordNo(payRecord.getId(), targetSmsType.name(), orderResponseDto.getRecordNo(),
				PayRecordStatusEnum.ORDERED, orderResponseDto.getNeedItem(),RedirectTypeEnum.NONE.name());
		paymentResponseDTO.setSmsType(targetSmsType);
		paymentResponseDTO.setPayOrderId(orderResponseDto.getRecordNo());
		paymentResponseDTO.setNeedItem(orderResponseDto.getNeedItem());
		return paymentResponseDTO;
	}

	@Override
	public PaymentRecord getRecord4PreauthRequest(PaymentRequest payRequest, CashierPaymentRequestDTO requestDto) {
		ExternalUserDTO externalUser = null;
		long tmpCardId = 0L;
		// 获取外部用户信息
		if (StringUtils.isNotBlank(payRequest.getIdentityId())
				&& StringUtils.isNotBlank(payRequest.getIdentityType())) {
			ExternalUserRequestDTO userReqeustDto = buildUserRequestDTO(payRequest);
			externalUser = cwhService.getExternalUser(userReqeustDto);
		}
		// 保存临时卡
		tmpCardId = saveTmpCard(null, requestDto.getCardInfo(), payRequest, true);
		// 构建用户信息
		PersonHoldCard personHoldCard = buildPersonHoldCard(requestDto);
		// 构建condition
		RecordCondition condition = buildRecordCondition(requestDto.getCardInfo().getCardno(), OrderAction.YSQ_ORDER,
				CashierVersionEnum.valueOf(payRequest.getCashierVersion()), requestDto.getTokenId(),
				requestDto.getOrderType(), 0L);
		// 创建paymentRecord
		PaymentRecord paymentRecord = paymentProcessService.createRecordWhenUnexsit(payRequest, condition,
				personHoldCard, CardInfoTypeEnum.TEMP.name(), externalUser != null ? externalUser.getId() : null,
				requestDto.getTokenId(), String.valueOf(tmpCardId),null);

		return paymentRecord;
	}
	
	@Override
	public PaymentRecord getRecord4PreauthBindRequest(PaymentRequest payRequest, CashierPaymentRequestDTO requestDto, boolean reuseRecord) {
		ExternalUserDTO externalUser = null;
		// 获取外部用户信息
		if (StringUtils.isNotBlank(payRequest.getIdentityId())
				&& StringUtils.isNotBlank(payRequest.getIdentityType())) {
			ExternalUserRequestDTO userReqeustDto = buildUserRequestDTO(payRequest);
			externalUser = cwhService.getExternalUser(userReqeustDto);
		}
		// 根据bindId获取卡信息
		supplyCardInfo(requestDto);
		// 构建用户信息
		PersonHoldCard personHoldCard = buildPersonHoldCard(requestDto);
		// 构造condition
		RecordCondition condition = buildRecordCondition(requestDto.getCardInfo().getCardno(), OrderAction.YSQ_ORDER,
				CashierVersionEnum.valueOf(payRequest.getCashierVersion()), requestDto.getTokenId(),
				requestDto.getOrderType(), requestDto.getBindId());
		// 创建paymentRecord
		PaymentRecord paymentRecord = null;
		if(reuseRecord){
			paymentRecord = paymentProcessService.createRecordWhenUnexsit(payRequest, condition,personHoldCard, CardInfoTypeEnum.BIND.name(), externalUser != null ? externalUser.getId() : null,
					requestDto.getTokenId(), null,null);
		}else{
			paymentRecord = paymentProcessService.createPaymentRecord(payRequest, condition, personHoldCard, CardInfoTypeEnum.BIND.name(), externalUser != null ? externalUser.getId() : null, null, null, null);
		}
				
		return paymentRecord;
	}

	@Override
	public PaymentRecord getRecord4PreauthConfirm(PaymentRequest paymentRequest, Long recordId,String paymentRecordNo,
			NCCashierOrderTypeEnum recordPayType) {
		RecordCondition condition = buildPreauthConfirmRecordCondition(paymentRequest, paymentRecordNo,
				OrderAction.YSQ_CONFIRM_PAY, recordPayType);
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(String.valueOf(recordId),
				condition);
		return paymentRecord;
	}
	
	private PaymentRecord getRecord4PreauthConfirmAPI(PaymentRequest paymentRequest, String paymentRecordNo,
			NCCashierOrderTypeEnum recordPayType) {
		RecordCondition condition = buildPreauthConfirmRecordCondition(paymentRequest, paymentRecordNo,
				OrderAction.YSQ_CONFIRM_PAY, recordPayType);
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo(), 
				condition);
		return paymentRecord;
	}

	@Override
	public PaymentRecord getRecord4PreauthSendSms(PaymentRequest paymentRequest, Long recordId, String paymentOrderNo) {
		RecordCondition condition = buildPreauthSendSmsRecordCondition(paymentRequest, paymentOrderNo,
				OrderAction.YSQ_SEND_SMS);
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(String.valueOf(recordId),
				condition);
		return paymentRecord;
	}
	
	private PaymentRecord getRecord4PreauthSendSms(PaymentRequest paymentRequest, String paymentOrderNo) {
		RecordCondition condition = buildPreauthSendSmsRecordCondition(paymentRequest, paymentOrderNo,
				OrderAction.YSQ_SEND_SMS);
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(paymentRequest.getTradeSysOrderId(),paymentRequest.getTradeSysNo(),
				condition);
		return paymentRecord;
	}

	@Override
	public void preAuthOrderConfirm(PaymentRecord payRecord, String verifyCode, Long cardId) {
		// 更新paying
		boolean isRepeatePay = updateRecordToPaying(payRecord.getId());
		if (isRepeatePay) {
			throw new CashierBusinessException(Errors.REPEAT_ORDER);
		}
		NcPayConfirmRequestDTO confirmDTO = buildPreauthConfirmRequestDTO(payRecord, verifyCode, cardId);
		PayRecordResponseDTO responseDTO = null;
		try {
			responseDTO = payProcessorService.preauthConfirm(confirmDTO);
		} catch (CashierBusinessException e) {
			// 针对短信校验的出错误码，可以支持重复支付，支付订单的状态回滚
			// N400094 短信验证码错误，N400091 短语验证码已经过期
			if ("N400094".equals(e.getDefineCode()) || "N400091".equals(e.getDefineCode())
					|| "N9003016".equals(e.getDefineCode())) {
				recoverRecordToObjStatus(payRecord.getId(), PayRecordStatusEnum.SMS_SEND, PayRecordStatusEnum.PAYING);
			}
			throw e;
		}
		// 如果没有抛异常，但是状态未成功，抛异常
		handlePreauthResponseNotUpdateRecord(responseDTO, responseDTO.getTrxStatus(),payRecord.getId(),null);
	}
	
	

	@Override
	public void preauthSmsSend(PaymentRecord paymentRecord, CashierSmsSendRequestDTO smsRequest, Long cardId) {
		NcSmsRequestDTO requestDTO = buildSmsRequestDTO(paymentRecord, smsRequest, cardId);
		try {
			NcSmsResponseDTO smsResponse = payProcessorService.verifyAndSendSms(requestDTO);
			if (PayRecordStatusEnum.SMS_SEND != paymentRecord.getState()) {
				paymentProcessService.updateRecordNo(paymentRecord.getId(),
						SmsSendTypeEnum.NONE.name().equals(paymentRecord.getSmsVerifyType())
								? SmsSendTypeEnum.NONE.name()
								: (smsResponse.getSmsSendType() == null ? "" : smsResponse.getSmsSendType().name()),
						smsResponse.getRecordNo(), PayRecordStatusEnum.SMS_SEND, paymentRecord.getNeedItem(),RedirectTypeEnum.NONE.name());
			}
		} catch (CashierBusinessException e) {
			if ("N400093".equals(e.getDefineCode())) {
				// 短信发送次数超限，此时该笔支付子单已经完结，无法再次支付，需要将record置为PAYING
				recoverRecordToObjStatus(paymentRecord.getId(), PayRecordStatusEnum.PAYING,
						PayRecordStatusEnum.SMS_SEND);
			}
			throw e;
		}
	}

	private NcSmsRequestDTO buildSmsRequestDTO(PaymentRecord paymentRecord, CashierSmsSendRequestDTO smsRequest,
			Long cardId) {
		NcSmsRequestDTO requestDTO = new NcSmsRequestDTO();
		requestDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
		requestDTO.setSmsSendType(com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum
				.valueOf(smsRequest.getReqSmsSendTypeEnum().name()));
		if (cardId != 0) {
			requestDTO.setTmpCardId(cardId);
		}
		return requestDTO;
	}

	private PersonHoldCard buildPersonHoldCard(CashierPaymentRequestDTO requestDto) {
		PersonHoldCard personHoldCard = new PersonHoldCard();
		CardInfoDTO cardInfoDto = requestDto.getCardInfo();
		if (null != requestDto.getCardInfo()) {
			personHoldCard.setOwner(cardInfoDto.getName());
			personHoldCard.setIdno(cardInfoDto.getIdno());
			personHoldCard.setPhoneN0(cardInfoDto.getPhone());
		}
		CardInfo card = new CardInfo();
		BankInfo bank = new BankInfo();
		bank.setBankCode(cardInfoDto.getBankCode());
		bank.setBankName(cardInfoDto.getBankName());
		card.setBank(bank);
		card.setCardNo(cardInfoDto.getCardno());
		if (CardTypeEnum.CREDIT.name().equals(cardInfoDto.getCardType())) {
			card.setCardType(com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum.CREDIT);
		} else if (BankCardType.DEBITCARD.name().equals(cardInfoDto.getCardType())) {
			card.setCardType(com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum.DEBIT);
		}
		personHoldCard.setCard(card);
		return personHoldCard;
	}

	/**
	 * 构造record的比较条件对象
	 * 
	 * @param cardNo
	 * @param orderAction
	 * @param cashierVersion
	 * @param token
	 * @param recordPayType
	 * @return
	 */
	private RecordCondition buildRecordCondition(String cardNo, OrderAction orderAction,
			CashierVersionEnum cashierVersion, String token, NCCashierOrderTypeEnum recordPayType, Long cardId) {
		RecordCondition condition = new RecordCondition();
		condition.setPayTool(PayTool.YSQ.name());
		String[] recordPayTypes = null;
		if (NCCashierOrderTypeEnum.BIND == recordPayType) {
			condition.setBindId(String.valueOf(cardId));
			recordPayTypes = new String[] { CardInfoTypeEnum.BIND.name() };
		} else {
			condition.setCardN0(cardNo);
			recordPayTypes = new String[] { CardInfoTypeEnum.TEMP.name() };
		}
		condition.setRecordPayTypes(recordPayTypes);
		condition.setOrderAction(orderAction);
		condition.setToken(token);
		condition.setValidateStatus(true);
		return condition;
	}

	private NcPayOrderRequestDTO buildNcPayOrderRequestDTO(PaymentRequest paymentRequest, PaymentRecord payRecord,
			CashierPaymentRequestDTO requestDto) {
		NcPayOrderRequestDTO param = new NcPayOrderRequestDTO();
		String riskInfo = this.buildTradeRiskInfoUseTokenAndRequest(requestDto.getTokenId(), paymentRequest);
		param.setGoodsInfo(riskInfo);

		buildBasicRequestDTO(paymentRequest, param);
		param.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
		param.setPayProduct(PayTool.YSQ.name());
		param.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
		param.setPayScene(paymentRequest.getBizModeCode());
		// 设置为预授权
		param.setPayOrderType(PayOrderType.PREAUTH_RE);
		param.setMemberType(MemberTypeEnum.valueOf(payRecord.getMemberType()));
		param.setMemberNO(payRecord.getMemberNo());
		if (NCCashierOrderTypeEnum.FIRST == requestDto.getOrderType()) {
			param.setCardInfoType(CardInfoTypeEnum.TEMP);
			param.setCardInfoId(Long.parseLong(payRecord.getCardInfoId()));
		} else {
			param.setCardInfoType(CardInfoTypeEnum.BIND);
			param.setCardInfoId(Long.parseLong(payRecord.getBindId()));
		}

		param.setPayTool(payRecord.getPayProductCode());

		/* 设置零售产品码和基础产品码 */
		param.setBasicProductCode(
				CommonUtil.getBasicProductCode(param.getPayProduct(), paymentRequest.getTradeSysNo()));
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		param.setRetailProductCode(jsonObject.getString("saleProductCode"));
		return param;
	}

	/**
	 * 构造外部用户请求
	 * 
	 * @param payRequest
	 * @return
	 */
	private ExternalUserRequestDTO buildUserRequestDTO(PaymentRequest payRequest) {
		ExternalUserRequestDTO userRequestDto = new ExternalUserRequestDTO();
		userRequestDto.setMerchantAccount(payRequest.getMerchantNo());
		userRequestDto.setIdentityId(payRequest.getIdentityId());
		userRequestDto.setIdentityType(IdentityType.valueOf(payRequest.getIdentityType()));
		return userRequestDto;
	}

	private RecordCondition buildPreauthConfirmRecordCondition(PaymentRequest paymentRequest, String paymentRecordNo,
			OrderAction orderAction, NCCashierOrderTypeEnum recordPayType) {
		RecordCondition condition = new RecordCondition();
		condition.setPayTool(PayTool.YSQ.name());
		condition.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
		condition.setTradeSysNo(paymentRequest.getTradeSysNo());
		condition.setPayOrderId(paymentRecordNo);
		condition.setOrderAction(orderAction);
		String[] recordPayTypes = null;
		if (NCCashierOrderTypeEnum.BIND == recordPayType) {
			recordPayTypes = new String[] { CardInfoTypeEnum.BIND.name() };
		}
		if (NCCashierOrderTypeEnum.FIRST == recordPayType) {
			recordPayTypes = new String[] { CardInfoTypeEnum.TEMP.name() };
		}
		condition.setRecordPayTypes(recordPayTypes);
		return condition;
	}

	private RecordCondition buildPreauthSendSmsRecordCondition(PaymentRequest paymentRequest, String paymentOrderNo,
			OrderAction orderAction) {
		RecordCondition condition = new RecordCondition();
		condition.setTradeSysNo(paymentRequest.getTradeSysNo());
		condition.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
		condition.setPayOrderId(paymentOrderNo);
		condition.setPayTool(PayTool.YSQ.name());
		condition.setOrderAction(orderAction);
		return condition;
	}

	private NcPayConfirmRequestDTO buildPreauthConfirmRequestDTO(PaymentRecord payRecord, String verifyCode,
			Long cardId) {
		NcPayConfirmRequestDTO confirmDto = new NcPayConfirmRequestDTO();
		confirmDto.setRecordNo(payRecord.getPaymentOrderNo());
		confirmDto.setSmsCode(verifyCode);
		if (null != cardId && cardId > 0) {
			confirmDto.setTmpCardId(cardId);
		}
		return confirmDto;
	}

	private void supplyCardInfo(CashierPaymentRequestDTO requestDto) {
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(requestDto.getBindId());
		if (null != bindCardDTO && bindCardDTO.getStatus() == BindCardStatus.VALID) {
			BankCardDetailDTO bankCardDetailDTO = cwhService.getBankCard(bindCardDTO.getCardId());
			if (bankCardDetailDTO != null) {
				BaseInfo baseInfo = bankCardDetailDTO.getBaseInfo();
				if (baseInfo != null) {
					cardInfoDTO.setBankCode(baseInfo.getBankCode());
					cardInfoDTO.setCardno(baseInfo.getCardNo());
					BankCardType bankCardType = baseInfo.getBankCardType();
					if (BankCardType.CREDITCARD == bankCardType) {
						cardInfoDTO.setCardType(CardTypeEnum.CREDIT.name());
					} else if (BankCardType.DEBITCARD == bankCardType) {
						cardInfoDTO.setCardType(CardTypeEnum.DEBIT.name());
					}
					cardInfoDTO.setIdno(baseInfo.getIdcard());
					cardInfoDTO.setName(baseInfo.getOwner());
					cardInfoDTO.setPhone(baseInfo.getBankMobile());
					requestDto.setCardInfo(cardInfoDTO);
				}
			}
		} else {
			throw new CashierBusinessException(Errors.SYSTEM_BINDID_NULL);
		}
	}

	@Override
	public Long saveTmpCard(PaymentRecord payRecord, CardInfoDTO cardInfo, PaymentRequest paymentRequest,
			boolean checkOnePersion) {
		// 处理补充项逻辑
		PayTmpCardDTO tmpcard = new PayTmpCardDTO();
		boolean isneed = false;
		if (null != payRecord) {
			tmpcard.setCardNo(payRecord.getCardNo());
		} else if (null != cardInfo) {
			tmpcard.setCardNo(cardInfo.getCardno());
		}
		if (null != cardInfo) {
			if (null != cardInfo.getValid()) {
				tmpcard.setCardExpireDate(cardInfo.getValid());
				isneed = true;
			}
			if (null != cardInfo.getPass()) {
				tmpcard.setCardPin(cardInfo.getPass());
				isneed = true;
			}
			if (null != cardInfo.getCvv2()) {
				tmpcard.setCardCvn2(cardInfo.getCvv2());
				isneed = true;
			}
			if (null != cardInfo.getName()) {
				tmpcard.setUserName(cardInfo.getName());
				isneed = true;
			}
			if (null != cardInfo.getPhone()) {
				tmpcard.setPhoneNum(cardInfo.getPhone());
				isneed = true;
			}
			if (null != cardInfo.getIdno()) {
				tmpcard.setUserCardType(IdcardType.ID);
				tmpcard.setUserCardId(cardInfo.getIdno());
				isneed = true;
			}
			if (StringUtils.isNotEmpty(cardInfo.getCardType())) {
				if (cardInfo.getCardType().equals(CardTypeEnum.DEBIT.name())) {
					tmpcard.setCardType(BankCardType.DEBITCARD);
				} else if (cardInfo.getCardType().equals(CardTypeEnum.CREDIT.name())) {
					tmpcard.setCardType(BankCardType.CREDITCARD);
				}
				isneed = true;
			}
			tmpcard.setBankCode(cardInfo.getBankCode());
			tmpcard.setBankName(cardInfo.getBankName());
		}
		if (checkOnePersion) {
			BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo4bind(paymentRequest);
			if (null != bindLimitInfoResDTO
					&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
				if (null != payRecord && payRecord.getNeedItem() != 0) {
					NCPayParamMode nCPayParamMode = new NCPayParamMode(payRecord.getNeedItem());
					if (nCPayParamMode.needIdCardNumber()
							&& StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit())) {
						tmpcard.setUserCardType(IdcardType.ID);
						tmpcard.setUserCardId(bindLimitInfoResDTO.getIdentityNoLimit());
						isneed = true;
					}
					if (nCPayParamMode.needUserName()
							&& StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())) {
						tmpcard.setUserName(bindLimitInfoResDTO.getUserNameLimit());
						isneed = true;
					}
				} else {
					if (StringUtils.isNotEmpty(bindLimitInfoResDTO.getUserNameLimit())) {
						tmpcard.setUserName(bindLimitInfoResDTO.getUserNameLimit());
						isneed = true;
					}
					if (StringUtils.isNotEmpty(bindLimitInfoResDTO.getIdentityNoLimit())) {
						tmpcard.setUserCardId(bindLimitInfoResDTO.getIdentityNoLimit());
						tmpcard.setUserCardType(IdcardType.ID);
						isneed = true;
					}
				}
			}
		}
		if (isneed) {
			return cwhService.addPayTmpCard(tmpcard);
		}
		return 0l;
	}

	// 防重处理
	private boolean updateRecordToPaying(Long paymentRecordId) {
		boolean isRepeatePay = false;
		try {
			paymentProcessService.updateRecordStateBaseOnOriginalStatus(paymentRecordId, PayRecordStatusEnum.PAYING,
					Arrays.asList(
							new PayRecordStatusEnum[] { PayRecordStatusEnum.SMS_SEND, PayRecordStatusEnum.ORDERED }));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为paying状态失败", e);
			isRepeatePay = true;
		}
		return isRepeatePay;
	}

	private void recoverRecordToObjStatus(Long paymentRecordId, PayRecordStatusEnum objStatus,
			PayRecordStatusEnum preStatus) {
		try {
			logger.info("开始恢复支付订单为" + objStatus + "状态");
			paymentProcessService.updateRecordStateBaseOnOriginalStatus(paymentRecordId, objStatus,
					Arrays.asList(new PayRecordStatusEnum[] { preStatus }));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为" + objStatus + "状态失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}

	/**
	 * 下单后处理短验类型、补充项、提交补充项场景等
	 *
	 * @param smsType
	 * @param needItemNum
	 * @param responseDTO
	 */
	private void handlerSMSTypeAndNeedItem(SmsSendTypeEnum smsType, int needItemNum,
			APIPreauthPaymentResponseDTO responseDTO) {
		// 1，处理验证码类型及提交补充项场景
		// 需要发验证码的，在请求短验时补充；不需要验证码的，在确认支付时补充
		if (smsType == SmsSendTypeEnum.NONE) {
			// 无需验证码->验证码=none;提交补充=确认时
			responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_CONFIRM);
		} else if (smsType == SmsSendTypeEnum.BANK || smsType == SmsSendTypeEnum.YEEPAY) {
			// 短信验证码->验证码=SMS;提交补充=发短验时
			responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_VERITY);
		} else if (smsType == SmsSendTypeEnum.VOICE || smsType == SmsSendTypeEnum.MERCHANT_SEND) {
			// 语音验证码或商户发送验证码->验证码=VOICE;提交补充=发短验时
			responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_VERITY);
		} else {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		ReqSmsSendTypeEnum smsSendTypeEnum = CommonUtil.transferBindPaySMSType(smsType);
		if (smsSendTypeEnum == null) {
			responseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_NONE);
		}
		if (ReqSmsSendTypeEnum.YEEPAY.equals(smsSendTypeEnum)) {
			responseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_SMS);
		}
		if (ReqSmsSendTypeEnum.VOICE.equals(smsSendTypeEnum)) {
			responseDTO.setVerifyCodeType(Constant.VERIFY_CODE_TYPE_VOICE);
		}

		// 2，处理补充项信息
		if (0 == needItemNum) {
			// 无需补充项->提交补充=NONE
			responseDTO.setNeedItemScene(Constant.SUPPLY_NEEDITEM_SCENE_NONE);
		}
		NCPayParamMode nCPayParamMode = new NCPayParamMode(needItemNum);
		StringBuilder needItemStr = new StringBuilder();
		if (nCPayParamMode.needIdCardNumber()) {
			needItemStr.append("idnoIsNeed,");
		}
		if (nCPayParamMode.needUserName()) {
			needItemStr.append("ownerIsNeed,");
		}
		if (nCPayParamMode.needBankMobilePhone()) {
			needItemStr.append("phoneNoIsNeed,");
		}
		if (nCPayParamMode.needCvv()) {
			needItemStr.append("cvvIsNeed,");
		}
		if (nCPayParamMode.needAvlidDate()) {
			needItemStr.append("validDateIsNeed,");
		}
		if (nCPayParamMode.needBankPWD()) {
			needItemStr.append("bankPWDIsNeed,");
		}
		String s = needItemStr.toString();
		if (StringUtils.isNotBlank(s) && s.length() >= 1) {
			responseDTO.setNeedItems(s.substring(0, s.length() - 1));
		}
	}

	private PaymentRecord buildPreauthOperationRecord(PaymentRequest paymentRequest, String payType) {
		PaymentRecord completePaymentRecord = paymentRequest.buildPaymentRecord(null, null);
		completePaymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
		completePaymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
		completePaymentRecord.setPayTool(PayTool.YSQ.name());
		completePaymentRecord.setPayType(payType);
		return completePaymentRecord;
	}

	@Override
	public void preauthComplete(APIPreauthCompleteReqDTO completeRequestDTO, APIPreauthCompleteResDTO responseDTO,
			OrderDetailInfoModel orderInfo) {
		// 查询paymentRequest TODO 是否要校验订单有效期
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);
		// 找到预授权成功的那笔支付记录- 获取支付处理器子订单号
		PaymentRecord preauthRecord = findPaymentRecord(completeRequestDTO.getPaymentOrderNo(), paymentRequest, false);
		// 针对该笔商户订单，如果对应的最近一笔的支付记录是预授权完成且未终态，则不允许继续进行 TODO PP已做，收银台先不做
		// 如果允许继续做完成，创建一条完成记录
		PaymentRecord completePaymentRecord = buildPreauthOperationRecord(paymentRequest,
				PreauthPayTypeEnum.COMPLETE.name());
		long completePaymentRecordId = paymentProcessService.savePaymentRecord(completePaymentRecord);
		completePaymentRecord.setId(completePaymentRecordId);
		// 构造请求PP的预授权完成接口的入参
		PreAuthCompleteRequestDTO preAuthCompleteRequestDTO = buildPreAuthCompleteRequestDTO(paymentRequest,
				preauthRecord, completeRequestDTO.getCompleteAmount());
		// 调用支付系统预授权完成接口
		PreAuthCompleteResponseDTO response = payProcessorService.preauthComplete(preAuthCompleteRequestDTO);
		responseDTO.setCompletePaymentOrderNo(response.getRecordNo());
		// 判断状态，如果预授权完成成功，将支付记录状态更新为预授权完成成功状态
		handlePreauthResponse(response, response.getTrxStatus(), completePaymentRecord.getId(), response.getRecordNo(),
				responseDTO);

	}

	@Override
	public void preauthCancel(APIPreauthCancelReqDTO cancelReqDTO, APIPreauthCancelResDTO responseDTO,
			OrderDetailInfoModel orderInfo) {
		// 查询paymentRequest TODO 是否要校验订单有效期
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);
		// 找到预授权成功的那笔支付记录- 获取支付处理器子订单号
		PaymentRecord preauthRecord = findPaymentRecord(cancelReqDTO.getPaymentOrderNo(), paymentRequest, false);
		// // 如果允许继续做完成，创建一条完成记录
		// PaymentRecord canclePaymentRecord =
		// buildPreauthOperationRecord(paymentRequest,
		// PreauthPayTypeEnum.CANCLE.name());
		// paymentProcessService.savePaymentRecord(canclePaymentRecord);
		// 构造请求PP的预授权完成接口的入参
		PreAuthCancelRequestDTO preAuthCancelRequestDTO = buildPreAuthCancelRequestDTO(paymentRequest, preauthRecord,
				PreauthCancelType.PREAUTHCANCEL);
		// 调用支付系统预授权完成接口
		PreAuthCancelResponseDTO response = payProcessorService.preauthCancel(preAuthCancelRequestDTO);
		// 判断状态，如果预授权完成成功，将支付记录状态更新为预授权完成成功状态
		handlePreauthResponseNotUpdateRecord(response, response.getTrxStatus(), preauthRecord.getId(),
				responseDTO);
	}

	/**
	 * 构造预授权完成撤销金额
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 * @param cancelType
	 * @param cardNo
	 * @return
	 */
	private PreAuthCancelRequestDTO buildPreAuthCompleteCancelRequestDTO(PaymentRequest paymentRequest,
			PaymentRecord paymentRecord, PreauthCancelType cancelType, String cardNo) {
		PreAuthCancelRequestDTO completeCancelRequestDTO = buildPreAuthCancelRequestDTO(paymentRequest, paymentRecord,
				cancelType);
		completeCancelRequestDTO.setPayerCardNo(cardNo);
		return completeCancelRequestDTO;
	}

	@Override
	public void preauthCompleteCancel(APIPreauthCompleteCancelReqDTO completeCancelReqDTO,
			APIPreauthCompleteCancelResDTO responseDTO, OrderDetailInfoModel orderInfo) {
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), false);
		// 找到预授权完成成功的那笔支付记录- 获取支付处理器子订单号
		PaymentRecord completeRecord = new PaymentRecord();
		completeRecord.setPaymentOrderNo(completeCancelReqDTO.getPaymentOrderNo());
		 
		// 构造请求PP的预授权完成接口的入参
		PreAuthCancelRequestDTO preAuthCancelRequestDTO = buildPreAuthCompleteCancelRequestDTO(paymentRequest,
				completeRecord, PreauthCancelType.PREAUTHCONFIRMCANCEL, completeCancelReqDTO.getCardNo());
		// 调用支付系统预授权完成接口
		PreAuthCancelResponseDTO response = payProcessorService.preauthCancel(preAuthCancelRequestDTO);
		// 判断状态，如果预授权完成成功，将支付记录状态更新为预授权完成成功状态
		handlePreauthResponseNotUpdateRecord(response, response.getTrxStatus(), completeRecord.getId(),
				responseDTO);
	}

	private void handlePreauthResponseNotUpdateRecord(ResponseStatusDTO ppResponseDTO, TrxStatusEnum ppTrxStatus, Long recordId,
			APIBasicResponseDTO responseDTO) {
		// 返回值处理
		if(responseDTO != null){
			responseDTO.setRecordId(recordId + "");
		}
		if (ppTrxStatus == TrxStatusEnum.DOING) {
			// 支付结果未知
			throw CommonUtil.handleException(Errors.ORDER_STATUS_UNKNOWN);
		} else if (ppTrxStatus == TrxStatusEnum.REVERSE) {
			// 支付结果为冲正
			throw CommonUtil.handleException(Errors.ORDER_STATUS_REVERSE);
		} else if(ppTrxStatus == TrxStatusEnum.FAILUER){
			throw CommonUtil.handleException(SysCodeEnum.PP.name(), ppResponseDTO.getResponseCode(), 
					ppResponseDTO.getResponseMsg());
		}
		
	}

	private void handlePreauthResponse(BasicResponseDTO ppResponseDTO, TrxStatusEnum ppTrxStatus, long recordId, String paymentOrderNo,
			APIBasicResponseDTO responseDTO) {
		PayRecordStatusEnum recordStatusEnum = (TrxStatusEnum.SUCCESS == ppTrxStatus) ? PayRecordStatusEnum.SUCCESS
				: ((TrxStatusEnum.FAILUER == ppTrxStatus || TrxStatusEnum.REVERSE == ppTrxStatus)
						? PayRecordStatusEnum.FAILED : PayRecordStatusEnum.PAYING);
		paymentProcessService.updateRecordNo(recordId, null, paymentOrderNo, recordStatusEnum, 0,RedirectTypeEnum.NONE.name());
		// 返回值处理
		responseDTO.setRecordId(recordId + "");
		if (ppTrxStatus == TrxStatusEnum.DOING) {
			// 支付结果未知
			throw CommonUtil.handleException(Errors.ORDER_STATUS_UNKNOWN);
		} else if (ppTrxStatus == TrxStatusEnum.REVERSE) {
			// 支付结果为冲正
			throw CommonUtil.handleException(Errors.ORDER_STATUS_REVERSE);
		} else if(ppTrxStatus == TrxStatusEnum.FAILUER){
			throw CommonUtil.handleException(SysCodeEnum.PP.name(), ppResponseDTO.getResponseCode(), 
					ppResponseDTO.getResponseMsg());
		}
	}

	private PaymentRecord findPaymentRecord(String paymentOrderNo, PaymentRequest paymentRequest, boolean validateStatus) {
		RecordCondition condition = buildRecordCondition(paymentRequest, paymentOrderNo, null, validateStatus);
		PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo(), condition);
		return paymentRecord;
	}

	private RecordCondition buildRecordCondition(PaymentRequest paymentRequest, String paymentOrderNo,
			OrderAction orderAction, boolean validateStatus) {
		RecordCondition condition = new RecordCondition();
		condition.setTradeSysNo(paymentRequest.getTradeSysNo());
		condition.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
		condition.setPayOrderId(paymentOrderNo);
		condition.setPayTool(PayTool.YSQ.name());
		condition.setOrderAction(orderAction);
		condition.setValidateStatus(validateStatus);
		return condition;
	}

	@Override
	public OrderDetailInfoModel queryOrder(APIBasicRequestDTO reqDTO, OrderSystemPreauthStatusEnum[] objStatusList) {
		// 反查订单
		OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(reqDTO.getMerchantNo(), reqDTO.getToken(),
				reqDTO.getBizType(), null);
		if (orderInfo.getTransactionType() == null || TransactionTypeEnum.PREAUTH != orderInfo.getTransactionType()) {
			// 校验当前的订单是否为预授权订单
			throw new CashierBusinessException(Errors.NON_PREAUTH_ORDER);
		}
		OrderSystemPreauthStatusEnum currentPreauthStatus = OrderSystemPreauthStatusEnum
				.getStatus(orderInfo.getCurrentPreauthStatus());
		if (currentPreauthStatus == null) {
			// 预期，如果是预授权的订单，从订单系统反查回来的预授权状态不能是空的，否则就是哪个系统有bug
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		for (OrderSystemPreauthStatusEnum objStatus : objStatusList) {
			if (currentPreauthStatus == objStatus) {
				return orderInfo;
			}
		}
		// 校验当前预授权状态
		throw new CashierBusinessException(Errors.PREAUTH_COMPLETE_STATUS_ERROR.getCode(),
				Errors.PREAUTH_COMPLETE_STATUS_ERROR.getMsg() + ",当前状态为" + currentPreauthStatus.getDesc());

	}

}
