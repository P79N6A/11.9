package com.yeepay.g3.core.nccashier.gateway.service;


import com.yeepay.g3.core.nccashier.vo.IntelligentNetResultFrontCallbackResInfo;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.cwh.param.ExternalUserDTO;
import com.yeepay.g3.facade.nccashier.dto.CardBinResDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.PayConfirmResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayQueryResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.PayResultTaskRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.RequestPaymentParam;
import com.yeepay.g3.facade.ncpay.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.ReverseResponseDTO;
import com.yeepay.g3.facade.ncpay.dto.SmsSendRequestDTO;
import com.yeepay.g3.facade.ncpay.dto.SmsSendResponseDTO;

/**
 * Created by xiewei on 15-10-20.
 */
public interface NcPayService {
	
	/**
	 * 已经废弃了
	 * @param merchantAccount
	 * @param identityId
	 * @param identityType
	 * @param userName
	 * @param userIdentityNo
	 * @return
	 * @throws CashierBusinessException
	 */
	@Deprecated
	ExternalUserDTO getExternalUser(String merchantAccount, String identityId,
			IdentityType identityType, String userName, String userIdentityNo)
					throws CashierBusinessException;

	PaymentResponseDTO requestPayment(RequestPaymentParam param) throws CashierBusinessException,IllegalArgumentException;

	SmsSendResponseDTO verifyAndSendSms(SmsSendRequestDTO requestDTO) throws CashierBusinessException;

	PayConfirmResponseDTO confirmPay(PayConfirmRequestDTO confirmDTO) throws CashierBusinessException;

	PayQueryResponseDTO queryPaymentOrder(String paymentRecordId) throws CashierBusinessException;

	/**
	 * 冲正接口
	 */
	ReverseResponseDTO reversePayOrder(ReverseRequestDTO reverseRequestDTO)throws CashierBusinessException;
	/**
	 * 补单接口
	 * @param paymentOrderNo
	 * @return
	 * @throws CashierBusinessException
	 * @throws Exception
	 */
	public PayQueryResponseDTO supplementPaymentOrder(String paymentOrderNo) throws CashierBusinessException;

	/**
	 * 已废弃
	 * @param cardNo
	 * @return
	 */
	@Deprecated
	public CardBinResDTO getBankCardByCardNo(String cardNo);
	
	/**
	 * 回调ncpay的mq
	 * @param payResultTaskRequestDTO
	 * @return
	 */
	public boolean updateTaskStatus(PayResultTaskRequestDTO payResultTaskRequestDTO);
	
	/**
	 * 调用ncpay查询前端回调地址及所需参数
	 * 
	 * @param paymentNo
	 * @return
	 */
	IntelligentNetResultFrontCallbackResInfo queryPreRouteRedirectInfo(String paymentNo);


}
