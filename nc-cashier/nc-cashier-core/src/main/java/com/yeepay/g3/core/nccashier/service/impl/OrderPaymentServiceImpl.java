package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.CwhService;
import com.yeepay.g3.core.nccashier.gateway.service.NcPayService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.BindNeedItemInfo;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.core.nccashier.vo.ExternalUserRequestDTO;
import com.yeepay.g3.core.nccashier.vo.SimpleRecodeInfoModel;
import com.yeepay.g3.facade.cwh.enumtype.BankCardType;
import com.yeepay.g3.facade.cwh.enumtype.BindCardStatus;
import com.yeepay.g3.facade.cwh.enumtype.IdcardType;
import com.yeepay.g3.facade.cwh.param.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.CommonUtils;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncpay.dto.PageRedirectDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.RequestPaymentParam;
import com.yeepay.g3.facade.ncpay.dto.SmsSendRequestDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.OrderTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.BankCardInfoDTO;
import com.yeepay.g3.facade.payprocessor.dto.CombRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcSmsRequestDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhen.tan
 * @since：2016年5月25日 下午6:27:39
 */
@Service("orderPaymentService")
public class OrderPaymentServiceImpl extends NcCashierBaseService implements OrderPaymentService {

	private static final Logger logger =
			NcCashierLoggerFactory.getLogger(OrderPaymentServiceImpl.class);
	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private CwhService cwhService;

	@Resource
	private NcPayService ncPayService;

	@Resource
	private CashierBankCardService cashierBankCardService;
	@Resource
	private BankCardLimitInfoService bankCardLimitInfoService;

	@Resource
	private PayProcessorService payProcessorService;

	@Override
	public CombinedPaymentDTO validateBindPayBusinInfo(CashierPaymentRequestDTO requestDto)
			throws CashierBusinessException {

		if (requestDto.getBindId() <= 0) {
			throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
		}

		PaymentRequest payRequest = paymentRequestService.findPaymentRequestByRequestId(requestDto.getRequestId());

		PaymentRecord record = null;
		boolean isNeedOrderAgain = false;
		if (requestDto.getRecordId() > 0) {
			record = paymentProcessService.findRecordByPaymentRecordId(requestDto.getRecordId() + "");
			if (record != null) {
				if (PayRecordStatusEnum.SUCCESS == record.getState()) {
					throw CommonUtil.handleException(Errors.THRANS_FINISHED);
				} else if (PayRecordStatusEnum.FAILED == record.getState()) {
					isNeedOrderAgain = true;
				} else if(StringUtils.isNotBlank(requestDto.getPayTool()) && !requestDto.getPayTool().equals(record.getPayTool())){
					isNeedOrderAgain = true;
				}
				else if (record.getBindId() ==null || requestDto.getBindId() != Long.parseLong(record.getBindId())) {
					isNeedOrderAgain = true;
				} else if (StringUtils.isEmpty(record.getPaymentOrderNo())) {
					isNeedOrderAgain = true;
				}else if (StringUtils.isNotBlank(record.getRedirectType()) && !RedirectTypeEnum.NONE.name().equalsIgnoreCase(record.getRedirectType())){
                    isNeedOrderAgain = true;
                }
			} else {
				isNeedOrderAgain = true;
			}
		} else {
			isNeedOrderAgain = true;
		}

		CombinedPaymentDTO combinedPaymentDto = new CombinedPaymentDTO();
		combinedPaymentDto.setNeedOrderRecord(isNeedOrderAgain);
		combinedPaymentDto.setPaymentRequest(payRequest);
		combinedPaymentDto.setPaymentRecord(record);

		return combinedPaymentDto;
	}

	@Override
	public CombinedPaymentDTO validateFirstPayBusinInfo(CashierPaymentRequestDTO requestDto)
			throws CashierBusinessException {

		CardInfoDTO cardInfo = requestDto.getCardInfo();
		if (cardInfo == null || StringUtils.isBlank(cardInfo.getCardno())) {
			throw CommonUtil.handleException(Errors.CARD_INFO_ERROR);
		}

		PaymentRequest payRequest =	paymentRequestService.findPaymentRequestByRequestId(requestDto.getRequestId());
		PaymentRecord record = null;
		Boolean isNeedOrderAgain = false;
		if (requestDto.getRecordId() > 0) {
			record = paymentProcessService
					.findRecordByPaymentRecordId(requestDto.getRecordId() + "");
			if (record != null) {
				if (PayRecordStatusEnum.SUCCESS == record.getState()) {
					throw CommonUtil.handleException(Errors.THRANS_FINISHED);
				} else if (record.getPaymentRequestId() != requestDto.getRequestId()
						|| !requestDto.getTokenId().equals(record.getTokenId())) {
					throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
				} else if (PayRecordStatusEnum.FAILED == record.getState()) {
					isNeedOrderAgain = true;
				} else if (StringUtils.isEmpty(record.getPaymentOrderNo())) {
					isNeedOrderAgain = true;
				} else if (StringUtils.isNotBlank(record.getRedirectType()) && !RedirectTypeEnum.NONE.name().equalsIgnoreCase(record.getRedirectType())){
				    isNeedOrderAgain = true;
                }
				else {
					isNeedOrderAgain =
							cashierBankCardService.validateCardNeed(cardInfo, payRequest, record);
				}
			} else {
				isNeedOrderAgain = true;
			}
		} else {
			isNeedOrderAgain = true;
		}

		CombinedPaymentDTO combinedPaymentDto = new CombinedPaymentDTO();
		combinedPaymentDto.setNeedOrderRecord(isNeedOrderAgain);
		combinedPaymentDto.setPaymentRequest(payRequest);

		logger.info("首次支付下单业务校验完成,下单标识={}", isNeedOrderAgain + "");
		return combinedPaymentDto;

	}

	@Override
	public void doBindtPayCreatePayment(CashierPaymentRequestDTO requestDto,
			CombinedPaymentDTO combinedPaymentDto) {
		PaymentRequest payRequest = combinedPaymentDto.getPaymentRequest();

		ExternalUserDTO externalUser = null;
		if (StringUtils.isNotBlank(payRequest.getIdentityId())
				&& StringUtils.isNotBlank(payRequest.getIdentityType())) {
			ExternalUserRequestDTO userReqeustDto = buildUserRequestDTO(payRequest);
			externalUser = cwhService.getExternalUser(userReqeustDto);
		}
		supplyCardInfo(requestDto);

		// 创建支付订单记录
		PaymentRecord paymentRecord = buildPaymentRecord(requestDto, payRequest,
				externalUser != null ? externalUser.getId() : null, CardInfoTypeEnum.BIND,
				requestDto.getBindId() + "");
		long recordId = paymentProcessService.savePaymentRecord(paymentRecord);
		requestDto.setRecordId(recordId);
		combinedPaymentDto.setPaymentRecord(paymentRecord);

		userProceeService.getAndUpdatePaymentRecordId(requestDto.getTokenId(), recordId + "");
	}

	@Override
	public void doFirsttPayCreatePayment(CashierPaymentRequestDTO requestDto,
			CombinedPaymentDTO combinedPaymentDto) {
		PaymentRequest payRequest = combinedPaymentDto.getPaymentRequest();

		ExternalUserDTO externalUser = null;
		if (StringUtils.isNotBlank(payRequest.getIdentityId())
				&& StringUtils.isNotBlank(payRequest.getIdentityType())) {
			ExternalUserRequestDTO userReqeustDto = buildUserRequestDTO(payRequest);
			externalUser = cwhService.getExternalUser(userReqeustDto);
		}

        // 创建支付订单记录
		PaymentRecord paymentRecord = buildPaymentRecord(requestDto, payRequest,
				externalUser != null ? externalUser.getId() : null, CardInfoTypeEnum.TEMP,
				"-1");//移除临时卡依赖，不保存临时卡ID
		long recordId = paymentProcessService.savePaymentRecord(paymentRecord);
		requestDto.setRecordId(recordId);
		combinedPaymentDto.setPaymentRecord(paymentRecord);

		userProceeService.getAndUpdatePaymentRecordId(requestDto.getTokenId(), recordId + "");
	}

	public BindNeedItemInfo getNeedItemAndSmsType(CashierPaymentRequestDTO requestDto, PaymentResponseDTO paymentResponseDTO, CashierPaymentResponseDTO response, PaymentRequest paymentRequest){
		SmsSendTypeEnum smsSendTypeEnum = null;
		int needItem = 0;
		if (paymentResponseDTO == null) {
			smsSendTypeEnum = RedisTemplate.getTargetFromRedis(
						Constant.NCCASHIER_SMS_TYPE_KEY + requestDto.getTokenId(), SmsSendTypeEnum.class);
			Integer items = RedisTemplate.getTargetFromRedis(
					Constant.NCCASHIER_BIND_NEEDITEM_KEY + requestDto.getTokenId(), Integer.class);
			needItem = (items!=null) ? items.intValue():0;
			if(smsSendTypeEnum == null){
				long recordId = requestDto.getRecordId();
				if(recordId ==0){
					throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
				}
				PaymentRecord paymentRecord = paymentProcessService.findRecordByPaymentRecordId(String.valueOf(recordId));
				if(paymentRecord == null){
					throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
				}
				smsSendTypeEnum = SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType());
				needItem = paymentRecord.getNeedItem();
			}
		}else{
			smsSendTypeEnum = paymentResponseDTO.getSmsType();
			needItem = paymentResponseDTO.getNeedItem();
			RedisTemplate.setCacheObjectSumValue(
					Constant.NCCASHIER_SMS_TYPE_KEY + requestDto.getTokenId(), paymentResponseDTO.getSmsType(),
					Constant.NCCASHIER_SMS_SEND_TIME_LIMIT);
			RedisTemplate.setCacheObjectSumValue(
					Constant.NCCASHIER_BIND_NEEDITEM_KEY + requestDto.getTokenId(), paymentResponseDTO.getNeedItem(),
					Constant.NCCASHIER_NEEDBANKNEEDITEM_REDIS_TIME_LIMIT);
		}
		return new BindNeedItemInfo(smsSendTypeEnum, needItem);
	}

	@Override
	public void supplyBindOrderResult(CashierPaymentRequestDTO requestDto, PaymentResponseDTO paymentResponseDTO, CashierPaymentResponseDTO response, PaymentRequest paymentRequest) {
		BindNeedItemInfo bindNeedItemInfo = getNeedItemAndSmsType(requestDto, paymentResponseDTO, response, paymentRequest);
		ReqSmsSendTypeEnum reqSmsSendTypeEnum = this.transferBindPaySMSType(bindNeedItemInfo.getSmsSendTypeEnum());
		response.setNeedBankCardDto(getBindCardAndNeedSupplement(requestDto.getBindId(),bindNeedItemInfo.getNeedItem(),paymentRequest));
		response.setReqSmsSendTypeEnum(reqSmsSendTypeEnum);

		setPageRedirect(paymentResponseDTO, response);

		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}

	public void supplyBindPayOrderResult(CashierPaymentRequestDTO paymentRequestDto, PaymentResponseDTO paymentResponseDTO, FirstBindCardPayResponseDTO response, PaymentRequest paymentRequest, NeedBankCardDTO needItemByUserInput){
		BindNeedItemInfo bindNeedItemInfo = getNeedItemAndSmsType(paymentRequestDto, paymentResponseDTO, response, paymentRequest);
		response.setReqSmsSendTypeEnum(bindNeedItemInfo.getSmsSendTypeEnum() == SmsSendTypeEnum.BANK ? ReqSmsSendTypeEnum.YEEPAY : null);
		NeedBankCardDTO bkdto = new NeedBankCardDTO();
		if(bindNeedItemInfo.getSmsSendTypeEnum() == SmsSendTypeEnum.BANK){
			BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(paymentRequestDto.getBindId());
			bkdto.setPhoneNo(bindCardDTO.getBankMobile());//发短验用
			bkdto.setYpMobile(bindCardDTO.getYbMobile());//发短验用
		}

		boolean loseSomeInfo = setNeedItem(bindNeedItemInfo.getNeedItem(), needItemByUserInput, bkdto, paymentRequest);
		response.setNeedBankCardDto(bkdto);
		response.setLoseNeedItem(loseSomeInfo);
	}

	@Override
	public NeedBankCardDTO getBindCardAndNeedSupplement(long bindId, int needItem, PaymentRequest paymentRequest) {
		NeedBankCardDTO bkdto = new NeedBankCardDTO();
		BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(bindId);
		if (bindCardDTO != null){
			bkdto.setPhoneNo(bindCardDTO.getBankMobile());//发短验用
			bkdto.setYpMobile(bindCardDTO.getYbMobile());//发短验用
		}
		//ncpay返回的补充项 获取绑卡补充项并设置透传项
		NeedSurportDTO needSurportDTO = setCardNeedSupplement(needItem, paymentRequest, bkdto);
		bkdto.setNeedSurportDTO(needSurportDTO);
		return bkdto;
	}

	public NeedBankCardDTO checkNeedItem(long bindId, int needItem, PaymentRequest paymentRequest, NeedBankCardDTO needBankCardDTO){
		NeedBankCardDTO bkdto = new NeedBankCardDTO();
		BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(bindId);
		bkdto.setPhoneNo(bindCardDTO.getBankMobile());//发短验用
		bkdto.setYpMobile(bindCardDTO.getYbMobile());//发短验用
		NeedSurportDTO needSurportDTO = setCardNeedSupplement(needItem, paymentRequest, bkdto);
		bkdto.setNeedSurportDTO(needSurportDTO);
		return bkdto;
	}

	/**
	 * 短验类型转换
	 * @param smsSendTypeEnum
	 * @return
	 */
	private ReqSmsSendTypeEnum transferBindPaySMSType(SmsSendTypeEnum smsSendTypeEnum) {
		return CommonUtil.transferBindPaySMSType(smsSendTypeEnum);
	}

	@Override
	public void supplyFirstOrderResult(PaymentResponseDTO paymentResponseDTO,
			CashierPaymentResponseDTO response,CashierPaymentRequestDTO paymentRequestDto) {
		ReqSmsSendTypeEnum reqSmsSendTypeEnum = null;
		//非首次下单先从缓存中取短验类型和补充项
		if (paymentResponseDTO == null) {
			paymentResponseDTO = RedisTemplate.getTargetFromRedis(
					Constant.NCCASHIER_SMS_TYPE_KEY + response.getTokenId(), PaymentResponseDTO.class);
			if(paymentResponseDTO == null){
				long recordId = paymentRequestDto.getRecordId();
				if(recordId ==0){
					throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
				}
				PaymentRecord paymentRecord = paymentProcessService.findRecordByPaymentRecordId(String.valueOf(recordId));
				 if(paymentRecord == null){
					 throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
				 }
				 if(StringUtils.isNotBlank(paymentRecord.getSmsVerifyType())){
					 paymentResponseDTO = new PaymentResponseDTO();
					 paymentResponseDTO.setSmsType(SmsSendTypeEnum.valueOf(paymentRecord.getSmsVerifyType()));
				 }
			}
		} else {
			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_SMS_TYPE_KEY + response.getTokenId(),
					paymentResponseDTO,Constant.NCCASHIER_SMS_SEND_TIME_LIMIT);// 存储短验类型
		}

		//短验类型转换
		if (paymentResponseDTO.getSmsType() == SmsSendTypeEnum.VOICE
				|| paymentResponseDTO.getSmsType() == SmsSendTypeEnum.MERCHANT_SEND) {
			reqSmsSendTypeEnum = ReqSmsSendTypeEnum.VOICE;
		} else if (paymentResponseDTO.getSmsType() == SmsSendTypeEnum.YEEPAY
				||paymentResponseDTO.getSmsType() == SmsSendTypeEnum.BANK
				|| paymentResponseDTO.getSmsType() == SmsSendTypeEnum.NONE) {
			reqSmsSendTypeEnum = ReqSmsSendTypeEnum.YEEPAY;
		}


		setPageRedirect(paymentResponseDTO, response);

		response.setReqSmsSendTypeEnum(reqSmsSendTypeEnum);
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}

	private void setPageRedirect(PaymentResponseDTO paymentResponseDTO, CashierPaymentResponseDTO response) {
		com.yeepay.g3.facade.nccashier.dto.PageRedirectDTO pageRedirectDTO = new com.yeepay.g3.facade.nccashier.dto.PageRedirectDTO();
		if (null == paymentResponseDTO){
			response.setPageRedirectDTO(pageRedirectDTO);
			return;
		}
		PageRedirectDTO pageRedirect = paymentResponseDTO.getPageRedirectDTO();
		response.setRedirectType(paymentResponseDTO.getRedirectType());
				if (null != pageRedirect) {
			pageRedirectDTO.setEncoding(pageRedirect.getEncoding());
			pageRedirectDTO.setExtMap(pageRedirect.getExtMap());
			pageRedirectDTO.setRedirectUrl(pageRedirect.getRedirectUrl());
			pageRedirectDTO.setRedirectSceneType(pageRedirect.getRedirectSceneType());
			pageRedirectDTO.setMethod(pageRedirect.getMethod());
		}
		response.setPageRedirectDTO(pageRedirectDTO);
	}

	@Override
	public PaymentResponseDTO callNcPayOrder(CashierPaymentRequestDTO requestDto,
			CombinedPaymentDTO combinedPaymentDto, CardInfoTypeEnum cardInfoType) {

		PaymentRecord paymentRecord = combinedPaymentDto.getPaymentRecord();
		PaymentRequest paymentRequest = combinedPaymentDto.getPaymentRequest();

		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo(paymentRequest);
		CardInfoDTO cardInfo = buildCardInfoDTO(requestDto.getCardInfo(), paymentRequest, bindLimitInfoResDTO);


		if(!CommonUtils.isPayProcess(paymentRequest.getPaySysCode(),paymentRequest.getTradeSysNo())){//非订单处理器请求，走NCPAY
			RequestPaymentParam requestPaymentParam =
					buildNcPayPaymentRequest(paymentRequest,paymentRecord, cardInfoType,requestDto,cardInfo);
			PaymentResponseDTO paymentResponseDTO = ncPayService.requestPayment(requestPaymentParam);
			if (cardInfoType == CardInfoTypeEnum.TEMP && paymentResponseDTO.getNeedItem() != 0) {
				throw CommonUtil.handleException(Errors.CARD_INFO_ERROR);
			}

			String redirectType = RedirectTypeEnum.NONE.name();
			if (RedirectTypeEnum.PAY.name().equalsIgnoreCase(paymentResponseDTO.getRedirectType()) || RedirectTypeEnum.SIGN.name().equals(paymentResponseDTO.getRedirectType())){
				redirectType = paymentResponseDTO.getRedirectType();
			}

			// 把NCPAY的支付订单号更新到无卡收银台的支付记录表中
			paymentProcessService.updateRecordNo(paymentRecord.getId(),
					paymentResponseDTO.getSmsType().name(), paymentResponseDTO.getPayOrderId(),
					PayRecordStatusEnum.ORDERED,paymentResponseDTO.getNeedItem(),redirectType);
			return paymentResponseDTO;
		}else{
			String riskInfo = this.buildTradeRiskInfoUseTokenAndRequest(requestDto.getTokenId(),paymentRequest);
			NcPayOrderRequestDTO requesDTO =buildNcPayOrderRequestDTO(paymentRequest,paymentRecord,cardInfoType,riskInfo,cardInfo,requestDto);
			//支持营销立减
			buildCompPayInfo(requestDto.getTokenId(), paymentRequest, requesDTO, paymentRecord);
			NcPayOrderResponseDTO responseDTO = payProcessorService.ncPayRequest(requesDTO);
			if (cardInfoType == CardInfoTypeEnum.TEMP && responseDTO.getNeedItem() != 0) {
				throw CommonUtil.handleException(Errors.CARD_INFO_ERROR);
			}

			String redirectType = RedirectTypeEnum.NONE.name();
			if (RedirectTypeEnum.PAY.name().equalsIgnoreCase(responseDTO.getPageRedirectType()) || RedirectTypeEnum.SIGN.name().equals(responseDTO.getPageRedirectType())){
				redirectType = responseDTO.getPageRedirectType();
			}
			// 把支付处理器的支付子订单号更新到无卡收银台的支付记录表中
			paymentProcessService.updateRecordNo(paymentRecord.getId(), responseDTO.getSmsType().name(), responseDTO.getRecordNo(),
			PayRecordStatusEnum.ORDERED,responseDTO.getNeedItem(),redirectType);
			//PP绑卡支付时，把record精简信息放入缓存，便于前端成功监听支付结果
			if(cardInfoType == CardInfoTypeEnum.BIND){
				SimpleRecodeInfoModel recordInfo = new SimpleRecodeInfoModel(requestDto.getRecordId(),responseDTO.getRecordNo());

				String resultJson = RedisTemplate.getTargetFromRedisToString(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + requestDto.getTokenId());
				List<SimpleRecodeInfoModel> result = JSONObject.parseArray(resultJson, SimpleRecodeInfoModel.class);
				if(CollectionUtils.isEmpty(result)){
					result = new ArrayList<SimpleRecodeInfoModel>();
					result.add(recordInfo);
					RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + requestDto.getTokenId(), result,
							Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY_TIMEOUT);
				} else if(!result.contains(recordInfo)){
					result.add(recordInfo);
					RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + requestDto.getTokenId(), result,
							Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY_TIMEOUT);
				}
			}
			PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
			paymentResponseDTO.setNeedItem(responseDTO.getNeedItem());
			paymentResponseDTO.setSmsType(responseDTO.getSmsType());
			paymentResponseDTO.setPayOrderId(responseDTO.getRecordNo());

			paymentResponseDTO.setRedirectType(responseDTO.getPageRedirectType());
			PageRedirectDTO pageRedirectDTO = new PageRedirectDTO();
			if (responseDTO.getPageRedirectDTO() != null){
				pageRedirectDTO.setEncoding(responseDTO.getPageRedirectDTO().getEncoding());
				pageRedirectDTO.setExtMap(responseDTO.getPageRedirectDTO().getExtMap());
				pageRedirectDTO.setMethod(responseDTO.getPageRedirectDTO().getMethod());
				pageRedirectDTO.setRedirectSceneType(responseDTO.getPageRedirectDTO().getRedirectSceneType());
				pageRedirectDTO.setRedirectUrl(responseDTO.getPageRedirectDTO().getRedirectUrl());
			}

			paymentResponseDTO.setPageRedirectDTO(pageRedirectDTO);

			return paymentResponseDTO;
		}

	}

	public String validateSmsBusinInfo(CashierSmsSendRequestDTO smsRequest) {


		paymentRequestService.findPaymentRequestByRequestId(smsRequest.getRequestId());

		PaymentRecord record =
				paymentProcessService.findRecordByPaymentRecordId(smsRequest.getRecordId() + "");
		if (record == null) {
			throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
		}

		if (record.getPaymentRequestId() != smsRequest.getRequestId()) {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}

		return record.getPaymentOrderNo();
	}

	public void verifyAndSendSms(String payorderId, CashierSmsSendRequestDTO smsRequest,CardInfoDTO cardInfo , String tradeSysCode, String paymentSysCode) {

		if(!CommonUtils.isPayProcess(paymentSysCode,tradeSysCode)){// 非订单处理器订单，调用ncpay发短验接口
			SmsSendRequestDTO smsSendRequestDTO = new SmsSendRequestDTO();
			smsSendRequestDTO.setPayOrderId(payorderId);
			smsSendRequestDTO.setSmsSendType(com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum.valueOf(smsRequest.getReqSmsSendTypeEnum().name()));
			//添加临时卡
			if(cardInfo != null){
				smsSendRequestDTO.setCardInfoDTO(cardInfo.transferNcPayCardInfoDTO());
			}

			ncPayService.verifyAndSendSms(smsSendRequestDTO);
		}else{
			NcSmsRequestDTO requestDTO = new NcSmsRequestDTO();
			requestDTO.setRecordNo(payorderId);
			requestDTO.setSmsSendType(com.yeepay.g3.facade.ncpay.enumtype.ReqSmsSendTypeEnum.valueOf(smsRequest.getReqSmsSendTypeEnum().name()));
			if(cardInfo != null){
				requestDTO.setBankCardInfoDTO(cardInfo.transferPayProcessBankCardInfoDTO());
			}
			payProcessorService.verifyAndSendSms(requestDTO);
		}
		try {
			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_SMS_SEND_TIME_KEY + smsRequest.getTokenId(),
					System.currentTimeMillis(), 30 * 1000);
		} catch (Exception e) {
			logger.error("将成功发短验的时间放入缓存中时异常", e);
		}
	}

	/**
	 * 构造外部用户请求
	 *
	 * @param payRequest
	 * @return
	 */
	private ExternalUserRequestDTO buildUserRequestDTO(PaymentRequest payRequest) {
		ExternalUserRequestDTO userRequestDto = new ExternalUserRequestDTO(payRequest);
		return userRequestDto;
	}

	/**
	 * 构造支付记录
	 *
	 * @param requestDto
	 * @param payRequest
	 * @param memberId
	 * @param payType
	 * @param cardId
	 * @return
	 */
	private PaymentRecord buildPaymentRecord(CashierPaymentRequestDTO requestDto,
			PaymentRequest payRequest, String memberId, CardInfoTypeEnum payType, String cardId) {
		CardInfoDTO cardInfoDTO = requestDto.getCardInfo();
		PaymentRecord paymentRecord = new PaymentRecord();

		paymentRecord.setAreaInfo(payRequest.getAreaInfo());
		paymentRecord.setBankCode(cardInfoDTO.getBankCode());
		paymentRecord.setBindId(cardId);
		paymentRecord.setBizModeCode(payRequest.getBizModeCode());
		paymentRecord.setCardNo(StringUtils.isNotBlank(payRequest.getCardNo())
				? payRequest.getCardNo() : cardInfoDTO.getCardno());
		paymentRecord.setCardType(cardInfoDTO.getCardType());
		paymentRecord.setPhoneNo(StringUtils.isNotBlank(payRequest.getPhoneNo())
				? payRequest.getPhoneNo() : cardInfoDTO.getPhone());
		paymentRecord.setOwner(StringUtils.isNotBlank(payRequest.getOwner()) ? payRequest.getOwner()
				: cardInfoDTO.getName());
		paymentRecord.setCreateTime(new Date());
		paymentRecord.setIdCard(StringUtils.isNotBlank(payRequest.getIdCard())
				? payRequest.getIdCard() : cardInfoDTO.getIdno());
		paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
		paymentRecord.setMcc(payRequest.getIndustryCatalog());
		paymentRecord.setMerchantName(payRequest.getMerchantName());
		paymentRecord.setMerchantNo(payRequest.getMerchantNo());
		paymentRecord.setMerchantOrderId(payRequest.getMerchantOrderId());
		paymentRecord.setPaymentAmount(payRequest.getOrderAmount());
		paymentRecord.setPaymentRequestId(payRequest.getId());
		if(CommonUtils.isPayProcess(payRequest.getPaySysCode(),payRequest.getTradeSysNo())){
			paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);

		}else {
			paymentRecord.setPaymentSysNo(PaymentSysCode.NCPAY);

		}
		paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
		// payType不要区分首次和绑卡支付 跟产品确认
		paymentRecord.setPayType(payType.name());
		paymentRecord.setProductName(payRequest.getProductName());
		paymentRecord.setState(PayRecordStatusEnum.INIT);
		paymentRecord.setTokenId(requestDto.getTokenId());
		paymentRecord.setTradeSysNo(payRequest.getTradeSysNo());
		paymentRecord.setTradeSysOrderId(payRequest.getTradeSysOrderId());
		paymentRecord.setOrderOrderId(payRequest.getOrderOrderId());
		paymentRecord.setOrderSysNo(payRequest.getOrderSysNo());
		paymentRecord.setMemberNo(Constant.YIBAO.equals(payRequest.getMemberType())?payRequest.getMemberNo():memberId);
		paymentRecord.setMemberType(StringUtils.isNotBlank(payRequest.getMemberType())? payRequest.getMemberType():Constant.JOINLY);

		paymentRecord.setUpdateTime(new Date());
		paymentRecord.setVersion(1);
		//openYjzf: true,BK_ZF;false,NCPAY
		boolean openYjzf = paymentRequestService.openPayType(payRequest, requestDto.getTokenId(), PayTool.BK_ZF.name());
		paymentRecord.setPayTool(openYjzf ? PayTool.BK_ZF.name() : PayTool.NCPAY.name());
		return paymentRecord;
	}

	/**
	 * 构造临时卡信息
	 *
	 * @param cardInfoDTO
	 * @param payRequest
	 * @return
	 */
	private CardInfoDTO buildCardInfoDTO(CardInfoDTO cardInfoDTO, PaymentRequest payRequest,BindLimitInfoResDTO bindLimitInfoResDTO) {
        CardInfoDTO cardInfo = new CardInfoDTO();
		cardInfo.setCardno(cardInfoDTO.getCardno());
		if (StringUtils.isNotEmpty(payRequest.getPhoneNo())) {
			cardInfo.setPhone(payRequest.getPhoneNo());
		} else {
			cardInfo.setPhone(cardInfoDTO.getPhone());
		}
		if(bindLimitInfoResDTO !=null &&StringUtils.isNotEmpty(bindLimitInfoResDTO.getUserNameLimit())){
			cardInfo.setName(bindLimitInfoResDTO.getUserNameLimit());
		}else if (StringUtils.isNotEmpty(payRequest.getOwner())) {
			cardInfo.setName(payRequest.getOwner());
		} else {
			cardInfo.setName(cardInfoDTO.getName());
		}
		if(bindLimitInfoResDTO !=null &&StringUtils.isNotEmpty(bindLimitInfoResDTO.getIdentityNoLimit())){
			cardInfo.setIdno(bindLimitInfoResDTO.getIdentityNoLimit());
		}else if(StringUtils.isNotEmpty(payRequest.getIdCard())){
			cardInfo.setIdno(payRequest.getIdCard());
		} else {
			cardInfo.setIdno(cardInfoDTO.getIdno());
		}

		cardInfo.setIdType(IdcardType.ID.name());
		cardInfo.setCvv2(cardInfoDTO.getCvv2());
		cardInfo.setValid(cardInfoDTO.getValid());
		cardInfo.setPass(cardInfoDTO.getPass());

		if(cardInfoDTO.getCardType() == null){
			cardInfo.setCardType(null);
		}else {
			cardInfo.setCardType(cardInfoDTO.getCardType());
		}
		cardInfo.setBankCode(cardInfoDTO.getBankCode());
		cardInfo.setBankName(cardInfoDTO.getBankName());
		return cardInfo;
	}

	private PayTmpCardDTO buildPayTmpCardDTO(CardInfoDTO cardInfoDTO){
        PayTmpCardDTO tmpCard = new PayTmpCardDTO();
        tmpCard.setCardNo(cardInfoDTO.getCardno());
        tmpCard.setPhoneNum(cardInfoDTO.getPhone());
        tmpCard.setUserName(cardInfoDTO.getName());
        tmpCard.setUserCardId(cardInfoDTO.getIdno());
        tmpCard.setUserCardType(IdcardType.ID);
        tmpCard.setCardCvn2(cardInfoDTO.getCvv2());
        tmpCard.setCardExpireDate(cardInfoDTO.getValid());
        tmpCard.setCardPin(cardInfoDTO.getPass());
        if(cardInfoDTO.getCardType() == null){
            tmpCard.setCardType(null);
        }else if (cardInfoDTO.getCardType().equals(CardTypeEnum.DEBIT.name())) {
            tmpCard.setCardType(BankCardType.DEBITCARD);
        } else if (cardInfoDTO.getCardType().equals(CardTypeEnum.CREDIT.name())) {
            tmpCard.setCardType(BankCardType.CREDITCARD);
        }
        tmpCard.setBankCode(cardInfoDTO.getBankCode());
        tmpCard.setBankName(cardInfoDTO.getBankName());
        return tmpCard;
    
	}
	
    private PayTmpCardDTO buildPayTmpCardDTO(CardInfoDTO cardInfoDTO, PaymentRequest payRequest,BindLimitInfoResDTO bindLimitInfoResDTO) {
        PayTmpCardDTO tmpCard = new PayTmpCardDTO();
        tmpCard.setCardNo(cardInfoDTO.getCardno());
        if (StringUtils.isNotEmpty(payRequest.getPhoneNo())) {
            tmpCard.setPhoneNum(payRequest.getPhoneNo());
        } else {
            tmpCard.setPhoneNum(cardInfoDTO.getPhone());
        }
        if(bindLimitInfoResDTO !=null &&StringUtils.isNotEmpty(bindLimitInfoResDTO.getUserNameLimit())){
            tmpCard.setUserName(bindLimitInfoResDTO.getUserNameLimit());
        }else if (StringUtils.isNotEmpty(payRequest.getOwner())) {
            tmpCard.setUserName(payRequest.getOwner());
        } else {
            tmpCard.setUserName(cardInfoDTO.getName());
        }
        if(bindLimitInfoResDTO !=null &&StringUtils.isNotEmpty(bindLimitInfoResDTO.getIdentityNoLimit())){
            tmpCard.setUserCardId(bindLimitInfoResDTO.getIdentityNoLimit());
        }else if(StringUtils.isNotEmpty(payRequest.getIdCard())){
            tmpCard.setUserCardId(payRequest.getIdCard());
        } else {
            tmpCard.setUserCardId(cardInfoDTO.getIdno());
        }

        tmpCard.setUserCardType(IdcardType.ID);
        tmpCard.setCardCvn2(cardInfoDTO.getCvv2());
        tmpCard.setCardExpireDate(cardInfoDTO.getValid());
        tmpCard.setCardPin(cardInfoDTO.getPass());

        if(cardInfoDTO.getCardType() == null){
            tmpCard.setCardType(null);
        }else if (cardInfoDTO.getCardType().equals(CardTypeEnum.DEBIT.name())) {
            tmpCard.setCardType(BankCardType.DEBITCARD);
        } else if (cardInfoDTO.getCardType().equals(CardTypeEnum.CREDIT.name())) {
            tmpCard.setCardType(BankCardType.CREDITCARD);
        }
        tmpCard.setBankCode(cardInfoDTO.getBankCode());
        tmpCard.setBankName(cardInfoDTO.getBankName());
        return tmpCard;
    }

	/**
	 * 补充卡信息
	 *
	 * @param requestDto
	 */
	private void supplyCardInfo(CashierPaymentRequestDTO requestDto) {
		CardInfoDTO cardInfoDTO = requestDto.getCardInfo();

		if (requestDto.getOrderType() == NCCashierOrderTypeEnum.BIND) {
			BindCardDTO bindCardDTO = cwhService.getBindCardInfoByBindId(requestDto.getBindId());
			if (null != bindCardDTO && bindCardDTO.getStatus() == BindCardStatus.VALID) {
				BankCardDetailDTO bankCardDetailDTO =
						cwhService.getBankCard(bindCardDTO.getCardId());
				if (bankCardDetailDTO != null) {
					BaseInfo baseInfo = bankCardDetailDTO.getBaseInfo();
					if (baseInfo != null) {
						if (cardInfoDTO == null) {
							cardInfoDTO = new CardInfoDTO();
							requestDto.setCardInfo(cardInfoDTO);
						}

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
					} else {
						throw CommonUtil.handleException(Errors.SYSTEM_BINDID_NULL);
					}
				} else {
					throw CommonUtil.handleException(Errors.SYSTEM_BINDID_NULL);
				}
			} else {
				throw CommonUtil.handleException(Errors.SYSTEM_BINDID_NULL);
			}
		}
	}

	/**
	 * 构造ncpay下单请求
	 *
	 * @param payRecord
	 * @param type
	 * @return
	 * @throws CashierBusinessException
	 */
	private RequestPaymentParam buildNcPayPaymentRequest(PaymentRequest paymentRequest,PaymentRecord payRecord,
			CardInfoTypeEnum type,CashierPaymentRequestDTO requestDto,CardInfoDTO cardInfo) throws CashierBusinessException {
		RequestPaymentParam param = new RequestPaymentParam();
		param.setBizType(Long.valueOf(payRecord.getOrderSysNo()));
		param.setBizOrderNum(payRecord.getOrderOrderId());
		param.setOrderType(OrderTypeEnum.SALE);
		param.setBizOrderDate(payRecord.getCreateTime());
		param.setMerchantNo(payRecord.getMerchantNo());
		param.setMerchantName(payRecord.getMerchantName());
		param.setProductName(payRecord.getProductName());
		param.setRequestNo(payRecord.getMerchantOrderId());
		param.setMemberType(MemberTypeEnum.valueOf(payRecord.getMemberType()));
		param.setMemberNO(payRecord.getMemberNo());
		param.setOrderAmount(payRecord.getPaymentAmount());
		param.setCardInfoType(type);
		param.setCardInfoId(StringUtils.isNotBlank(payRecord.getBindId()) ?  Long.parseLong(payRecord.getBindId()) : null);
		String riskInfo = buildTradeRiskInfoUseTokenAndRequest(requestDto.getTokenId(),paymentRequest);
		param.setGoodsInfo(riskInfo);
		param.setPayToolId(payRecord.getPayProductCode());
		param.setIndustryCode(payRecord.getMcc());
		param.setTerminalId(payRecord.getBizModeCode());
		param.setRequestSystem(Constant.NCCASHIER_MQ_QUEUE_NAME);

		param.setSignRedirectUrl(requestDto.getSignRedirectUrl());
		param.setPayRedirectUrl(requestDto.getPayRedirectUrl());

		if (CardInfoTypeEnum.TEMP.equals(type)) {
            param.setCardInfoId(null);
            param.setCardInfoDTO(cardInfo.transferNcPayCardInfoDTO());
        }

		/*设置零售产品码和基础产品码*/
		param.setBasicProductCode(CommonUtil.getBasicProductCode(PayTool.NCPAY.name(),paymentRequest.getTradeSysNo()));
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		param.setRetailProductCode(jsonObject.getString("saleProductCode"));
		if(jsonObject.get("groupTag") != null){
			Map<String, String> extMap = new HashMap<String, String>();
			extMap.put("groupTag",(String) jsonObject.get("groupTag"));
			param.setExtParam(extMap);
		}
		return param;
	}

	/**
	 * 构造订单处理器ncpay下单请求
	 *
	 * @param paymentRequest
	 * @param payRecord
	 * @param type
	 * @param riskInfo 组装完成的风控参数
	 * @return
	 * @throws CashierBusinessException
	 */
	private NcPayOrderRequestDTO buildNcPayOrderRequestDTO(PaymentRequest paymentRequest,PaymentRecord payRecord,
			CardInfoTypeEnum type,String riskInfo,CardInfoDTO cardInfo,CashierPaymentRequestDTO requestDto) throws CashierBusinessException {
		NcPayOrderRequestDTO param = new NcPayOrderRequestDTO();
		param.setGoodsInfo(riskInfo);
		buildBasicRequestDTO(paymentRequest,param);
		param.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
		param.setPayProduct(StringUtils.isBlank(payRecord.getPayTool())?PayTool.NCPAY.name():payRecord.getPayTool());
		param.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
		param.setPayScene(paymentRequest.getBizModeCode());
		param.setPayOrderType(PayOrderType.SALE);
		param.setMemberType(MemberTypeEnum.valueOf(payRecord.getMemberType()));
		param.setMemberNO(payRecord.getMemberNo());
		param.setCardInfoType(type);
        param.setCardInfoId(StringUtils.isNotBlank(payRecord.getBindId()) ? Long.parseLong(payRecord.getBindId()) : null);
		param.setPayTool(payRecord.getPayProductCode());

		if (requestDto != null){
            param.setSignRedirectUrl(requestDto.getSignRedirectUrl());
            param.setPayRedirectUrl(requestDto.getPayRedirectUrl());

        }
		if (CardInfoTypeEnum.TEMP.equals(type) && cardInfo != null) {
            param.setCardInfoId(null);
            param.setBankCardInfoDTO(cardInfo.transferPayProcessBankCardInfoDTO());

        }
		/*设置零售产品码和基础产品码*/
		param.setBasicProductCode(CommonUtil.getBasicProductCode(param.getPayProduct(), paymentRequest.getTradeSysNo()));
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		param.setRetailProductCode(jsonObject.getString("saleProductCode"));
		if(jsonObject.get("groupTag") != null){
			Map<String, String> extMap = new HashMap<String, String>();
			extMap.put("groupTag",(String) jsonObject.get("groupTag"));
			param.setExtParam(extMap);
		}
		return param;
	}
	
	private void buildCompPayInfo(String token, PaymentRequest paymentRequest, NcPayOrderRequestDTO param,
			PaymentRecord payRecord) {
		// 组合支付信息
		Set<String> marketActivityIds = marketInfoManageService.getMarketActivityInfoByPayInfo(token, paymentRequest,
				param.getPayProduct(), payRecord.getCardType(), payRecord.getBankCode());
		if(CollectionUtils.isEmpty(marketActivityIds)){
			return;
		}
		if(param.getBankCardInfoDTO()==null){
			BankCardInfoDTO bankCardInfoDTO = new BankCardInfoDTO();
			bankCardInfoDTO.setBankCode(payRecord.getBankCode());
			bankCardInfoDTO.setCardType(payRecord.getCardType());
			param.setBankCardInfoDTO(bankCardInfoDTO);
		}
		CombRequestDTO combRequestDTO = new CombRequestDTO();
		combRequestDTO.setMarketingNo(marketActivityIds.size()==1?marketActivityIds.iterator().next():JSON.toJSONString(marketActivityIds));
		combRequestDTO.setPayOrderType(CombPayOrderTypeEnum.MKTG.name());
		if (param.getBankCardInfoDTO() != null){
			combRequestDTO.setPaymentType(param.getBankCardInfoDTO().getCardType());
		}
		param.setCombRequestDTO(combRequestDTO);
	}
	
	/**
	 * 
	 * @param needItem
	 * @param needItemByUserInput
	 * @param needBankCardDTO
	 * @param paymentRequest
	 * @return 是否需要补充项
	 */
	private boolean setNeedItem(int needItem, NeedBankCardDTO needItemByUserInput, NeedBankCardDTO needBankCardDTO, PaymentRequest paymentRequest) {
		if (needItem == 0) {
			return false;
		}
		NCPayParamMode nCPayParamMode = new NCPayParamMode(needItem);
		NeedSurportDTO needSurportDTO = new NeedSurportDTO();
		boolean loseSomeInfo = false;
		if (nCPayParamMode.needAvlidDate()) {
			needBankCardDTO.setAvlidDate(needItemByUserInput.getAvlidDate());
			if (StringUtils.isBlank(needItemByUserInput.getAvlidDate())) {
				needSurportDTO.setAvlidDateIsNeed(true);
				loseSomeInfo = true;
			}
		}
		if (nCPayParamMode.needBankMobilePhone()) {
			needBankCardDTO.setPhoneNo(needItemByUserInput.getPhoneNo());
			if (StringUtils.isBlank(needItemByUserInput.getPhoneNo())) {
				needSurportDTO.setPhoneNoIsNeed(true);
				loseSomeInfo = true;
			}
		}
		// 借记卡支付密码（易宝短验的情况才会生效）
		if (nCPayParamMode.needBankPWD()) {
			needBankCardDTO.setBankPWD(needItemByUserInput.getBankPWD());
			if (StringUtils.isBlank(needItemByUserInput.getBankPWD())) {
				needSurportDTO.setBankPWDIsNeed(true);
				loseSomeInfo = true;
			}
		}
		if (nCPayParamMode.needCvv()) {
			needBankCardDTO.setCvv(needItemByUserInput.getCvv());
			if (StringUtils.isBlank(needItemByUserInput.getCvv())) {
				needSurportDTO.setCvvIsNeed(true);
				loseSomeInfo = true;
			}
		}
		if (nCPayParamMode.needIdCardType()) {
			needBankCardDTO.setIdCardType(needItemByUserInput.getIdCardType());
			if (needItemByUserInput.getIdCardType() == null) {
				needSurportDTO.setIdCardTypeIsNeed(true);
				loseSomeInfo = true;
			}
		}
		if (nCPayParamMode.needNumber()) {
			needBankCardDTO.setCardno(needItemByUserInput.getCardno());
			if (StringUtils.isBlank(needItemByUserInput.getCardno())) {
				needSurportDTO.setCardnoIsNeed(true);
				loseSomeInfo = true;
			}
		}
		if (nCPayParamMode.needYeepayMobilePhone()) {
			needSurportDTO.setYpMobileIsNeed(true);
			loseSomeInfo = true;
		}

		// ncpay需要补充，但是绑卡限制值有值，则不需要补充 -- 需要重新整理一下下面这段逻辑
		checkNeedItemByBindLimit(paymentRequest, nCPayParamMode.needUserName(), nCPayParamMode.needIdCardNumber(),
				needSurportDTO);
		if (needSurportDTO.getIdnoIsNeed()) {
			needBankCardDTO.setIdno(needItemByUserInput.getIdno());
			if (StringUtils.isBlank(needItemByUserInput.getIdno())) {
				needSurportDTO.setIdnoIsNeed(true);
				loseSomeInfo = true;
			}
		}
		if (needSurportDTO.getOwnerIsNeed()) {
			needBankCardDTO.setOwner(needItemByUserInput.getOwner());
			if (StringUtils.isBlank(needItemByUserInput.getOwner())) {
				needSurportDTO.setOwnerIsNeed(true);
				loseSomeInfo = true;
			}
		}
		needBankCardDTO.setNeedSurportDTO(needSurportDTO);
		return loseSomeInfo;
	}

	/**
	 * 获取绑卡补充项并设置透传项
	 * @param needItem
	 * @param paymentRequest
	 * @param bkdto
	 * @return
	 */
	private NeedSurportDTO setCardNeedSupplement(int needItem, PaymentRequest paymentRequest, NeedBankCardDTO bkdto){
		//NCPAY返回的当前卡所需补充项不为空
		NeedSurportDTO needSurportDTO = null;
		if (needItem != 0) {
			// 获取透传值  后续设置NeedBankCardDTO返回值，将必填且有值得项塞进去
			BindLimitInfoResDTO samePersonInfo = bankCardLimitInfoService.getLimitInfo4bind(paymentRequest);
			PassCardInfoDTO passCardInfoDTO = cashierBankCardService.getPassCardInfo(paymentRequest);
			NCPayParamMode nCPayParamMode = new NCPayParamMode(needItem);
			logger.info("ncpay所需补充项={}", nCPayParamMode);
			needSurportDTO = new NeedSurportDTO();
			if (nCPayParamMode.needAvlidDate()) {
				needSurportDTO.setAvlidDateIsNeed(true);
			}
			if (nCPayParamMode.needBankMobilePhone()) {
				needSurportDTO.setPhoneNoIsNeed(true);
				//手机号
				bkdto.setPhoneNo((passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getPhone()))?passCardInfoDTO.getPhone():null);
			}//密码
			if (nCPayParamMode.needBankPWD()) {
				needSurportDTO.setBankPWDIsNeed(true);
			}//CVV
			if (nCPayParamMode.needCvv()) {
				needSurportDTO.setCvvIsNeed(true);
			}//证件号
			if (nCPayParamMode.needIdCardNumber()) {
				needSurportDTO.setIdnoIsNeed(true);
				bkdto.setIdno((passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getIdNo()))?passCardInfoDTO.getIdNo():null);
			}//证件类型
			if (nCPayParamMode.needIdCardType()) {
				needSurportDTO.setIdCardTypeIsNeed(true);
				if(passCardInfoDTO!=null && StringUtils.isNotBlank(passCardInfoDTO.getIdType())){
					IdcardType idcardType = getIdCardType(passCardInfoDTO.getIdType());
					bkdto.setIdCardType(idcardType);
				}
			}
			if (nCPayParamMode.needNumber()) {
				needSurportDTO.setCardnoIsNeed(true);
				bkdto.setCardno((passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getCardNo()))?passCardInfoDTO.getCardNo():null);
			}//姓名
			if (nCPayParamMode.needUserName()) {
				needSurportDTO.setOwnerIsNeed(true);
				bkdto.setOwner((passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getOwner()))?passCardInfoDTO.getOwner():null);
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
				// 需要补充证件号、姓名的交易放入redis
				if (nCPayParamMode.needUserName()
						|| nCPayParamMode.needIdCardNumber()) {
					RedisTemplate.setCacheObjectSumValue(
							Constant.MISS_NEEDITEM_BIND
									+ paymentRequest.getId(), needSurportDTO,
							Constant.NCCASHIER_CARD_LIMIT_TIME);
				}
			}
			bkdto.setNeedSurportDTO(needSurportDTO);
			
		}
		
		return needSurportDTO;
	}
	
	
	private void checkNeedItemByBindLimit(PaymentRequest paymentRequest, boolean needUserName,
			boolean needIdNo, NeedSurportDTO needSurportDTO) {
		
		if(!needUserName && !needIdNo){
			return;
		}
		if(needUserName){
			needSurportDTO.setOwnerIsNeed(true);
		}
		if(needIdNo){
			needSurportDTO.setIdnoIsNeed(true);
		}
		
		BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo(paymentRequest);
		if (bindLimitInfoResDTO != null
				&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO
						.getBindCardLimitType())
				&& StringUtils.isNotBlank(bindLimitInfoResDTO
						.getIdentityNoLimit())
				&& StringUtils
						.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())) {
			if (needUserName) {
				needSurportDTO.setOwnerIsNeed(false);
			}
			if (needIdNo) {
				needSurportDTO.setIdnoIsNeed(false);
			}
		}
		
		
	}
	
	/**
	 * 证件类型转换
	 * @param idCardType
	 * @return
	 */
	private IdcardType getIdCardType(String idCardType){
		if(idCardType!=null && "IDENTITY".equals(idCardType)){
			return IdcardType.ID;
		}
		return null;
	}

	@Override
	public CombinedPaymentDTO buildCombinedPaymentDTO(PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		CombinedPaymentDTO combinedPaymentDto = new CombinedPaymentDTO();
		combinedPaymentDto.setPaymentRequest(paymentRequest);
		combinedPaymentDto.setPaymentRecord(paymentRecord);
		return combinedPaymentDto;
	}

	@Override
	public long addPayTmpCard(CardInfoDTO cardInfoDTO, PaymentRequest payRequest, BindLimitInfoResDTO bindLimitInfoResDTO) {
		PayTmpCardDTO tmpCard = buildPayTmpCardDTO(cardInfoDTO, payRequest, bindLimitInfoResDTO);
		long tmpid = cwhService.addPayTmpCard(tmpCard);
		return tmpid;
	}
	
	@Override
	public long addPayTmpCard(CardInfoDTO cardInfoDTO){
		PayTmpCardDTO tmpCard = buildPayTmpCardDTO(cardInfoDTO);
		return cwhService.addPayTmpCard(tmpCard);
	}

	@Override
	public NcPayOrderResponseDTO payProcessorRequestOrder(PaymentRequest paymentRequest, PaymentRecord payRecord, CardInfoTypeEnum cardInfoType) {
		String riskInfo = buildTradeRiskInfoByUseripAndRequest(paymentRequest.getUserIp(), paymentRequest);
		NcPayOrderRequestDTO requesDTO = buildNcPayOrderRequestDTO(paymentRequest,payRecord,cardInfoType,riskInfo,null,null);
		NcPayOrderResponseDTO responseDTO = payProcessorService.ncPayRequest(requesDTO);
		return responseDTO;
	}

}
