package com.yeepay.g3.core.payprocessor.biz.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import org.springframework.stereotype.Service;

import com.yeepay.g3.component.member.dto.MemberFundTradeInfoDTO;
import com.yeepay.g3.component.member.enumtype.MemberTradeTypeEnum;
import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.biz.QueryBiz;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLogger;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.account.pay.dto.PayOrderQueryDTO;
import com.yeepay.g3.facade.account.pay.enums.RequestStatusEnum;
import com.yeepay.g3.facade.frontend.dto.FrontendQueryResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.frontend.dto.NetPayResultMessage;
import com.yeepay.g3.facade.frontend.dto.PayResultMessage;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.ncpay.dto.GuaranteeCflQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.GuaranteeCflResultMessage;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordQueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.PaymentStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author chronos.
 * @createDate 2016/11/11.
 */
@Service
public class QueryBizImpl extends BaseBizImpl implements QueryBiz {

	public static final PayLogger logger = (PayLogger) PayLoggerFactory.getLogger(QueryBizImpl.class);

	@Override
	public QueryResponseDTO query(QueryRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[订单处理器查单] - [orderNo = " + requestDTO.getOrderNo() + "]");
		QueryResponseDTO responseDTO = new QueryResponseDTO();
		try {
			PaymentRequest payment = paymentRequestService.queryBySystemAndOrderNo(requestDTO.getOrderSystem(),
					requestDTO.getOrderNo());
			PayRecord record = queryRecord(payment);
			buildQueryResponseDTO(payment, record, responseDTO);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(responseDTO, th);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}

	@Override
	public PayRecordResponseDTO query(PayRecordQueryRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[统一收银台查单] - [recordNo = " + requestDTO.getRecordNo() + "]");
		PayRecordResponseDTO responseDTO = new PayRecordResponseDTO();
		try {
			PayRecord payRecord = payRecordService.queryRecordById(requestDTO.getRecordNo());
			PaymentRequest payment = paymentRequestService.selectByPrimaryKey(payRecord.getRequestId());
			buildPayRecordResponse(payRecord, payment, responseDTO);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(responseDTO, th);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}

	@Override
	public void repairOrder() {
		PayLoggerFactory.TAG_LOCAL.set("[定时补单]");
		int startTimeOffset = ConstantUtils.getRepairOrderStartTimeOffset();
		int endTimeOffset = ConstantUtils.getRepairOrderEndTimeOffset();
		int maxRowCount = ConstantUtils.getRepairOrderMaxRowCount();
		
		Date now = new Date();
		Date start = DateUtils.addMinute(now, startTimeOffset);
		Date end = DateUtils.addMinute(now, endTimeOffset);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		logger.info("查询参数,开始时间" + sdf.format(start) + 
		        ", 结束时间:" + sdf.format(end) +", 最大行数:" + maxRowCount);
		
		List<PayRecord> doingRecords = null;
		try {
			doingRecords = payRecordService.queryDoingRecord(start, end , maxRowCount);
		} catch (Throwable th) {
			logger.error("系统异常", th);
		}
		if (doingRecords == null || doingRecords.size() < 1) {
		    logger.info("无未支付订单");
			PayLoggerFactory.TAG_LOCAL.remove();
			return;
		}
		logger.info("[未支付订单] - [size = " + doingRecords.size() + "]");
		for (PayRecord record : doingRecords) {
			try {
				repairByRecord(record);
			} catch (PayBizException e) {
				logger.warn("[补单失败] [recordNo=" + record.getRecordNo() + "]- [code = " + e.getDefineCode()
						+ "] - [message = " + e.getMessage() + "]");
			} catch (Throwable th) {
				logger.error("[补单失败,系统异常] - [recordNo=" + record.getRecordNo() + "]", th);
			}
		}
		PayLoggerFactory.TAG_LOCAL.remove();
	}

	@Override
	public OperationResponseDTO batchRepair(OperationRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[批量补单] - [size =" + requestDTO.getRecordList().size() + " ]");
		OperationResponseDTO responseDTO = new OperationResponseDTO();
		for (String recordNo : requestDTO.getRecordList()) {
			try {
				PayRecord record = payRecordService.queryRecordById(recordNo);
				if (!TrxStatusEnum.DOING.name().equals(record.getStatus())) {
					responseDTO.setIgnore(responseDTO.getIgnore() + 1);
					continue;
				}
				repairByRecord(record);
				responseDTO.setSuccess(responseDTO.getSuccess() + 1);
			} catch (PayBizException e) {
				logger.warn("[补单失败] [recordNo=" + recordNo + "]- [code = " + e.getDefineCode() + "] - [message = "
						+ e.getMessage() + "]");
				responseDTO.getErrorList().add("[" + recordNo + "] - [" + e.getDefineCode() + "]" + e.getMessage());
			} catch (Throwable th) {
				logger.error("[补单失败,系统异常] - [recordNo=" + recordNo + "]", th);
				responseDTO.getErrorList().add("[" + recordNo + "]" + th.getMessage());
			}
		}
		PayLoggerFactory.TAG_LOCAL.remove();
		return responseDTO;
	}

	@Override
	public QueryResponseDTO queryHisOrder(QueryRequestDTO requestDTO) {
		PayLoggerFactory.TAG_LOCAL.set("[查询历史订单] - [orderNo = " + requestDTO.getOrderNo() + "]");
		QueryResponseDTO responseDTO = new QueryResponseDTO();
		PaymentRequest payment = null;
		PayRecord record = null;

		try {
			payment = paymentRequestService.queryBySystemAndOrderNo(requestDTO.getOrderSystem(),
					requestDTO.getOrderNo());
			if(payment == null) {
				logger.info("交易库未查到主单，查历史库，orderNo=" + requestDTO.getOrderNo() + ", orderSystem=" + requestDTO.getOrderSystem());
				payment = hisPaymentRequestService.queryBySystemAndOrderNo(requestDTO.getOrderSystem(),
						requestDTO.getOrderNo());
			}
			if (payment == null) {
				throw new PayBizException(ErrorCode.P9002004);
			}
			if (PaymentStatusEnum.SUCCESS.name().equals(payment.getPayStatus())) {
				record = payRecordService.queryRecordByRecord(payment.getRecordNo());
				if(record == null) {
					logger.info("交易库未查到子单，查历史库，orderNo=" + requestDTO.getOrderNo() + ", orderSystem=" + requestDTO.getOrderSystem()
							+ "recordNo=" + payment.getRecordNo());
					record = hisPayRecordService.queryRecordById(payment.getRecordNo());
				}
			}
			buildQueryResponseDTO(payment, record, responseDTO);
		} catch (Throwable th) {
			logger.error("[业务异常]", th);
			setErrorInfo(responseDTO, th);
		} finally {
			PayLoggerFactory.TAG_LOCAL.remove();
		}
		return responseDTO;
	}

	@Override
	public void repairCombOrder() {
		PayLoggerFactory.TAG_LOCAL.set("[组合定时补单]");
		int startTimeOffset = ConstantUtils.getRepairCombOrderStartTimeOffset();
		int endTimeOffset = ConstantUtils.getRepairCombOrderEndTimeOffset();
		int maxRowCount = ConstantUtils.getRepairCombOrderMaxRowCount();

		Date now = new Date();
		Date start = DateUtils.addMinute(now, startTimeOffset);
		Date end = DateUtils.addMinute(now, endTimeOffset);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		logger.info("查询参数,开始时间" + sdf.format(start) +
				", 结束时间:" + sdf.format(end) +", 最大行数:" + maxRowCount);
		List<CombPayRecord> depositRecords = null;
		try {
			depositRecords = combPayRecordService.selectDepositPayRecords(start, end , maxRowCount);
		} catch (Throwable th) {
			logger.error("系统异常", th);
		}
		if (depositRecords == null || depositRecords.size() < 1) {
			logger.info("无未支付订单");
			PayLoggerFactory.TAG_LOCAL.remove();
			return;
		}
		logger.info("[未支付订单] - [size = " + depositRecords.size() + "]");
		for (CombPayRecord combPayRecord : depositRecords) {
			try {
				PayRecord payRecord = payRecordService.queryRecordById(combPayRecord.getRecordNo());
				if(payRecord == null) {
					logger.error("获取第一支付子单失败");
					continue;
				}
				if(TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
					// 第一支付子单明确失败，更新第二支付子单为失败
					combAbstractService.updateFailByRecordNo(payRecord);
				}else if(TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
					//第一支付子单明确成功，补偿第二支付子单
					if(payRecord.isCombinedPay()) {
						combAbstractService.processCombResult(payRecord, combPayRecord);
					}
				}else {
					// 第一支付子单其他状态不做处理
					logger.info("第二支付子单DEPOSIT，第一支付子单非终态，不做处理，recordNo=" + payRecord.getRecordNo());
				}
			} catch (PayBizException e) {
				logger.warn("[补单失败] [recordNo=" + combPayRecord.getRecordNo() + "]- [code = " + e.getDefineCode()
						+ "] - [message = " + e.getMessage() + "]");
			} catch (Throwable th) {
				logger.error("[补单失败,系统异常] - [recordNo=" + combPayRecord.getRecordNo() + "]", th);
			}
		}
		PayLoggerFactory.TAG_LOCAL.remove();
	}

	private boolean checkPayOrderTypeForComb(PayRecord record) {
		if (PayOrderType.SALE.name().equals(record.getPayOrderType())
				|| PayOrderType.ONLINE.name().equals(record.getPayOrderType())
				|| PayOrderType.OFFLINE.name().equals(record.getPayOrderType())
				|| PayOrderType.NET.name().equals(record.getPayOrderType())
				|| PayOrderType.ACCOUNT.name().equals(record.getPayOrderType())
				|| PayOrderType.MEMBER_PAY.name().equals(record.getPayOrderType())
				) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 根据payment查成功的Record
	 * 
	 * @param payment
	 * @return
	 */
	private PayRecord queryRecord(PaymentRequest payment) {
		if (payment == null)
			throw new PayBizException(ErrorCode.P9002004);
		// 只有预授权的订单才会有失败状态，循环预授权查主单时，在有终态的情况下，需要返回子单信息
		if (!PaymentStatusEnum.SUCCESS.name().equals(payment.getPayStatus())
				&& !PaymentStatusEnum.FAILURE.name().equals(payment.getPayStatus()))
			return null;
		PayRecord record = payRecordService.queryRecordById(payment.getRecordNo());
		return record;
	}

	/**
	 * ncpay补单
	 * 
	 * @param record
	 */
	private void repairNcpayOrder(PayRecord record) {
		if (StringUtils.isBlank(record.getPaymentNo())) {
			throw new PayBizException(ErrorCode.P9001000, "[请求NCPAY支付失败,无法补单]");
		}
		PayQueryResponseDTO queryResult = ncPayService.queryPaymentOrder(record.getPaymentNo());
		if (queryResult != null) {
			if (PaymentOrderStatusEnum.SUCCESS.equals(queryResult.getStatus())
					|| PaymentOrderStatusEnum.FAILURE.equals(queryResult.getStatus())) {
				if(record.isCombinedPay()) {
					ncPayResultProccess.processForNcPayMsgComb(buildNcMessage(queryResult), record);
				}else {
					ncPayResultProccess.processForNcPayMsg(buildNcMessage(queryResult), record);
				}

			}
		}
	}

	/**
	 * 构建ncpay回调参数
	 * 
	 * @param queryResult
	 * @return
	 */
	private PaymentResultMessage buildNcMessage(PayQueryResponseDTO queryResult) {
		PaymentResultMessage resultMessage = new PaymentResultMessage();
		resultMessage.setAccountNo(queryResult.getAccountNo());
		resultMessage.setBankOrderNo(queryResult.getBankOrderNo());
		resultMessage.setBankCode(queryResult.getBankCode());
		resultMessage.setBankName(queryResult.getBankName());
		resultMessage.setBizOrderNum(queryResult.getBizOrderId());
		resultMessage.setCardType(queryResult.getBankCardType().name());
		resultMessage.setCost(queryResult.getCost());
		resultMessage.setFrpCode(queryResult.getFrpCode());
		resultMessage.setMerchantNo(queryResult.getMerchantNo());
		resultMessage.setMerchantName(queryResult.getMerchantName());
		resultMessage.setPayAmount(queryResult.getRealAmount());
		resultMessage.setPayCompleteDate(queryResult.getPayCompleteDate());
		resultMessage.setPayOrderNum(queryResult.getPayOrderId());
		resultMessage.setPayStatus(queryResult.getStatus());
		resultMessage.setTradeSerialNo(queryResult.getTradeSerialNo());
		resultMessage.setBasicProductCode(queryResult.getBasicProductCode());
		return resultMessage;
	}

	/**
	 * 构建fe回调参数
	 * 
	 * @param queryResult
	 * @return
	 */
	private PayResultMessage buildFeMessage(FrontendQueryResponseDTO queryResult) {
		PayResultMessage resultMessage = new PayResultMessage();
		resultMessage.setBankSuccessTime(queryResult.getBankSuccessTime());
		resultMessage.setBankTotalCost(queryResult.getBankTotalCost());
		resultMessage.setCustomerNumber(queryResult.getCustomerNumber());
		resultMessage.setCreateTime(new Date());
		// resultMessage.setExpireTime(queryResult.gete);
		resultMessage.setOrderNo(queryResult.getOrderNo());
		resultMessage.setOutTradeNo(queryResult.getOutTradeNo());
		resultMessage.setOrderType(queryResult.getOrderType());
		resultMessage.setOpenId(queryResult.getOpenId());
		resultMessage.setPayBank(queryResult.getPayBank());
		resultMessage.setPayBankcardType(queryResult.getPayBankcardType());
		resultMessage.setPayInterface(queryResult.getPayInterface());
		resultMessage.setPayStatus(queryResult.getPayStatus());
		resultMessage.setPaySuccessTime(queryResult.getPaySuccessTime());
		resultMessage.setPlatformType(queryResult.getPlatformType());
		resultMessage.setRequestId(queryResult.getRequestId());
		resultMessage.setRequestSystem(queryResult.getRequestSystem());
		resultMessage.setTransactionId(queryResult.getTransactionId());
		resultMessage.setTotalAmount(queryResult.getTotalAmount());
		resultMessage.setBasicProductCode(queryResult.getBasicProductCode());
		resultMessage.setExtParam(queryResult.getExtParam());// 扩展参数 added by zhijun.wang 2017-12-13
		return resultMessage;
	}
	/**
	 * fe补单
	 * 
	 * @param record
	 */
	private void repairFeOrder(PayRecord record) {
		FrontendQueryResponseDTO responseDTO = frontEndService.queryPaymentOrder(record.getRecordNo(),
				record.getPlatformType());
		if (responseDTO == null) {
			throw new PayBizException(ErrorCode.P9001001, "调用FE查单失败");
		}
		if (StringUtils.isNotBlank(responseDTO.getResponseCode())) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), responseDTO.getResponseCode(),
					responseDTO.getResponseMsg(), ErrorCode.P9001000);
		}
		if (!PayStatusEnum.SUCCESS.equals(responseDTO.getPayStatus())) {
			throw new PayBizException(ErrorCode.P9002005,
					"[订单未成功] - [currentStatus = " + responseDTO.getPayStatus().name() + "]");
		}
		if(record.isCombinedPay()) {
			feResultProccess.processForFePayMsgComb(buildFeMessage(responseDTO), record);
		}else {
			feResultProccess.processForFePayMsg(buildFeMessage(responseDTO), record);
		}

	}

	/**
	 * fe网银补单补单
	 * 
	 * @param record
	 */
	private void repairFeNetPayOrder(PayRecord record) {
		NetPayResultMessage responseDTO = frontEndService.queryNetPayPaymentOrder(record.getRecordNo());
		if (responseDTO == null) {
			throw new PayBizException(ErrorCode.P9001001, "调用FE查单失败");
		}
		if (StringUtils.isNotBlank(responseDTO.getResponseCode())) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), responseDTO.getResponseCode(),
					responseDTO.getResponseMsg(), ErrorCode.P9001000);
		}
		if (!PayStatusEnum.SUCCESS.equals(responseDTO.getPayStatus())) {
			throw new PayBizException(ErrorCode.P9002005,
					"[订单未成功] - [currentStatus = " + responseDTO.getPayStatus().name() + "]");
		}
		if(record.isCombinedPay()) {
			netPayResultProcess.processForFePayMsgComb(responseDTO, record);
		}else {
			netPayResultProcess.processForFePayMsg(responseDTO, record);
		}

	}

	/**
	 * fe分期补单补单
	 * 
	 * @param record
	 */
	private void repairFeCflPayOrder(PayRecord record) {
		InstallmentResultMessage responseDTO = frontEndService.queryCflPayPaymentOrder(record.getRecordNo());
		if (responseDTO == null) {
			throw new PayBizException(ErrorCode.P9001001, "调用FE查单失败");
		}
		if (StringUtils.isNotBlank(responseDTO.getResponseCode())) {
			throw ErrorCodeUtil.mapErrorCode(ErrorCodeSource.FRONTEND.getSysCode(), responseDTO.getResponseCode(),
					responseDTO.getResponseMsg(), ErrorCode.P9001000);
		}
		if (!PayStatusEnum.SUCCESS.equals(responseDTO.getPayStatus())) {
			throw new PayBizException(ErrorCode.P9002005,
					"[订单未成功] - [currentStatus = " + responseDTO.getPayStatus().name() + "]");
		}
		if(record.isCombinedPay()) {
			cflPayResultProcess.processForCflPayMsgComb(responseDTO, record);
		}else {
			cflPayResultProcess.processForCflPayMsg(responseDTO, record);
		}
	}
	
	/**
	 * 账户支付补单
	 * 
	 * @param record
	 */
	private void repairAccountPayOrder(PayRecord record) {
	     PayOrderQueryDTO responseDTO = accountPayService.queryPayInfoByOrderNo(record.getRecordNo() , record.getCreateTime());
	     if(responseDTO == null) {
	         throw new PayBizException(ErrorCode.P9001001, "调用账户支付查单失败");
	     }
	     
	     if(!RequestStatusEnum.SUCCESS.equals(responseDTO.getStatus())) {
	         throw new PayBizException(ErrorCode.P9002005,
	                    "[订单未成功] - [currentStatus = " + responseDTO.getStatus().name() + "]");
	     }
	     // 组合支付
	     if(record.isCombinedPay()) {
			 accountPayResultProccess.processForAccountPayResponseComb(responseDTO, record);
		 }else {
			 accountPayResultProccess.processForAccountPayResponse(responseDTO, record);
		 }

	}

	/**
	 * 预授权补单
	 */
	private void repairNcPayPreAuthPayOrder(PayRecord record) {

		if (StringUtils.isBlank(record.getPaymentNo())) {
			throw new PayBizException(ErrorCode.P9001000, "[请求NCPAY支付失败,无法补单]");
		}
		PayQueryResponseDTO queryResult = ncPayService.queryPaymentOrder(record.getPaymentNo());
		if (queryResult != null) {
			if (PaymentOrderStatusEnum.SUCCESS.equals(queryResult.getStatus())
					|| PaymentOrderStatusEnum.FAILURE.equals(queryResult.getStatus())) {
				ncPayResultPreAuthProccess.processForNcPayPreAuthMsg(buildNcMessage(queryResult), record);
			}
		}

	}

    /**
     * 个人会员同步支付补单
     * @param record
     */
    private void repairPersonalMemberSyncPayOrder(PayRecord record) {
         MemberFundTradeInfoDTO responseDTO = personalMemberPayService.queryPayInfoByOrderNo(record.getRecordNo(),MemberTradeTypeEnum.PAYMENT);
         if(responseDTO == null) {
             throw new PayBizException(ErrorCode.P9004080, "调用个人会员支付查单失败");
         }
         if("DEBITED".equals(responseDTO.getStatusDesc())) {
         	if(record.isCombinedPay()) {
         		personalMemberPayResultProccess.processForSyncPaySuccessComb(record.getRecordNo(), responseDTO.getTransFlowId());
			}else {
				personalMemberPayResultProccess.processForSyncPaySuccess(record.getRecordNo(), responseDTO.getTransFlowId());
			}
         }
         /*
         else {
             payRecordService.updatePaymentToFail(record.getRecordNo(), null, null);
         }*/
    }
	
	private void repairGuaranteeCflOrder(PayRecord record){
	    if (StringUtils.isBlank(record.getPaymentNo())) {
            throw new PayBizException(ErrorCode.P9001000, "[请求NCPAY支付失败,无法补单]");
        }
	    
	    GuaranteeCflQueryResponseDTO response = ncPayService.queryGuaranteeCflOrder(record.getPaymentNo());
	    
	    GuaranteeCflResultMessage message = new GuaranteeCflResultMessage();
	    message.setBankCode(response.getBankCode());
	    message.setBankOrderNo(response.getBankOrderNo());
	    message.setBankSeq(response.getBankSeq());
	    message.setBasicProductCode(response.getBasicProductCode());
	    message.setBiz(response.getBiz());
	    message.setBizOrder(response.getBizOrder());
	    message.setCardType(response.getCardType());
	    message.setCflCount(response.getCflCount());
	    message.setConfirmTime(response.getConfirmTime());
	    message.setCost(response.getCost());
	    message.setErrorCode(response.getErrorCode());
	    message.setErrorMsg(response.getErrorMsg());
	    message.setFrpCode(response.getFrpCode());
	    message.setMerchantNo(response.getMerchantNo());
	    message.setOrderNo(response.getOrderNo());
	    message.setOrderSystem(response.getOrderSystem());
	    message.setOrderType(response.getOrderType());
	    message.setOutTradeNo(response.getOutTradeNo());
	    message.setPaymentNo(response.getPaymentNo());
	    message.setRealAmount(response.getRealAmount());
	    message.setRetailProductCode(response.getRetailProductCode());
	    message.setState(response.getState());
	    message.setTradeSerialNo(response.getTradeSerialNo());
	    message.setCardId(response.getCardId());
	    message.setPayerInterestRate(response.getPayerInterestRate());
	    
	    guaranteeCflResultProccess.processForMQ(message);
	}
	
	private void repairByRecord(PayRecord record) {
		if (PayOrderType.SALE.name().equals(record.getPayOrderType())
				|| PayOrderType.CFL_EASY.name().equals(record.getPayOrderType())) {
			repairNcpayOrder(record);
		} else if (PayOrderType.ONLINE.name().equals(record.getPayOrderType())
				|| PayOrderType.OFFLINE.name().equals(record.getPayOrderType())) {
			// 分期
			repairFeCflPayOrder(record);
		} else if (PayOrderType.NET.name().equals(record.getPayOrderType())) {
			// 网银
			repairFeNetPayOrder(record);
		} else if(PayOrderType.ACCOUNT.name().equals(record.getPayOrderType())) {
			// 账户支付
		    repairAccountPayOrder(record);
		} else if(PayOrderType.PREAUTH_RE.name().equals(record.getPayOrderType()) ||
				PayOrderType.PREAUTH_CL.name().equals(record.getPayOrderType()) ||
				PayOrderType.PREAUTH_CM.name().equals(record.getPayOrderType()) ||
				PayOrderType.PREAUTH_CC.name().equals(record.getPayOrderType())) {
			repairNcPayPreAuthPayOrder(record);
		}else if(PayOrderType.MEMBER_PAY.name().equals(record.getPayOrderType())) {
			repairPersonalMemberSyncPayOrder(record);
		} else if (PayOrderType.GUAR_CFL.name().equals(record.getPayOrderType())) {
		    repairGuaranteeCflOrder(record);
		} else {
			repairFeOrder(record);
		}
	}




   
}
