package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.OrderSystemPreauthStatusEnum;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteResDTO;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;

public interface PreauthService {


	/**
	 * 预授权下单API
	 * @param requestDTO
	 * @param orderInfo
	 * @return
	 */
	void preAuthFirstRequestAPI(APIPreauthFirstRequestDTO requestDTO, APIPreauthPaymentResponseDTO responseDTO, OrderDetailInfoModel orderInfo);


	/**
	 * 预授权下单API
	 * @param requestDTO
	 * @param orderInfo
	 * @return
	 */
	void preAuthBindRequestAPI(APIPreauthBindRequestDTO requestDTO, APIPreauthPaymentResponseDTO responseDTO, OrderDetailInfoModel orderInfo);


	/**
	 * 预授权发短验API
	 * @param requestDTO
	 * @param responseDTO
	 * @param orderInfo
	 */
	void preauthSmsSendAPI(APIPreauthSmsSendRequestDTO requestDTO,APIBasicResponseDTO responseDTO, OrderDetailInfoModel orderInfo);

	/**
	 * 预授权确认API
	 *
	 * @param requestDTO
	 * @param responseDTO
	 * @param orderInfo
	 */
	void preAuthOrderConfirmAPI(APIPreauthConfirmRequestDTO requestDTO, APIPreauthConfirmResponseDTO responseDTO, OrderDetailInfoModel orderInfo);


	/**
	 * 预授权下单
	 * 
	 * @param payRequest
	 * @param payRecord
	 * @param requestDto
	 * @return
	 */
	PaymentResponseDTO preAuthOrderRequest(PaymentRequest payRequest, PaymentRecord payRecord,
			CashierPaymentRequestDTO requestDto);

	/**
	 * 预授权下单时获取record
	 * 
	 * @param paymentRequest
	 * @param requestDto
	 * @return
	 */
	PaymentRecord getRecord4PreauthRequest(PaymentRequest paymentRequest, CashierPaymentRequestDTO requestDto);

	/**
	 * 预授权确认时获取record
	 * 
	 * @param paymentRequest
	 * @param recordId
	 * @return
	 */
	PaymentRecord getRecord4PreauthConfirm(PaymentRequest paymentRequest, Long recordId,String paymentRecordNo,
			NCCashierOrderTypeEnum recordPayType);

	/**
	 * 预授权确认
	 * 
	 * @param payRecord
	 * @param verifyCode
	 * @param cardId
	 */
	void preAuthOrderConfirm(PaymentRecord payRecord, String verifyCode, Long cardId);

	/**
	 * 预授权撤销
	 * 
	 * @param requestDTO
	 * @param orderInfo
	 * @return
	 */
	void preauthCancel(APIPreauthCancelRequestDTO requestDTO, APIPreauthResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo);

	/**
	 * 预授权完成
	 * 
	 * @param completeRequestDTO
	 * @param orderInfo
	 * @return
	 */
	void preauthComplete(APIPreauthCompleteRequestDTO completeRequestDTO,  APIPreauthCompleteResponseDTO responseDTO,
			OrderDetailInfoModel orderInfo);

	/**
	 * 预授权完成撤销
	 * 
	 * @param completeCancelRequestDTO
	 * @param orderInfo
	 * @return
	 */
	void preauthCompleteCancel(APIPreauthCompleteCancelRequestDTO completeCancelRequestDTO,
			APIPreauthResponseDTO responseDTO, OrderDetailInfoModel orderInfo);

	PaymentRecord getRecord4PreauthBindRequest(PaymentRequest payRequest, CashierPaymentRequestDTO requestDto, boolean reuseRecord);

	/**
	 * 获取满足条件的record
	 * 
	 * @param paymentRequest
	 * @param recordId
	 * @param paymentOrderNo
	 * @return
	 */
	PaymentRecord getRecord4PreauthSendSms(PaymentRequest paymentRequest, Long recordId, String paymentOrderNo);

	void preauthSmsSend(PaymentRecord paymentRecord, CashierSmsSendRequestDTO smsRequest, Long cardId);

	Long saveTmpCard(PaymentRecord paymentRecord, CardInfoDTO cardInfo, PaymentRequest paymentRequest, boolean checkOnePersion);

	/**
	 * 预授权完成
	 * 
	 * @param completeRequestDTO
	 * @param responseDTO
	 * @param orderInfo
	 */
	void preauthComplete(APIPreauthCompleteReqDTO completeRequestDTO, APIPreauthCompleteResDTO responseDTO,
			OrderDetailInfoModel orderInfo);

	/**
	 * 反查订单，并对预授权的订单进行相应的校验
	 * 
	 * @param reqDTO
	 * @param objStatus
	 * @return
	 */
	OrderDetailInfoModel queryOrder(APIBasicRequestDTO reqDTO, OrderSystemPreauthStatusEnum[] objStatus);

	/**
	 * 预授权撤销
	 * 
	 * @param cancelReqDTO
	 * @param responseDTO
	 * @param orderInfo
	 */
	void preauthCancel(APIPreauthCancelReqDTO cancelReqDTO, APIPreauthCancelResDTO responseDTO,
			OrderDetailInfoModel orderInfo);
	
	
	/**
	 * 预授权完成撤销
	 * 
	 * @param completeCancelReqDTO
	 * @param responseDTO
	 * @param orderInfo
	 */
	void preauthCompleteCancel(APIPreauthCompleteCancelReqDTO completeCancelReqDTO,
			APIPreauthCompleteCancelResDTO responseDTO, OrderDetailInfoModel orderInfo);
}