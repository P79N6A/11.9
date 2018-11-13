package com.yeepay.g3.core.nccashier.service;

import com.alibaba.fastjson.JSONObject;
import com.ibm.db2.jcc.a.e;
import com.yeepay.g3.common.Amount;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.frontend.dto.PayResultMessage;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

@Service
public class NcPayResultProccess extends NcCashierBaseService {
	protected static final Logger logger =
			NcCashierLoggerFactory.getLogger(NcCashierBaseService.class);

	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private PaymentRequestService paymentRequestService;
	@Resource
	private NcCashierNoticeService ncCashierNotice;

	public Boolean processForCallBack(PaymentResultMessage resultMessage) {
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestByOrderOrderId(
				resultMessage.getBizOrderNum(), String.valueOf(resultMessage.getBizType()));
		if (null == paymentRequest) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		// 日志监控埋点
		logger.info("[monitor],event:nccashier_close,callbackMessage:{},merchantOrderId:{}",resultMessage.toString(),paymentRequest.getMerchantOrderId());
		PaymentRecord paymentRecord = null;
		List<PaymentRecord> recordList = paymentProcessService.findRecordListByOrderOrderId(
				paymentRequest.getOrderOrderId(), paymentRequest.getOrderSysNo());
		if (CollectionUtils.isNotEmpty(recordList)) {
			for (PaymentRecord record : recordList) {
				if (resultMessage.getPayOrderNum().equals(record.getPaymentOrderNo())) {
					paymentRecord = record;
					break;
				}
			}
		}
		if (null == paymentRecord) {
			throw CommonUtil.handleException(Errors.PAY_RECORD_NULL);
		}
		boolean isNeedNotice = false;
		if (
//				paymentRecord.getState() == PayRecordStatusEnum.SUCCESS || 
				paymentRecord.getState() == PayRecordStatusEnum.FAILED
				|| paymentRecord.getState() == PayRecordStatusEnum.PAYREVERSE) {
			logger.info("nccashier的支付订单状态已经是终态");
			return true;
		}
		if(paymentRecord.getState() == PayRecordStatusEnum.SUCCESS){
			isNeedNotice = true;
		}else{
			PayQueryResponseDTO payQueryResponseDTO = transferResultMessageToqueryResult(resultMessage);
			fillPaymentRecordByResultInfo(payQueryResponseDTO, paymentRecord);
			fillPaymentRequestByResultInfo(payQueryResponseDTO, paymentRequest, paymentRecord);
			// 更新支付请求订单和支付订单的状态
			isNeedNotice = updateState(paymentRecord, paymentRequest);
		}
		if (isNeedNotice) {
			ncCashierNotice.sendNoticeToTradeSys(paymentRequest);
		}
		// 5.将结果放入redis中
		// TradeNoticeDTO tradeNoticeDTO = makeWapTradeNoticeDTO(paymentRequest, paymentRecord);
		// NcCashierRedisUtil.setTradeNoticeDTOToRedis(tradeNoticeDTO,
		// paymentRecord.getPaymentOrderNo());
		return true;
	}

	private PayQueryResponseDTO transferResultMessageToqueryResult(
			PaymentResultMessage resultMessage) {
		PayQueryResponseDTO dto = new PayQueryResponseDTO();
		dto.setAccountNo(resultMessage.getAccountNo());
		dto.setBankCode(resultMessage.getBankCode());
		dto.setBankName(resultMessage.getBankName());
		dto.setBankOrderNo(resultMessage.getBankOrderNo());
		dto.setBizOrderId(resultMessage.getPayOrderNum());
		dto.setCost(resultMessage.getCost());
		dto.setErrorCode(resultMessage.getErrorCode());
		dto.setErrorMsg(resultMessage.getErrorMsg());
		dto.setFrpCode(resultMessage.getFrpCode());
		dto.setPayCompleteDate(resultMessage.getPayCompleteDate());
		dto.setPayOrderId(resultMessage.getPayOrderNum());
		dto.setStatus(resultMessage.getPayStatus());
		dto.setTradeSerialNo(resultMessage.getTradeSerialNo());
		dto.setBasicProductCode(resultMessage.getBasicProductCode());
		return dto;
	}

	public void processForQuery(PayQueryResponseDTO payQueryResponseDTO,
			PaymentRecord paymentRecord, PaymentRequest paymentRequest) {
		boolean  noticeTradeSys =false;
		// 处理状态下的，不处理
		if (PaymentOrderStatusEnum.DOING == payQueryResponseDTO.getStatus()) {
			return;
		}
		fillPaymentRecordByResultInfo(payQueryResponseDTO, paymentRecord);
		fillPaymentRequestByResultInfo(payQueryResponseDTO, paymentRequest, paymentRecord);
		// 更新支付请求订单和支付订单的状态
		noticeTradeSys = updateState(paymentRecord, paymentRequest);
		if(noticeTradeSys){//2Gtrade只要更新状态就发消息通知
			ncCashierNotice.sendNoticeTradeByHessian(paymentRequest);
		}
	}

	private Boolean updateState(PaymentRecord paymentRecord, PaymentRequest paymentRequest) {
		if (paymentRecord.getState() == PayRecordStatusEnum.SUCCESS
				|| paymentRecord.getState() == PayRecordStatusEnum.FAILED
				|| paymentRecord.getState() == PayRecordStatusEnum.PAYREVERSE) {
			return paymentProcessService.updateStateByNcPayCallBack(paymentRequest, paymentRecord);
		}
		return false;
	}

	private void fillPaymentRequestByResultInfo(PayQueryResponseDTO payQueryResponseDTO,
			PaymentRequest paymentRequest, PaymentRecord paymentRecord) {
		paymentRequest.setErrorCode(payQueryResponseDTO.getErrorCode());
		paymentRequest.setErrorMsg(payQueryResponseDTO.getErrorMsg());
		paymentRequest.setCost(payQueryResponseDTO.getCost() != null
				? payQueryResponseDTO.getCost().getValue() : null);
		if (payQueryResponseDTO.getPayCompleteDate() != null && payQueryResponseDTO.getPayCompleteDate() > 0) {
			paymentRequest.setPayTime(new Date(payQueryResponseDTO.getPayCompleteDate()));
		}
		paymentRequest.setUpdateTime(new Date());
		paymentRequest.setBindId(paymentRecord.getBindId());
		paymentRequest.setBankCode(payQueryResponseDTO.getBankCode());
		paymentRequest.setCardNo(paymentRecord.getCardNo());
		
		/*设置基础产品码*/
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		jsonObject.put("basicProductCode", payQueryResponseDTO.getBasicProductCode());
		paymentRequest.setExtendInfo(jsonObject.toJSONString());
	}

	private void fillPaymentRecordByResultInfo(PayQueryResponseDTO payQueryResponseDTO,
			PaymentRecord paymentRecord) {
		paymentRecord.setBankCode(payQueryResponseDTO.getBankCode());
		paymentRecord.setBankOrderNo(payQueryResponseDTO.getBankOrderNo());
		paymentRecord.setBankChannelNo(payQueryResponseDTO.getFrpCode());
		paymentRecord.setErrorCode(payQueryResponseDTO.getErrorCode());
		paymentRecord.setErrorMsg(payQueryResponseDTO.getErrorMsg());
		paymentRecord.setCardInfoId(payQueryResponseDTO.getAccountNo());
		paymentRecord.setTradeSerialNo(payQueryResponseDTO.getTradeSerialNo());
		paymentRecord.setCost(payQueryResponseDTO.getCost() != null
				? payQueryResponseDTO.getCost().getValue() : null);
		paymentRecord.setCardInfoId(payQueryResponseDTO.getAccountNo());
		paymentRecord.setUpdateTime(new Date());
		if (payQueryResponseDTO.getStatus() == PaymentOrderStatusEnum.SUCCESS) {
			paymentRecord.setState(PayRecordStatusEnum.SUCCESS);
		} else if (payQueryResponseDTO.getStatus() == PaymentOrderStatusEnum.FAILURE) {
			paymentRecord.setState(PayRecordStatusEnum.FAILED);
		} else if (payQueryResponseDTO.getStatus() == PaymentOrderStatusEnum.FAULT_REFUND) {
			paymentRecord.setState(PayRecordStatusEnum.PAYREVERSE);
		}
	}
	
	public Boolean processForCallBack(PayResultMessage resultMessage) {
		List<PaymentRequest> requestList = paymentRequestService.findRequestListByMerchantNoAndMerchantOrderId(
				resultMessage.getCustomerNumber(),resultMessage.getOutTradeNo());
		PaymentRequest paymentRequest = null;
		for(PaymentRequest request : requestList){
			if(request.getTradeSysOrderId().equals(resultMessage.getRequestId())){
				paymentRequest = request;
				break;
			}
		}
		if (null == paymentRequest) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		
		PaymentRecord paymentRecord = paymentProcessService.findRecordByMerchantOrderId(resultMessage.getOutTradeNo(),
				resultMessage.getCustomerNumber(),resultMessage.getOrderNo());

		if (paymentRecord.getState() == PayRecordStatusEnum.SUCCESS
				|| paymentRecord.getState() == PayRecordStatusEnum.FAILED
				|| paymentRecord.getState() == PayRecordStatusEnum.PAYREVERSE) {
			logger.info("nccashier的支付订单状态已经是终态");
			return true;
		}
		PayQueryResponseDTO payQueryResponseDTO = transferFEResultMessageToqueryResult(resultMessage);
		paymentRecord.setCardType(resultMessage.getPayBankcardType()==null?PayBankcardType.CFT.name()
				:resultMessage.getPayBankcardType().name());
		
		fillPaymentRecordByResultInfo(payQueryResponseDTO, paymentRecord);
		fillPaymentRequestByResultInfo(payQueryResponseDTO, paymentRequest, paymentRecord);
		// 更新支付请求订单和支付订单的状态
		boolean isNeedNotice = updateState(paymentRecord, paymentRequest);
		if (isNeedNotice) {
			ncCashierNotice.sendNoticeToTradeSys(paymentRequest);
		}
		RedisTemplate.setCacheObjectSumValue(Constant.FRONT_NOTIFY_KEY+ paymentRecord.getTokenId(),
				"SUCCESS", 60 * 1000);
		return true;
	}
	
	private PayQueryResponseDTO transferFEResultMessageToqueryResult(
			PayResultMessage resultMessage) {
		PayQueryResponseDTO dto = new PayQueryResponseDTO();
		dto.setBankCode(resultMessage.getPayBank());
		dto.setBankOrderNo(resultMessage.getOrderNo());
		dto.setCost(resultMessage.getBankTotalCost()==null?null:new Amount(resultMessage.getBankTotalCost()));
		dto.setErrorCode(resultMessage.getResponseCode());
		dto.setErrorMsg(resultMessage.getResponseMsg());
		dto.setPayCompleteDate(resultMessage.getPaySuccessTime()==null?null:resultMessage.getPaySuccessTime().getTime());
		dto.setPayOrderId(resultMessage.getOrderNo());
		dto.setFrpCode(resultMessage.getPayInterface());
		if(PayStatusEnum.FAILURE == resultMessage.getPayStatus() ||
				PayStatusEnum.INIT == resultMessage.getPayStatus()){
			dto.setStatus(PaymentOrderStatusEnum.DOING);
		}else if(PayStatusEnum.SUCCESS == resultMessage.getPayStatus()){
			dto.setStatus(PaymentOrderStatusEnum.SUCCESS);
		}
		dto.setBasicProductCode(resultMessage.getBasicProductCode());
		dto.setTradeSerialNo(resultMessage.getTransactionId());
		return dto;
	}
	/**
	 * 分期付款MQ监听结果处理
	 * @param resultMessage
	 * @return
	 */
	public Boolean processForCallBack(InstallmentResultMessage resultMessage) {
		PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(Long.parseLong(resultMessage.getRequestId()));
		if (null == paymentRequest) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}

		PaymentRecord paymentRecord = paymentProcessService.findRecordByMerchantOrderId(resultMessage.getOutTradeNo(),
				resultMessage.getCustomerNumber(),resultMessage.getOrderNo());

		if (paymentRecord.getState() == PayRecordStatusEnum.SUCCESS
				|| paymentRecord.getState() == PayRecordStatusEnum.FAILED
				|| paymentRecord.getState() == PayRecordStatusEnum.PAYREVERSE) {
			logger.info("nccashier的支付订单状态已经是终态");
			return true;
		}
		PayQueryResponseDTO payQueryResponseDTO = transferFEInstallmentResultMessageToqueryResult(resultMessage);
		paymentRecord.setCardType(PayBankcardType.CREDIT.name());
		fillPaymentRecordByResultInfo(payQueryResponseDTO, paymentRecord);
		fillPaymentRequestByResultInfo(payQueryResponseDTO, paymentRequest, paymentRecord);
		// 更新支付请求订单和支付订单的状态
		boolean isNeedNotice = updateState(paymentRecord, paymentRequest);
		if (isNeedNotice) {
			ncCashierNotice.sendNoticeToTradeSys(paymentRequest);
		}
		RedisTemplate.setCacheObjectSumValue(Constant.FRONT_NOTIFY_KEY+ paymentRecord.getTokenId(),
				"SUCCESS", 60 * 1000);
		return true;
	
		
	}


	private PayQueryResponseDTO transferFEInstallmentResultMessageToqueryResult(
			InstallmentResultMessage resultMessage) {

		PayQueryResponseDTO dto = new PayQueryResponseDTO();
		dto.setBankCode(resultMessage.getLoanTerm());//分期期数放在银行编码中
		dto.setBankOrderNo(resultMessage.getOrderNo());
		dto.setCost(resultMessage.getBankTotalCost()==null?null:new Amount(resultMessage.getBankTotalCost()));
		dto.setErrorCode(resultMessage.getResponseCode());
		dto.setErrorMsg(resultMessage.getResponseMsg());
		dto.setPayCompleteDate(resultMessage.getPaySuccessTime()==null?null:resultMessage.getPaySuccessTime().getTime());
		dto.setPayOrderId(resultMessage.getTransactionId());
		dto.setFrpCode(resultMessage.getPayInterface());
		if(PayStatusEnum.FAILURE == resultMessage.getPayStatus() ||
				PayStatusEnum.INIT == resultMessage.getPayStatus()){
			dto.setStatus(PaymentOrderStatusEnum.DOING);
		}else if(PayStatusEnum.SUCCESS == resultMessage.getPayStatus()){
			dto.setStatus(PaymentOrderStatusEnum.SUCCESS);
		}
		dto.setBasicProductCode(resultMessage.getBasicProductCode());
		return dto;
	
	}
}
