package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.FrontCallbackBiz;
import com.yeepay.g3.core.nccashier.gateway.service.NcPayService;
//import com.yeepay.g3.core.nccashier.gateway.service.YOPService;
import com.yeepay.g3.core.nccashier.vo.IntelligentNetResultFrontCallbackResInfo;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackDTO;
import com.yeepay.g3.facade.nccashier.dto.IntelligentNetResultFrontCallbackResDTO;
//import com.yeepay.g3.utils.common.StringUtils;

@Service
public class FrontCallbackBizImpl extends NcCashierBaseBizImpl implements FrontCallbackBiz {

	@Resource
	private NcPayService ncpayService;

//	private YOPService yopService;

	@Override
	public IntelligentNetResultFrontCallbackResDTO receiveIntelligentNetResultFrontCallback(
			IntelligentNetResultFrontCallbackDTO intelligentNetResultFrontCallbackDTO) {
		IntelligentNetResultFrontCallbackResDTO resDTO = new IntelligentNetResultFrontCallbackResDTO();
		try {
			// 调用NCPAY获取前端回调地址及参数
			IntelligentNetResultFrontCallbackResInfo resInfo = ncpayService
					.queryPreRouteRedirectInfo(intelligentNetResultFrontCallbackDTO.getPaymentNo());
			// 构造返回值
			buildIntelligentNetResultFrontCallbackResDTO(resInfo, resDTO);
		} catch (Throwable t) {
			handleException(resDTO, t);
		}

		return resDTO;
	}

	private IntelligentNetResultFrontCallbackResDTO buildIntelligentNetResultFrontCallbackResDTO(
			IntelligentNetResultFrontCallbackResInfo resInfo, IntelligentNetResultFrontCallbackResDTO resDTO) {
//		String plainText = getSignPlainTextOfIntelligentNetResultFrontCallback(resInfo);
//		String frontCallbackUrl = resInfo.getRedirectUrl();
//		if (StringUtils.isNotBlank(plainText)) {
//			String sign = yopService.sign(plainText.substring(0, plainText.length() - 1));
//			frontCallbackUrl = frontCallbackUrl + "?" + plainText + "sign=" + sign;
//		}
//		resDTO.setFrontCallbackUrl(frontCallbackUrl);
		
		resDTO.setFrontCallbackUrl(resInfo.getRedirectUrl());
		return resDTO;
	}

//	private String getSignPlainTextOfIntelligentNetResultFrontCallback(
//			IntelligentNetResultFrontCallbackResInfo resInfo) {
//		StringBuilder plainText = new StringBuilder();
//		if (StringUtils.isNotBlank(resInfo.getBizOrderNo())) {
//			plainText.append("bizOrderNo=").append(resInfo.getBizOrderNo()).append("&");
//		}
//		if (resInfo.getBizType() != null && resInfo.getBizType() > 0) {
//			plainText.append("bizType=").append(resInfo.getBizType()).append("&");
//		}
//		if (StringUtils.isNotBlank(resInfo.getConfirmTime())) {
//			plainText.append("confirmTime=").append(resInfo.getConfirmTime()).append("&");
//		}
//		if (StringUtils.isNotBlank(resInfo.getMerchantNo())) {
//			plainText.append("merchantNo=").append(resInfo.getMerchantNo()).append("&");
//		}
//		if (resInfo.getOrderAmount() != null && resInfo.getOrderAmount().isPositive()) {
//			plainText.append("orderAmount=").append(resInfo.getOrderAmount()).append("&");
//		}
//		if (StringUtils.isNotBlank(resInfo.getPaymentNo())) {
//			plainText.append("paymentNo=").append(resInfo.getPaymentNo()).append("&");
//		}
//		if (resInfo.getRealAmount() != null && resInfo.getRealAmount().isPositive()) {
//			plainText.append("realAmount=").append(resInfo.getRealAmount()).append("&");
//		}
//		if (StringUtils.isNotBlank(resInfo.getRequestNo())) {
//			plainText.append("requestNo=").append(resInfo.getRequestNo()).append("&");
//		}
//		if (resInfo.getState() != null) {
//			plainText.append("state=").append(resInfo.getState()).append("&");
//		}
//		return plainText.toString();
//	}

}
