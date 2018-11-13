package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import com.yeepay.g3.core.payprocessor.enumtype.CancelStatusEnum;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.ncpay.dto.*;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 处理NCPAY的mq消息
 * 
 * @author yp-tc-m-2804
 */
@Service
public class NcPayResultPreAuthProccess extends AbstractService {

	private static final Logger logger = PayLoggerFactory.getLogger(NcPayResultPreAuthProccess.class);

	@Autowired
	private ResultProcessService resultProcessService;

	@Autowired
	private NotifyService notifyService;


	/**
	 * 预授权确认
	 * 同步结果转消息
	 */
	public void processForNcPayPreAuth(PayRecord record, PayPreAuthConfirmResponseDTO response) {
		PaymentResultMessage resultMessage = new PaymentResultMessage();
		resultMessage.setBizOrderNum(record.getRecordNo());
		resultMessage.setBankOrderNo(response.getBankOrderNo());
		resultMessage.setCost(response.getCost());
		resultMessage.setPayCompleteDate(response.getCompleteDate() == null ? System.currentTimeMillis() : response.getCompleteDate());
		resultMessage.setPayStatus(response.getStatus());
		resultMessage.setFrpCode(response.getFrpCode());
		resultMessage.setBasicProductCode(record.getBasicProductCode());
		resultMessage.setPayOrderNum(response.getPaymentNo());
		resultMessage.setErrorCode(response.getReturnCode());
		resultMessage.setErrorMsg(response.getReturnMsg());
		resultMessage.setAccountNo(response.getCardId());
		// 需通知业务方
		this.processForNcPayPreAuthMsg(resultMessage, record);
	}


	/**
	 * 预授权撤销、预授权完成撤销
	 * 同步结果转消息
	 */
	public void processForNcPayPreAuthCancel(PayRecord record, PayPreAuthCancelResponseDTO response) {
		PaymentResultMessage resultMessage = new PaymentResultMessage();
		resultMessage.setBizOrderNum(record.getRecordNo());
		resultMessage.setBankOrderNo(response.getBankOrderNo());
		resultMessage.setCost(response.getCost());
		resultMessage.setPayCompleteDate(response.getCompleteDate() == null ? System.currentTimeMillis() : response.getCompleteDate());
		resultMessage.setPayStatus(response.getStatus());
		resultMessage.setFrpCode(response.getFrpCode());
		resultMessage.setBasicProductCode(record.getBasicProductCode());
		resultMessage.setPayOrderNum(response.getPaymentNo());
		resultMessage.setErrorCode(response.getReturnCode());
		resultMessage.setErrorMsg(response.getReturnMsg());
		this.processForNcPayPreAuthMsg(resultMessage, record);
	}


	/**
	 * 预授权完成
	 * 同步结果转消息
	 */
	public void processForNcPayPreAuthComplete(PayRecord record, PayPreAuthCompleteResponseDTO response) {
		PaymentResultMessage resultMessage = new PaymentResultMessage();
		resultMessage.setBizOrderNum(record.getRecordNo());
		resultMessage.setBankOrderNo(response.getBankOrderNo());
		resultMessage.setCost(response.getCost());
		resultMessage.setPayCompleteDate(response.getCompleteDate() == null ? System.currentTimeMillis() : response.getCompleteDate());
		resultMessage.setPayStatus(response.getStatus());
		resultMessage.setFrpCode(response.getFrpCode());
		resultMessage.setBasicProductCode(record.getBasicProductCode());
		resultMessage.setPayOrderNum(response.getPaymentNo());
		resultMessage.setErrorCode(response.getReturnCode());
		resultMessage.setErrorMsg(response.getReturnMsg());
		resultMessage.setBankCode(response.getBankCode());
		resultMessage.setCardType(response.getBankCardType() == null ? null : response.getBankCardType().name());
		this.processForNcPayPreAuthMsg(resultMessage, record);
	}



	/**
	 * 预授权统一结果处理
	 * @param resultMessage 回调消息
	 * @param payRecord 支付子单
	 */
	public PayRecord processForNcPayPreAuthMsg(PaymentResultMessage resultMessage, PayRecord payRecord) {
		// 一、判断子单状态（排除成功状态，失败、冲正或者DOING需要分情况来考虑）
		if(payRecord == null) {
			payRecord = payRecordDao.selectByPrimaryKey(resultMessage.getBizOrderNum());
		}
		checkPayRecordForPreAuth(payRecord);
		// 二、判断回调内容
		// 1、失败
		if (PaymentOrderStatusEnum.FAILURE.equals(resultMessage.getPayStatus())) {
			// 更新子单
			processPayRecord(payRecord, resultMessage);
			// 更新主单
			resultProcessService.updatePaymentToFailForPreAuth(payRecord);
			// 回调
			notifyService.notifyForPreAuth(payRecord);
			// 存缓存,只有预授权请求的订单才通知收银台，收银台需求
			if(PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())) {
				setPayResultToRedis(resultMessage.getBizOrderNum());
			}
		// 2、成功
		} else if (PaymentOrderStatusEnum.SUCCESS.equals(resultMessage.getPayStatus())) {
			// 2.1 判断子单状态（失败、冲正或者DOING）
			// 2.1.1 冲正(暂不支持冲正)
			if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
				// 更新信息，然后发起退款
				// 判断是否发起过退款
				if (preAuthReverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
					payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
					// 只有预授权订单才能发起冲正，其他不做处理
					if(PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())) {
						createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
					}
				}
			// 2.1.2 DOING
			} else if (TrxStatusEnum.DOING.name().equals(payRecord.getStatus())) {
				// 更新子单
				processPayRecord(payRecord, resultMessage);
				// 更新主单
				resultProcessService.updatePaymentToSuccessForPreAuth(payRecord);
				// 回调
				notifyService.notifyForPreAuth(payRecord);
				// 存缓存,只有预授权请求的订单才通知收银台，收银台需求
				if(PayOrderType.PREAUTH_RE.name().equals(payRecord.getPayOrderType())) {
					setPayResultToRedis(resultMessage.getBizOrderNum());
				}
			// 2.1.3 失败（先失败后成功的订单，打日志，更新状态，回调业务方），此种情况存在于通道强制补单
			} else if (TrxStatusEnum.FAILUER.name().equals(payRecord.getStatus())) {
				logger.error("强制补单：recordNo=" + payRecord.getRecordNo() + ",payOrderType=" + payRecord.getPayOrderType() +
						",trxStatus=" + resultMessage.getPayStatus() + ",orgTrxStatus=" + payRecord.getStatus());
				// 只允许预授权完成撤销的订单能先失败后成功，不改状态只通知
				if(PayOrderType.PREAUTH_CC.name().equals(payRecord.getPayOrderType())) {
					// 更新子单和主单
					payRecord.setStatus(TrxStatusEnum.SUCCESS.name());
					// 回调业务方
					notifyService.notifyForPreAuth(payRecord);
				} else {
					logger.error("强制补单失败,非法订单类型：recordNo=" + payRecord.getRecordNo() + ", payOrderType=" + payRecord.getPayOrderType());
				}
			}
		// 3、处理中
		} else if(PaymentOrderStatusEnum.DOING.equals(resultMessage.getPayStatus())) {
			// 处理中不需要更新子单和主单
		}
		return payRecord;
	}


	/**
	 * 校验子单状态
	 */
	protected void checkPayRecordForPreAuth(PayRecord payRecord) {
		if (payRecord == null) {
			logger.error("payRecord not exist");
			throw new PayBizException(ErrorCode.P9002006);
		}
		if (TrxStatusEnum.SUCCESS.name().equals(payRecord.getStatus())) {
			logger.error("payRecord already fail or success, " + payRecord.getRecordNo());
			throw new PayBizException(ErrorCode.P9002002);
		}
	}


	/**
	 * 更新子单内容
	 */
	private void processPayRecord(PayRecord payRecord, PaymentResultMessage resultMessage) {
		// 成功
		if(PaymentOrderStatusEnum.SUCCESS.equals(resultMessage.getPayStatus())) {
			payRecord.setStatus(TrxStatusEnum.SUCCESS.name());
		// 失败
		} else if(PaymentOrderStatusEnum.FAILURE.equals(resultMessage.getPayStatus())) {
			payRecord.setStatus(TrxStatusEnum.FAILUER.name());
			String errorCode = null;
			String errormsg = null;
			if (StringUtils.isNotBlank(resultMessage.getErrorCode())) {
				ErrorMeta meta = ErrorCodeUtil.translateCode(ErrorCodeSource.NCPAY.getSysCode(),
						resultMessage.getErrorCode(), resultMessage.getErrorMsg(), ErrorCode.P9001000);
				if (meta != null) {
					errorCode = meta.getErrorCode();
					errormsg = meta.getErrorMsg();
				} else {
					errorCode = "P" + resultMessage.getErrorCode();
					errormsg = resultMessage.getErrorMsg();
				}
			}
			// 更新支付失败，更新失败原因和失败错误码
			payRecord.setErrorCode(errorCode);
			payRecord.setErrorMsg(errormsg);
		} else {
			logger.error("不支持的回调支付类型：recordNo=" + payRecord.getRecordNo() + ", payStatus=" + resultMessage.getPayStatus());
		}
		payRecord.setPaymentNo(resultMessage.getPayOrderNum());
		payRecord.setBankId(resultMessage.getBankCode());
		payRecord.setBankOrderNo(resultMessage.getBankOrderNo());
		payRecord.setBankSeq(resultMessage.getBankSeq());
		payRecord.setBankTrxId(resultMessage.getTradeSerialNo());
		payRecord.setCost(resultMessage.getCost() == null ? null : resultMessage.getCost().getValue());
		payRecord.setPayTime(new Date(resultMessage.getPayCompleteDate()));
		payRecord.setCardId(resultMessage.getAccountNo());
		payRecord.setCardType(resultMessage.getCardType());
		payRecord.setFrpCode(resultMessage.getFrpCode());
		payRecord.setUpdateTime(new Date());
		if(StringUtils.isBlank(payRecord.getBasicProductCode())) {
			payRecord.setBasicProductCode(resultMessage.getBasicProductCode());
		}
	}


	/**
	 * 预授权"冲正"
	 * 结果统一处理
	 * @param resultMessage
	 * @param record
	 * @return
	 */
	public void processForNcPayReversePreAuthMsg(PaymentResultMessage resultMessage, PreAuthReverseRecord record) {
		if(record == null || CancelStatusEnum.SUCCESS.name().equals(record.getCancelStatus())
				|| CancelStatusEnum.FAILURE.name().equals(record.getCancelStatus())) {
			logger.info("预授权冲正已经终态，无需处理， recordNo=" + resultMessage.getBizOrderNum());
			return;
		}
		// 二、判断回调内容
		// 1、失败
		if (PaymentOrderStatusEnum.FAILURE.equals(resultMessage.getPayStatus())) {
			// 更新子单
			record.setCancelStatus(CancelStatusEnum.FAILURE.name());
			processPreAuthReverseRecord(resultMessage, record);
			preAuthReverseRecordDao.updateByPrimaryKey(record);
		// 2、成功
		} else if (PaymentOrderStatusEnum.SUCCESS.equals(resultMessage.getPayStatus())) {
			// 2.1 判断子单状态（失败或者DOING）
			// 2.1.1 DOING
			if (CancelStatusEnum.DOING.name().equals(record.getCancelStatus())) {
				// 同时更新冲正记录表和冲正表
				processPreAuthReverseRecord(resultMessage, record);
				ReverseRecord reverseRecord = reverseRecordDao.queryByRecordNo(record.getRecordNo());
				resultProcessService.updateReverseRecord(record, reverseRecord);
			} else if (CancelStatusEnum.FAILURE.name().equals(record.getCancelStatus())) {
				logger.error("强制补单：reverseNo=" + record.getReverseNo() + ",payOrderType=" + record.getPayOrderType() +
						",trxStatus=" + resultMessage.getPayStatus() + ",orgTrxStatus=" + record.getCancelStatus());
			}
		// 3、处理中
		} else if(PaymentOrderStatusEnum.DOING.equals(resultMessage.getPayStatus())) {
			// 处理中不需要更新子单和主单
		}

	}

	private void processPreAuthReverseRecord(PaymentResultMessage resultMessage, PreAuthReverseRecord record) {
		record.setPaymentNo(resultMessage.getPayOrderNum());
	}


	/**
	 * 预授权"冲正"时
	 * 撤销或完成撤销
	 * 同步结果转消息
	 */
	public void processForNcPayReversePreAuthCancel(PreAuthReverseRecord record, PayPreAuthCancelResponseDTO response) {
		PaymentResultMessage resultMessage = new PaymentResultMessage();
		resultMessage.setBizOrderNum(record.getRecordNo());
		resultMessage.setBankOrderNo(response.getBankOrderNo());
		resultMessage.setCost(response.getCost());
		resultMessage.setPayCompleteDate(response.getCompleteDate() == null ? System.currentTimeMillis() : response.getCompleteDate());
		resultMessage.setPayStatus(response.getStatus());
		resultMessage.setFrpCode(response.getFrpCode());
		resultMessage.setPayOrderNum(response.getPaymentNo());
		resultMessage.setErrorCode(response.getReturnCode());
		resultMessage.setErrorMsg(response.getReturnMsg());
		this.processForNcPayReversePreAuthMsg(resultMessage, record);
	}
}
