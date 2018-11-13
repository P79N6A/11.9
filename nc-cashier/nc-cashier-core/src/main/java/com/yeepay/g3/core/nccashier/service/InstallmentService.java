package com.yeepay.g3.core.nccashier.service;

import java.util.List;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentFeeInfoResponseDTO;

public interface InstallmentService {

	/**
	 * 开通并支付下单
	 * 
	 * @param installmentPayInfo
	 * @return
	 */
	UrlInfo openAndPay(InstallmentInfoNeeded installmentPayInfo);

	/**
	 * 签约卡下单
	 * 
	 * @param installmentPayInfo
	 */
	void signedCardOrder(InstallmentInfoNeeded installmentPayInfo);

	/**
	 * 发短验
	 * 
	 * @param installmentPayInfo
	 */
	void smsSend(CombinedPaymentDTO installmentPayInfo);

	void comfirmPay(PaymentRecord payrecord, String verifyCode);

	/**
	 * 银行卡分期同步确认支付
	 * @param payrecord
	 * @param verifyCode
	 */
	void syncComfirmPay(PaymentRecord payrecord, String verifyCode);

	void checkSmsSend(PaymentRecord payrecord, boolean isneedVeryCode);


	/**
	 * 校验签约关系ID是否合法 查询用户中心获取对应的签约关系记录，并校验外部用户
	 *
	 * @param signRetionId
	 * @param externalUser
	 * @param request
	 * @return 银行编码
	 */
	String isSignRelationIdIllegal(String signRetionId, CashierUserInfo externalUser, InstallmentInfoNeeded request);


	/**
	 * 校验卡号是否合法 先调用ncconfig校验cardBin，再查询用户中心获取对应的签约关系记录，并且比较外部用户信息是否合法
	 *
	 * @param cardNo
	 * @param externalUser
	 * @param request
	 * @return
	 */
	String isCardNoIllegal(String cardNo, CashierUserInfo externalUser, InstallmentInfoNeeded request);

	/**
	 * 校验是否存在满足条件的paymentRecord以支持银行卡签约支付发短验
	 * 
	 * @param paymentRequest
	 * @param recordId
	 * @return
	 */
	PaymentRecord exsitValidRecordToSms(PaymentRequest paymentRequest, String recordId);

	/**
	 * 获取满足条件的record以支持银行卡分期下单，若不存在record则新建
	 * 
	 * @param cardNo
	 * @param infoNeeded
	 * @param paymentRequest
	 * @return
	 */
	PaymentRecord getRecordToOrder(String cardNo, InstallmentInfoNeeded infoNeeded, PaymentRequest paymentRequest);
	
	/**
	 * 获取满足条件的paymentRecord以支持银行卡分期签约支付的确认支付
	 * 
	 * @param cardNo
	 * @param recordId
	 * @param paymentRequest
	 * @return
	 */
	PaymentRecord getRecordToConfirm(String recordId, PaymentRequest paymentRequest);
	
	List<SignRelationInfo> getSignCardList(PaymentRequest paymentRequest);
	
	void calculateInstallmentAmount(PaymentRequest paymentRequest, InstallmentFeeInfoRequestDTO requestDTO, InstallmentFeeInfoResponseDTO response);

	void judgeSignStatusOfCardNo(String cardNo, CashierUserInfo externalUser, InstallmentInfoNeeded request);
}
