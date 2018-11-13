package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.AccountPayBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.service.AccountPayService;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.MerchantAccountPayRequestInfo;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;

/**
 * 账户支付业务层实现
 * 
 * @author duangduang
 * @date 2017-06-06
 */
@Service("accountPayBiz")
public class AccountPayBizImpl extends NcCashierBaseBizImpl implements AccountPayBiz {

	@Autowired
	private AccountPayService accountPayService;

	@Override
	public CashierAccountPayResponseDTO pay(CashierAccountPayRequestDTO requestDTO) {
		CashierAccountPayResponseDTO responseDTO = new CashierAccountPayResponseDTO();
		try {
			// 参数校验
			BeanValidator.validate(requestDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[账户支付支付下单|pay],账户名="+requestDTO.getUserAccount()+",paymentRequestId="+requestDTO.getRequestId());
			// 校验paymentRequest
			PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
			// 获取当前的支付记录
			PaymentRecord paymentRecord = getCurrentPaymentRecord(requestDTO, paymentRequest);
			// 调用支付处理器账户支付下单接口
			accountPay(requestDTO, paymentRequest, paymentRecord);
		} catch (Throwable t) {
			handleException(responseDTO, t);
		}
		return responseDTO;
	}
	
	/**
	 * 进行账户支付，适用场景：PC收银台和H5收银台
	 * 
	 * @param requestDTO
	 * @param paymentRequest
	 * @param paymentRecord
	 */
	private void accountPay(CashierAccountPayRequestDTO requestDTO, PaymentRequest paymentRequest,
			PaymentRecord paymentRecord) {
		MerchantAccountPayRequestInfo requestInfo = buildMerchantAccountPayRequestInfo(requestDTO);
		accountPayService.accountPay(paymentRequest, paymentRecord, requestInfo);
	}
	
	/**
	 * 构造账户支付基础的BO入参
	 * 
	 * @param requestDTO
	 * @return
	 */
	private MerchantAccountPayRequestInfo buildMerchantAccountPayRequestInfo(CashierAccountPayRequestDTO requestDTO) {
		MerchantAccountPayRequestInfo requestInfo = new MerchantAccountPayRequestInfo();
		requestInfo.setDebitCustomerNo(requestDTO.getDebitCustomerNo());
		requestInfo.setTokenId(requestDTO.getTokenId());
		requestInfo.setUserAccount(requestDTO.getUserAccount());
		return requestInfo;
	}

	/**
	 * 获取当前的支付记录
	 * 
	 * @param request
	 * @param paymentRequest
	 * @return
	 */
	private PaymentRecord getCurrentPaymentRecord(CashierAccountPayRequestDTO request, PaymentRequest paymentRequest) {
		// 判断是否需要新建支付记录
		PaymentRecord paymentRecord = accountPayService.needCreateNewRecord(request);
		if (paymentRecord != null) {
			return paymentRecord;
		}
		paymentRecord = accountPayService.createRecordAndUpdateUserAccount(request, paymentRequest);
		return paymentRecord;
	}

	@Override
	public AccountPayValidateResponseDTO validateMerchantAccount(AccountPayValidateRequestDTO request) {
		AccountPayValidateResponseDTO responseDTO = new AccountPayValidateResponseDTO();
		try {
			// 参数校验
			BeanValidator.validate(request);
			NcCashierLoggerFactory.TAG_LOCAL.set("[账户支付支付校验|validateMerchantAccount],账户名="+request.getUserAccount());
			// 权限校验
			accountPayService.validateMerchantPermission(request.getUserAccount());
			// 交易密码校验
			accountPayService.validateTradePassword(request, responseDTO);
		} catch (Throwable t) {
			handleException(responseDTO, t);
		}

		return responseDTO;
	}

}
