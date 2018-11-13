package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.CardBinInfoResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardInfoVO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierSmsSendRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;

public interface PreauthService {
	/**
	 * 预授权下单请求
	 * @param token
	 * @param info
	 * @param cardInfo
	 * @return
	 */
	ReqSmsSendTypeEnum preAuthOrderRequest(String token, RequestInfoDTO info, CardInfoVO cardInfo, CardBinInfoResponseVO cardBinInfo);
	/**
	 * 预授权发送短验
	 * @param token
	 * @param info
	 * @param smsType
	 */
	void preAuthSendSMS(String token, RequestInfoDTO info, CardInfoVO cardInfo,ReqSmsSendTypeEnum smsType);
	/**
	 * 预授权确认
	 * @param token
	 * @param info
	 * @param verifyCode
	 */
	void preAuthOrderConfirm(String token, RequestInfoDTO info,String verifyCode);
	/**
	 * 预授权绑卡下单
	 * @param token
	 * @param bindId
	 * @param info
	 */
	CashierPaymentResponseDTO preAuthBindOrderRequest(String token, String bindId, RequestInfoDTO info);
	/**
	 * 预授权绑卡确认
	 * @param token
	 * @param info
	 * @param verifyCode
	 * @param cardInfo
	 */
	void preAuthBindOrderConfirm(String token, RequestInfoDTO info, String verifyCode, CardInfoVO cardInfo);
	
}
