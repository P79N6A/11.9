package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.*;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.PayRequestStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

import org.apache.commons.collections.MapUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("paymentRequestService")
public class PaymentRequestServiceImpl extends NcCashierBaseService
		implements PaymentRequestService {
	private static Logger logger = NcCashierLoggerFactory.getLogger(PaymentRequestServiceImpl.class);

	@Override
	public long savePaymentRequest(PaymentRequest paymentRequest) throws CashierBusinessException {
		long id = 0;
		try {
			// 加密敏感信息
			aesEncrypt(paymentRequest);
			id = paymentRequestDao.savePaymentRequest(paymentRequest);
		} catch (DuplicateKeyException e) {
			logger.error("违反唯一索引约束，重复下单", e);
			throw CommonUtil.handleException(Errors.REPEAT_ORDER);
		} catch (Exception e) {
			logger.error("保存支付请求表失败", e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} finally {
			// 解密敏感信息
			aesDecrypt(paymentRequest);
		}
		return id;
	}


	@Override
	public PaymentRequest findPayRequestById(long requestId) throws CashierBusinessException {
		PaymentRequest request = paymentRequestDao.findPayRequestById(requestId);
		// 解密敏感信息
		aesDecrypt(request);
		return request;
	}

	@Override
	public void updatePayRequestState(PaymentRequest paymentRequest) {
		try {
			// 加密敏感信息
			aesEncrypt(paymentRequest);
			int effortRow = paymentRequestDao.updatePayRequest(paymentRequest);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("更新支付请求失败", e);
		} finally {
			// 解密敏感信息
			aesDecrypt(paymentRequest);
		}
	}

	@Override
	public PaymentRequest findPayRequestByOrderOrderId(String orderOrderId, String orderSysNo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderOrderId", orderOrderId);
		map.put("orderSysNo", orderSysNo);
		PaymentRequest request = paymentRequestDao.findPayRequestByOrderOrderId(map);
		aesDecrypt(request);
		return request;
	}

	@Override
	public PaymentRequest findPayRequestByTradeSysOrderId(String tradeSysOrderId,
			String tradeSysNo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tradeSysOrderId", tradeSysOrderId);
		map.put("tradeSysNo", tradeSysNo);
		PaymentRequest request = paymentRequestDao.findPayRequestByTradeSysOrderId(map);
		aesDecrypt(request);
		return request;
	}
	
	@Override
	public List<PaymentRequest> findRequestListByMerchantNoAndMerchantOrderId(String merchantNo,String merchantOrderId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchantOrderId", merchantOrderId);
		map.put("merchantNo", merchantNo);
		List<PaymentRequest> requestList = paymentRequestDao.findRequestListByMerchantNoAndMerchantOrderId(map);
		if(CollectionUtils.isNotEmpty(requestList)){
			for(PaymentRequest request : requestList){
				aesDecrypt(request);
			}
		}else{
			logger.warn("merchantNo={},merchantOrderId={}获取不到支付请求", merchantNo, merchantOrderId);
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		return requestList;
	}

	@Override
	public int updatePayRequestExtendInfoById(PaymentRequest paymentRequest) {
		try {
			int effortRow = paymentRequestDao.updatePayRequestExtendInfoById(paymentRequest);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
			return effortRow;
		} catch (Exception e) {
			logger.error("更新请求订单扩展信息失败 tosate=" + paymentRequest.getState(), e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}

	@Override
	public int updateUserRequestInfo(PaymentRequest paymentRequest) {
		try {
			int effortRow = paymentRequestDao.updateUserRequestInfo(paymentRequest);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
			return effortRow;
		} catch (Exception e) {
			logger.error("更新用户请求订单信息失败 requestid=" + paymentRequest.getId(), e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}


	@Override
	public PaymentRequest findPaymentRequestByRequestId(long requestId) {
		PaymentRequest paymentRequest = this.findPayRequestById(requestId);
		if (paymentRequest == null) {
			throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
		}
		if (isRequestExpired(paymentRequest)) {
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		if (PayRequestStatusEnum.SUCCESS.getValue().equals(paymentRequest.getState())
				|| PayRequestStatusEnum.FAILED.getValue().equals(paymentRequest.getState())) {
			throw CommonUtil.handleException(Errors.THRANS_FINISHED);
		}
		return paymentRequest;
	}

	@Override
	public boolean isRequestExpired(PaymentRequest paymentRequest) {
		long validateTime = getExpiredTime(paymentRequest);
		long currentTime = new Date().getTime();
		return validateTime < currentTime;
	}
	
	@Override
	public long getExpiredTime(PaymentRequest paymentRequest){
		String orderExpDateType = StringUtils.isBlank(paymentRequest.getOrderExpDateType())?"MINUTE":paymentRequest.getOrderExpDateType();
		// 过期时间全部转化为分钟
		long validateMin = 0;
		if("DAY".equals(orderExpDateType)){
			validateMin = 1000l*paymentRequest.getOrderExpDate()*24*60*60;
		}else if("HOUR".equals(orderExpDateType)){
			validateMin = 1000l*paymentRequest.getOrderExpDate()*60*60;
		}else if("MINUTE".equals(orderExpDateType)){
			validateMin = 1000l*paymentRequest.getOrderExpDate()*60;
		}else if("SECOND".equals(orderExpDateType)){
			validateMin = 1000l*paymentRequest.getOrderExpDate();
		}else {
			validateMin = 1000l*paymentRequest.getOrderExpDate()*60;
		}
		//默认一天的分钟
		if (paymentRequest.getOrderExpDate() <= 0) {
			validateMin = 24*60*60*1000l;
		}
		long validateTime = paymentRequest.getOrderTimeNotNull().getTime() + validateMin;
		return validateTime;
	}
	
	@Override
	public boolean openPayType(PaymentRequest paymentRequest, String token, String payTool){
		String productOpenInfo = "";
		if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(paymentRequest.getMerchantConfigFrom())
				&& Constant.MERCHANT_CONFIG_FROM_USER_REQUEST.equals(paymentRequest.getMerchantConfigFrom())){
			UserRequestInfo userRequestInfo = userRequestInfoDao.getUserRequestInfoBytoken(token);
			if(userRequestInfo != null){
				productOpenInfo = userRequestInfo.getMerchantConfigInfo();
			}
		}else{
			productOpenInfo = paymentRequest.getRemark();
		}
		
		if (StringUtils.isNotBlank(productOpenInfo)) {
			try {
				JSONObject json = JSONObject.parseObject(productOpenInfo);
				String payTools = json.getString("payTool");
				if(StringUtils.isNotBlank(payTools) && payTools.contains(payTool)){
					return true;
				}
			} catch (JSONException e) {
				logger.warn("支付来源JSON转化异常，requestId=" + paymentRequest.getId(), e);
			}
		}
		return false;
	}

	@Override
	public void updateRequestBaseOnStatus(PaymentRequest paymentRequest, List<String> statusList) {
		try {
			aesEncrypt(paymentRequest);
			int effortRow = paymentRequestDao.updateRequestBaseOnStatus(paymentRequest, statusList);
			if (effortRow != 1) {
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("更新请求订单状态失败 tosate=" + paymentRequest.getState(), e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		} finally {
			// 解密敏感信息
			aesDecrypt(paymentRequest);
		}
	}


	private void aesDecrypt(PaymentRequest paymentRequest) {
		if (null != paymentRequest) {
			if (StringUtils.isNotBlank(paymentRequest.getCardNo())) {
				paymentRequest.setCardNo(AESUtil.aesDecrypt(paymentRequest.getCardNo()));
			}
			if (StringUtils.isNotBlank(paymentRequest.getIdCard())) {
				paymentRequest.setIdCard(AESUtil.aesDecrypt(paymentRequest.getIdCard()));
			}
			if (StringUtils.isNotBlank(paymentRequest.getOwner())) {
				paymentRequest.setOwner(AESUtil.aesDecrypt(paymentRequest.getOwner()));
			}
			if (StringUtils.isNotBlank(paymentRequest.getPhoneNo())) {
				paymentRequest.setPhoneNo(AESUtil.aesDecrypt(paymentRequest.getPhoneNo()));
			}
		}
	}
	
	private void aesEncrypt(PaymentRequest paymentRequest) {
		if (null != paymentRequest) {
			if (StringUtils.isNotBlank(paymentRequest.getCardNo())) {
				paymentRequest.setCardNo(AESUtil.aesEncrypt(paymentRequest.getCardNo()));
			}
			if (StringUtils.isNotBlank(paymentRequest.getIdCard())) {
				paymentRequest.setIdCard(AESUtil.aesEncrypt(paymentRequest.getIdCard()));
			}
			if (StringUtils.isNotBlank(paymentRequest.getOwner())) {
				paymentRequest.setOwner(AESUtil.aesEncrypt(paymentRequest.getOwner()));
			}
			if (StringUtils.isNotBlank(paymentRequest.getPhoneNo())) {
				paymentRequest.setPhoneNo(AESUtil.aesEncrypt(paymentRequest.getPhoneNo()));
			}
		}
	}

	/**
	 * 重复下单，更新paymentRequest信息，主要更新商户配置信息
	 * 
	 * @param paymentRequest
	 * @param configResult
	 */
	private void updatePaymentRequest(PaymentRequest paymentRequest, MerchantInNetConfigResult configResult) {
		PaymentRequest newPaymentRequest = new PaymentRequest();
		newPaymentRequest.setId(paymentRequest.getId());
		if (configResult != null) {
			newPaymentRequest.setRemark(configResult.buildRemark());
		}
		newPaymentRequest.setExtendInfo(paymentRequest.getExtendInfo());
		newPaymentRequest.setUpdateTime(new Date());
		newPaymentRequest.setVersion(paymentRequest.getVersion());
		newPaymentRequest.setCashierVersion(paymentRequest.getCashierVersion());
		newPaymentRequest.setCardType(paymentRequest.getCardType());
		// 以第一次为准
		newPaymentRequest.setIdentityId(paymentRequest.getIdentityId());
		newPaymentRequest.setIdentityType(paymentRequest.getIdentityType());
		newPaymentRequest.setBizModeCode(paymentRequest.getBizModeCode());
		newPaymentRequest.setEbankPayScene(paymentRequest.getEbankPayScene());
		updateUserRequestInfo(newPaymentRequest);
	}

	@Override
	public PaymentRequest createRequestWhenUnexsit(OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig, ProductLevel productLevel, PaymentRequestExtInfo paymentRequestExtInfo) {
		PaymentRequest paymentRequest = findPayRequestByTradeSysOrder(orderInfo.getUniqueOrderNo(),
				orderInfo.getOrderSysNo(), true);
		if (paymentRequest != null) {
			updatePaymentRequest(paymentRequest, merchantInNetConfig);
			return paymentRequest;
		}
		CashierUserInfo user = paymentRequestExtInfo == null ? null : paymentRequestExtInfo.getCashierUser();
		orderInfoComplate(orderInfo, user, productLevel, paymentRequestExtInfo.getUserIp(), paymentRequestExtInfo.getAppId());
		paymentRequest = orderInfo.toPaymentRequest();
		supplyPaymentRequest(merchantInNetConfig, paymentRequest, paymentRequestExtInfo.getBindId(), paymentRequestExtInfo.getPayScene(),paymentRequestExtInfo.getMcc(), productLevel);
		try {
			long requestId = savePaymentRequest(paymentRequest);
			paymentRequest.setId(requestId);
		} catch (CashierBusinessException e) {
			if (Errors.REPEAT_ORDER.getCode().equals(e.getDefineCode())) {
				paymentRequest = findPayRequestByTradeSysOrderId(orderInfo.getUniqueOrderNo(),
						orderInfo.getOrderSysNo());
			} else {
				logger.error("商编=" + orderInfo.getMerchantAccountCode() + ", 商户订单号=" + orderInfo.getMerchantOrderId()
						+ "保存paymentRequest失败，e=", e);
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		}
		if (isRequestExpired(paymentRequest)) {
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		return paymentRequest;
	}

	
	
	@Override
	public PaymentRequest findNonNullPayRequestByTradeSysOrder(String tradeSysOrderId, String tradeSysNo, boolean checkExpiredTime) {
		PaymentRequest paymentRequest = findPayRequestByTradeSysOrder(tradeSysOrderId, tradeSysNo, checkExpiredTime);
		if (paymentRequest == null) {
			throw new CashierBusinessException(Errors.PAY_REQUEST_NULL);
		}
		return paymentRequest;
	}
	

	@Override
	public PaymentRequest findPayRequestByTradeSysOrder(String tradeSysOrderId, String tradeSysNo, boolean checkExpiredTime) {
		PaymentRequest paymentRequest = findPayRequestByTradeSysOrderId(tradeSysOrderId, tradeSysNo);
		if (paymentRequest == null) {
			return null;
		}
		if (checkExpiredTime && isRequestExpired(paymentRequest)) {
			throw CommonUtil.handleException(Errors.THRANS_EXP_DATE);
		}
		return paymentRequest;
	}

	/**
	 * @title 补充订单信息
	 * @description include appId、openId、remark、mcc、merchantConfigFrom
	 * @param merchantInNetConfig
	 * @param paymentRequest
	 * @param bindId
	 * @param payScene 一键支付API时可选传入
	 * @param mcc    一键支付API时可选传入
	 * @param productLevel
	 */
	private void supplyPaymentRequest(MerchantInNetConfigResult merchantInNetConfig, PaymentRequest paymentRequest, String bindId, String payScene, String mcc, ProductLevel productLevel) {
		paymentRequest.setMerchantConfigFrom(Constant.MERCHANT_CONFIG_FROM_PAYMENT_REQUEST);
		if (merchantInNetConfig != null) {
			paymentRequest.setIndustryCatalog(merchantInNetConfig.getMcc());
			paymentRequest.setRemark(merchantInNetConfig.buildRemark());
			//从配置中心获取支付场景。目前处理了一键支付、绑卡支付的支付场景。
			Map<PayTool, String> paymentSceneMap = merchantInNetConfig.getPaymentSceneMap();
			if(MapUtils.isNotEmpty(paymentSceneMap)){
				if(PayTool.NCPAY.equals(productLevel.getPayTool())){
					String ncayScene = paymentSceneMap.get(PayTool.NCPAY);
					paymentRequest.setBizModeCode(ncayScene);
				}else if(PayTool.BK_ZF.equals(productLevel.getPayTool())){
					String ncayScene = paymentSceneMap.get(PayTool.BK_ZF);
					paymentRequest.setBizModeCode(ncayScene);
				}else if (PayTool.YSQ.equals(productLevel.getPayTool())){
					String ncayScene = paymentSceneMap.get(PayTool.YSQ);
					paymentRequest.setBizModeCode(ncayScene);
				}
			}
		}
		//一键支付API-二次支付下单、绑卡支付API-二次支付下单，保存绑卡id
		paymentRequest.setBindId(bindId);
		if(StringUtils.isNotBlank(payScene)){
			//一键支付API，如果下单时透传了支付场景，则使用透传参数
			paymentRequest.setBizModeCode(payScene);
		}
		if(StringUtils.isNotBlank(mcc)){
			//一键支付API，如果下单时透传了mcc，则使用透传参数
			paymentRequest.setIndustryCatalog(mcc);
		}
	}

	/**
	 * @title 补充orderInfo信息
	 * @description 包括外部用户信息、收银台版本（API、WAP、PC）、userIp和appIp的补充
	 * @param orderInfo
	 * @param user
	 * @param productLevel
	 * @param userIp
	 * @param appId
	 */
	private void orderInfoComplate(OrderDetailInfoModel orderInfo, CashierUserInfo user,
			ProductLevel productLevel, String userIp, String appId) {
		if (user != null) {
			user.supplyOrderInfo(orderInfo);
		}
		productLevel.supplyOrderInfo(orderInfo);
		orderInfo.setUserIp(userIp);
		if(StringUtils.isNotBlank(appId)){
			orderInfo.setAppId(appId);
		}
	}

}
