package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PreauthBindConfirmRequestDTO;

public interface PreauthBiz {
	/**
	 * 预授权首次下单
	 * @param paymentRequestDto
	 * @return
	 */
	CashierPaymentResponseDTO preAuthOrderRequest(CashierPaymentRequestDTO paymentRequestDto);
	/**
	 * 预授权绑卡下单
	 * @param paymentRequestDto
	 * @return
	 */
	CashierPaymentResponseDTO preAuthBindOrderRequest(CashierPaymentRequestDTO paymentRequestDto);
	/**
	 * 预授权确认
	 * @param requestDTO
	 * @return
	 */
	PayConfirmBaseResponseDTO preAuthOrderConfirm(PayConfirmBaseRequestDTO requestDTO);
	/**
	 * 预授权绑卡确认
	 * @param requestDTO
	 * @return
	 */
	PayConfirmBaseResponseDTO preAuthBindOrderConfirm(PreauthBindConfirmRequestDTO requestDTO);
	/**
	 * 预授权发送短验
	 * @param smsRequest
	 * @return
	 */
	CashierSmsSendResponseDTO preAuthSendSms(CashierSmsSendRequestDTO smsRequest);
}
