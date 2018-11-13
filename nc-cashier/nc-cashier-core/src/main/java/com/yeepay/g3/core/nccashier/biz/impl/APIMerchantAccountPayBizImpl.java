package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIMerchantAccountPayBiz;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.AccountPayService;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.MerchantAccountPayRequestInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

@Service("apiMerchantAccountPayBiz")
public class APIMerchantAccountPayBizImpl extends APIBaseBiz implements APIMerchantAccountPayBiz {

	@Resource
	private NewOrderHandleService newOrderHandleService;

	@Resource
	private MerchantVerificationService merchantVerificationService;

	@Resource
	private AccountPayService accountPayService;

	// 前置收银台企业账户支付
	private static final ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.ZF_ZHZF,
			PayTypeEnum.ZF_ZHZF);

	@Override
	public APIBasicResponseDTO pay(APIMerchantAccountPayRequestDTO requestDTO) {
		APIBasicResponseDTO responseDTO = new APIBasicResponseDTO();
		try {
			// 参数校验
			validateMerchantAccountPayParam(requestDTO);
			// 反查订单
			OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(),
					requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
			// 商户配置校验
			VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
			requestParam.setMerchantNo(orderInfo.getMerchantAccountCode());
			requestParam.setProductLevel(productLevel);
			requestParam.setTransactionType(orderInfo.getTransactionType());
			MerchantInNetConfigResult merchantInNetConfig = merchantVerificationService.verifyMerchantAuthority(requestParam);
			// 进行会员支付
			this.accountPay(requestDTO, orderInfo, merchantInNetConfig);
			
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, requestDTO);
		return responseDTO;
	}

	/**
	 * 获取或创建支付请求和支付记录，组装支付处理器的支付接口入参，调用支付处理器的支付接口进行支付
	 * 
	 * @param requestDTO
	 * @param orderInfo
	 * @param merchantInNetConfig
	 */
	private void accountPay(APIMerchantAccountPayRequestDTO requestDTO, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig) {
		MerchantAccountPayRequestInfo requestInfo = buildMerchantAccountPayRequestInfo(requestDTO, orderInfo);
		accountPayService.accountPay(requestInfo, orderInfo, merchantInNetConfig, productLevel);
	}

	/**
	 * 构建商户账户支付的通用BO请求参数
	 * 
	 * @param requestDTO
	 * @param orderInfo
	 * @return
	 */
	private MerchantAccountPayRequestInfo buildMerchantAccountPayRequestInfo(APIMerchantAccountPayRequestDTO requestDTO,
			OrderDetailInfoModel orderInfo) {
		MerchantAccountPayRequestInfo requestInfo = new MerchantAccountPayRequestInfo();
		requestInfo.setUserIp(requestDTO.getUserIp());
		requestInfo.setDebitCustomerNo(orderInfo.getAccountPayMerchantNo());
		requestInfo.setPwsd(requestDTO.getPwsd());
		return requestInfo;
	}

	/**
	 * 商户账户支付入参校验
	 * 
	 * @param requestDTO
	 */
	private void validateMerchantAccountPayParam(APIMerchantAccountPayRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
		// 商编的转化 —— 将OPR：等前缀去掉
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[merchantAccountPay],token=" + requestDTO.getToken() + "]");
	}

}
