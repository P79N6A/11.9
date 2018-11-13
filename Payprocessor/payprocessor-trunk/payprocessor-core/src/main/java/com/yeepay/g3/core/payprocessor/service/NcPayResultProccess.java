package com.yeepay.g3.core.payprocessor.service;

import java.sql.Timestamp;
import java.util.Date;

import com.yeepay.g3.core.payprocessor.entity.CombPayRecord;
import com.yeepay.g3.core.payprocessor.service.impl.CombAbstractService;
import com.yeepay.g3.facade.payprocessor.enumtype.CombTrxStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeSource;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCodeUtil;
import com.yeepay.g3.core.payprocessor.external.service.NcPayService;
import com.yeepay.g3.core.payprocessor.service.impl.AbstractService;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;
import com.yeepay.g3.facade.ncpay.enumtype.PaymentOrderStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 处理NCPAY的mq消息
 * 
 * @author yp-tc-m-2804
 */
@Service
public class NcPayResultProccess extends AbstractService {

	private static final Logger logger = PayLoggerFactory.getLogger(NcPayResultProccess.class);

	@Autowired
	private NcPayService ncPayService;

	@Autowired
	private ResultProcessService resultProcessService;

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private CombAbstractService combAbstractService;

	public void processForNcPayMsg(PaymentResultMessage resultMessage, PayRecord payRecord) {
		if (PaymentOrderStatusEnum.FAILURE.equals(resultMessage.getPayStatus())) {
			updateNcPaymentToFail(resultMessage);
			combAbstractService.updateFailByRecordNo(payRecord);
			setPayResultToRedis(resultMessage.getBizOrderNum());
		} else if (PaymentOrderStatusEnum.SUCCESS.equals(resultMessage.getPayStatus())) {
			// 查询支付子表
			checkPayRecord(payRecord);
			processPayRecord(payRecord, resultMessage);
			if (TrxStatusEnum.REVERSE.name().equals(payRecord.getStatus())) {
				// 更新信息，然后发起退款
				// 判断是否发起过退款
				if (reverseRecordDao.queryByRecordNo(payRecord.getRecordNo()) == null) {
					payRecordDao.updatePaymentBankInfo(payRecord, new Timestamp(System.currentTimeMillis()));
					createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正退款");
				}
			} else if (TrxStatusEnum.DOING.name().equals(payRecord.getStatus())) {
				resultProcessService.updatePaymentToSuccess(payRecord);
				notifyService.notify(payRecord);
				setPayResultToRedis(resultMessage.getBizOrderNum());
			}
		}
		ncPayService.updateTaskStatus(resultMessage.getPayOrderNum(), resultMessage.getBizOrderNum());
	}

	/**
	 * @param payQueryResponseDTO
	 * @throws Exception
	 */
	public TrxStatusEnum processForNcPayQueryResponse(PayQueryResponseDTO payQueryResponseDTO, PayRecord payRecord) {
		// 更新状态
		PayRecord dbRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
		if (TrxStatusEnum.REVERSE.name().equals(dbRecord.getStatus())) {
			return TrxStatusEnum.REVERSE;
		} else if (TrxStatusEnum.SUCCESS.name().equals(dbRecord.getStatus())) {
			try {
				// 通知订单方
				notifyService.notify(dbRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
			}
		} else {
			try {
				PaymentResultMessage resultMessage = transformResultMessage(payQueryResponseDTO);
				processPayRecord(payRecord, resultMessage);
				resultProcessService.updatePaymentToSuccess(payRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
				payRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
			}
			try {
				// 通知订单方
				notifyService.notify(payRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
			}
		}
		String updateRecordStatus = payRecordDao.queryStatus(payRecord.getRecordNo());
		return TrxStatusEnum.valueOf(updateRecordStatus);
	}

	private PaymentResultMessage transformResultMessage(PayQueryResponseDTO payQueryResponseDTO) {
		PaymentResultMessage resultMessage = new PaymentResultMessage();
		resultMessage.setBizOrderNum(payQueryResponseDTO.getBizOrderId());
		resultMessage.setBankCode(payQueryResponseDTO.getBankCode());
		resultMessage.setBankOrderNo(payQueryResponseDTO.getBankOrderNo());
		// resultMessage.set //TODO 退款需要
		resultMessage.setTradeSerialNo(payQueryResponseDTO.getTradeSerialNo());
		resultMessage.setCost(payQueryResponseDTO.getCost());
		resultMessage.setPayCompleteDate(payQueryResponseDTO.getPayCompleteDate());
		resultMessage.setPayStatus(payQueryResponseDTO.getStatus());
		resultMessage.setAccountNo(payQueryResponseDTO.getAccountNo());
		resultMessage.setCardType(payQueryResponseDTO.getBankCardType().name());
		resultMessage.setFrpCode(payQueryResponseDTO.getFrpCode());
		resultMessage.setBasicProductCode(payQueryResponseDTO.getBasicProductCode());
		return resultMessage;
	}

	private void updateNcPaymentToFail(PaymentResultMessage resultMessage) {
		// 对ncpay的错误码进行转码处理
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
		payRecordDao.updatePaymentToFail(resultMessage.getBizOrderNum(), errorCode, errormsg);
	}

	private void processPayRecord(PayRecord payRecord, PaymentResultMessage resultMessage) {
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
		//如果下单的时候，没有传basicProductCode，底层支付系统会补充
		//将底层支付系统补充的，回写到支付处理器的数据库中
		if(StringUtils.isBlank(payRecord.getBasicProductCode())) {
			payRecord.setBasicProductCode(resultMessage.getBasicProductCode());
		}
	}

	private void bindCard(PayRecord payRecord) {
		try {
			if(StringUtils.isBlank(payRecord.getBindCardInfoId())){
				long bindId = ncPayService.bindCardByOrderId(payRecord.getPaymentNo());
				if (bindId > 0) {
					payRecordDao.updateBindId(String.valueOf(bindId), payRecord.getRecordNo());
				}
			}
		} catch (Exception e) {
			logger.error("bindCard error: ", e);
		}
	}

	/**
	 * 组合支付回调处理逻辑
	 * @param resultMessage
	 */
	public void processForNcPayMsgComb(PaymentResultMessage resultMessage, PayRecord payRecord) {
		if (PaymentOrderStatusEnum.FAILURE.equals(resultMessage.getPayStatus())) {
			updateNcPaymentToFail(resultMessage);
			combAbstractService.updateFailByRecordNo(payRecord);
			setPayResultToRedis(resultMessage.getBizOrderNum());
		} else if (PaymentOrderStatusEnum.SUCCESS.equals(resultMessage.getPayStatus())) {
			// 检查第一支付子单，第二支付子单状态
			CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
			checkPayRecordComb(payRecord, combPayRecord);
			processPayRecord(payRecord, resultMessage);
			combAbstractService.processCombResult(payRecord, combPayRecord);
		}
		ncPayService.updateTaskStatus(resultMessage.getPayOrderNum(), resultMessage.getBizOrderNum());
	}


	/**
	 * 组合支付同步支付结果处理
	 * @param payQueryResponseDTO
	 * @param payRecord
	 * @return
	 */
	public TrxStatusEnum processForNcPayQueryResponseComb(PayQueryResponseDTO payQueryResponseDTO, PayRecord payRecord) {
		// 更新状态
		PayRecord dbRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
		if (TrxStatusEnum.REVERSE.name().equals(dbRecord.getStatus())) {
			return TrxStatusEnum.REVERSE;
		} else if (TrxStatusEnum.SUCCESS.name().equals(dbRecord.getStatus())) {
			CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
			if(CombTrxStatusEnum.SUCCESS.name().equals(combPayRecord.getStatus())) {
				try {
					// 通知订单方
					notifyService.notify(dbRecord);
				} catch (Exception e) {
					logger.info("PayBizException: ", e);
				}
			}
		} else {
			try {
				PaymentResultMessage resultMessage = transformResultMessage(payQueryResponseDTO);
				CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
				processPayRecord(payRecord, resultMessage);
				combAbstractService.processCombResult(payRecord, combPayRecord);
			} catch (Exception e) {
				logger.info("PayBizException: ", e);
				payRecord = payRecordDao.selectByPrimaryKey(payRecord.getRecordNo());
			}
		}
		String updateRecordStatus = payRecordDao.queryStatus(payRecord.getRecordNo());
		return TrxStatusEnum.valueOf(updateRecordStatus);
	}
}
