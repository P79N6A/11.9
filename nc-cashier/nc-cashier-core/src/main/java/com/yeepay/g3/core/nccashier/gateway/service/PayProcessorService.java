/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.facade.payprocessor.dto.*;

/**
 * @author zhen.tan
 * 对接支付处理器服务
 *
 */
public interface PayProcessorService {

	/**
	 * 无卡支付下单
	 * @param NcPayOrderRequestDTO
	 * @return NcPayOrderResponseDTO
	 */
	public NcPayOrderResponseDTO ncPayRequest(NcPayOrderRequestDTO requesDTO);

	/**
	 * FEwx/zfb统一下单接口
	 * @param OpenPayRequestDTO
	 * @return OpenPayResponseDTO
	 */
	public OpenPayResponseDTO openPayRequest(OpenPayRequestDTO requestDTO);
	
	/**
	 * 网银下单接口
	 * @param NetPayRequestDTO
	 * @return NetPayResponseDTO
	 */
	public NetPayResponseDTO onlinePayRequest(NetPayRequestDTO requestDTO);
	
	/**
	 * 无卡短验发送
	 * @param NcSmsRequestDTO
	 * @return NcSmsResponseDTO
	 */
	public NcSmsResponseDTO verifyAndSendSms(NcSmsRequestDTO requestDTO);

	/**
	 * 无卡确认支付
	 * @param NcPayConfirmRequestDTO
	 * @return NcPayConfirmResponseDTO
	 */
	public NcPayConfirmResponseDTO confirmPay(NcPayConfirmRequestDTO confirmDTO);
	
	/**
	 * 结果查询
	 * @param PayRecordQueryRequestDTO
	 * @return PayRecordResponseDTO
	 */
	public PayRecordResponseDTO query(PayRecordQueryRequestDTO requestDTO);


	public CflPayResponseDTO cflRequest(CflPayRequestDTO requestDTO);
	

	/**
	 * fe 的预路由接口
	 * @param openPrePayRequestDTO
	 * @return
	 */
	public OpenPrePayResponseDTO payOrderPrePay(OpenPrePayRequestDTO openPrePayRequestDTO);
	
	/**
	 * 调用支付处理器请求被扫支付接口进行支付
	 * 
	 * @param request
	 * @return
	 */
	PassiveScanPayResponseDTO merchantScanPay(PassiveScanPayRequestDTO request);
	
	/**
	 * 调用支付处理器的账户支付接口 - 同步接口
	 * 
	 * @param ppAccountPayRequestDTO
	 * @return
	 */
	AccountSyncPayResponseDTO accountSyncPay(AccountPayRequestDTO ppAccountPayRequestDTO);
	
	/**
	 * 银行卡分期下单接口
	 * @param requestDTO
	 * @return
	 */
	NcPayCflOrderResponseDTO bankInstallmentRequest(NcPayCflOrderRequestDTO requestDTO);
	
	/**
	 * 银行卡分期发短验接口
	 * @param requestDTO
	 * @return
	 */
	NcPayCflSmsResponseDTO bankInstallmentSendSms(NcPayCflSmsRequestDTO requestDTO);
	
	/**
	 * 银行卡分期开通并支付接口
	 * 
	 * @param requestDTO
	 * @return 返回url为空时，认为是异常情况
	 */
	NcPayCflOpenResponseDTO bankInstallmentOpenAndPay(NcPayCflOpenRequestDTO requestDTO);
	
	/**
	 * 银行卡分期确认支付接口
	 * @param requestDTO
	 * @return
	 */
	NcPayCflConfirmResponseDTO bankInstallmentConfirmPay(NcPayCflConfirmRequestDTO requestDTO);

	/**
	 * 银行卡分期同步确认支付接口
	 * @param requestDTO
	 * @return
	 */
	PayRecordResponseDTO bankInstallmentSyncConfirmPay(NcPayCflSynConfirmRequestDTO requestDTO);

	/**
	 * 绑卡支付确认支付同步接口
	 * @param confirmDTO
	 * @return
	 */
	PayRecordResponseDTO synConfirmPay(NcPayConfirmRequestDTO confirmDTO);
	
	/**
	 * 预授权下单接口
	 * @param requestDTO
	 * @return
	 */
	NcPayOrderResponseDTO preauthRequest(NcPayOrderRequestDTO requestDTO);
	
	/**
	 * 预授权确认接口
	 * @param preauthResDTO
	 * @return
	 */
	PayRecordResponseDTO preauthConfirm(NcPayConfirmRequestDTO preauthResDTO);
	
	/**
	 * 预授权完成
	 * 
	 * @param completeRequestDTO
	 * @return
	 */
	PreAuthCompleteResponseDTO preauthComplete(PreAuthCompleteRequestDTO completeRequestDTO);
	
	/**
	 * 预授权撤销、完成撤销
	 * 
	 * @param cancelRequestDTO
	 * @return
	 */
	PreAuthCancelResponseDTO preauthCancel(PreAuthCancelRequestDTO cancelRequestDTO);
	
	/**
	 * 交易结果查询接口
	 * 
	 * @param queryRequestDTO
	 * @return
	 */
	QueryResponseDTO queryOrderResult(QueryRequestDTO queryRequestDTO);
	
	/**
	 * 个人会员余额支付
	 * 
	 * @param requestDTO
	 * @return
	 */
	PersonalMemberSyncPayResponseDTO memberBalancePay(PersonalMemberSyncPayRequestDTO requestDTO);
	
	/**
	 * 账户支付异步支付，目前使用于有页面的场景（PC和H5收银台）
	 * 
	 * @param ppAccountPayRequestDTO
	 * @return
	 */
	AccountPayResponseDTO accountPay(AccountPayRequestDTO ppAccountPayRequestDTO);

	/**
	 * 担保分期-调用PP预授权接口
	 * @param ncGuaranteeCflPrePayRequestDTO
	 * @return
	 */
	NcGuaranteeCflPrePayResponseDTO guaranteeCflPrePay(NcGuaranteeCflPrePayRequestDTO  ncGuaranteeCflPrePayRequestDTO);

	/**
	 * 担保分期-调用PP支付下单接口
	 * @param ncGuaranteeCflPayRequestDTO
	 * @return
	 */
	NcGuaranteeCflPayResponseDTO guaranteeCflRequest(NcGuaranteeCflPayRequestDTO ncGuaranteeCflPayRequestDTO);


	/**
	 * 分期易下单接口
	 * @param ncCflEasyRequestDTO
	 * @return
	 */
	NcCflEasyResponseDTO cflEasyCreatePayment(NcCflEasyRequestDTO ncCflEasyRequestDTO);

	/**
	 * 分期易发短信接口
	 * @param ncCflEasySmsRequestDTO
	 * @return
	 */
	NcCflEasySmsResponseDTO cflEasySendSms(NcCflEasySmsRequestDTO ncCflEasySmsRequestDTO);

	/**
	 * 分期易同步确认支付接口
	 * @param ncCflEasyConfirmRequestDTO
	 * @return
	 */
	PayRecordResponseDTO cflEasySynConfirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO);


	/**
	 * 分期易异步确认支付接口
	 * @param ncCflEasyConfirmRequestDTO
	 * @return
     */
	NcCflEasyConfirmResponseDTO clfEasyConfirmPay(NcCflEasyConfirmRequestDTO ncCflEasyConfirmRequestDTO);

}
