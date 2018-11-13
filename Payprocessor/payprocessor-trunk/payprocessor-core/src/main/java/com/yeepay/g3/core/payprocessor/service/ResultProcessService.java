
package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.*;
import com.yeepay.g3.facade.mktg.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.OrderSystemStatusEnum;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author peile.fan
 *
 */
public interface ResultProcessService {
	/**
	 * 更新子表和主表为成功
	 * @param payRecord
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatePaymentToSuccess(PayRecord payRecord);

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatePaymentToReverse(PaymentRequest paymentRequest, PayRecord payRecord,String reverseRemark);

	/**
	 * 预授权，更新子表和主表为成功
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	void updatePaymentToSuccessForPreAuth(PayRecord payRecord);

	/**
	 * 预授权，更新子表和主表为失败
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	void updatePaymentToFailForPreAuth(PayRecord payRecord);

	/**
	 * 预授权，强制补单更新子表和主表
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	void updatePaymentAndRecord(PayRecord payRecord);

	/**
	 * 预授权，强制补单更新子表和主表
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	void updateReverseRecord(PreAuthReverseRecord preAuthReverseRecord, ReverseRecord reverseRecord);

	/**
	 * 更新第二支付子表和主表为成功
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	void updateCombPayRecordAndPaymentSuccess(PayRecord payRecord, CombPayRecord combPayRecord);

	/**
	 * 更新第二支付子表为失败
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	void updateCombPayRecordFail(PayRecord payRecord, CombPayRecord combPayRecord);

}
