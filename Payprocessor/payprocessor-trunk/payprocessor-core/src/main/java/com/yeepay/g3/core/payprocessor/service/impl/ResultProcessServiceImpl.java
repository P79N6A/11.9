
package com.yeepay.g3.core.payprocessor.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.core.payprocessor.entity.*;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.facade.mktg.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.CombTrxStatusEnum;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.errorcode.ErrorCode;
import com.yeepay.g3.core.payprocessor.service.ResultProcessService;
import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;

/**
 * @author peile.fan
 *
 */
@Service
public class ResultProcessServiceImpl extends AbstractService implements ResultProcessService {

	protected static final Logger logger = PayLoggerFactory.getLogger(ResultProcessServiceImpl.class);

	@Override
	public void updatePaymentToSuccess(PayRecord payRecord) {
		int payRecord_result = payRecordDao.updateRecordToSuccess(payRecord);
		if (payRecord_result == 0) {
			throw new PayBizException(ErrorCode.P9002002);
		}
		payRecord.setStatus(TrxStatusEnum.SUCCESS.name());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", payRecord.getRequestId());
		map.put("recordNo", payRecord.getRecordNo());
		int paymentRequest_result = paymentRequestDao.updateRequestToSuccess(map);
		if (paymentRequest_result == 0) {
			// 主表已经成功或冲正，子表冲正并生成退款记录
			int payRecord_result_reverser = payRecordDao.updateRecordToReverse(payRecord);
			payRecord.setStatus(TrxStatusEnum.REVERSE.name());
			if (payRecord_result_reverser == 0) {
				throw new PayBizException(ErrorCode.P9001000);
			}
			createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正或差错退款");
		}
	}

	@Override
	public void updatePaymentToReverse(PaymentRequest paymentRequest, PayRecord payRecord,String reverseRemark) {
		int rows = payRecordDao.updateRecordToReverse(payRecord);
		if (rows == 0) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		// 如果组合支付，需要把第二支付子单也置成reverse
		if(payRecord.isCombinedPay()) {
			CombPayRecord combPayRecord = combPayRecordDao.selectByRecordNo(payRecord.getRecordNo());
			int combPayrecord_reverse = combPayRecordDao.updateToReverse(combPayRecord);
			if (combPayrecord_reverse == 0) {
				throw new PayBizException(ErrorCode.P9001000);
			}
		}
		String reverseRemarkAccount = ConstantUtils.getAccountPayRefundRemark();
		if(PayOrderType.ACCOUNT.name().equals(payRecord.getPayOrderType())){
			reverseRemark = reverseRemarkAccount;
		}
		paymentRequestDao.updateRequestToReverse(paymentRequest.getId(), paymentRequest.getPayStatus());
		createRefundRecord(paymentRequest.getId(), payRecord.getRecordNo(), reverseRemark);
	}

	@Override
	public void updatePaymentToSuccessForPreAuth(PayRecord payRecord) {
		int payRecord_result = payRecordDao.updateRecordForPreAuth(payRecord);
		if (payRecord_result == 0) {
			throw new PayBizException(ErrorCode.P9002002);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", payRecord.getRequestId());
		map.put("recordNo", payRecord.getRecordNo());
		// 预授权完成的订单，还需要更新预授权完成金额
		if(PayOrderType.PREAUTH_CM.name().equals(payRecord.getPayOrderType())) {
			map.put("preAuthAmount", payRecord.getAmount());
		}
		int paymentRequest_result = paymentRequestDao.updatePaymentToSuccessForPreAuth(map);
		if (paymentRequest_result == 0) {
			// 主表已经成功，子表冲正并生成撤销记录
			payRecord.setStatus(TrxStatusEnum.REVERSE.name());
			int payRecord_result_reverser = payRecordDao.updateRecordToReverseForPreAuth(payRecord);// 此处只有成功状态才能更改为冲正
			if (payRecord_result_reverser == 0) {
				logger.error("更新冲正状态失败， recordNo=" + payRecord.getRecordNo());
				throw new PayBizException(ErrorCode.P9001000);
			}
			// 创建"撤销"记录
			createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正或差错退款");
		}
	}

	@Override
	public void updatePaymentToFailForPreAuth(PayRecord payRecord) {
		int payRecord_result = payRecordDao.updateRecordForPreAuth(payRecord);
		if (payRecord_result == 0) {
			throw new PayBizException(ErrorCode.P9003033);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", payRecord.getRequestId());
		map.put("recordNo", payRecord.getRecordNo());
		int paymentRequest_result = paymentRequestDao.updateRequestToFail(map);
	}

	/**
	 * 预授权强制补单，更新子表和主表
	 */
	@Override
	public void updatePaymentAndRecord(PayRecord payRecord) {
		payRecord.setStatus(TrxStatusEnum.SUCCESS.name());
		int payRecord_result = payRecordDao.updateRecordFromFailToSuccess(payRecord);
		if(payRecord_result == 0) {
			logger.error("强制补单更新子单失败，recordNo=" + payRecord.getRecordNo());
			throw new PayBizException(ErrorCode.P9002002);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", payRecord.getRequestId());
		map.put("recordNo", payRecord.getRecordNo());
		// 因为要通知业务方，所以通知前必须保证是doing状态
		map.put("orderSystemStatus", OrderSystemStatusEnum.DOING.name());
		int paymentRequest_result = paymentRequestDao.updateRequestFromFailToSuccess(map);
		if(paymentRequest_result == 0) {
			logger.error("强制补单更新主单失败，recordNo=" + payRecord.getRecordNo());
			throw new PayBizException(ErrorCode.P9002001);
		}
	}

	/**
	 * 预授权冲正，更新冲正记录表和冲正表
	 * @param preAuthReverseRecord
	 */
	@Override
	public void updateReverseRecord(PreAuthReverseRecord preAuthReverseRecord, ReverseRecord reverseRecord) {
		preAuthReverseRecord.setCancelStatus(TrxStatusEnum.SUCCESS.name());
		int update = preAuthReverseRecordDao.updateByPrimaryKey(preAuthReverseRecord);
		if(update == 0) {
			logger.error("预授权冲正，更新冲正记录表失败，该记录已成功，revserNo=" + preAuthReverseRecord.getReverseNo());
			throw new PayBizException(ErrorCode.P9003059);
		}
		int updateReverse = reverseRecordDao.updateRecordToSuccessFromCSDONE(reverseRecord.getId());
		if(updateReverse == 0) {
			logger.error("预授权冲正，更新冲正表失败，该记录已成功，revserNo=" + preAuthReverseRecord.getReverseNo());
			throw new PayBizException(ErrorCode.P9003058);
		}
	}

	/**
	 * 更新第二支付子单和主单为成功
	 * @param payRecord
	 * @param combPayRecord
	 */
	@Override
	public void updateCombPayRecordAndPaymentSuccess(PayRecord payRecord, CombPayRecord combPayRecord) {
		// 更新第二支付子单为成功
		int updateCombPayRecord = combPayRecordDao.updateToSuccess(combPayRecord);
		if(updateCombPayRecord == 0) {
			throw new PayBizException(ErrorCode.P9003053);
		}
		combPayRecord.setStatus(CombTrxStatusEnum.SUCCESS.name());
		// 更新主单
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", payRecord.getRequestId());
		map.put("recordNo", payRecord.getRecordNo());
		int paymentRequest_result = paymentRequestDao.updateRequestToSuccess(map);
		if(paymentRequest_result == 0) {
			// 主表已经成功或冲正，第一支付子单和第二支付子单都需要冲正
			int payRecord_result_reverser = payRecordDao.updateRecordToReverse(payRecord);
			int combPayrecord_reverse = combPayRecordDao.updateToReverse(combPayRecord);
			payRecord.setStatus(TrxStatusEnum.REVERSE.name());
			if (payRecord_result_reverser == 0 || combPayrecord_reverse == 0) {
				throw new PayBizException(ErrorCode.P9001000);
			}
			createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正或差错退款");
		}
	}

	/**
	 * 更新第二支付子单为失败，同时冲正第一支付子单
	 * @param payRecord
	 * @param combPayRecord
	 */
	@Override
	public void updateCombPayRecordFail(PayRecord payRecord, CombPayRecord combPayRecord) {
		// 更新第二支付子单为失败
		int updateCombPayRecord = combPayRecordDao.updateToFail(combPayRecord);
		if(updateCombPayRecord == 0) {
			throw new PayBizException(ErrorCode.P9003054);
		}
		// 冲正第一支付子单
		int payRecord_result_reverser = payRecordDao.updateRecordToReverse(payRecord);
		if(payRecord_result_reverser == 0) {
			throw new PayBizException(ErrorCode.P9001000);
		}
		createRefundRecord(payRecord.getRequestId(), payRecord.getRecordNo(), "冲正或差错退款");
	}
}
