package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.*;

/**
 * @author chronos.
 * @createDate 2016/11/8.
 */
public interface PayManageFacade {

	/**
	 * 无卡请求短验接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	NcSmsResponseDTO sendSms(NcSmsRequestDTO requestDTO);

	/**
	 * 无卡确认支付接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	NcPayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO requestDTO);

	/**
	 * 无卡同步确认支付接口
	 * 
	 * @param requestDTO
	 * @return
	 */
	PayRecordResponseDTO synConfirmPay(NcPayConfirmRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 下单接口
	 * @param requestDTO
	 * @return
	 */
	NcPayCflOrderResponseDTO ncpayCflRequest(NcPayCflOrderRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 发短信接口
	 * @param requestDTO
	 * @return
	 */
	NcPayCflSmsResponseDTO ncpayCflSendSms(NcPayCflSmsRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 开通并支付接口
	 * @param requestDTO
	 * @return
	 */
	NcPayCflOpenResponseDTO ncpayCflOpenAndPay(NcPayCflOpenRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 确认支付接口
	 * @param requestDTO
	 * @return
	 */
	NcPayCflConfirmResponseDTO ncpayCflConfirmPay(NcPayCflConfirmRequestDTO requestDTO);

	/**
	 * 银行卡分期
	 * 同步确认支付接口
	 * @param requestDTO
	 * @return
	 */
	PayRecordResponseDTO ncpayCflSynConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO);

}
