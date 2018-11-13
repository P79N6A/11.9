package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.MerchantAccountPayRequestInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;

/**
 * 账户支付service
 * 
 * @author duangduang
 * @date 2017-06-06
 */
public interface AccountPayService {

	/**
	 * 商户权限校验
	 * 
	 * @param merchantAccountNo
	 * @exception 此商户账户无会员支付的权限，则抛Errors.MERCHANT_PERMISSION_INVALID异常
	 *                此商户账户不存在，则抛Errors.MERCHANT_PLATFORM_ACCOUNT_NAME_NOEXSIT
	 *                其他异常：系统异常，错误码37开头
	 */
	void validateMerchantPermission(String merchantAccountNo);

	/**
	 * 交易密码校验
	 * 
	 * @param requestDTO
	 * @param responseDTO
	 */
	void validateTradePassword(AccountPayValidateRequestDTO requestDTO, AccountPayValidateResponseDTO responseDTO);

	/**
	 * 校验是否需要重新创建paymentRecord
	 * 
	 * @param requestDTO
	 * @return 需要重新创建paymentRecord，返回null
	 *         不需要重新创建paymentRecord，返回数据库已经存在的paymentRecord
	 */
	PaymentRecord needCreateNewRecord(CashierAccountPayRequestDTO requestDTO);

	/**
	 * 创建账户支付的支付记录paymentRecord并保存到数据库中，更新用户行为表
	 * 
	 * @param requestDTO
	 * @param paymentRequest
	 * @return
	 * 返回新创建的支付记录
	 */
	PaymentRecord createRecordAndUpdateUserAccount(CashierAccountPayRequestDTO requestDTO, PaymentRequest paymentRequest);
	
	/**
	 * 调用下游系统进行商户账户支付
	 * 
	 * @param paymentRequest
	 * @param paymentRecord
	 * @param requestInfo
	 */
	void accountPay(PaymentRequest paymentRequest, PaymentRecord paymentRecord, MerchantAccountPayRequestInfo requestInfo);
	
	/**
	 * 包括获取PaymentRequest和paymentRecord，以及调用下游支付系统进行支付。目前只用在前置收银台，
	 * 后续可以把页面版的也改成使用这个方法
	 * 
	 * @param requestInfo
	 * @param orderInfo
	 * @param merchantInNetConfig
	 * @param productLevel
	 */
	void accountPay(MerchantAccountPayRequestInfo requestInfo, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig, ProductLevel productLevel);
}
