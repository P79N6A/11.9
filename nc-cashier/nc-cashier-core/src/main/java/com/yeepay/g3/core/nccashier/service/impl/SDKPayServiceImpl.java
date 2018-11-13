/**
 * 
 */
package com.yeepay.g3.core.nccashier.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.service.SDKPayService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.SDKCreateOrderRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.SDKPayResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.IdCardTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * @author xueping.ni
 * @date 2017年3月14日
 *
 */
@Service("sdkPayService")
public class SDKPayServiceImpl extends NcCashierBaseService implements
		SDKPayService {
	@Resource
	private PayProcessorService payProcessorService;
	@Resource
	private PaymentProcessService paymentProcessService;
	@Resource
	private PaymentRequestService paymentRequestService;
	@Resource
	private NewOrderHandleService newOrderHandleService;
	@Override
	public void pay(SDKPayResponseDTO response, PaymentRequest paymentRequest,
			SDKPayRequestDTO request) {
		//创建paymentrecord
		PaymentRecord paymentRecord = createPaymentRecord(response,request,paymentRequest);
		//调用pp下单
		callPP2CreateOrder(response,paymentRecord,paymentRequest,request);
	}
	/**
	 * @param response 
	 * @param paymentRecord
	 * @param paymentRequest
	 */
	private void callPP2CreateOrder(SDKPayResponseDTO response, PaymentRecord paymentRecord,
			PaymentRequest paymentRequest,SDKPayRequestDTO request) {
		OpenPayRequestDTO openPayRequestDTO = new OpenPayRequestDTO();
		buildBasicRequestDTO(paymentRequest,openPayRequestDTO);
		openPayRequestDTO.setCustomerLevel(Constant.CUSTOMER_LEVEL_V);
		openPayRequestDTO.setCashierType(CommonUtil.transformToOPRVersion(CashierVersionEnum.SDK.name()));
		openPayRequestDTO.setPayProduct(paymentRecord.getPayTool());
		openPayRequestDTO.setGoodsInfo(paymentRequest.getTradeRiskInfo());
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		openPayRequestDTO.setRetailProductCode(jsonObject.getString("saleProductCode"));//零售产品码
		openPayRequestDTO.setPayInterface(jsonObject.getString("payInterface"));//通道编码
		if(StringUtils.isNotBlank(jsonObject.getString("bankTotalCost"))) {//通道成本
			openPayRequestDTO.setBankTotalCost(new BigDecimal(jsonObject.getString("bankTotalCost")));
		}
		openPayRequestDTO.setReportMerchantNo(jsonObject.getString("reportMerchantNo"));//二级商户号
		openPayRequestDTO.setPayBusinessType(PayBusinessType.DC);
		openPayRequestDTO.setPayerIp(request.getUserIp());
		openPayRequestDTO.setAppId(paymentRequest.getAppID());
		openPayRequestDTO.setPlatformType(transferPlatformType(request.getPayType()));
		//支付订单类型目前支付宝没有SDK通道需要走H5通道，故该字段传ACTIVESCAN
		if("ALIPAY".equals(request.getPayType())){
			openPayRequestDTO.setPayOrderType(PayOrderType.ACTIVESCAN);
		}else{
			openPayRequestDTO.setPayOrderType(PayOrderType.SDK);
		}
		openPayRequestDTO.setBasicProductCode(CommonUtil.getBasicProductCode(paymentRecord.getPayTool(),paymentRequest.getTradeSysNo()));
		openPayRequestDTO.setPageCallBack(CommonUtil.getH5FrontCallbackUrl(paymentRequest.getMerchantNo(), paymentRecord.getTokenId()));
		OpenPayResponseDTO responseDTO = payProcessorService.openPayRequest(openPayRequestDTO);
		if("ALIPAY".equals(request.getPayType())){
			response.setAppMessage(CommonUtil.getAlipayPayUrl(responseDTO.getPrepayCode()));
		}else{
			response.setAppMessage(responseDTO.getPrepayCode());
		}
		paymentProcessService.updateRecordNo(paymentRecord.getId(),"",responseDTO.getRecordNo(),PayRecordStatusEnum.PAYING);
		
	}
	/**
	 * @param payType
	 * @return
	 */
	private PlatformType transferPlatformType(String payType) {
		if("WECHAT".equals(payType)){
			return PlatformType.WECHAT;
		}else if("ALIPAY".equals(payType)){
			return PlatformType.ALIPAY;
		}
		return null;
	}
	/**
	 * @param response
	 * @param request
	 * @param payRequest
	 */
	private PaymentRecord createPaymentRecord(SDKPayResponseDTO response,
			SDKPayRequestDTO request, PaymentRequest payRequest) {

		PaymentRecord paymentRecord = new PaymentRecord();
		paymentRecord.setAreaInfo(payRequest.getAreaInfo());
		paymentRecord.setBizModeCode(payRequest.getBizModeCode());	
		paymentRecord.setCreateTime(new Date());
		paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
		paymentRecord.setMcc(payRequest.getIndustryCatalog());
		paymentRecord.setMerchantName(payRequest.getMerchantName());
		paymentRecord.setMerchantNo(payRequest.getMerchantNo());
		paymentRecord.setMerchantOrderId(payRequest.getMerchantOrderId());
		paymentRecord.setPaymentAmount(payRequest.getOrderAmount());
		paymentRecord.setPaymentRequestId(payRequest.getId());
		paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
		paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
		paymentRecord.setPayType(transferPayType(request.getPayType()));
		paymentRecord.setProductName(payRequest.getProductName());
		paymentRecord.setState(PayRecordStatusEnum.INIT);
		paymentRecord.setTokenId(UUID.randomUUID().toString());
		paymentRecord.setTradeSysNo(payRequest.getTradeSysNo());
		paymentRecord.setTradeSysOrderId(payRequest.getTradeSysOrderId());
		paymentRecord.setOrderOrderId(payRequest.getOrderOrderId());
		paymentRecord.setOrderSysNo(payRequest.getOrderSysNo());
		paymentRecord.setMemberNo("");
		paymentRecord.setMemberType(StringUtils.isNotBlank(payRequest.getMemberType())? payRequest.getMemberType():Constant.JOINLY);
		paymentRecord.setUpdateTime(new Date());
		paymentRecord.setVersion(CashierVersionEnum.getVersionValue(CashierVersionEnum.SDK.name()));
		paymentRecord.setPayTool(PayTool.EWALLET.name());
		long recordId = paymentProcessService.savePaymentRecord(paymentRecord);
		response.setRecordId(recordId);
		return paymentRecord;
	
	}
	/**
	 * @param payType
	 */
	private String transferPayType(String payType) {
		if("WECHAT".equals(payType)){
			return PayTypeEnum.WECHAT_SDK.name();
		}else if("ALIPAY".equals(payType)){
			return PayTypeEnum.ALIPAY_SDK.name();
		}
		return null;
	}
	
	@Override
	public PaymentRequest createPayRequestWhenUnexsit(SDKCreateOrderRequestDTO createOrderRequest, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfigResult) {
		// 补充前端透传信息
		supplyPassInfo(createOrderRequest, orderInfo);
		// 查询支付请求记录
		PaymentRequest paymentRequest = paymentRequestService
				.findPayRequestByTradeSysOrderId(orderInfo.getUniqueOrderNo(), orderInfo.getOrderSysNo());
		long id = 0;
		if (paymentRequest == null) {
			paymentRequest = orderInfo.toPaymentRequest();
			supplyPaymentInfo(merchantInNetConfigResult, paymentRequest, createOrderRequest, orderInfo);
			try {
				id = paymentRequestService.savePaymentRequest(paymentRequest);
			} catch (CashierBusinessException e) {
				if (Errors.REPEAT_ORDER.getCode().equals(e.getDefineCode())) {
					paymentRequest = paymentRequestService.findPayRequestByTradeSysOrderId(orderInfo.getUniqueOrderNo(),
							orderInfo.getOrderSysNo());
					id = paymentRequest.getId();
				} else {
					throw e;
				}
			}
		} else {
			id = paymentRequest.getId();
		}
		paymentRequest.setId(id);
		boolean isOutTime = paymentRequestService.isRequestExpired(paymentRequest);
		if (isOutTime) {
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		return paymentRequest;
	}
	
	/**
	 * 补充前端透传信息
	 * @param createOrderRequest
	 * @param orderInfo
	 */
	private void supplyPassInfo(SDKCreateOrderRequestDTO createOrderRequest,
			OrderDetailInfoModel orderInfo) {
		orderInfo.setCashierVersion(CashierVersionEnum.SDK);
		if(StringUtils.isNotBlank(createOrderRequest.getAppId())){
			orderInfo.setAppId(createOrderRequest.getAppId());
		}
		if(StringUtils.isNotBlank(createOrderRequest.getRiskInfo())){
			orderInfo.setTradeRiskInfo(createOrderRequest.getRiskInfo());
		}
	}
	/**
	 * 补充支付信息
	 * @param merchantInNetConfigResult
	 * @param paymentRequest
	 * @param createOrderRequest
	 * @param orderInfo 
	 */
	private void supplyPaymentInfo(
			MerchantInNetConfigResult merchantInNetConfigResult,
			PaymentRequest paymentRequest,
			SDKCreateOrderRequestDTO createOrderRequest, OrderDetailInfoModel orderInfo) {
		JSONObject configJson = new JSONObject();
		configJson.put("payType", merchantInNetConfigResult.getPayTypes().toString());
		configJson.put("directPayType", createOrderRequest.getDirectPayType());
		paymentRequest.setRemark(configJson.toJSONString());
		paymentRequest.setFeeType(merchantInNetConfigResult.getFeeType().name());
		paymentRequest.setIndustryCatalog(merchantInNetConfigResult.getMcc());
		Map<PayTool,String> paymentSceneMap = merchantInNetConfigResult.getPaymentSceneMap();
		if(MapUtils.isNotEmpty(paymentSceneMap)){
			String ncayScene = paymentSceneMap.get(PayTool.NCPAY);
			paymentRequest.setBizModeCode(ncayScene);
		}
		//补充用户手续费
		BigDecimal fee = newOrderHandleService.queryUserFee(paymentRequest,orderInfo.getCallFeeItem());
		paymentRequest.setFee(fee);
	}
}
