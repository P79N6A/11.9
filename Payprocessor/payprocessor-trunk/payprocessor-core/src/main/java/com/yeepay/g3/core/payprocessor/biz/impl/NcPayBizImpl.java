package com.yeepay.g3.core.payprocessor.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.yeepay.g3.facade.ncpay.dto.*;
import com.yeepay.g3.facade.payprocessor.dto.PageRedirectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.biz.NcPayBiz;
import com.yeepay.g3.core.payprocessor.constants.PlatformTypeConstants;
import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.service.NotifyService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.ValidTypeAndStatusPair;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.facade.ncpay.enumtype.SmsCheckResultEnum;
import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcGuaranteeCflPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflConfirmResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOpenRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOpenResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflOrderResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflSmsRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflSmsResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayCflSynConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayConfirmRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayConfirmResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcPayOrderResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcSmsRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NcSmsResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCancelRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCancelResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCompleteRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PreAuthCompleteResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 
 * @author peile.fan
 */
@Service
public class NcPayBizImpl extends BaseBizImpl implements NcPayBiz {

	private static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(NcPayBizImpl.class);
	@Autowired
	private NotifyService notifyService;

	@Override
	public NcPayOrderResponseDTO ncRequest(NcPayOrderRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[无卡请求下单] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		NcPayOrderResponseDTO response = new NcPayOrderResponseDTO();
		PayRecord record = null;
		try {
			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			PaymentResponseDTO paymentResponse = ncPayService.requestPayment(requestDTO, record);
			updatePayRecordByNcPayResponse(record.getRecordNo(), paymentResponse.getPayOrderId());
			bulidRequestResponse(response, record, payment, paymentResponse);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record == null ? null : record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}


	@Override
	public NcSmsResponseDTO sendSms(NcSmsRequestDTO requestDTO) {
		NcSmsResponseDTO respones = new NcSmsResponseDTO();
		try {
			PayLoggerFactory.TAG_LOCAL.set("[发送短信sendSms] - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 校验支付订单
			String ncpayPaymentNo = payRecordService.queryNcPaymentNo(requestDTO.getRecordNo());
			// 构造请求对象
			SmsSendResponseDTO ncPaySmsResponse = ncPayService.verifyAndSendSms(requestDTO, ncpayPaymentNo);
			payRecordService.updateNcPaymentSmsState(requestDTO.getRecordNo(), SmsCheckResultEnum.SEND.name());
			bulidSmsResponse(respones, requestDTO.getRecordNo(), ncPaySmsResponse);
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(respones, e);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return respones;
	}

	@Override
	public NcPayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO requestDTO) {

		NcPayConfirmResponseDTO respones = new NcPayConfirmResponseDTO();
		try {
			PayLoggerFactory.TAG_LOCAL.set("[确认支付confirmPay] - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 查询支付子单
			// 校验支付订单
			String ncpayPaymentNo = payRecordService.queryNcPaymentNo(requestDTO.getRecordNo());
			PayConfirmResponseDTO ncpayConfirmResopnse = ncPayService.confirmPay(requestDTO, ncpayPaymentNo);
			bulidConfirmResponse(respones, requestDTO.getRecordNo(), ncpayConfirmResopnse);
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(respones, e);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return respones;
	}

	@Override
	public PayRecordResponseDTO synConfirmPay(NcPayConfirmRequestDTO requestDTO) {

		PayRecordResponseDTO response = new PayRecordResponseDTO();
		try {
			PayLoggerFactory.TAG_LOCAL.set("确认支付synConfirmPay] - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 查询支付子单
			PayRecord record = payRecordService.queryRecordById(requestDTO.getRecordNo());
			// 调用ncpay同步确认支付接口
			PayQueryResponseDTO payQueryResponseDTO = ncPayService.synConfirmPay(requestDTO, record.getPaymentNo());
			// 结果处理
			handlePayQueryResult(payQueryResponseDTO, record, response);
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(response, e);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}

	private void bulidConfirmResponse(NcPayConfirmResponseDTO respones, String recordNo,
			PayConfirmResponseDTO ncPayResponse) {
		respones.setRecordNo(recordNo);
		respones.setSmsStatus(ncPayResponse.getSmsStatus());
	}

	private void bulidSmsResponse(NcSmsResponseDTO respones, String recordNo, SmsSendResponseDTO ncPaySmsResponse) {
		respones.setRecordNo(recordNo);
		respones.setSmsCode(ncPaySmsResponse.getSmsCode());
		respones.setSmsSendType(ncPaySmsResponse.getSmsSendType());
	}

	private void updatePayRecordByNcPayResponse(String recordNo, String paymentNO) {
		payRecordService.updatePaymentNo(recordNo, paymentNO, TrxStatusEnum.DOING.name());
	}

	/**
	 * @param response
	 * @param record
	 * @param payment
	 * @param paymentResponse
	 */
	private void bulidRequestResponse(NcPayOrderResponseDTO response, PayRecord record, PaymentRequest payment,
			PaymentResponseDTO paymentResponse) {
		response.setNeedItem(paymentResponse.getNeedItem());
		response.setRecordNo(record.getRecordNo());
		response.setSmsType(paymentResponse.getSmsType());
		response.setPageRedirectType(paymentResponse.getRedirectType());
		if(paymentResponse.getPageRedirectDTO() != null) {
			PageRedirectDTO pageRedirectDTO = new PageRedirectDTO();
			com.yeepay.g3.facade.ncpay.dto.PageRedirectDTO pageDTO = paymentResponse.getPageRedirectDTO();
			pageRedirectDTO.setRedirectSceneType(pageDTO.getRedirectSceneType());
			pageRedirectDTO.setRedirectUrl(pageDTO.getRedirectUrl());
			pageRedirectDTO.setMethod(pageDTO.getMethod());
			pageRedirectDTO.setEncoding(pageDTO.getEncoding());
			pageRedirectDTO.setExtMap(pageDTO.getExtMap());
			response.setPageRedirectDTO(pageRedirectDTO);
		}
	}


	@Override
	public NcPayCflOpenResponseDTO cflOpenAndPay(NcPayCflOpenRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[银行卡分期开通并支付] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		NcPayCflOpenResponseDTO response = new NcPayCflOpenResponseDTO();
		PayRecord record = null;
		CombPayRecord combPayRecord = null;
		try {

			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			CflOpenAndPayResponseDTO cflOpenAndPayResponseDTO = ncPayService.cflOpenAndPay(requestDTO, record);
			updatePayRecordByNcPayResponse(record.getRecordNo(), cflOpenAndPayResponseDTO.getPayOrderId());
			bulidCflOpenAndPayResponse(response, record, cflOpenAndPayResponseDTO);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record == null ? null : record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}

	/**
	 * 银行卡分期--开通并支付
	 * 组装返回参数
	 */
	private void bulidCflOpenAndPayResponse(NcPayCflOpenResponseDTO response, PayRecord record, CflOpenAndPayResponseDTO cflOpenAndPayResponseDTO) {
		response.setRecordNo(record.getRecordNo());
		response.setPayUrl(cflOpenAndPayResponseDTO.getPayUrl());
		response.setCharset(cflOpenAndPayResponseDTO.getCharset());
		response.setMethod(cflOpenAndPayResponseDTO.getMethod());
		response.setParamMap(cflOpenAndPayResponseDTO.getParamMap());
	}


	@Override
	public NcPayCflOrderResponseDTO cflRequest(NcPayCflOrderRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[银行卡分期请求下单] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		NcPayCflOrderResponseDTO response = new NcPayCflOrderResponseDTO();
		PayRecord record = null;
		CombPayRecord combPayRecord = null;
		try {
			PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			if(record.isCombinedPay()) {
				createCombPayRecord(requestDTO, record, payment);
			}
			CflOrderResponseDTO cflOrderResponseDTO = ncPayService.clfOrderRequest(requestDTO, record);
			updatePayRecordByNcPayResponse(record.getRecordNo(), cflOrderResponseDTO.getPayOrderId());
			bulidCflOrderResponse(response, record, cflOrderResponseDTO);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record == null ? null : record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}

	/**
	 * 银行卡分期--开通并支付
	 * 组装返回参数
	 */
	private void bulidCflOrderResponse(NcPayCflOrderResponseDTO responseDTO, PayRecord record, CflOrderResponseDTO cflOrderResponseDTO) {
		responseDTO.setRecordNo(record.getRecordNo());

	}

	@Override
	public NcPayCflSmsResponseDTO cflSendSms(NcPayCflSmsRequestDTO requestDTO) {
		NcPayCflSmsResponseDTO responseDTO = new NcPayCflSmsResponseDTO();
		try {
			PayLoggerFactory.TAG_LOCAL.set("[银行卡分期发送短信] - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 校验支付订单
			String ncpayPaymentNo = payRecordService.queryNcPaymentNo(requestDTO.getRecordNo());
			CflSmsSendResponseDTO cflSmsSendResponseDTO = ncPayService.cflSendSms(ncpayPaymentNo, requestDTO);
			payRecordService.updateNcPaymentSmsState(requestDTO.getRecordNo(), SmsCheckResultEnum.SEND.name());
			responseDTO.setRecordNo(requestDTO.getRecordNo());
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(responseDTO, e);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}



	@Override
	public NcPayCflConfirmResponseDTO cflConfirmPay(NcPayCflConfirmRequestDTO requestDTO) {
		NcPayCflConfirmResponseDTO responseDTO = new NcPayCflConfirmResponseDTO();
		try {
			PayLoggerFactory.TAG_LOCAL.set("[银行卡分期确认支付] - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 查询支付子单
			// 校验支付订单
			String ncpayPaymentNo = payRecordService.queryNcPaymentNo(requestDTO.getRecordNo());
			CflConfirmPayResponseDTO cflConfirmPayResponseDTO = ncPayService.cflConfirmPay(requestDTO, ncpayPaymentNo);
			bulidCflConfirmResponse(responseDTO, requestDTO.getRecordNo(), cflConfirmPayResponseDTO);
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(responseDTO, e);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}

	private void bulidCflConfirmResponse(NcPayCflConfirmResponseDTO responseDTO, String recordNo, CflConfirmPayResponseDTO cflConfirmPayResponseDTO) {
		responseDTO.setRecordNo(recordNo);
	}

	@Override
	public PayRecordResponseDTO cflSynConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO) {
		PayRecordResponseDTO response = new PayRecordResponseDTO();
		try {
			PayLoggerFactory.TAG_LOCAL.set("银行卡分期同步确认支付] - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 查询支付子单
			PayRecord record = payRecordService.queryRecordById(requestDTO.getRecordNo());
			// 调用ncpay同步确认支付接口
			PayQueryResponseDTO payQueryResponseDTO = ncPayService.cflSynConfirmPay(requestDTO, record.getPaymentNo());
			// 处理结果
			handlePayQueryResult(payQueryResponseDTO, record, response);
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(response, e);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}


	/**
	 * 预授权请求下单
	 */
	@Override
	public NcPayOrderResponseDTO ncPreAuthRequest(NcPayOrderRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[预授权请求下单] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
		NcPayOrderResponseDTO response = new NcPayOrderResponseDTO();
		PayRecord record = null;
		try {
			PaymentRequest payment = checkAndCreatePaymentForPreAuth(requestDTO);
			record = buildPayRecord(requestDTO, payment);
			createPayRecord(record);
			PayPreAuthRespDTO responseDTO = ncPayService.ncPreAuthRequest(requestDTO, record);
			updatePayRecordByNcPayResponse(record.getRecordNo(), responseDTO.getPaymentNo());
			buildNcPreAuthResponse(response, record, responseDTO);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(response, th);
			updateErrorInfoToRecord(response, record == null ? null : record.getRecordNo());
		} finally {
			setBasicResponseArgs(requestDTO, response);
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return response;
	}

	/**
	 * 预授权请求下单
	 * 组装返回参数
	 */
	private void buildNcPreAuthResponse(NcPayOrderResponseDTO response, PayRecord record, PayPreAuthRespDTO responseDTO) {
		response.setNeedItem(responseDTO.getNeedItem());
		response.setRecordNo(record.getRecordNo());
		response.setSmsType(responseDTO.getSmsType());
	}

	/**
	 * 预授权确认
	 */
	@Override
	public PayRecordResponseDTO ncPreAuthComfirm(NcPayConfirmRequestDTO requestDTO) {
		PayRecordResponseDTO responseDTO = new PayRecordResponseDTO();
		PayRecord payRecord = null;
		try {
			PayLoggerFactory.TAG_LOCAL.set("[预授权请求确认] - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 查询支付子单
			payRecord = payRecordService.queryRecordById(requestDTO.getRecordNo());
			// 校验支付子单
			checkPayRecord(payRecord);
			// 调ncpay
			PayPreAuthConfirmResponseDTO response = ncPayService.ncPreAuthComfirm(requestDTO, payRecord.getPaymentNo());
			// 结果处理
			ncPayResultPreAuthProccess.processForNcPayPreAuth(payRecord, response);
			// 查询支付主单
			PaymentRequest paymentRequest = paymentRequestService.selectByPrimaryKey(payRecord.getRequestId());
			buildPreAuthComfirmResponse(responseDTO, response, payRecord, paymentRequest);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(responseDTO, th);
			updateErrorInfoToRecord(responseDTO, payRecord == null ? null : payRecord.getRecordNo());
		}  finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}


	// 支付子单校验
	private void checkPayRecord(PayRecord payRecord) {
		if (TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())
				|| TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
			logger.error("payRecord already fail or success, " + payRecord.getRecordNo());
			throw new PayBizException(ErrorCode.P9002002);
		}
	}

	/**
	 * 预授权确认
	 * 组装返回参数
	 */
	public void buildPreAuthComfirmResponse(PayRecordResponseDTO responseDTO, PayPreAuthConfirmResponseDTO response,
											PayRecord payRecord, PaymentRequest paymentRequest) {
		responseDTO.setRecordNo(payRecord.getRecordNo());
		responseDTO.setSmsState(response.getSmsStatus());
		responseDTO.setFrpCode(payRecord.getFrpCode());
		responseDTO.setBankOrderNO(response.getBankOrderNo());
		responseDTO.setBankTrxId(response.getTradeSerialNo());
		if(response.getCost() != null) {
			responseDTO.setCost(response.getCost().getValue());
		}
		if(StringUtils.isNotBlank(payRecord.getStatus())) {
			responseDTO.setTrxStatus(TrxStatusEnum.valueOf(payRecord.getStatus()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getOrderSystemStatus())) {
			responseDTO.setOrderSystemStatus(OrderSystemStatusEnum.valueOf(paymentRequest.getOrderSystemStatus()));
		}
		if(TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
			responseDTO.setResponseCode(payRecord.getErrorCode());
			responseDTO.setResponseMsg(payRecord.getErrorMsg());
		}
	}

	/**
	 * 预授权撤销、预授权完成撤销
	 */
	@Override
	public PreAuthCancelResponseDTO ncPreAuthCancel(PreAuthCancelRequestDTO requestDTO) {
		PreAuthCancelResponseDTO responseDTO = new PreAuthCancelResponseDTO();
		PayRecord record = null;
		try {
			PayLoggerFactory.TAG_LOCAL.set("[预授权(完成)撤销 type=" + requestDTO.getCancelType() + "] - [recordNo="
					+ requestDTO.getRecordNo() + "]");
			// 查询原支付子单
			PayRecord prePayRecord = payRecordService.queryRecordById(requestDTO.getRecordNo());
			// 查询支付主单
			PaymentRequest paymentRequest = paymentRequestService.selectByPrimaryKey(prePayRecord.getRequestId());
			// 业务校验
			validPreAuthCancel(paymentRequest, requestDTO);
			// 更新订单类型+支付状态
			updatePaymentRequest(paymentRequest, requestDTO);
			// 创建新子单
			if(ConstantUtils.PRE_AUTH_CANCEL_TYPE_CANCLE.equals(requestDTO.getCancelType())) {
				record = buildRecordForPreAuth(prePayRecord, PayOrderType.PREAUTH_CL.name());
			} else {
				record = buildRecordForPreAuth(prePayRecord, PayOrderType.PREAUTH_CC.name());
				//预授权完成撤销先通知OPR再调NCPAY发起完成撤销
				notifyService.notifyOprNotToAccount(paymentRequest, prePayRecord);
			}
			createPayRecord(record);
			// 调ncpay
			PayPreAuthCancelResponseDTO response = ncPayService.ncPreAuthCancel(requestDTO, prePayRecord.getPaymentNo(), record);
			// 结果处理
			ncPayResultPreAuthProccess.processForNcPayPreAuthCancel(record, response);
			PaymentRequest payment = paymentRequestService.selectByPrimaryKey(prePayRecord.getRequestId());
			
			// 组装返回参数
			buildPreAuthCancelResponse(responseDTO, response, record, payment);
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(responseDTO, e);
			updateErrorInfoToRecord(responseDTO, record == null ? null : record.getRecordNo());
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}

	/**
	 * 预授权撤销、预授权完成撤销
	 * 业务校验
	 */
	private void validPreAuthCancel(PaymentRequest paymentRequest, PreAuthCancelRequestDTO requestDTO) {
		List<ValidTypeAndStatusPair> allowList = new ArrayList<ValidTypeAndStatusPair>();
		// 预授权撤销
		if(ConstantUtils.PRE_AUTH_CANCEL_TYPE_CANCLE.equals(requestDTO.getCancelType())) {
			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_RE.name(), "SUCCESS"));// 预授权请求 + 成功
//			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CL.name(), "DOING"));// 预授权撤销 + 处理中
			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CL.name(), "FAILURE"));// 预授权撤销 + 失败
//			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CM.name(), "DOING"));// 预授权完成 + 处理中
			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CM.name(), "FAILURE"));// 预授权完成 + 失败
			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CC.name(), "SUCCESS"));// 预授权完成撤销 + 成功
		// 预授权完成撤销
		} else if(ConstantUtils.PRE_AUTH_CANCEL_TYPE_COMPLETE_CANCEL.equals(requestDTO.getCancelType())){
			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CM.name(), "SUCCESS"));// 预授权完成 + 成功
//			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CC.name(), "DOING"));// 预授权完成撤销 + 处理中
			allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CC.name(), "FAILURE"));// 预授权完成撤销 + 失败
		} else {
			logger.error("未知的撤销类型, recordNo=" + requestDTO.getRecordNo() + ", requestId=" + paymentRequest.getId());
			throw new PayBizException(ErrorCode.P9003032);
		}
		validPreAuthStatus(paymentRequest, allowList);
		// 通知状态校验
		checkOrderSystemStatus(paymentRequest);
		// 如果是预授权完成撤销的订单，只允许预授权完成撤销成功一次
		if(ConstantUtils.PRE_AUTH_CANCEL_TYPE_COMPLETE_CANCEL.equals(requestDTO.getCancelType())) {
			checkPreAuthCompleteCancelCount(paymentRequest);
		}

	}

	/**
	 * 预授权撤销、预授权完成撤销
	 * 更新订单类型+状态
	 */
	private void updatePaymentRequest(PaymentRequest paymentRequest, PreAuthCancelRequestDTO requestDTO) {
		if(paymentRequest == null || requestDTO == null) {
			return;
		}
		// 预授权撤销
		if(ConstantUtils.PRE_AUTH_CANCEL_TYPE_CANCLE.equals(requestDTO.getCancelType())) {
			updateOrderTypeAndStatus(paymentRequest, PayOrderType.PREAUTH_CL.name(), TrxStatusEnum.DOING.name());
		// 预授权完成撤销
		} else if(ConstantUtils.PRE_AUTH_CANCEL_TYPE_COMPLETE_CANCEL.equals(requestDTO.getCancelType())) {
			updateOrderTypeAndStatus(paymentRequest, PayOrderType.PREAUTH_CC.name(), TrxStatusEnum.DOING.name());
		}
	}


	/**
	 * 预授权撤销、预授权完成撤销
	 * 组装返回参数
	 */
	private void buildPreAuthCancelResponse(PreAuthCancelResponseDTO responseDTO, PayPreAuthCancelResponseDTO response,
											PayRecord payRecord,PaymentRequest paymentRequest) {
		responseDTO.setRecordNo(payRecord.getRecordNo());
		responseDTO.setOutTradeNo(paymentRequest.getOutTradeNo());
		responseDTO.setOrderNo(paymentRequest.getOrderNo());
		responseDTO.setDealUniqueSerialNo(paymentRequest.getDealUniqueSerialNo());
		responseDTO.setCustomerNumber(paymentRequest.getCustomerNo());
		responseDTO.setFrpCode(payRecord.getFrpCode());
		responseDTO.setBankOrderNO(response.getBankOrderNo());
		responseDTO.setBankTrxId(response.getTradeSerialNo());
		if(response.getCost() != null) {
			responseDTO.setCost(response.getCost().getValue());
		}
		if(StringUtils.isNotBlank(payRecord.getStatus())) {
			responseDTO.setTrxStatus(TrxStatusEnum.valueOf(payRecord.getStatus()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getOrderSystemStatus())) {
			responseDTO.setOrderSystemStatus(OrderSystemStatusEnum.valueOf(paymentRequest.getOrderSystemStatus()));
		}
		if(TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
			responseDTO.setResponseCode(payRecord.getErrorCode());
			responseDTO.setResponseMsg(payRecord.getErrorMsg());
		}
	}




	/**
	 * 预授权完成
	 * 备注：ncpay在回调时做了优化，只有银行子系统异步通知ncpay，ncpay才会异步通知pp，正常情况下不会回调pp
	 * 所以不存在异步先于同步结果到达pp的情况
	 */
	@Override
	public PreAuthCompleteResponseDTO ncPreAuthComplete(PreAuthCompleteRequestDTO requestDTO) {
		PreAuthCompleteResponseDTO responseDTO = new PreAuthCompleteResponseDTO();
		PayRecord record = null;
		try {
			PayLoggerFactory.TAG_LOCAL.set("[预授权完成]" + " - [recordNo=" + requestDTO.getRecordNo() + "]");
			// 查询支付子单
			PayRecord prePayRecord = payRecordService.queryRecordById(requestDTO.getRecordNo());
			// 查询支付主单
			PaymentRequest paymentRequest = paymentRequestService.selectByPrimaryKey(prePayRecord.getRequestId());
			// 业务校验
			validPreAuthComplete(paymentRequest, requestDTO);
			// 创建新子单
			record = buildRecordForPreAuth(prePayRecord, PayOrderType.PREAUTH_CM.name());
			// 更新预授权完成金额
			record.setAmount(requestDTO.getAmount());
			createPayRecord(record);
			// 更新订单类型+支付状态
			updateOrderTypeAndStatus(paymentRequest, PayOrderType.PREAUTH_CM.name(), TrxStatusEnum.DOING.name());
			// 调ncpay
			PayPreAuthCompleteResponseDTO response = ncPayService.ncPreAuthComplete(requestDTO, prePayRecord.getPaymentNo(), record);
			// 结果处理
			ncPayResultPreAuthProccess.processForNcPayPreAuthComplete(record, response);
			PaymentRequest payment = paymentRequestService.selectByPrimaryKey(prePayRecord.getRequestId());
			// 组装返回参数
			buildPreAuthCompleteResponse(responseDTO, response, record, payment);
		} catch (Throwable e) {
			logger.error("[业务异常]", e);
			setErrorInfo(responseDTO, e);
			updateErrorInfoToRecord(responseDTO, record == null ? null : record.getRecordNo());
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}

	/**
	 * 预授权完成
	 * 业务校验
	 */
	private void validPreAuthComplete(PaymentRequest paymentRequest, PreAuthCompleteRequestDTO requestDTO) {
		//TODO 若有订单处于处理中不允许做下一步交易
		List<ValidTypeAndStatusPair> allowList = new ArrayList<ValidTypeAndStatusPair>();
		allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_RE.name(), "SUCCESS"));// 预授权请求 + 成功
		allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CM.name(), "FAILURE"));// 预授权完成 + 失败
//		allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CM.name(), "DOING"));// 预授权完成 + 处理中
//		allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CL.name(), "DOING"));// 预授权撤销 + 处理中
		allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CL.name(), "FAILURE"));// 预授权撤销 + 失败
		allowList.add(new ValidTypeAndStatusPair(PayOrderType.PREAUTH_CC.name(), "SUCCESS"));// 预授权完成撤销 + 成功
		// 类型+状态校验
		validPreAuthStatus(paymentRequest, allowList);
		// 通知状态校验
		checkOrderSystemStatus(paymentRequest);
	}

	/**
	 * 预授权完成
	 * 组装返回参数
	 */
	private void buildPreAuthCompleteResponse(PreAuthCompleteResponseDTO responseDTO, PayPreAuthCompleteResponseDTO response,
											  PayRecord payRecord, PaymentRequest paymentRequest) {
		responseDTO.setRecordNo(payRecord.getRecordNo());
		responseDTO.setOutTradeNo(paymentRequest.getOutTradeNo());
		responseDTO.setOrderNo(paymentRequest.getOrderNo());
		responseDTO.setDealUniqueSerialNo(paymentRequest.getDealUniqueSerialNo());
		responseDTO.setCustomerNumber(paymentRequest.getCustomerNo());
		responseDTO.setFrpCode(payRecord.getFrpCode());
		responseDTO.setBankOrderNO(response.getBankOrderNo());
		responseDTO.setBankTrxId(response.getTradeSerialNo());
		responseDTO.setBankPaySuccDate(payRecord.getPayTime());
		if(response.getCost() != null) {
			responseDTO.setCost(response.getCost().getValue());
		}
		if(StringUtils.isNotBlank(payRecord.getStatus())) {
			responseDTO.setTrxStatus(TrxStatusEnum.valueOf(payRecord.getStatus()));
		}
		if(StringUtils.isNotBlank(paymentRequest.getOrderSystemStatus())) {
			responseDTO.setOrderSystemStatus(OrderSystemStatusEnum.valueOf(paymentRequest.getOrderSystemStatus()));
		}
		if(TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
			responseDTO.setResponseCode(payRecord.getErrorCode());
			responseDTO.setResponseMsg(payRecord.getErrorMsg());
		}
	}


	/**
	 * 校验预授权订单类型+状态
	 */
	public void validPreAuthStatus(PaymentRequest paymentRequest, List<ValidTypeAndStatusPair> allowList) {
		if(paymentRequest == null || allowList.isEmpty()) {
			return;
		}
		String payType = paymentRequest.getPayType();
		String status = paymentRequest.getPayStatus();
		if(StringUtils.isBlank(payType) || StringUtils.isBlank(status)) {
			return;
		}
		// 遍历List,符合其中的一种，则校验通过
		for(int i=0; i<allowList.size(); i++) {
			ValidTypeAndStatusPair validPair = allowList.get(i);
			if(validPair != null && payType.equals(validPair.getPayType()) && status.equals(validPair.getStatus())) {
				return;
			}
		}
		logger.error("校验预授权订单状态不通过，支付订单号：" + paymentRequest.getRecordNo() + "主单类型+状态：" +
				paymentRequest.getPayType() + "," + paymentRequest.getPayStatus());
		throw new PayBizException(ErrorCode.P9003031);
	}

	/**
	 * 校验通知状态是否成功
	 * @param paymentRequest
	 */
	public void checkOrderSystemStatus(PaymentRequest paymentRequest) {
		// 通知不成功，报错
		if(!OrderSystemStatusEnum.SUCCESS.name().equals(paymentRequest.getOrderSystemStatus())) {
			logger.error("订单未通知业务方成功，不允许交易，orderNo=" + paymentRequest.getOrderNo());
			throw new PayBizException(ErrorCode.P9003057);
		}
	}

	/**
	 * 校验预授权完成撤销成功的次数
	 * @param paymentRequest
	 */
	public void checkPreAuthCompleteCancelCount(PaymentRequest paymentRequest) {
		int count = payRecordService.queryCompleteCancelCount(paymentRequest.getId());
		if(count > 0) {
			logger.error("订单预授权完成撤销成功次数超限，payment.id=" + paymentRequest.getId());
			throw new PayBizException(ErrorCode.P9003060);
		}
	}


	/**
	 * 创建支付子单
	 */
	private PayRecord buildRecordForPreAuth(PayRecord prePayRecord, String payType) {
		PayRecord payRecord = new PayRecord();
		if(!PayOrderType.PREAUTH_CM.equals(payType)) {
			payRecord.setAmount(prePayRecord.getAmount());
		}
		payRecord.setRequestId(prePayRecord.getRequestId());
//		payRecord.setPaymentNo(prePayRecord.getPaymentNo());
		payRecord.setRequestSysId(prePayRecord.getRequestSysId());
		payRecord.setRequestSystem(prePayRecord.getRequestSystem());
		payRecord.setExtendedInfo(prePayRecord.getExtendedInfo());
		payRecord.setCashierVersion(prePayRecord.getCashierVersion());
		payRecord.setPayProduct(prePayRecord.getPayProduct());
		payRecord.setPayOrderType(payType);
		payRecord.setPayerIp(prePayRecord.getPayerIp());
		payRecord.setUserFee(prePayRecord.getUserFee());
		payRecord.setStatus(TrxStatusEnum.DOING.name());
		payRecord.setBindCardInfoId(prePayRecord.getBindCardInfoId());
		payRecord.setPlatformType(PlatformTypeConstants.NCPAY);
		payRecord.setPayScene(prePayRecord.getPayScene());
		payRecord.setRetailProductCode(prePayRecord.getRetailProductCode());
		payRecord.setBasicProductCode(prePayRecord.getBasicProductCode());
		return payRecord;
	}

	/**
	 * 更新订单类型和订单状态
	 */
	private void updateOrderTypeAndStatus(PaymentRequest payment, String payType, String payStatus) {
		if(payment == null) {
			return;
		}
		int updateCount = 0;
		try {
			updateCount = paymentRequestService.updatePayTypeAndStatus(payment.getId(), payType, payStatus, payment.getPayType(),
					payment.getPayStatus());
		} catch (Exception e) {
			logger.error("更新主表订单类型和订单状态失败，recordNo=" + payment.getRecordNo() + ", requestId=" + payment.getId() +
			", payType=" + payType + ", payStatus=" + payStatus, e);
			throw new PayBizException(ErrorCode.P9003034);
		}
		// 防并发，要更新的状态和本身状态不一致，若更新为0需抛异常
		if((!payType.equals(payment.getPayType()) || !payStatus.equals(payment.getPayStatus())) && updateCount < 1) {
			logger.error("更新主表订单类型和订单状态失败，recordNo=" + payment.getRecordNo() + ", requestId=" + payment.getId() +
					", payType=" + payType + ", payStatus=" + payStatus);
			throw new PayBizException(ErrorCode.P9003034);
		}
		payment.setPayType(payType);
		payment.setPayStatus(payStatus);
	}


	/**
	 * 预授权"冲正"专用
	 * 只有预授权撤销、预授权完成撤销
	 */
	@Override
	public void ncPreAuthReverseCancel(PreAuthReverseRecord preAuthReverseRecord, PaymentRequest paymentRequest) {
		try {
			PayLoggerFactory.TAG_LOCAL.set("[预授权订单冲正 type=" + preAuthReverseRecord.getPayOrderType() + "] - [reverseNo="
					+ preAuthReverseRecord.getReverseNo() + "]");
			// 组装入参
			PayPreAuthCancelRequestDTO requestDTO = buildPreAuthCancelRequest(preAuthReverseRecord, paymentRequest);
			// 调ncpay
			PayPreAuthCancelResponseDTO response = ncPayService.ncPreAuthReverseCancel(requestDTO);
			// 结果处理
			ncPayResultPreAuthProccess.processForNcPayReversePreAuthCancel(preAuthReverseRecord, response);
		} catch (Throwable e) {
			logger.error("[冲正：预授权(完成)撤销异常]", e);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
	}

	private PayPreAuthCancelRequestDTO buildPreAuthCancelRequest(PreAuthReverseRecord preAuthReverseRecord, PaymentRequest payment) {
		PayPreAuthCancelRequestDTO requestDTO = new PayPreAuthCancelRequestDTO();
		requestDTO.setPreAuthPaymentNo(preAuthReverseRecord.getOrgPaymentNo());
		requestDTO.setBizType(preAuthReverseRecord.getBiz());
		requestDTO.setBizOrderNum(preAuthReverseRecord.getReverseNo());
		requestDTO.setRequestNo(payment.getOutTradeNo());
		requestDTO.setBizOrderTime(preAuthReverseRecord.getRequestTime());
		// 撤销
		if(PayOrderType.PREAUTH_CL.name().equals(preAuthReverseRecord.getPayOrderType())) {
			requestDTO.setCancelType(ConstantUtils.PRE_AUTH_CANCEL_TYPE_CANCLE);
		}
		return requestDTO;
	}

	/**
	 * 更新冲正表信息
	 */
	private void updatePreAuthReverseRecord(PreAuthCancelRequestDTO requestDTO, PayPreAuthCancelResponseDTO response) {
		if(response == null) {
			logger.error("预授权冲正撤销结果为空 recordNo=" + requestDTO.getRecordNo());
			return;
		}
		PreAuthReverseRecord preAuthReverseRecord = preAuthReverseRecordService.queryByRecordNo(requestDTO.getRecordNo());
		if(PaymentOrderStatusEnum.SUCCESS == response.getStatus()) {
			preAuthReverseRecord.setCancelStatus(TrxStatusEnum.SUCCESS.name());
		}else if(PaymentOrderStatusEnum.FAILURE == response.getStatus()) {
			preAuthReverseRecord.setCancelStatus(TrxStatusEnum.FAILUER.name());
		} else {
			logger.error("预授权冲正撤销结果为处理中， recordNo=" + requestDTO.getRecordNo());
		}
		preAuthReverseRecord.setPaymentNo(response.getPaymentNo());
		preAuthReverseRecord.setSuccessTime(new Date());

		preAuthReverseRecordService.updateRecord(preAuthReverseRecord);
	}

	/**
	 * 担保分期预路由
	 */
    @Override
    public NcGuaranteeCflPrePayResponseDTO authCflPrePay(NcGuaranteeCflPrePayRequestDTO requestDTO) {
        PayLoggerFactory.TAG_LOCAL.set("[担保分期预路由] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
        NcGuaranteeCflPrePayResponseDTO response = new NcGuaranteeCflPrePayResponseDTO();
        
        try{
            GuaranteeCflPrePayResponseDTO ncPayResponse = ncPayService.guaranteeCflPrePay(requestDTO);
            response = buildNcGuaranteeCflPrePayResponse(ncPayResponse);
        } catch (Throwable e) {
            logger.error("[业务异常]", e);
            setErrorInfo(response, e); 
        } finally {
            setBasicResponseArgs(requestDTO, response);
            PayLoggerFactory.TAG_LOCAL.remove();
        }
        return response;
    }
    
    private NcGuaranteeCflPrePayResponseDTO buildNcGuaranteeCflPrePayResponse(GuaranteeCflPrePayResponseDTO param){
        NcGuaranteeCflPrePayResponseDTO response = new NcGuaranteeCflPrePayResponseDTO();
        response.setProcessStatus(ProcessStatus.SUCCESS);
        response.setBankCode(param.getBankCode());
        response.setCardType(param.getCardType());
        response.setCflCount(param.getCflCount());
        response.setNeedSign(param.isNeedSign());
        response.setSignNeedRedirect(param.isSignNeedRedirect());
        response.setSignNeedSMS(param.isSignNeedSMS());
        response.setRequiredVerifySignItem(param.getRequiredVerifySignItem());
        response.setRequiredUnverifySignItem(param.getRequiredUnverifySignItem());
        response.setPayNeedRedirect(param.isPayNeedRedirect());
        response.setPayNeedSMS(param.isPayNeedSMS());
        response.setRequiredUnverifyPayItem(param.getRequiredUnverifyPayItem());
        response.setRequiredVerifyPayItem(param.getRequiredVerifyPayItem());
        response.setPayerInterestRate(param.getPayerInterestRate());
        response.setPayOrderCost(param.getPayOrderCost());
        response.setTraceIdExpireSec(param.getTraceIdExpireSec());
        response.setPreRouteId(param.getPreRouteId());
        return response;
        
    }

    @Override
    public NcGuaranteeCflPayResponseDTO authCflRequest(NcGuaranteeCflPayRequestDTO requestDTO) {
        
        PayLoggerFactory.TAG_LOCAL.set("[担保分期下单] - [DealUniqueSerialNo = " + requestDTO.getDealUniqueSerialNo() + "]");
        NcGuaranteeCflPayResponseDTO response = new NcGuaranteeCflPayResponseDTO();
        
        try{
            PaymentRequest payment = checkAndCreatePaymentRequest(requestDTO);
            PayRecord record = buildPayRecord(requestDTO, payment);
            createPayRecord(record);
            GuaranteeCflPayResponseDTO ncPayResponse = ncPayService.guaranteeCflPay(requestDTO, record);
            updatePayRecordByNcPayResponse(record.getRecordNo(), ncPayResponse.getPaymentNo());
            response = buildNcGuaranteeCflPayResponse(ncPayResponse, record);
         } catch (Throwable e) {
            logger.error("[业务异常]", e);
            setErrorInfo(response, e);
        } finally {
            setBasicResponseArgs(requestDTO, response);
            PayLoggerFactory.TAG_LOCAL.remove();
        }
    
        return response;
    }
    
    private NcGuaranteeCflPayResponseDTO buildNcGuaranteeCflPayResponse(GuaranteeCflPayResponseDTO ncPayResponse, PayRecord record){
        NcGuaranteeCflPayResponseDTO response = new NcGuaranteeCflPayResponseDTO();
        response.setAmount(ncPayResponse.getAmount());
        response.setBankOrderNo(ncPayResponse.getBankOrderNo());
        response.setBankTrxId(ncPayResponse.getBankTrxId());
        response.setCflCount(Integer.parseInt(ncPayResponse.getCflCount()));
        response.setCost(ncPayResponse.getCost() != null ? new Amount(ncPayResponse.getCost()) : null);
        response.setEncoding(ncPayResponse.getEncoding());
        response.setFrpCode(ncPayResponse.getFrpCode());
        response.setMethod(ncPayResponse.getMethod());
        response.setNeedRedirect(ncPayResponse.isNeedRedirect());
        response.setNeedSMS(ncPayResponse.isNeedSMS());
        response.setParamMap(ncPayResponse.getParamMap());
        response.setPayerInterestRate(ncPayResponse.getPayerInterestRate());
        response.setRecordNo(record.getRecordNo());
        response.setRedirectUrl(ncPayResponse.getRedirectUrl());
        return response;
     }
}

