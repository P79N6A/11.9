package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import com.yeepay.g3.core.nccashier.vo.*;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.ApiResultTypeEnum;
import com.yeepay.g3.core.nccashier.enumtype.InstallmentPayTypeEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.CashierBankCardService;
import com.yeepay.g3.core.nccashier.service.InstallmentService;
import com.yeepay.g3.core.nccashier.service.MerchantSupportBankService;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.SignCardService;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;

@Service("apiInstallmentRequestBiz")
public class APIInstallmentRequestBizImpl extends APICashierBaseBizTemplateImpl {

	@Resource
	private CashierBankCardService cashierBankCardService;

	@Resource
	private MerchantSupportBankService merchantSupportBankService;

	@Resource
	private SignCardService signCardService;

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private PaymentProcessService paymentProcessService;


	@Resource
	private OrderPaymentService orderPaymentService;

	@Resource
	private InstallmentService installmentService;

	@SuppressWarnings("unchecked")
	@Override
	protected CombinedPaymentDTO bizValidate(APIBasicRequestDTO basicRequestDTO, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantConfigInfo) {
		// 商户开通校验
		APIInstallmentRequestDTO requestDTO = (APIInstallmentRequestDTO) basicRequestDTO;
		// 校验cardNo或signRelationId是否合法
		return validateCardSupport(requestDTO, merchantConfigInfo, orderInfo);
	}
	
	@Override
	public <T> CombinedPaymentDTO handleRequestAndRecord(T installmentInfo, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantConfigInfo, ProductLevel productLevel, APIBasicRequestDTO requestDTO, APIBasicResponseDTO responseDTO) {
		InstallmentInfoNeeded infoNeeded = (InstallmentInfoNeeded) installmentInfo;
		APIInstallmentRequestDTO installmentRequestDTO = (APIInstallmentRequestDTO) requestDTO;
		// paymentRequest校验
		PaymentRequestExtInfo extInfo = buildPaymentRequestExtInfo(installmentRequestDTO.getUserIp(),infoNeeded.getCashierUser());
		PaymentRequest paymentRequest = paymentRequestService.createRequestWhenUnexsit(orderInfo, merchantConfigInfo, productLevel, extInfo);
		// paymentRecord校验
		PaymentRecord paymentRecord = null;
		try {
			paymentRecord = installmentService.getRecordToOrder(installmentRequestDTO.getCardNo(), infoNeeded,
					paymentRequest);
		} catch (CashierBusinessException e) {
			if (infoNeeded.getPaymentRecord() != null) {
				responseDTO.setRecordId(infoNeeded.getPaymentRecord().getId() + "");
			}
			throw e;
		}
		if(paymentRecord!=null){
			infoNeeded.setPaymentRecord(paymentRecord);
		}
		if(paymentRequest!=null){
			infoNeeded.setPaymentRequest(paymentRequest);
		}
		NcCashierLoggerFactory.TAG_LOCAL
				.set("[createPayment],支付请求ID=" + paymentRequest.getId() + ",支付记录ID=" + paymentRecord==null?"":paymentRecord.getId() + "]");
		return infoNeeded;
	}

	/**
	 * @title 校验卡号或签约关系ID是否有效
	 * @description include 对应银行及期数是否被支持，是否存在对应的签约关系
	 * @param requestDTO
	 * @param merchantConfigInfo
	 * @param orderInfo
	 * @return
	 */
	private InstallmentInfoNeeded validateCardSupport(APIInstallmentRequestDTO requestDTO,
			MerchantInNetConfigResult merchantConfigInfo, OrderDetailInfoModel orderInfo) {
		InstallmentInfoNeeded request = new InstallmentInfoNeeded();
		String bankCode = null;
		CashierUserInfo userInfo = CashierUserInfo.buildOrignalExternalUser(requestDTO.getUserNo(), requestDTO.getUserType(), requestDTO.getMerchantNo());
		if (StringUtils.isNotBlank(requestDTO.getSignRelationId())) {
			bankCode = installmentService.isSignRelationIdIllegal(requestDTO.getSignRelationId(), userInfo,
					request);
		} else if (StringUtils.isNotBlank(requestDTO.getCardNo())) {
			bankCode = installmentService.isCardNoIllegal(requestDTO.getCardNo(), userInfo, request);
		}
		// TODO 消化内存 看一下有没有优化方案
		InstallmentBankInfo installmentBankInfo = merchantSupportBankService.bankIsSupportedOrNot(merchantConfigInfo,
				bankCode, orderInfo.getOrderAmount(), Integer.valueOf(requestDTO.getNumber()));
		request.setCurrentBankInfo(installmentBankInfo);
		return request;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected UrlInfo callPPService(CombinedPaymentDTO combinedPaymentDTO, APIBasicRequestDTO requestDTO,
			APIBasicResponseDTO responseDTO) {
		InstallmentInfoNeeded installmentPayInfo = (InstallmentInfoNeeded) combinedPaymentDTO;
		PaymentRecord paymentRecord = installmentPayInfo.getPaymentRecord();
		if (InstallmentPayTypeEnum.FIRST.name().equals(paymentRecord.getPayType())) {
			return installmentService.openAndPay(installmentPayInfo);
		} else {
			installmentService.signedCardOrder(installmentPayInfo);
		}
		return null;
	}

	@Override
	protected void supplyOrderInfo(Object object, PaymentRecord paymentRecord) {

	}

	@Override
	protected <T> void buildResponse(APIBasicResponseDTO basicResponseDTO, APIBasicRequestDTO basicRequestDTO,
			CombinedPaymentDTO combinedPaymentDTO, T t) {
		APIInstallmentResponseDTO response = (APIInstallmentResponseDTO) basicResponseDTO;
		super.buildResponse(basicResponseDTO, basicRequestDTO, combinedPaymentDTO, t);
		if (t instanceof UrlInfo && t != null) {
			UrlInfo urlInfo = (UrlInfo) t;
			response.setResultData(urlInfo.toString());
			response.setResultType(ApiResultTypeEnum.JSON.getType());
		}
	}

	@Override
	public boolean needVerifyProductOpen() {
		return true;
	}

	@Override
	public boolean needBizValidate() {
		return true;
	}

	private PaymentRequestExtInfo buildPaymentRequestExtInfo(String userIp, CashierUserInfo user) {
		PaymentRequestExtInfo extInfo = new PaymentRequestExtInfo();
		extInfo.setUserIp(userIp);
		extInfo.setCashierUser(user);
		return extInfo;
	}
}
