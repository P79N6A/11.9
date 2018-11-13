package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;
import com.yeepay.g3.core.payprocessor.entity.PaymentRequest;
import com.yeepay.g3.core.payprocessor.entity.PreAuthReverseRecord;
import com.yeepay.g3.facade.payprocessor.dto.*;

/**
 * 处理ncpay逻辑
 * 
 * @author yp-tc-m-2804
 *
 */
public interface NcPayBiz {

	NcSmsResponseDTO sendSms(NcSmsRequestDTO requestDTO);

	/**
	 * @param requestDTO
	 * @return
	 */
	NcPayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO requestDTO);

	/**
	 * @param requestDTO
	 * @return
	 */
	NcPayOrderResponseDTO ncRequest(NcPayOrderRequestDTO requestDTO);

	/**
	 * @param requestDTO
	 * @return
	 */
	PayRecordResponseDTO synConfirmPay(NcPayConfirmRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 下单
	 * @param requestDTO
	 * @return
	 */
	NcPayCflOrderResponseDTO cflRequest(NcPayCflOrderRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 发短信
	 * @param requestDTO
	 * @return
	 */
	NcPayCflSmsResponseDTO cflSendSms(NcPayCflSmsRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 开通并支付
	 * @param requestDTO
	 * @return
	 */
	NcPayCflOpenResponseDTO cflOpenAndPay(NcPayCflOpenRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 确认支付
	 * @param requestDTO
	 * @return
	 */
	NcPayCflConfirmResponseDTO cflConfirmPay(NcPayCflConfirmRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 同步确认支付
	 * @param requestDTO
	 * @return
	 */
	PayRecordResponseDTO cflSynConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO);


	/**
	 * 预授权下单
	 * @param requestDTO
	 * @return
	 */
	NcPayOrderResponseDTO ncPreAuthRequest(NcPayOrderRequestDTO requestDTO);

	/**
	 * 预授权确认
	 * @param requestDTO
	 * @return
	 */
	PayRecordResponseDTO ncPreAuthComfirm(NcPayConfirmRequestDTO requestDTO);

	/**
	 * 预授权撤销、预授权完成撤销
	 * @param requestDTO
	 * @return
	 */
	PreAuthCancelResponseDTO ncPreAuthCancel(PreAuthCancelRequestDTO requestDTO);

	/**
	 * 预授权完成
	 * @param requestDTO
	 * @return
	 */
	PreAuthCompleteResponseDTO ncPreAuthComplete(PreAuthCompleteRequestDTO requestDTO);


	/**
	 * 预授权"冲正"专用
	 * 预授权撤销、预授权完成撤销
	 * @return
	 */
	void ncPreAuthReverseCancel(PreAuthReverseRecord preAuthReverseRecord, PaymentRequest paymentRequest);
	
	/**
	 * 担保分期预路由
	 * @param requestDTO
	 * @return {@link NcGuaranteeCflPrePayResponseDTO}
	 */
	NcGuaranteeCflPrePayResponseDTO authCflPrePay(NcGuaranteeCflPrePayRequestDTO requestDTO);
	
	/**
	 * 担保分期下单
	 * @param requestDTO
	 * @return {@link NcGuaranteeCflPayResponseDTO}
	 */
	NcGuaranteeCflPayResponseDTO authCflRequest(NcGuaranteeCflPayRequestDTO requestDTO);

}
