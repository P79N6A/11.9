package com.yeepay.g3.core.nccashier.gateway.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.FrontedInstallmentService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.frontend.dto.InstallmentOrderRequestDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentOrderResponseDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentQueryRequestDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentResultMessage;
import com.yeepay.g3.facade.frontend.dto.InstallmentReverseRequestDTO;
import com.yeepay.g3.facade.frontend.dto.InstallmentReverseResponseDTO;
import com.yeepay.g3.facade.frontend.enumtype.CflOrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.StringUtils;

@Service
public class FrontedInstallmentServiceImpl extends NcCashierBaseService implements FrontedInstallmentService{

	@Override
	public InstallmentReverseResponseDTO callInstallmentRefund(InstallmentReverseRequestDTO request) {
		
		InstallmentReverseResponseDTO response  = installmentReverseFacade.reverseRequest(request);
		if(response == null ){
			throw CommonUtil.handleException(Errors.FE_EXCEPITON_1);
		}
		return response;
	}

	@Override
	public InstallmentOrderResponseDTO callInstallmentCreateOrder(PaymentRequest request, PaymentRecord paymentRecord, LevelRespDTO respDTO) {
		InstallmentOrderRequestDTO installmentOrderRequestDTO = new InstallmentOrderRequestDTO();
		makeInstallmentRequest4FE(request,paymentRecord,respDTO,installmentOrderRequestDTO);
		InstallmentOrderResponseDTO response = installmentFacade.payWeb(installmentOrderRequestDTO);
		if(response== null){
			throw CommonUtil.handleException(Errors.FE_EXCEPITON_1);
		}else if(StringUtils.isEmpty(response.getResponseCode())){
			return response;
		}else{
			throw CommonUtil.handleException(SysCodeEnum.FRONTEND.name(), response.getResponseCode(), response.getResponseMsg());
		}
	}

	private void makeInstallmentRequest4FE(PaymentRequest paymentRequest, PaymentRecord paymentRecord, LevelRespDTO respDTO,
			InstallmentOrderRequestDTO req) {
		req.setRequestSystem(SysCodeEnum.NCCASHIER.name());
		req.setPaymentProduct(PayTool.CFL.name());
		req.setRequestId(paymentRequest.getId() + "");
		req.setDealUniqueSerialNo(paymentRequest.getTradeSysOrderId());
		req.setOrderSystem("YJZF");
		req.setOutTradeNo(paymentRequest.getMerchantOrderId());
		req.setCustomerNumber(paymentRequest.getMerchantNo());
		req.setTotalAmount(paymentRequest.getOrderAmount());
		req.setCustomerName(paymentRequest.getMerchantName());
		req.setCustomerLevel(respDTO.getCheckLevel());
		req.setIndustryCode(paymentRequest.getIndustryCatalog());
		req.setCflOrderType(CflOrderType.ONLINE);
		req.setIdentityType(paymentRequest.getIdentityType());
		req.setIdentityId(paymentRequest.getIdentityId());
		req.setGoodsDescription(paymentRequest.getProductName());
		req.setProductType("VIRTUAL");
		req.setPageCallBack(CommonUtil.getH5FrontCallbackUrl(paymentRequest.getMerchantNo(), paymentRecord.getTokenId()));
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		req.setRetailProductCode(jsonObject.getString("saleProductCode"));//零售产品码
	}

	@Override
	public InstallmentResultMessage queryInstallmentOrder(
			PaymentRecord paymentRecord) {
		InstallmentQueryRequestDTO InstallmentQueryRequestDTO = new InstallmentQueryRequestDTO();
		InstallmentQueryRequestDTO.setRequestId(paymentRecord.getPaymentRequestId()+"");
		InstallmentQueryRequestDTO.setRequestSystem(PayProductCode.NCCASHIER);
		InstallmentResultMessage response = installmentQueryFacade.queryOrderInfo(InstallmentQueryRequestDTO);
		if(response== null){
			throw CommonUtil.handleException(Errors.FE_EXCEPITON_1);
		}else if(PayStatusEnum.FAILURE != response.getPayStatus() && StringUtils.isEmpty(response.getResponseCode())){
			return response;
		}else{
			throw CommonUtil.handleException(SysCodeEnum.FRONTEND.name(), response.getResponseCode(), response.getResponseMsg());
		}

	}
}
