package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.PayConfirmBaseResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.PreauthBindConfirmRequestDTO;

/**
 * 
 * @Description 预授权facade，供H5收银台使用
 * @author yangmin.peng
 * @since 2017年12月8日下午12:00:30
 */
public interface PreauthFacade {
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
	 * 预授权首次确认
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
