package com.yeepay.g3.core.nccashier.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.biz.NcCashierCoreBiz;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.BankCardLimitInfoService;
import com.yeepay.g3.core.nccashier.service.CashierPageInfoService;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.service.QueryResultService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.facade.cwh.enumtype.IdcardType;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.cwh.param.PayTmpCardDTO;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitRequestDto;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitResponseDto;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.CommonUtils;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.NcPayConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordQueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tanzhen
 *
 */
@Service("ncCashierCoreBiz")
public class NcCashierCoreBizImpl extends NcCashierBaseBizImpl implements NcCashierCoreBiz {
	private static final Logger logger = NcCashierLoggerFactory.getLogger(NcCashierBaseBizImpl.class);
	@Resource
	private OrderPaymentService orderPaymentService;

	@Resource
	private QueryResultService queryResultService;
	@Resource
	private BankCardLimitInfoService bankCardLimitInfoService;
	@Resource
	private CashierPageInfoService cashierPageInfoService;
	@Override
	public CashierPaymentResponseDTO createPayment(CashierPaymentRequestDTO paymentRequestDto) {
		CashierPaymentResponseDTO response = new CashierPaymentResponseDTO();
		try {
			basicValidatePaymentRequest(paymentRequestDto, response);
			if (NCCashierOrderTypeEnum.FIRST == paymentRequestDto.getOrderType()) {
				firstPayCreatePayment(paymentRequestDto, response);
			} else {
				bindPayCreatePayment(paymentRequestDto, response);
			}
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}


	@Override
	public CashierSmsSendResponseDTO sendSms(CashierSmsSendRequestDTO smsRequest) {
		CashierSmsSendResponseDTO response = new CashierSmsSendResponseDTO();
		try {
			basicValidateSmsRequest(smsRequest, response);
			String payOrderId = orderPaymentService.validateSmsBusinInfo(smsRequest);
			NeedBankCardDTO needBankCardDTO = smsRequest.getNeedBankCardDTO(); 
			PaymentRequest payRequest = validateRequest(smsRequest.getRequestId());
			PaymentRecord payrecord  = validatePayRecord(smsRequest.getRecordId());
            CardInfoDTO cardInfoDTO = null;
			if(payrecord.getNeedItem() != 0||null != needBankCardDTO){
                cardInfoDTO = this.buildCardInfoDTO(payrecord, needBankCardDTO, payRequest, true);
			}
			// 日志监控埋点
			logger.info("[monitor],event:nccashier_sendSms_request,merchantOrderId:{},requestId:{},merchantNo:{}", payRequest.getMerchantOrderId(),payRequest.getId(),payRequest.getMerchantNo());
			orderPaymentService.verifyAndSendSms(payOrderId, smsRequest,cardInfoDTO,payRequest.getTradeSysNo(),payRequest.getPaySysCode());
			response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}

	private void basicValidateSmsRequest(CashierSmsSendRequestDTO smsRequest,
			CashierSmsSendResponseDTO response) {
		BeanValidator.validate(smsRequest);

		response.setTokenId(smsRequest.getTokenId());
		NcCashierLoggerFactory.TAG_LOCAL.set("[sendSms],支付请求ID=" + smsRequest.getRequestId()
				+ ",支付记录ID=" + smsRequest.getRecordId() + "]");

	}

	@Override
	public CashierQueryResponseDTO queryPayResult(CashierQueryRequestDTO queryRequestDto) {
		CashierQueryResponseDTO response = new CashierQueryResponseDTO();
		try {
			basicValidateQueryRequest(queryRequestDto, response);
			CombinedPaymentDTO combinedPaymentDTO =
					queryResultService.validateQueryBusinInfo(queryRequestDto);
			logger.info("[monitor],event:nccashier_queryResult_request,merchantOrderId:{},requestId:{},merchantNo:{}", combinedPaymentDTO.getPaymentRequest().getMerchantOrderId(), combinedPaymentDTO.getPaymentRequest().getId(), combinedPaymentDTO.getPaymentRequest().getMerchantNo());
			queryResultService.supplyQureyResult(combinedPaymentDTO, queryRequestDto, response);
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}

	@Override
	public CashierPayResponseDTO firstPay(CashierFirstPayRequestDTO firstPayRequest) {
		CashierPayResponseDTO response = new CashierPayResponseDTO();
		try {
			BeanValidator.validate(firstPayRequest);
			NcCashierLoggerFactory.TAG_LOCAL
					.set("[首次确认支付|firstPay],支付请求requestId=" + firstPayRequest.getPaymentRequestId()
							+ ",支付记录recordId=" + firstPayRequest.getPaymentRecordId());
			// 获取请求订单
			PaymentRequest payRequest = validateRequest(firstPayRequest.getPaymentRequestId());
			if (PayRequestStatusEnum.SUCCESS.getValue().equals(payRequest.getState())
					|| PayRequestStatusEnum.FAILED.getValue().equals(payRequest.getState())) {
				response.setRequestStatus(PayRequestStatusEnum.valueOf(payRequest.getState()));
				return response;
			} else {
				// 校验支付订单
				PaymentRecord payrecord = validatePayRecord(firstPayRequest.getPaymentRecordId());

				//校验短验
				if (StringUtils.isBlank(firstPayRequest.getVerifycode()) && (StringUtils.isBlank(payrecord.getRedirectType()) || RedirectTypeEnum.NONE.name().equalsIgnoreCase(payrecord.getRedirectType()))){
					throw CommonUtil.handleException(Errors.VERIFYCODE_MISS);
				}

				if (StringUtils.isNotBlank(payrecord.getRedirectType()) && RedirectTypeEnum.SIGN.name().equalsIgnoreCase(payrecord.getRedirectType())){
					CardInfoDTO cardInfoDTO = new CardInfoDTO();
					cardInfoDTO.setCardno(payrecord.getCardNo());
					firstPayRequest.setCardInfo(cardInfoDTO);
				}

				// 首次支付判断卡信息是否变更
				if (cashierBankCardService.validateCardNeed(firstPayRequest.getCardInfo(),
						payRequest, payrecord)) {
					throw CommonUtil.handleException(Errors.GET_SMS_FIRST);
				}
				// 校验验证码逻辑
				volidateVerifyCode(firstPayRequest.getVerifycode(), payrecord, Constant.YJZF_FIRST_PAY);
				// 成功、支付中、失败 返回支付订单状态，不再需要调用ncpay
				if (PayRecordStatusEnum.SUCCESS == payrecord.getState()
						|| PayRecordStatusEnum.PAYING == payrecord.getState()
						|| PayRecordStatusEnum.FAILED == payrecord.getState()) {
					response.setRecordStatus(payrecord.getState());
				} else if (PayRecordStatusEnum.ORDERED == payrecord.getState()) {
					response.setRecordStatus(PayRecordStatusEnum.ORDERED);
					// 监控日志埋点
					logger.info("[monitor],event:nccashier_first_confirm_request,merchantOrderId:{},requestId:{},merchantNo:{}", payRequest.getMerchantOrderId(),payRequest.getId(),payRequest.getMerchantNo());
					upRecToPayingAndCallNcpay(payrecord, firstPayRequest.getVerifycode(), null,payRequest);
					response.setRecordStatus(PayRecordStatusEnum.PAYING);
				}
			}

		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}


	private PaymentRequest validateRequest(long requestId) {
		PaymentRequest payRequest = paymentRequestService.findPayRequestById(requestId);
		if (payRequest == null) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		if (paymentRequestService.isRequestExpired(payRequest)) {
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		return payRequest;
	}


	@Override
	public CashierPayResponseDTO bindPay(CashierBindPayRequestDTO bidPayRequest) {
		CashierPayResponseDTO response = new CashierPayResponseDTO();
		try {
			BeanValidator.validate(bidPayRequest);
			NcCashierLoggerFactory.TAG_LOCAL.set("[绑卡确认支付|bindPay],支付请求requestId="
					+ bidPayRequest.getPaymentRequestId() + ",支付记录recordId="
					+ bidPayRequest.getPaymentRecordId() + ",bindId=" + bidPayRequest.getBindId());
			// 获取请求订单
			PaymentRequest payRequest = validateRequest(bidPayRequest.getPaymentRequestId());
			if (PayRequestStatusEnum.SUCCESS.getValue().equals(payRequest.getState())
					|| PayRequestStatusEnum.FAILED.getValue().equals(payRequest.getState())) {
				response.setRequestStatus(PayRequestStatusEnum.valueOf(payRequest.getState()));
				return response;
			} else {
				// 获取支付订单
				PaymentRecord payrecord = validatePayRecord(bidPayRequest.getPaymentRecordId());
				String bkFirstFlag = "";
				if(bidPayRequest.isBkFirst()){
					bkFirstFlag = Constant.BKZF_FIRST_PAY;
				}else {
					bkFirstFlag = RedisTemplate.getTargetFromRedis(Constant.BKZF_FIRST_REDIS_KEY + bidPayRequest.getPaymentRecordId(), String.class);
				}
				// 校验验证码逻辑
				volidateVerifyCode(bidPayRequest.getVerifycode(), payrecord, bkFirstFlag);
                CardInfoDTO cardInfoDTO = buildCardInfoDTO(payrecord, bidPayRequest.getNeedBankCardDTO(), payRequest, true);

                // 成功、支付中、失败 返回支付订单状态，不再需要调用ncpay
				if (PayRecordStatusEnum.SUCCESS == payrecord.getState()
						|| PayRecordStatusEnum.PAYING == payrecord.getState()
						|| PayRecordStatusEnum.FAILED == payrecord.getState()) {
					response.setRecordStatus(payrecord.getState());
				} else if (PayRecordStatusEnum.ORDERED == payrecord.getState()) {
					response.setRecordStatus(PayRecordStatusEnum.ORDERED);
					// 监控日志埋点
					logger.info("[monitor],event:nccashier_bind_confirm_request,merchantOrderId:{},requestId:{},merchantNo:{}", payRequest.getMerchantOrderId(),payRequest.getId(),payRequest.getMerchantNo());
					upRecToPayingAndCallNcpay(payrecord, bidPayRequest.getVerifycode(), cardInfoDTO, payRequest);
					response.setRecordStatus(PayRecordStatusEnum.PAYING);
				}
			}

		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}

	@Override
	public BankLimitAmountListResponseDTO queryBankLimitAmountList(BankLimiAmountRequestDTO bankLimiAmountRequestDTO) {
		BankLimitAmountListResponseDTO bankLimitAmountListResponseDTO = new BankLimitAmountListResponseDTO();
		try {
			NcCashierLoggerFactory.TAG_LOCAL.set("[查询银行限额列表|queryBankLimitAmountList],商编="+bankLimiAmountRequestDTO.getMerchantNo()+",paymentRequestId="+bankLimiAmountRequestDTO.getRequestId());
			PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(Long.parseLong(bankLimiAmountRequestDTO.getRequestId()));;
			MerchantLimitRequestDto merchantLimitRequestDto = buildMerchantLimitDto(paymentRequest);
			List<MerchantLimitResponseDto> limitResponseDtoList = merchantConfigCenterService.queryMerchantLimit(merchantLimitRequestDto);
			buildBankLimitAmountList(limitResponseDtoList,bankLimitAmountListResponseDTO,paymentRequest.getCardType(),paymentRequest.getBankCode());
		}catch (Throwable e){
			handleException(bankLimitAmountListResponseDTO,e);
		}
		return bankLimitAmountListResponseDTO;
	}

	@Override
	public String getYeepayWechatQRCode(String requestId) {
		NcCashierLoggerFactory.TAG_LOCAL.set("[获取易宝公众号二维码|getYeepayWechatQRCode]—[paymentRequestId=" + requestId + "]");
		return cashierPageInfoService.getYeepayWechatQRCode(requestId);
	}

	@Override
	public String getYeepayWechatQRCode(String merchantNo, String merchantOrderId) {
		NcCashierLoggerFactory.TAG_LOCAL.set("[获取易宝公众号二维码|getYeepayWechatQRCode]—[merchantNo=" + merchantNo + "]—[merchantOrderId="+merchantOrderId+"]");
		return cashierPageInfoService.getYeepayWechatQRCode(merchantNo,merchantOrderId);
	}

	private void volidateVerifyCode(String verifyCode, PaymentRecord payrecord,
			String firstPayFlag) {
		boolean isneedVeryCode = true;

		if(PayTool.BK_ZF.name().equals(payrecord.getPayTool()) && Constant.BKZF_FIRST_PAY.equals(firstPayFlag)){
			// 绑卡支付-首次支付，返回的短验类型若不是银行短验，则不需要发短验
			if(!SmsSendTypeEnum.BANK.name().equals(payrecord.getSmsVerifyType())){
				isneedVeryCode = false;
			}
		}
		else{
			SmsSendTypeEnum ncpayResponseSmsType = SmsSendTypeEnum.valueOf(payrecord.getSmsVerifyType());
			// 首次支付必须验证验证码
			if (Constant.YJZF_FIRST_PAY.equals(firstPayFlag)) {
				isneedVeryCode = true;
			}else {
				if (ncpayResponseSmsType == SmsSendTypeEnum.NONE
						&& CommonUtil.getRiskVerifyCodeSwitch()) {
					// ncpay建议发的话，需要校验验证码
					isneedVeryCode = false;
				} else {
					isneedVeryCode = true;
				}
			}
		}

		//跳转url则不需要短验
		if (StringUtils.isNotBlank(payrecord.getRedirectType()) && !RedirectTypeEnum.NONE.name().equalsIgnoreCase(payrecord.getRedirectType())){
			isneedVeryCode = false;
		}

		if (isneedVeryCode && StringUtils.isBlank(verifyCode)) {
			throw CommonUtil.handleException(Errors.VERIFYCODE_MISS);
		}
		if(!CommonUtils.isPayProcess(payrecord.getPaymentSysNo(),payrecord.getTradeSysNo())){
			PayQueryResponseDTO queryResponseDTO =
					ncPayService.queryPaymentOrder(payrecord.getPaymentOrderNo());
			if (isneedVeryCode && (queryResponseDTO == null
					|| queryResponseDTO.getSmsState() == SmsCheckResultEnum.NONE)) {
				throw CommonUtil.handleException(Errors.GET_SMS_FIRST);
			}
		}else{
			PayRecordQueryRequestDTO requestDTO = new PayRecordQueryRequestDTO();
			requestDTO.setRecordNo(payrecord.getPaymentOrderNo());
			PayRecordResponseDTO responseDTO = payProcessorService.query(requestDTO);
			if (isneedVeryCode && (responseDTO.getSmsState() == SmsCheckResultEnum.NONE)) {
				throw CommonUtil.handleException(Errors.GET_SMS_FIRST);
			}
		}
		
	}

	/**
	 * 下单入参基本校验
	 * @param paymentRequestDto
	 */
	private void basicValidatePaymentRequest(CashierPaymentRequestDTO paymentRequestDto,
			CashierPaymentResponseDTO response) {
		basicValidatePaymentRequest(paymentRequestDto);
		response.setTokenId(paymentRequestDto.getTokenId());
	}

	private void basicValidatePaymentRequest(CashierPaymentRequestDTO paymentRequestDto){
		BeanValidator.validate(paymentRequestDto);
		NcCashierLoggerFactory.TAG_LOCAL
		.set("[createPayment],支付请求ID=" + paymentRequestDto.getRequestId() + ",支付记录ID="
				+ paymentRequestDto.getRecordId() + "");
	}

	/**
	 * 首次支付下单
	 * 
	 * @param paymentRequestDto
	 * @param response
	 * @return
	 */
	private void firstPayCreatePayment(CashierPaymentRequestDTO paymentRequestDto,
			CashierPaymentResponseDTO response) {
		CombinedPaymentDTO combinedPaymentDto =
				orderPaymentService.validateFirstPayBusinInfo(paymentRequestDto);
		if (combinedPaymentDto.isNeedOrderRecord()) {
			orderPaymentService.doFirsttPayCreatePayment(paymentRequestDto, combinedPaymentDto);
			// 日志监控埋点
            logger.info("[monitor],event:nccashier_first_createOrder_request,merchantOrderId:{},requestId:{},merchantNo:{}", combinedPaymentDto.getPaymentRequest().getMerchantOrderId(),combinedPaymentDto.getPaymentRequest().getId(),combinedPaymentDto.getPaymentRequest().getMerchantNo());
			PaymentResponseDTO paymentResponseDTO = orderPaymentService
					.callNcPayOrder(paymentRequestDto, combinedPaymentDto, CardInfoTypeEnum.TEMP);
			orderPaymentService.supplyFirstOrderResult(paymentResponseDTO, response,paymentRequestDto);
		} else {
			orderPaymentService.supplyFirstOrderResult(null, response,paymentRequestDto);
		}
	}

	/**
	 * 绑卡支付下单
	 * 
	 * @param paymentRequestDto
	 * @param response
	 * @return
	 */
	private void bindPayCreatePayment(CashierPaymentRequestDTO paymentRequestDto, CashierPaymentResponseDTO response) {
		CombinedPaymentDTO combinedPaymentDto = orderPaymentService.validateBindPayBusinInfo(paymentRequestDto);
		if (combinedPaymentDto.isNeedOrderRecord()) {
			// 日志监控埋点
			logger.info("[monitor],event:nccashier_bind_createOrder_request,merchantOrderId:{},requestId:{},merchantNo:{}", combinedPaymentDto.getPaymentRequest().getMerchantOrderId(),combinedPaymentDto.getPaymentRequest().getId(),combinedPaymentDto.getPaymentRequest().getMerchantNo());
			orderPaymentService.doBindtPayCreatePayment(paymentRequestDto, combinedPaymentDto);
			PaymentResponseDTO paymentResponseDTO = orderPaymentService.callNcPayOrder(paymentRequestDto, combinedPaymentDto, CardInfoTypeEnum.BIND);
			orderPaymentService.supplyBindOrderResult(paymentRequestDto, paymentResponseDTO,response, combinedPaymentDto.getPaymentRequest());
		} else {
			orderPaymentService.supplyBindOrderResult(paymentRequestDto, null, response, combinedPaymentDto.getPaymentRequest());
		}
	}

	private PaymentRecord validatePayRecord(Long paymentRecordId) {
		// 获取支付订单
		PaymentRecord payrecord = getPaymentRecord(paymentRecordId);
		// 支付订单为空，重新创建支付订单
		if (payrecord == null || StringUtils.isEmpty(payrecord.getPaymentOrderNo())) {
			throw CommonUtil.handleException(Errors.GET_SMS_FIRST);
		}
		if (PayRecordStatusEnum.FAILED == payrecord.getState()) {
			throw CommonUtil.handleException(Errors.THRANS_FINISHED);
		}
		return payrecord;
	}

	private void basicValidateQueryRequest(CashierQueryRequestDTO queryRequestDto,
			CashierQueryResponseDTO response) {
		BeanValidator.validate(queryRequestDto);
		response.setRecordId(queryRequestDto.getRecordId());
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[queryPayResult],支付请求ID=" + queryRequestDto.getRequestId() + ",支付记录ID="
						+ queryRequestDto.getRecordId() + "]");
	}

	@Override
	public void upRecToPayingAndCallNcpay(PaymentRecord payrecord, String verifyCode, CardInfoDTO cardInfo, PaymentRequest payRequest) {
		//确认支付时需要record中的状态为ordered,确认支付会把状态改为paying，且只有当短验错误时，才会把状态回滚为ordered，以防止重复请求
		boolean isRepeatePay = updateRecordToPaying(payrecord.getId());
		if (isRepeatePay) {
			//是重复支付，直接返回
			return;
		}
		if (!CommonUtils.isPayProcess(payrecord.getPaymentSysNo(), payrecord.getTradeSysNo())) {//非订单处理器请求，走NCPAY
			PayConfirmRequestDTO payConfirmRequestDTO = new PayConfirmRequestDTO();
            JSONObject extendInfoJson = CommonUtil.parseJson(payRequest.getExtendInfo());
            if(extendInfoJson !=null && extendInfoJson.get("groupTag") != null){
                Map<String, String> extMap = new HashMap<String, String>();
                extMap.put("groupTag",(String) extendInfoJson.get("groupTag"));
                payConfirmRequestDTO.setExtParam(extMap);
            }
			payConfirmRequestDTO.setPayOrderId(payrecord.getPaymentOrderNo());
			payConfirmRequestDTO.setSmsCode(verifyCode);
			// 设置补充项临时卡信息
			if (cardInfo != null) {
				payConfirmRequestDTO.setCardInfoDTO(cardInfo.transferNcPayCardInfoDTO());
			}
			PayConfirmResponseDTO confirmResponse = null;
			try {
				confirmResponse = ncPayService.confirmPay(payConfirmRequestDTO);
			} catch (CashierBusinessException e) {
				// 针对短信校验的出错误码，可以支持重复支付，支付订单的状态回滚
				// N400094 短信验证码错误
				// N400091 短语验证码已经过期
				String errorCode = e.getDefineCode();
				if ("N400094".equals(errorCode) || "N400091".equals(errorCode)) {
					recoverRecordToOrdered(payrecord.getId());
				}
				throw e;
			}
			if (confirmResponse != null) {
				if (StringUtils.isNotBlank(confirmResponse.getErrorCode())) {
					throw CommonUtil.handleException(SysCodeEnum.NCPAY.name(), confirmResponse.getErrorCode(), confirmResponse.getErrorMsg());
				}
			}
		} else {
			NcPayConfirmRequestDTO confirmDTO = new NcPayConfirmRequestDTO();
			confirmDTO.setRecordNo(payrecord.getPaymentOrderNo());
			confirmDTO.setSmsCode(verifyCode);

			JSONObject extendInfoJson = CommonUtil.parseJson(payRequest.getExtendInfo());
			if(extendInfoJson !=null && extendInfoJson.get("groupTag") != null){
				Map<String, String> extMap = new HashMap<String, String>();
				extMap.put("groupTag",(String) extendInfoJson.get("groupTag"));
				confirmDTO.setExtParam(extMap);
			}

			// 设置补充项临时卡信息
			if (cardInfo != null) {
				confirmDTO.setBankCardInfoDTO(cardInfo.transferPayProcessBankCardInfoDTO());
			}
			try {
				payProcessorService.confirmPay(confirmDTO);
			} catch (CashierBusinessException e) {
				// 针对短信校验的出错误码，可以支持重复支付，支付订单的状态回滚
				// N400094 短信验证码错误
				// N400091 短语验证码已经过期
				String errorCode = e.getDefineCode();
				if ("N400094".equals(errorCode) || "N400091".equals(errorCode)) {
					recoverRecordToOrdered(payrecord.getId());
				}
				throw e;
			}
		}
	}

	@Override
	public void payprocessorSyncConfirmPay(PaymentRecord payrecord, String verifyCode, Long tmpCardId,PaymentRequest payRequest) {
		//一定是订单处理器请求，走PP
		NcPayConfirmRequestDTO confirmDTO = new NcPayConfirmRequestDTO();
		confirmDTO.setRecordNo(payrecord.getPaymentOrderNo());
		confirmDTO.setSmsCode(verifyCode);
		if (tmpCardId != null && tmpCardId > 0) {
			confirmDTO.setTmpCardId(tmpCardId);
		}

		JSONObject extendInfoJson = CommonUtil.parseJson(payRequest.getExtendInfo());
		if(extendInfoJson !=null && extendInfoJson.get("groupTag") != null){
			Map<String, String> extMap = new HashMap<String, String>();
			extMap.put("groupTag",(String) extendInfoJson.get("groupTag"));
			confirmDTO.setExtParam(extMap);
		}

		payProcessorService.synConfirmPay(confirmDTO);
	}

	// 防重处理 TODO 改造一下
	private boolean updateRecordToPaying(Long paymentRecordId) {
		boolean isRepeatePay = false;
		try {
			paymentProcessService.updateRecordStateBaseOnOriginalStatus(paymentRecordId,
					PayRecordStatusEnum.PAYING,
					Arrays.asList(new PayRecordStatusEnum[] {PayRecordStatusEnum.ORDERED}));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为paying状态失败", e);
			// 防重处理，失败不抛出异常
			isRepeatePay = true;
		}
		return isRepeatePay;
	}

	private void recoverRecordToOrdered(Long paymentRecordId) {
		try {
			logger.info("短信校验失败，开始恢复支付订单为ORDERED状态");
			paymentProcessService.updateRecordStateBaseOnOriginalStatus(paymentRecordId,
					PayRecordStatusEnum.ORDERED,
					Arrays.asList(new PayRecordStatusEnum[] {PayRecordStatusEnum.PAYING}));
		} catch (CashierBusinessException e) {
			logger.error("更新支付订单为ORDERED状态失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public Long saveTmpCard(PaymentRecord payrecord, NeedBankCardDTO needBankCardDTO, PaymentRequest paymentRequest, boolean checkOnePersion) {
		// 处理补充项逻辑
		PayTmpCardDTO tmpcard = new PayTmpCardDTO();
		boolean isneed = false;
		tmpcard.setCardNo(payrecord.getCardNo());
		if (null!=needBankCardDTO) {
			//获取透传项
			PassCardInfoDTO passCardInfoDTO = cashierBankCardService.getPassCardInfo(paymentRequest);
			if (null!=needBankCardDTO.getAvlidDate()) {
				tmpcard.setCardExpireDate(needBankCardDTO.getAvlidDate());
				isneed = true;
			}
			if (null!=needBankCardDTO.getBankPWD()) {
				tmpcard.setCardPin(needBankCardDTO.getBankPWD());
				isneed = true;
			}
			if (null!=needBankCardDTO.getCvv()) {
				tmpcard.setCardCvn2(needBankCardDTO.getCvv());
				isneed = true;
			}
			if (null!=needBankCardDTO.getOwner()) {
				//姓名需要先解掩码
				String owner = (passCardInfoDTO!=null && StringUtils.isNotBlank(passCardInfoDTO.getOwner()))?passCardInfoDTO.getOwner():needBankCardDTO.getOwner();
				tmpcard.setUserName(owner);
				isneed = true;
			}
			if (null!=needBankCardDTO.getPhoneNo()) {
				//手机号需要先解掩码
				String phoneNo = (passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getPhone()))?passCardInfoDTO.getPhone():needBankCardDTO.getPhoneNo();
				tmpcard.setPhoneNum(phoneNo);
				isneed = true;
			}
			if (null!=needBankCardDTO.getIdno()) {
				tmpcard.setUserCardType(IdcardType.ID);
				//证件号需要先解掩码
				String idNo = (passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getIdNo()))?passCardInfoDTO.getIdNo():needBankCardDTO.getIdno();
				tmpcard.setUserCardId(idNo);
				isneed = true;
			}
		}

		//同人限制值不空、绑卡且补充项包含用户名和证件号，补充项填充同人限制值信息
		if (payrecord.getNeedItem() != 0 && checkOnePersion) {
			BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo4bind(paymentRequest);
			if (payrecord.getNeedItem() != 0 && null != bindLimitInfoResDTO
					&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
				NCPayParamMode nCPayParamMode = new NCPayParamMode(payrecord.getNeedItem());
				if (nCPayParamMode.needIdCardNumber() && StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit())) {
					tmpcard.setUserCardType(IdcardType.ID);
					tmpcard.setUserCardId(bindLimitInfoResDTO.getIdentityNoLimit());
					isneed = true;
				}
				if (nCPayParamMode.needUserName() && StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())) {
					tmpcard.setUserName(bindLimitInfoResDTO.getUserNameLimit());
					isneed = true;
				}
			}
		}

		if (isneed) {
			return cwhService.addPayTmpCard(tmpcard);
		}
		return 0l;
	}


	private CardInfoDTO buildCardInfoDTO(PaymentRecord payrecord, NeedBankCardDTO needBankCardDTO, PaymentRequest paymentRequest, boolean checkOnePersion ){
		// 处理补充项逻辑
        CardInfoDTO cardInfo = new CardInfoDTO();
		cardInfo.setCardno(payrecord.getCardNo());

		if (null!=needBankCardDTO) {
			//获取透传项
			PassCardInfoDTO passCardInfoDTO = cashierBankCardService.getPassCardInfo(paymentRequest);
			if (null!=needBankCardDTO.getAvlidDate()) {
				cardInfo.setValid(needBankCardDTO.getAvlidDate());
			}
			if (null!=needBankCardDTO.getBankPWD()) {
				cardInfo.setPass(needBankCardDTO.getBankPWD());
			}
			if (null!=needBankCardDTO.getCvv()) {
				cardInfo.setCvv2(needBankCardDTO.getCvv());
			}
			if (null!=needBankCardDTO.getOwner()) {
				//姓名需要先解掩码
				String owner = (passCardInfoDTO!=null && StringUtils.isNotBlank(passCardInfoDTO.getOwner()))?passCardInfoDTO.getOwner():needBankCardDTO.getOwner();
				cardInfo.setName(owner);
			}
			if (null!=needBankCardDTO.getPhoneNo()) {
				//手机号需要先解掩码
				String phoneNo = (passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getPhone()))?passCardInfoDTO.getPhone():needBankCardDTO.getPhoneNo();
				cardInfo.setPhone(phoneNo);
			}
			if (null!=needBankCardDTO.getIdno()) {
				cardInfo.setIdType(IdcardType.ID.name());
				//证件号需要先解掩码
				String idNo = (passCardInfoDTO!=null&&StringUtils.isNotBlank(passCardInfoDTO.getIdNo()))?passCardInfoDTO.getIdNo():needBankCardDTO.getIdno();
				cardInfo.setIdno(idNo);
			}
		}

		//同人限制值不空、绑卡且补充项包含用户名和证件号，补充项填充同人限制值信息
		if (payrecord.getNeedItem() != 0 && checkOnePersion) {
			BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfo4bind(paymentRequest);
			if (payrecord.getNeedItem() != 0 && null != bindLimitInfoResDTO
					&& !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
				NCPayParamMode nCPayParamMode = new NCPayParamMode(payrecord.getNeedItem());
				if (nCPayParamMode.needIdCardNumber() && StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit())) {
					cardInfo.setIdType(IdcardType.ID.name());
					cardInfo.setIdno(bindLimitInfoResDTO.getIdentityNoLimit());
				}
				if (nCPayParamMode.needUserName() && StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())) {
					cardInfo.setName(bindLimitInfoResDTO.getUserNameLimit());
				}
			}
		}
		return cardInfo;
	}

	/*封装商户限额查询参数*/
	private MerchantLimitRequestDto buildMerchantLimitDto(PaymentRequest paymentRequest){
		if (paymentRequest == null) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		MerchantLimitRequestDto merchantLimitRequestDto = new MerchantLimitRequestDto();
		merchantLimitRequestDto.setMerchantNo(paymentRequest.getMerchantNo());
		merchantLimitRequestDto.setBizSystemCode(Long.parseLong(paymentRequest.getOrderSysNo()));
		merchantLimitRequestDto.setToolCode(PayProductCode.NCCASHIER);
		merchantLimitRequestDto.setClientCode(paymentRequest.getBizModeCode());
		merchantLimitRequestDto.setGoodsCode(paymentRequest.getIndustryCatalog());
		merchantLimitRequestDto.setRequestTime(new Date());
		return merchantLimitRequestDto;
	}

	/*封装查询商户限额返回dto*/
	private void buildBankLimitAmountList(List<MerchantLimitResponseDto> limitResponseDtoList,BankLimitAmountListResponseDTO bankLimitAmountListResponseDTO,String cardType,String bankCode){

		if(CollectionUtils.isNotEmpty(limitResponseDtoList)){
			List<BankLimitAmountResponseDTO> arrayList = new ArrayList<BankLimitAmountResponseDTO>();
			for(MerchantLimitResponseDto merchantLimitResponseDto : limitResponseDtoList){
				//如果没有透传卡类型和银行卡编码都则返回全部
				if(merchantLimitResponseDto.getLimitOfBill()==0 ||merchantLimitResponseDto.getLimitOfDay() == 0 || merchantLimitResponseDto.getLimitOfMonth() == 0){
					continue;
				}
				if(StringUtils.isBlank(cardType) && StringUtils.isBlank(bankCode) ) {
					arrayList.add(buildBankLimitAmountResponseDTO(merchantLimitResponseDto));
				}else if (StringUtils.isBlank(bankCode) && cardType.equals(merchantLimitResponseDto.getDebit())){
					arrayList.add(buildBankLimitAmountResponseDTO(merchantLimitResponseDto));
				} else if (StringUtils.isBlank(cardType) && bankCode.equals(merchantLimitResponseDto.getBankCode())){
					arrayList.add(buildBankLimitAmountResponseDTO(merchantLimitResponseDto));
				}else if(merchantLimitResponseDto.getDebit().equals(cardType) && merchantLimitResponseDto.getBankCode().equals(bankCode)){
					arrayList.add(buildBankLimitAmountResponseDTO(merchantLimitResponseDto));
				}
			}
			bankLimitAmountListResponseDTO.setLimitAmountResponseDTOList(arrayList);
		}
	}

	private BankLimitAmountResponseDTO buildBankLimitAmountResponseDTO(MerchantLimitResponseDto merchantLimitResponseDto){
		BankLimitAmountResponseDTO bankLimitAmountResponseDTO = new BankLimitAmountResponseDTO();
		bankLimitAmountResponseDTO.setBankCode(merchantLimitResponseDto.getBankCode());
		bankLimitAmountResponseDTO.setDebit(merchantLimitResponseDto.getDebit());
		bankLimitAmountResponseDTO.setLimitMinOfBill(merchantLimitResponseDto.getLimitMinOfBill());
		bankLimitAmountResponseDTO.setLimitOfBill(merchantLimitResponseDto.getLimitOfBill());
		bankLimitAmountResponseDTO.setLimitOfDay(merchantLimitResponseDto.getLimitOfDay());
		bankLimitAmountResponseDTO.setBankName(merchantLimitResponseDto.getBankName());
		bankLimitAmountResponseDTO.setLimitOfMonth(merchantLimitResponseDto.getLimitOfMonth());
		return bankLimitAmountResponseDTO;
	}
}
