/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.FrontEndService;
import com.yeepay.g3.core.nccashier.gateway.service.FrontedInstallmentService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.frontend.enumtype.RefundType;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhen.tan
 *
 */
@Service
public class FrontEndServiceImpl extends NcCashierBaseService  implements FrontEndService{
	
	public static final Logger logger = NcCashierLoggerFactory.getLogger(FrontEndServiceImpl.class);
	
	@Resource
	private FrontedInstallmentService frontedInstallmentService;
	
	


	@Override
	public PayResponseDTO frontendOpenPay(PayRequestDTO payRequestDTO) {
		// TODO: 2018/11/12  获得银行方面？支付接口的响应
		PayResponseDTO response = frontendPayFacade.openPay(payRequestDTO);
		if(response== null ){
//			FE_EXCEPITON_1("3500003", "系统异常，请更换其他支付方式或稍后再试"),
			throw CommonUtil.handleException(Errors.FE_EXCEPITON_1);
		}else if(StringUtils.isEmpty(response.getResponseCode())){
			//没有错误响应码
			return response;
		}else{
			//有错误码抛异常
			throw CommonUtil.handleException(SysCodeEnum.FRONTEND.name(), response.getResponseCode(), response.getResponseMsg());
		}
	}

	@Override
	public FrontendRefundResponseDTO callRefund(String merchantAccountNo,RefundType refundType,String requestId,PlatformType platformType){
		FrontendRefundRequestDTO requestDTO = new FrontendRefundRequestDTO();
		requestDTO.setCustomerNumber(merchantAccountNo);
		requestDTO.setRefundType(refundType);
		requestDTO.setRequestId(requestId);
		requestDTO.setRequestSystem(PayProductCode.NCCASHIER);
		requestDTO.setPlatformType(platformType);
		FrontendRefundResponseDTO response = frontendRefundFacade.refund(requestDTO);
		if(response== null ){
			throw CommonUtil.handleException(Errors.FE_EXCEPITON_1);
		}else{
			return response;
		}
	}
	
	@Override
	public FrontendQueryResponseDTO queryPaymentOrder(String tradeSysOrderId,String payType){
		FrontendQueryRequestDTO requestDTO = new FrontendQueryRequestDTO();
		requestDTO.setRequestId(tradeSysOrderId);
		requestDTO.setRequestSystem(PayProductCode.NCCASHIER);
		if(PayTypeEnum.WECHAT_OPENID.name().equals(payType) || PayTypeEnum.WECHAT_H5_WAP.name().equals(payType) || PayTypeEnum.WECHAT_H5_LOW.name().equals(payType)){
			requestDTO.setPlatformType(PlatformType.WECHAT);
		}else{
			requestDTO.setPlatformType(PlatformType.ALIPAY);
		}
		
		FrontendQueryResponseDTO response = frontendQueryFacade.queryOrderInfo(requestDTO);
		
		if(response== null ){
			throw CommonUtil.handleException(Errors.FE_EXCEPITON_1);
		}else if(PayStatusEnum.FAILURE != response.getPayStatus() && StringUtils.isEmpty(response.getResponseCode())){
			return response;
		}else{
			throw CommonUtil.handleException(SysCodeEnum.FRONTEND.name(), response.getResponseCode(), response.getResponseMsg());
		}
		
	}

}
