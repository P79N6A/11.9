/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.biz.SDKCashierBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.service.SDKPayService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.CommonUtils;
import com.yeepay.utils.jdbc.dal.utils.StringUtils;

/**
 * @author xueping.ni
 * @date 2017年3月9日
 *
 */
@Service("sdkCashierBiz")
public class SDKCashierBizImpl extends NcCashierBaseBizImpl implements
		SDKCashierBiz {
	@Resource
	private MerchantVerificationService merchantVerificationService;
	@Resource
	private NewOrderHandleService newOrderHandleService;
	
	@Resource
	private SDKPayService sdkPayService;

	@Override
	public SDKCreateOrderResponseDTO payRequest(
			SDKCreateOrderRequestDTO createOrderRequest) {
		SDKCreateOrderResponseDTO response = new SDKCreateOrderResponseDTO();
		response.setMerchantNo(createOrderRequest.getMerchantNo());
		try {
			// 0、校验入参
			valideCreateOrderRequestParam(createOrderRequest);
			// 1、反查订单
			OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(createOrderRequest.getMerchantNo(), createOrderRequest.getToken(), CommonUtil.BIZ_SYS_CONFIG_DEFAULT_KEY, TransactionTypeEnum.PREAUTH);
			// 2、产品开通校验
			VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
			requestParam.setMerchantNo(orderInfo.getMerchantAccountCode());
			requestParam.setProductLevel(new ProductLevel(CashierVersionEnum.SDK, null, null));
			requestParam.setTransactionType(orderInfo.getTransactionType());
			MerchantInNetConfigResult merchantInNetConfigResult = merchantVerificationService.verifyMerchantAuthority(requestParam);
			if(CollectionUtils.isEmpty(merchantInNetConfigResult.getPayTypes())){
				throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
			}
			// 3、查询收银台的请求流水单paymentRequest，不存在则创建
			PaymentRequest paymentRequest = sdkPayService.createPayRequestWhenUnexsit(createOrderRequest, orderInfo, merchantInNetConfigResult);
			// 3、封装返回参数
			buildReturnMessage(paymentRequest,response,merchantInNetConfigResult);
		} catch (Throwable e) {
			handleException(response, e);
		}

		return response;
	}




	/**
	 * @param paymentRequest
	 * @param response
	 * @param payTypes 
	 */
	private void buildReturnMessage(PaymentRequest paymentRequest,
			SDKCreateOrderResponseDTO response, MerchantInNetConfigResult merchantInNetConfigResult) {
		response.setPayTypes(merchantInNetConfigResult.getPayTypes());
		response.setMerchantName(paymentRequest.getMerchantName());
		response.setMerchantOrderId(paymentRequest.getMerchantOrderId());
		response.setOrderAmount(paymentRequest.getOrderAmount());
		response.setProductName(paymentRequest.getProductName());
		response.setUniqueOrderNo(paymentRequest.getOrderOrderId());
		response.setRequestId(paymentRequest.getId());
		response.setSdkUrl(Constant.SDK_PAY_URL);
	}

	@Override
	public SDKPayResponseDTO pay(SDKPayRequestDTO request) {
		
		SDKPayResponseDTO response = new SDKPayResponseDTO();
		try{
//			1、校验入参
			PaymentRequest paymentRequest = validePayParam(request,response);
//			2、生成paymentRecord并调用底层返回唤醒APP所需参数信息
			sdkPayService.pay(response,paymentRequest,request);
			
		}catch(Throwable e){
			handleException(response,e);
		}
		return response;
	}

	/**
	 * 校验支付入参
	 * @param request
	 */
	private PaymentRequest validePayParam(SDKPayRequestDTO request,SDKPayResponseDTO response) {
		BeanValidator.validate(request);
		NcCashierLoggerFactory.TAG_LOCAL.set("[SDK支付|payRecord],支付请求ID="
				+ request.getRequestId());
		PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(request.getRequestId());
		checkPayType(paymentRequest,request.getPayType());
		//微信支付，APPID必须存在
		if(StringUtils.isNotBlank(request.getPayType())&&"WECHAT".equals(request.getPayType())){
			if(StringUtils.isBlank(paymentRequest.getAppID())){
				logger.info("PayType为微信，但APPID未传");
				throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
		}
		response.setRequestId(request.getRequestId());
		response.setPayType(request.getPayType());
		return paymentRequest;
	}



	/**
	 * @param paymentRequest
	 * @param payType
	 */
	private void checkPayType(PaymentRequest paymentRequest, String payType) {
		JSONObject json = CommonUtil.parseJson(paymentRequest.getRemark());
		if(json.containsKey("payType")){
			String payTypes= json.getObject("payType", String.class);
			if(!payTypes.contains(payType)){
				throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR); 
			}
		}
	}


	/**校验支付请求入参
	 * @param createOrderRequest
	 */
	private void valideCreateOrderRequestParam(
			SDKCreateOrderRequestDTO createOrderRequest) {
		BeanValidator.validate(createOrderRequest);
		NcCashierLoggerFactory.TAG_LOCAL.set("[SDK支付请求|payRequest],OPRtoken="
				+ createOrderRequest.getToken());
//		微信支付必须传APPID 
		if(StringUtils.isNotBlank(createOrderRequest.getDirectPayType())&&"WECHAT".equals(createOrderRequest.getDirectPayType())){
			if(StringUtils.isBlank(createOrderRequest.getAppId())){
				logger.info("DirectPayType为微信，但APPID 未传");
				throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
		}
//		时间戳校验
		CommonUtils.checkUrlOutOfExpDate(Long.parseLong(createOrderRequest.getTimeStamp()));
	}

}
