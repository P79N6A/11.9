package com.yeepay.g3.core.nccashier.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.biz.APIMerchantScanPayBiz;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.utils.jdbc.dal.utils.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("apiMerchantScanPayBiz")
public class APIMerchantScanPayBizImpl extends NcCashierBaseBizImpl implements
		APIMerchantScanPayBiz {
	@Resource
	private PaymentRequestService paymentRequestService;
	@Resource
	private NewOrderHandleService newOrderHandleService;
	@Resource
	private MerchantVerificationService merchantVerificationService;
	@Resource
	private APIMerchantScanService apiMerchantScanService;
	@Resource
	private PaymentProcessService paymentProcessService;

	@Override
	public BasicResponseDTO pay(APIMerchantScanPayDTO request) {
		BasicResponseDTO response = new BasicResponseDTO();
		try {
			valideInputParam(request);
			//获取业务方配置信息
			OrderSysConfigDTO o = CommonUtil.getBizSysCnfigParams(request.getBizType());
			// 反查订单处理器订单信息
			OrderDetailInfoModel orderInfo = orderInfoAccessAdapterService
					.getOrderDetailInfoModel(request.getToken(), o);
			newOrderHandleService.orderReferCheck(o,orderInfo);

			// 校验商户编号
			if (StringUtils.isBlank(request.getMerchantNo())
					|| !request.getMerchantNo().equals(
							orderInfo.getMerchantAccountCode())) {
				throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
			// 校验商户产品开通 ：是否开通API-MSCANPAY-WX/ZFB/YL支付
			VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
			requestParam.setMerchantNo(orderInfo.getMerchantAccountCode());
			ProductLevel productLevel = new ProductLevel();
			productLevel.setVersion(CashierVersionEnum.API);
			productLevel.setPayTool(PayTool.MSCANPAY);
			productLevel.setPayType(PayTypeEnum.valueOf(request.getCodeType()));
			requestParam.setProductLevel(productLevel);
			requestParam.setTransactionType(orderInfo.getTransactionType());
			MerchantInNetConfigResult merchantInNetConfigResult = merchantVerificationService
					.verifyMerchantAuthority(requestParam);
			// 请求支付处理器支付下单
			PaymentRequest paymentRequest = validePaymentRequest(orderInfo,
					merchantInNetConfigResult, request);
			PaymentRecord record = buildPaymentRecord(request,
					PayTypeEnum.valueOf(request.getCodeType()), paymentRequest);
			if(PayRecordStatusEnum.SUCCESS!=record.getState()){
				apiMerchantScanService.callPP2CreateOrder(request, paymentRequest,
						record);
			}
			
		} catch (Throwable e) {
			handleException(response, e);
		}

		return response;
	}

	private PaymentRecord buildPaymentRecord(APIMerchantScanPayDTO request,
			PayTypeEnum payType, PaymentRequest payRequest) {
		PaymentRecord paymentRecord = null;
		List<PaymentRecord> paymentRecords = paymentProcessService
				.findRecordList(payRequest.getTradeSysOrderId(),
						payRequest.getTradeSysNo());
		if (CollectionUtils.isNotEmpty(paymentRecords)) {
			paymentRecord = paymentRecords.get(0);
			if (!request.getCode().equals(paymentRecord.getCardInfoId())
					||! payType.name().equals(paymentRecord.getPayType())) {
				paymentRecord =null;
			}
		}
		if (null == paymentRecord) {
			logger.info("paymentRecord需要重新创建");
			paymentRecord = new PaymentRecord();
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
			paymentRecord.setPayType(payType.name());// 商户传的支付类型
			paymentRecord.setPayTool(PayTool.MSCANPAY.name());// 支付工具为商家扫码
			paymentRecord.setProductName(payRequest.getProductName());
			paymentRecord.setState(PayRecordStatusEnum.INIT);
			paymentRecord.setTokenId(UUID.randomUUID().toString());// 随机生成
			paymentRecord.setTradeSysNo(payRequest.getTradeSysNo());
			paymentRecord.setTradeSysOrderId(payRequest.getTradeSysOrderId());
			paymentRecord.setOrderOrderId(payRequest.getOrderOrderId());
			paymentRecord.setOrderSysNo(payRequest.getOrderSysNo());
			paymentRecord.setMemberNo("");
			paymentRecord.setMemberType(StringUtils.isNotBlank(payRequest
					.getMemberType()) ? payRequest.getMemberType()
					: Constant.JOINLY);
			paymentRecord.setUpdateTime(new Date());
			paymentRecord.setCardInfoId(request.getCode());// 授权码
			paymentRecord.setVersion(payRequest.getVersion());
			paymentProcessService.savePaymentRecord(paymentRecord);
		}
		return paymentRecord;
	}

	private PaymentRequest validePaymentRequest(OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult configResult,
			APIMerchantScanPayDTO request) {
		PaymentRequest paymentRequest = paymentRequestService
				.findPayRequestByTradeSysOrderId(orderInfo.getUniqueOrderNo(),
						orderInfo.getOrderSysNo());
		if (paymentRequest == null) {
			orderInfo.setCashierVersion(CashierVersionEnum.API);
			paymentRequest = orderInfo.toPaymentRequest();
			// 上面已经设置过extendInfo了 包括：accountPayExt、orderType、productType、saleProductCode、origAppId
//			ExtendInfoFromPayRequest extendInfo = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
			// TODO 可能是个坑
//			JSONObject saleProduct = new JSONObject();
//			if(configResult.getSaleProductCode()!=null){
//				saleProduct.put("saleProductCode", configResult.getSaleProductCode());
//				paymentRequest.setExtendInfo(saleProduct.toJSONString());	
//			}
			JSONObject configJson = new JSONObject();
			if (configResult.getDirectPayType() != null) {
				configJson.put("DirectPayType", configResult.getDirectPayType()
						.name());
			}

			List<String> payTypes = new ArrayList<String>();
			for (String payType : configResult.getPayTypes()) {
				PayTypeEnum payTypeEnum = PayTypeEnum.valueOf(payType);
				payTypes.add(payTypeEnum.value() + "");

			}
			configJson.put("PayType", payTypes.toString());
			configJson.put("payTool", configResult.getPayToolAndPayTypeMap()
					.keySet());
			paymentRequest.setRemark(configJson.toJSONString());			
			paymentRequest.setIndustryCatalog(configResult.getMcc());
			paymentRequest.setVersion(2);
			Map<PayTool, String> paymentSceneMap = configResult
					.getPaymentSceneMap();
			// TODO 被扫的支付场景没存
			if (MapUtils.isNotEmpty(paymentSceneMap)) {
				String ncayScene = paymentSceneMap.get(PayTool.NCPAY);
				String ebankPayScene = paymentSceneMap.get(PayTool.EANK);
				paymentRequest.setBizModeCode(ncayScene);
				paymentRequest.setEbankPayScene(ebankPayScene);
			}

			try {
				paymentRequestService.savePaymentRequest(paymentRequest);
			} catch (CashierBusinessException e) {
				if (Errors.REPEAT_ORDER.getCode().equals(e.getDefineCode())) {
					paymentRequest = paymentRequestService
							.findPayRequestByTradeSysOrderId(
									orderInfo.getUniqueOrderNo(),
									orderInfo.getOrderSysNo());
				} else {
					throw e;
				}
			}
		}

		boolean isOutTime = paymentRequestService
				.isRequestExpired(paymentRequest);
		if (isOutTime) {
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		return paymentRequest;
	}

	private void valideInputParam(APIMerchantScanPayDTO request) {
		if (StringUtils.isBlank(request.getToken())
				|| StringUtils.isBlank(request.getCodeType())) {
			throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
		}
		NcCashierLoggerFactory.TAG_LOCAL.set("[API支付|pay],订单处理器订单号token="
				+ request.getToken());
	}

}
