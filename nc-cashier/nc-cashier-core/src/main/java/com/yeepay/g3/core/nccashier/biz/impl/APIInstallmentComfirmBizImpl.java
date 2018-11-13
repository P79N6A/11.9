package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.InstallmentService;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.facade.nccashier.dto.APIBasicRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIInstallmentComfirmRequestDTO;

/**
 * 银行卡分期确认支付biz实现
 * 
 * @author duangduang
 *
 */
@Service("apiInstallmentConfirmPayBiz")
public class APIInstallmentComfirmBizImpl extends APICashierBaseBizTemplateImpl {

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private InstallmentService installmentService;

	@Resource
	private OrderPaymentService orderPaymentService;

	@Override
	protected <T> T callPPService(CombinedPaymentDTO combinedPaymentDTO, APIBasicRequestDTO requestDTO,
			APIBasicResponseDTO responseDTO) {
		APIInstallmentComfirmRequestDTO request = (APIInstallmentComfirmRequestDTO) requestDTO;
		installmentService.syncComfirmPay(combinedPaymentDTO.getPaymentRecord(), request.getVerifyCode());
		return null;
	}

	@Override
	protected <T> CombinedPaymentDTO handleRequestAndRecord(T otherInfoNeeded, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantConfigInfo, ProductLevel productLevel,
			APIBasicRequestDTO basicRequestDTO, APIBasicResponseDTO responseDTO) {
		APIInstallmentComfirmRequestDTO requestDTO = (APIInstallmentComfirmRequestDTO) basicRequestDTO;
		NcCashierLoggerFactory.TAG_LOCAL.set("[confirmPay],支付记录ID=" + requestDTO.getRecordId() + "]");
		PaymentRequest paymentRequest = paymentRequestService
				.findNonNullPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo(), true);
		// paymentRecord校验
		PaymentRecord paymentRecord = installmentService.getRecordToConfirm(requestDTO.getRecordId(), paymentRequest);
		return orderPaymentService.buildCombinedPaymentDTO(paymentRequest, paymentRecord);
	}

	@Override
	public boolean needVerifyProductOpen() {
		return false;
	}

	@Override
	public boolean needBizValidate() {
		return false;
	}

	@Override
	protected void supplyOrderInfo(Object object, PaymentRecord paymentRecord) {

	}
}
