package com.yeepay.g3.facade.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * nccashier业务facade
 * 
 * @author：peile.fan;zhen.tan
 * @since：2016年5月23日 下午4:09:37
 * @version:
 */
public interface NcCashierCoreFacade {

	/**
	 * 创建支付订单facade
	 * 
	 * @param paymentRequestDto
	 * @return
	 */
	public CashierPaymentResponseDTO createPayment(CashierPaymentRequestDTO paymentRequestDto);

	/**
	 * 发送短验,统计首次支付和绑卡支付发送短信
	 * @param smsRequest
	 */
	public CashierSmsSendResponseDTO sendSms(CashierSmsSendRequestDTO smsRequest);

	/**
	 * 首次支付确认支付
	 * 
	 * @param payResult
	 * @return
	 */
	public CashierPayResponseDTO firstPay(CashierFirstPayRequestDTO payResult);

	/**
	 * 绑卡支付确认支付
	 * 
	 * @param payResult
	 * @return
	 */
	public CashierPayResponseDTO bindPay(CashierBindPayRequestDTO payResult);
	/**
	 * 查询支付结果
	 * @param queryRequestDto
	 * @return
	 */
	public CashierQueryResponseDTO queryPayResult(CashierQueryRequestDTO queryRequestDto);

	/**
	 * 查询银行限额列表
	 * @param bankLimiAmountRequestDTO
	 * @return
	 */
	public BankLimitAmountListResponseDTO queryBankLimitAmountList(BankLimiAmountRequestDTO bankLimiAmountRequestDTO);

	/**
	 * 获取微信公众号二维码地址，用于展示在PC和WAP的成功及失败页
	 * @param requestId
	 * @return
	 */
	String getYeepayWechatQRCode(String requestId);

	/**
	 * 获取微信公众号二维码地址，用于展示在PC和WAP的成功及失败页
	 * @param merchantNo
	 * @param merchantOrderId
	 * @return
	 */
	String getYeepayWechatQRCode(String merchantNo,String merchantOrderId);
}
