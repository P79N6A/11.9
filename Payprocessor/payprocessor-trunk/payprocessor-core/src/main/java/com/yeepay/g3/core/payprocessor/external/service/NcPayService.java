package com.yeepay.g3.core.payprocessor.external.service;

import com.yeepay.g3.core.payprocessor.Exception.PayBizException;
import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.facade.ncpay.dto.*;
import com.yeepay.g3.facade.ncpay.dto.PayPreAuthCancelResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayPreAuthCompleteResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.*;

/**
 * 
 * @author yp-tc-m-2804
 *
 */
public interface NcPayService {

	PayQueryResponseDTO queryPaymentOrder(String paymentRecordId) throws PayBizException;

	/**
	 * 
	 * @param paymentNo
	 * @param bizOrderNum
	 * @return
	 */
	public boolean updateTaskStatus(String paymentNo, String bizOrderNum);

	/**
	 * @param requestDTO
	 * @return
	 */
	SmsSendResponseDTO verifyAndSendSms(NcSmsRequestDTO requestDTO, String ncpayPaymentNo);

	/**
	 * @param requestDTO
	 * @return
	 */
	PayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO requestDTO, String ncpayPaymentNo);

	/**
	 * @param requestDTO
	 * @param record
	 * @return
	 */
	PaymentResponseDTO requestPayment(NcPayOrderRequestDTO requestDTO, PayRecord record);

	/**
	 * @param requestDTO
	 * @return
	 */
	PayQueryResponseDTO synConfirmPay(NcPayConfirmRequestDTO requestDTO, String ncpayPaymentNo);
	
	/**
	 * ncpay绑卡接口
	 * @param payOrderId
	 * @return
	 */
	long bindCardByOrderId(String payOrderId);

	/**
	 * 银行卡分期
	 * 下单
	 * @param requestDTO
	 * @return
	 */
	CflOrderResponseDTO clfOrderRequest(NcPayCflOrderRequestDTO requestDTO, PayRecord record);

	/**
	 * 银行卡分期
	 * 发短信
	 */
	CflSmsSendResponseDTO cflSendSms(String ncpayPaymentNo, NcPayCflSmsRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 开通并支付
	 * @param requestDTO
	 * @return
	 */
	CflOpenAndPayResponseDTO cflOpenAndPay(NcPayCflOpenRequestDTO requestDTO, PayRecord record);

	/**
	 * 银行卡分期
	 * 确认支付
	 */
	CflConfirmPayResponseDTO cflConfirmPay(NcPayCflConfirmRequestDTO requestDTO, String ncpayPaymentNo);


	/**
	 * 银行卡分期
	 * 同步确认支付
	 */
	PayQueryResponseDTO cflSynConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO, String ncpayPaymentNo);
	/**
	 * 预授权请求下单
	 */
	PayPreAuthRespDTO ncPreAuthRequest(NcPayOrderRequestDTO requestDTO, PayRecord record);


	/**
	 * 预授权确认
	 */
	PayPreAuthConfirmResponseDTO ncPreAuthComfirm(NcPayConfirmRequestDTO requestDTO, String ncpayPaymentNo);

	/**
	 * 预授权撤销、预授权完成撤销
	 */
	PayPreAuthCancelResponseDTO ncPreAuthCancel(PreAuthCancelRequestDTO requestDTO, String preAuthPaymentNo, PayRecord record);

	/**
	 * 预授权完成
	 */
	PayPreAuthCompleteResponseDTO ncPreAuthComplete(PreAuthCompleteRequestDTO requestDTO, String preAuthPaymentNo, PayRecord record);


	/**
	 * 预授权"冲正"专用
	 * 预授权撤销、预授权完成撤销
	 */
	PayPreAuthCancelResponseDTO ncPreAuthReverseCancel(PayPreAuthCancelRequestDTO requestDTO);
	
	/**
	 * 担保分期预路由
	 * 
	 * @param requestDTO
	 * @return {@link GuaranteeCflPrePayResponseDTO}
	 */
	GuaranteeCflPrePayResponseDTO guaranteeCflPrePay(NcGuaranteeCflPrePayRequestDTO requestDTO);
	
	/**
	 * 担保分期支付
	 * 
	 * @param requestDTO
	 * @param record
	 * @return {@link GuaranteeCflPayResponseDTO}
	 */
	GuaranteeCflPayResponseDTO guaranteeCflPay(NcGuaranteeCflPayRequestDTO requestDTO, PayRecord record);
	
	/**
	 * 查询担保分期订单
	 * 
	 * @param paymentNo
	 * @return
	 */
	GuaranteeCflQueryResponseDTO queryGuaranteeCflOrder(String paymentNo);
}
