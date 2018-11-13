package com.yeepay.g3.app.nccashier.wap.service;

import java.util.Map;

import com.yeepay.g3.app.nccashier.wap.vo.AlipayStandardJumpInfo;
import com.yeepay.g3.app.nccashier.wap.vo.CarnivalVO;
import com.yeepay.g3.app.nccashier.wap.vo.PayResultQueryStateListenVO;
import com.yeepay.g3.app.nccashier.wap.vo.SuccessActivateVO;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.BusinessTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;

import javax.servlet.http.HttpServletRequest;


/**
 * 新的WAP支付业务service Created by zhen.tan on 16-06-02
 */
public interface NewWapPayService {

	/**
	 * 查询支付结果
	 * 
	 * @param info
	 * @param isRequery
	 * @return
	 */
	TradeNoticeDTO queryResult(RequestInfoDTO info, String isRequery);

	void validateCardInfoAndTimeout(CardInfoDTO cardInfo, String token,String bindId);

	ReqSmsSendTypeEnum createPaymentOrSendSMS(String token, String bindId,
			ReqSmsSendTypeEnum reqSmsSendTypeEnum, CardInfoDTO cardInfo);

	Map<String, Object> createPayment4RedirectUrl(String token, CardInfoDTO cardInfo);

	Map<String, Object> getFirstPayModel(long requestId, String cardNo,
			BusinessTypeEnum businessType, BankCardDTO bindCardDTO);

	Map<String, Object> getBindPayModel(RequestInfoDTO info, String token, String bindId,
			BankCardDTO bindCardDTO);

	void validateCardInfo(String bindId,CardInfoDTO cardInfo);

	CashierPayResponseDTO firstPay(RequestInfoDTO info, String token, CardInfoDTO cardInfo,
			String verifycode);

	CashierPayResponseDTO bindPay(RequestInfoDTO info, String token, String bindId,
			NeedBankCardDTO needBankCardDTO,
			String verifycode);

	void validateBindCardAndTimeout(
			NeedBankCardDTO needBankCardDTO, String token, String bindId);

	void bindSendSMS(ReqSmsSendTypeEnum reqSmsSendTypeEnum,NeedBankCardDTO needBankCardDTO,
			String token, String bindId);

	Map<String, Object> createPayment(String token, String bindId, RequestInfoDTO info, BankCardDTO bindCardDTO);

	Map<String, Object> getCardValidates(Long requestId, String cardno);


	/**
	 * 直连支付
	 * @param token
	 * @param payType
	 * @return
	 */
	String directPay(String token,String payType);

	void setCardOwner(String bindId, String name, String idCardNo,RequestInfoDTO info);

	Map<String, String> getPayFailErrors();

	/**
	 * 查询参加嘉年华的资格并返回url
	 * @param tradeNoticeDTO
	 * @return
	 */
	CarnivalVO queryQualification4Carnival(TradeNoticeDTO tradeNoticeDTO);


	/**
	 * 查询成功页面活动资格并返回url
	 * @param tradeNoticeDTO
	 * @return
	 */
	SuccessActivateVO querySuccessActivate(TradeNoticeDTO tradeNoticeDTO);


	/**
	 * 支付处理器是否可查标识监听处理
	 * @param token
	 * @param 
	 * @return
	 */
	PayResultQueryStateListenVO listenCanPayResultQuery(String token);
	
	/**
	 * pc&扫码查询支付结果
	 * @param token
	 */
	TradeNoticeDTO queryPayResult(RequestInfoDTO info, String token);
	
	/**
	 * 校验token并获取订单基本信息
	 * @param token
	 * @return
	 */
	RequestInfoDTO validateRequestInfoDTO(String token);

	CardOwnerConfirmResDTO getOwnersInfo(RequestInfoDTO info);

	void unbindCard(Long paymentRequestId, Long paymentRecordId);

	/**
	 * 绑卡支付，在已绑卡列表，主动解绑选中的卡
	 * @param paymentRequestId
	 * @param bindId
	 */
	void unbindCard(String paymentRequestId, String bindId);

	/**
	 * 根据商编，检查绑卡支付，是否定制了解绑功能
	 * @param merchantNo
	 * @return
	 */
	boolean checkAbleToUnbindCard(String merchantNo);

	NeedBankCardDTO hiddenNeedBankCardDTO(NeedBankCardDTO needBankCardDTO);

	String wechatH5PreRouter(PayExtendInfo extendInfo, RequestInfoDTO info, String token, HttpServletRequest request) throws Exception;
	
	/**
	 * 支付宝标准版：从普通浏览器跳转到支付宝浏览器之前，所需的操作，包括预路由和获取授权key
	 * 
	 * @param paymentRequestId
	 * @param token
	 * @param merchantNo
	 * @return
	 */
	AlipayStandardJumpInfo alipayStandardPreHandleBeforeJumping(String paymentRequestId, String token, String merchantNo) throws Exception;
	
	/**
	 * 支付宝生活号预路由，忽略商户透传的userId。目前的使用场景：支付宝标准版
	 * 
	 * @param paymentRequestId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	String alipayLifeNoPreRouteIgnoreOriUserId(String paymentRequestId, String token) throws Exception;
}
