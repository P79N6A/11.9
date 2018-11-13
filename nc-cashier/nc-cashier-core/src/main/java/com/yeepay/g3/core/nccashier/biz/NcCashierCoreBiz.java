/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.nccashier.dto.*;

/**
 * 核心业务biz，包括创建支付订单，发短验，确认付款，支付结果查询等流程组装
 * 
 * @author zhen.tan
 * @since：2016年5月24日 下午9:31:25
 */
public interface NcCashierCoreBiz {
	
	/**
	 * 创建支付订单
	 * 
	 * @param paymentRequestDto
	 * @return
	 */
	public CashierPaymentResponseDTO createPayment(CashierPaymentRequestDTO paymentRequestDto);

	/**
	 * 下发短验
	 * 
	 * @param smsRequest
	 * @return
	 */
	public CashierSmsSendResponseDTO sendSms(CashierSmsSendRequestDTO smsRequest);

	/**
	 * 保存临时卡
	 * @param payrecord
	 * @param needBankCardDTO
	 * @param paymentRequest
	 * @param checkOnePersion 是否检查同人限制
	 * @return
	 */
	Long saveTmpCard(PaymentRecord payrecord, NeedBankCardDTO needBankCardDTO, PaymentRequest paymentRequest, boolean checkOnePersion);

	/**
	 * 更新pay_record为paying状态，调用PP或NCPAY确认支付
	 * @param payrecord
	 * @param verifyCode
	 * @param cardInfoDTO
	 */
	void upRecToPayingAndCallNcpay(PaymentRecord payrecord, String verifyCode, CardInfoDTO cardInfoDTO, PaymentRequest payRequest);

	/**
	 * 调用PP确认支付，使用同步接口
	 * @param payrecord
	 * @param verifyCode
	 * @param tmpCardId
	 */
	void payprocessorSyncConfirmPay(PaymentRecord payrecord, String verifyCode, Long tmpCardId,PaymentRequest paymentRequest);


	/**
	 * 查询结果
	 * 
	 * @param queryRequestDto
	 * @return
	 */
	public CashierQueryResponseDTO queryPayResult(CashierQueryRequestDTO queryRequestDto);

	/**
	 * 确认支付
	 * 
	 * @param payResult
	 * @return
	 */
	public CashierPayResponseDTO firstPay(CashierFirstPayRequestDTO payResult);


	/**
	 * 确认支付
	 * 
	 * @param payResult
	 * @return
	 */
	public CashierPayResponseDTO bindPay(CashierBindPayRequestDTO payResult);


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
