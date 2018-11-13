/**
 * 
 */
package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.MerchantProductInfo;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.BibasicTradeOrderService;
import com.yeepay.g3.core.nccashier.gateway.service.CallFeeService;
import com.yeepay.g3.core.nccashier.gateway.service.NewMerchantConfigCenterService;
import com.yeepay.g3.core.nccashier.gateway.service.RiskControlService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.service.OrderInfoAccessService;
import com.yeepay.g3.core.nccashier.service.PaymentRequestService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.MerchantConfigRequestParam;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel.Product;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.OrderSysConfigDTO;
import com.yeepay.g3.facade.fee.front.dto.CalcuateFeeRequestDTO;
import com.yeepay.g3.facade.fee.front.dto.CalcuateFeeResultDTO;
import com.yeepay.g3.facade.fee.front.enumtype.CalFeeOwnerSourceTypeEnum;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.riskcontrol.facade.util.DoorgodCheckFactorType;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckReqDto;
import com.yeepay.riskcontrol.facade.v2.RcBlCheckRspDto;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import com.yeepay.riskcontrol.facade.util.DoorgodCheckResultType;
import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhen.tan
 *
 */
@Service
public class NewOrderHandleServiceImpl extends NcCashierBaseService implements NewOrderHandleService {

	private static final Logger logger =
			NcCashierLoggerFactory.getLogger(NewOrderHandleServiceImpl.class);
	
	@Resource
	protected PaymentRequestService paymentRequestService;

	@Resource
	protected BibasicTradeOrderService bibasicTradeOrderService;

	@Resource
	protected CallFeeService callFeeService;

	@Resource
	private RiskControlService riskControlService;
	
	@Resource
	protected OrderInfoAccessService orderInfoAccessAdapterService;

	@Resource
	protected NewMerchantConfigCenterService newMerchantConfigCenterService;
	
	/**
	 * PC上的快捷扫码是否可用
	 * 
	 * @param version
	 * @param payToolAndPayTypeMap
	 */
	private void filterNcActiveScan(CashierVersionEnum version, Map<String, List<String>> payToolAndPayTypeMap) {
		if (version == CashierVersionEnum.PC && payToolAndPayTypeMap.containsKey(PayTool.SCCANPAY.name())
				&& payToolAndPayTypeMap.get(PayTool.SCCANPAY.name()).contains(PayTypeEnum.NC_ATIVE_SCAN.name())
				&& !payToolAndPayTypeMap.containsKey(PayTool.NCPAY.name())) {
			// 开通了用户扫码的扫码一键，但是未开通PC上的一键支付，扫码一键不可用
			List<String> payTypeList = payToolAndPayTypeMap.get(PayTool.SCCANPAY.name());
			Iterator<String> it = payTypeList.iterator();
			while (it.hasNext()) {
				String payType = it.next();
				if (PayTypeEnum.NC_ATIVE_SCAN.name().equals(payType)) {
					it.remove();
				}
			}
			if (CollectionUtils.isEmpty(payTypeList)) {
				payToolAndPayTypeMap.remove(PayTool.SCCANPAY.name());
			}
		}
	}
	
	@Override
	public MerchantInNetConfigResult filterMerchantInNetConfig(OrderDetailInfoModel detailInfo) {
		
		String merchantAccountCode = detailInfo.getMerchantAccountCode();
		CashierVersionEnum version = detailInfo.getCashierVersion();
		PayTool payTool = detailInfo.getPayTool();
		DirectPayType directPayType = detailInfo.getDirectPayType();
		
		MerchantConfigRequestParam merchantConfigRequestParam = new MerchantConfigRequestParam();
		merchantConfigRequestParam.setMerchantNo(merchantAccountCode);
		merchantConfigRequestParam.setTransactionType(detailInfo.getTransactionType());
		MerchantInNetConfigModel inNetConfigs = merchantConfigService.getMerchantInNetConfig(merchantConfigRequestParam);
		logger.info("uniqueOrderNo={},产品开通信息={}", detailInfo.getUniqueOrderNo(), inNetConfigs);
		Map<String, List<String>> payToolAndPayTypeMap = new HashMap<String, List<String>>();
		Map<CashierVersionEnum, List<Product>> productMap = inNetConfigs.getProductMap();
		Map<PayTool, String> paymentSceneMap = new HashMap<PayTool, String>();
		/**
		 * 先按version过滤
		 */
		List<Product> products = productMap.get(version); // 这里才按照版本过滤
		if (CollectionUtils.isEmpty(products)) {
			logger.error("该商户为开通该收银台对应的支付产品，merchantNo:{},version{}", detailInfo.getMerchantAccountCode(), version);
			return null;
		}
		
		for (Product product : products) { // 设置payToolAndPayTypeMap和paymentSceneMap的值
			String payToolName = product.getPayTool().name();
			List<String> payTypes = product.getPayTypes();
			
			if(PayTool.EWALLETH5.name().equals(payToolName)){
				//特殊过滤： EWALLETH5 下的WECHAT_H5,需替换为 WECHAT_H5_WAP，以保持与现有内部枚举的兼容
				Iterator<String> iterator = payTypes.iterator();
				while (iterator.hasNext()){
					String payType = iterator.next();
					if("WECHAT_H5".equals(payType)){
						iterator.remove();
						payTypes.add(PayTypeEnum.WECHAT_H5_WAP.name());
						break;
					}
				}
			}else if(PayTool.ZF_FQY.name().equals(payToolName)){
				// 分期易的需要特殊判断
				logger.info("是否包含页面版分期易=" + payTypes.contains(PayTypeEnum.ZF_FQY_NORMAL.name()));
				if(!payTypes.contains(PayTypeEnum.ZF_FQY_NORMAL.name())){
					continue;
				}
			}
			payToolAndPayTypeMap.put(payToolName, payTypes);
			if (StringUtils.isNotEmpty(product.getPayScene())) {
				paymentSceneMap.put(product.getPayTool(), product.getPayScene());
			}
		}

		
		PayTool appointedPayTool = payTool;
		if (appointedPayTool != null) { 
			Set<Map.Entry<String, List<String>>> entrys = payToolAndPayTypeMap.entrySet();
			Iterator<Map.Entry<String, List<String>>> it = entrys.iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<String>> entry = it.next();
				if (!appointedPayTool.name().equals(entry.getKey())) {
					it.remove();
				}
			}
		}

		if (appointedPayTool == PayTool.NCPAY) {
			directPayType = DirectPayType.YJZF;
		}

		if(payToolAndPayTypeMap.containsKey(PayTool.BK_ZF.name()) && payToolAndPayTypeMap.containsKey(PayTool.NCPAY.name())){
			logger.info("merchantN0={}同时开通绑卡支付和一键支付，忽略一键支付", merchantAccountCode);
			payToolAndPayTypeMap.remove(PayTool.NCPAY.name()); // 当payType=23，对应的payTool是NCPAY
		}
		filterNcActiveScan(version, payToolAndPayTypeMap);
		
		if (payToolAndPayTypeMap.size() <= 0) {
			return null;
		}
		
		if (directPayType != null && directPayType != DirectPayType.NONE) {
			/**
			 * pc、wap:判断直连是否合法；
			 * pc:按直连过滤支付工具和支付类型
			 */
			if (version == CashierVersionEnum.PC) {//PC直连判断
				if (directPayType == DirectPayType.WECHAT) {//pc-直连微信
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.SCCANPAY.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.WECHAT_ATIVE_SCAN.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.SCCANPAY.name(), payTypes, DirectPayType.WECHAT, payToolAndPayTypeMap);
					}
				} else if (directPayType == DirectPayType.ALIPAY) {//pc-直连支付宝
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.SCCANPAY.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.ALIPAY.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.SCCANPAY.name(), payTypes, DirectPayType.ALIPAY, payToolAndPayTypeMap);
					}
				} else if (directPayType == DirectPayType.UPOP) {//pc-直连银联
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.SCCANPAY.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.UPOP_ATIVE_SCAN.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.SCCANPAY.name(), payTypes, DirectPayType.UPOP, payToolAndPayTypeMap);
					}
				} else if (directPayType == DirectPayType.JD) {//pc-直连京东
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.SCCANPAY.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.JD_ATIVE_SCAN.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.SCCANPAY.name(), payTypes, DirectPayType.JD, payToolAndPayTypeMap);
					}
				} else if (directPayType == DirectPayType.QQ) {//pc-直连QQ
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.SCCANPAY.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.QQ_ATIVE_SCAN.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.SCCANPAY.name(), payTypes, DirectPayType.QQ, payToolAndPayTypeMap);
					}
				} else if(directPayType == DirectPayType.NC_ATIVE_SCAN){ // pc-扫码一键直连
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.SCCANPAY.name());
					if(CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.NC_ATIVE_SCAN.name())){
						return null;
					}
					filterPayToolAndTypeMapByDirectPay(PayTool.SCCANPAY.name(), payTypes, DirectPayType.NC_ATIVE_SCAN, payToolAndPayTypeMap);
				} else if (directPayType == DirectPayType.ZHZF) {//pc-直连账户支付
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.ZF_ZHZF.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.ZF_ZHZF.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.ZF_ZHZF.name(), payTypes, DirectPayType.ZHZF, payToolAndPayTypeMap);
					}
				} else if (directPayType == DirectPayType.YJZF) {//pc-直连一键
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.NCPAY.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.BANK_PAY_WAP.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.NCPAY.name(), payTypes, DirectPayType.YJZF, payToolAndPayTypeMap);
					}
				} else if (directPayType == DirectPayType.CFL) {//pc-直连分期
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.CFL.name());
					if (CollectionUtils.isEmpty(payTypes)) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.CFL.name(), payTypes, DirectPayType.CFL, payToolAndPayTypeMap);
					}
				} else if (DirectPayType.isDirectEbank(directPayType.name())) {//pc-网银直连
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.EANK.name());
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.NET_BANK.name())) {
						return null;
					} else {
						filterPayToolAndTypeMapByDirectPay(PayTool.EANK.name(), payTypes, directPayType, payToolAndPayTypeMap);
					}
				}

			} else if (version == CashierVersionEnum.WAP) {//WAP直连判断
				if (directPayType == DirectPayType.WECHAT) {
					List<String> payTypes = new ArrayList<String>();
					if(CollectionUtils.isNotEmpty(payToolAndPayTypeMap.get(PayTool.EWALLET.name()))){
						payTypes.addAll(payToolAndPayTypeMap.get(PayTool.EWALLET.name()));
					}
					if(CollectionUtils.isNotEmpty(payToolAndPayTypeMap.get(PayTool.EWALLETH5.name()))){
						payTypes.addAll(payToolAndPayTypeMap.get(PayTool.EWALLETH5.name()));
					}
					//实际EWALLET下应该不会有WECHAT_H5_WAP
					if (CollectionUtils.isEmpty(payTypes) ||
							(!payTypes.contains(PayTypeEnum.WECHAT_H5_WAP.name()) && !payTypes.contains(PayTypeEnum.WECHAT_H5_LOW.name()))) {
						return null;
					}
				} else if (directPayType == DirectPayType.ALIPAY) {
					List<String> payTypesEwallet = payToolAndPayTypeMap.get(PayTool.EWALLET.name());
					List<String> payTypesEwalleth5 = payToolAndPayTypeMap.get(PayTool.EWALLETH5.name());
					if ((payTypesEwallet == null || !payTypesEwallet.contains(PayTypeEnum.ALIPAY.name())) // 钱包下没有支付宝
							&& (payTypesEwalleth5 == null || (!payTypesEwalleth5.contains(PayTypeEnum.ALIPAY_H5.name())
									&& !payTypesEwalleth5.contains(PayTypeEnum.ALIPAY_H5_STANDARD.name())))) {
						// EWALLET及EWALLETH5下均无payType ，或 ，EWALLET下无ALIPAY 且
						// EWALLETH5下无ALIPAY_H5且无ALIPAY_H5_STANDARD
						return null;
					}
				} else if (directPayType == DirectPayType.YJZF) {
					if (!payToolAndPayTypeMap.containsKey(PayTool.NCPAY.name())) {
						return null;
					}
				} else if (directPayType == DirectPayType.BK_ZF) {
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.BK_ZF.name());
					if(CollectionUtils.isEmpty(payTypes)){
						return null;
					}
				} else if (directPayType == DirectPayType.YSQ){//预授权修改
					List<String> payTypes = payToolAndPayTypeMap.get(PayTool.YSQ.name());
					if(CollectionUtils.isEmpty(payTypes)){
						return null;
					}
				}
				else if (directPayType == DirectPayType.ZHZF) {
					if (!payToolAndPayTypeMap.containsKey(PayTool.ZF_ZHZF.name())) {
						return null;
					}
				}else if (directPayType == DirectPayType.JD) {
					List<String> payTypes = new ArrayList<String>();
					payTypes.addAll(payToolAndPayTypeMap.get(PayTool.EWALLETH5.name()));
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.JD_H5.name())) {
						return null;
					}
				}else if (directPayType == DirectPayType.DBFQ) {
					List<String> payTypes = new ArrayList<String>();
					payTypes.addAll(payToolAndPayTypeMap.get(PayTool.DBFQ.name()));
					if (CollectionUtils.isEmpty(payTypes) || !payTypes.contains(PayTypeEnum.DBFQ_TL.name())) {
						return null;
					}
				}
			}
		}

		List<String> allPayTypes = new ArrayList<String>();
		for (List<String> values : payToolAndPayTypeMap.values()) {
			allPayTypes.addAll(values);
		}

		MerchantInNetConfigResult configResult = new MerchantInNetConfigResult();
		configResult.setMerchantAccountCode(merchantAccountCode);
		configResult.setVersion(version);
		configResult.setPayToolAndPayTypeMap(payToolAndPayTypeMap);
		configResult.setDirectPayType(directPayType);
		configResult.setPayTypes(allPayTypes);
		configResult.setPaymentSceneMap(paymentSceneMap);
		configResult.setMcc(inNetConfigs.getMcc());
		configResult.setFeeType(inNetConfigs.getFeeTypeEnum());
		configResult.setMerchantAttributes(inNetConfigs.getMerchantAttributes());
		return configResult;

	}
	
	public String suppleConfigCenterInfoToPaymentRequest(PaymentRequest paymentRequest,OrderDetailInfoModel orderInfo){
		if("BAC_LOAD".equals(paymentRequest.getTradeSysNo())){ // 查询配置中心新接口	目前根据充值系统来判断
			List<MerchantProductInfo> list = newMerchantConfigCenterService.getAllMerchantInNetConfig(paymentRequest.getMerchantNo(),paymentRequest.getTradeSysNo(),paymentRequest.getOrderOrderId());
			if(CollectionUtils.isEmpty(list))
				throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
			return JSONUtils.toJsonString(list);
		}
		else{	//查询配置中心老接口
			MerchantInNetConfigResult configResult = filterMerchantInNetConfig(orderInfo);
			if(configResult == null){
				throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
			}
			JSONObject configJson = new JSONObject();
			if(configResult.getDirectPayType() != null){
				configJson.put("DirectPayType", configResult.getDirectPayType().name());
			}

			List<String> payTypes = new ArrayList<String>();
			for(String payType :configResult.getPayTypes()){
				try {
					PayTypeEnum payTypeEnum = PayTypeEnum.valueOf(payType);
					payTypes.add(payTypeEnum.value() + "");
				}catch (Throwable e){
					logger.error("新增加的支付方式payType="+payType+"无法转换成现有的枚举类型");
				}

			}
			configJson.put("PayType", payTypes.toString());
			configJson.put("payTool", configResult.getPayToolAndPayTypeMap().keySet());
			paymentRequest.setRemark(configJson.toJSONString());
			paymentRequest.setIndustryCatalog(configResult.getMcc());
			Map<PayTool,String> paymentSceneMap = configResult.getPaymentSceneMap();
			if(MapUtils.isNotEmpty(paymentSceneMap)){

				String ncayScene = paymentSceneMap.get(PayTool.NCPAY);
				String ebankPayScene = paymentSceneMap.get(PayTool.EANK);
				String bindPayScene = paymentSceneMap.get(PayTool.BK_ZF);
				//如果开通了预授权，且传了预授权直连参数，则记录预授权的支付场景
				String ysqPayScene = paymentSceneMap.get(PayTool.YSQ);
				if(StringUtils.isNotBlank(ysqPayScene) && (configResult.getDirectPayType() == DirectPayType.YSQ)){
					paymentRequest.setBizModeCode(ysqPayScene);
				}else if(StringUtils.isNotBlank(bindPayScene)){
					paymentRequest.setBizModeCode(bindPayScene);
				}else if(StringUtils.isNotBlank(ncayScene)){
					paymentRequest.setBizModeCode(ncayScene);
				}
				paymentRequest.setEbankPayScene(ebankPayScene);
			}
			paymentRequest.setFeeType(configResult.getFeeType().name());
			//将钱包支付h5的优先级属性，保存在json中，以便后续保存在user_request_info表，不存到payment_request表(字段空间不足)
			Map<String, String> merchantAttributes = configResult.getMerchantAttributes();
			if(MapUtils.isNotEmpty(merchantAttributes)){
				configJson.putAll(merchantAttributes);
			}
			return configJson.toJSONString();
		}
	}

	public BigDecimal queryUserFee(PaymentRequest paymentRequest,String callFeeItem){
		if(FeeTypeEnum.USER_FEE.name().equals(paymentRequest.getFeeType())){
			CalcuateFeeRequestDTO calFeeDTO = new CalcuateFeeRequestDTO();
			calFeeDTO.setCustomerNumber(paymentRequest.getMerchantNo());
			calFeeDTO.setCustomerName(paymentRequest.getMerchantName());
			calFeeDTO.setOrderNumber(paymentRequest.getMerchantOrderId());
			calFeeDTO.setAmount(paymentRequest.getOrderAmount());
			calFeeDTO.setCalFeeItemStr(callFeeItem);
			calFeeDTO.setSource(CalFeeOwnerSourceTypeEnum.MERCHANT);
			calFeeDTO.setFlowNumber(paymentRequest.getTradeSysOrderId());
			calFeeDTO.setTransferTime(paymentRequest.getPayTime());
			calFeeDTO.setPayWay(Constant.PAY_WAY_TO_CALL_FEE);
			calFeeDTO.setBankInterNumber(Constant.BANK_INTER_NUMBER_TO_CALL_FEE);
			calFeeDTO.setPayProduct(Constant.PAY_PRODUCT_TO_CALL_FEE);
			CalcuateFeeResultDTO calFeeResultDTO = callFeeService.getUserFee(calFeeDTO);

			return calFeeResultDTO.getPayerFee();
		}else {
			return null;
		}
	}

	@Override
	public void orderReferCheck(OrderSysConfigDTO orderSysConfigDTO, OrderDetailInfoModel orderDetailInfoModel) {
		RcBlCheckReqDto request = new RcBlCheckReqDto();
		if(StringUtils.isBlank(orderSysConfigDTO.getReferFromOrder()) || Constant.ORDER_REFFER_NONE.equals(orderSysConfigDTO.getReferFromOrder())){
			return;
		}else if(Constant.ORDER_REFFER_NOT_NONE_AND_WHITER.equals(orderSysConfigDTO.getReferFromOrder())){
			// 需要走白名单
			request.setIsWhiteCheck("1");
		}else{
			// 不需要走白名单
			request.setIsWhiteCheck("0");
		}
		
		request.setBizOrder(orderDetailInfoModel.getUniqueOrderNo());
		request.setSequenceId(orderDetailInfoModel.getMerchantOrderId());
		Map<DoorgodCheckFactorType, Object> checkParams = new HashMap<DoorgodCheckFactorType, Object>();
		checkParams.put(DoorgodCheckFactorType.refer, orderDetailInfoModel.getReffer());
		checkParams.put(DoorgodCheckFactorType.merchantNo, orderDetailInfoModel.getMerchantAccountCode());
		request.setCheckParams(checkParams);
		RcBlCheckRspDto response=null;
		try {
			response = riskControlService.check(request);
		}catch (Throwable throwable){
			// 一般只有网络异常或者返回值isDealResult为false才会走到这里
			logger.error("调用风控系统异常",throwable);
		}
		if (response != null) {
			if(DoorgodCheckResultType.RISK_TRADE_BLACK == response.getResultCode() || DoorgodCheckResultType.RISK_TRADE_REFER == response.getResultCode()){
				throw new CashierBusinessException(Errors.ORDER_RISK_ERROR.getCode(),Errors.ORDER_RISK_ERROR.getMsg());
			}
		}
	}

	/**
	 * 直连支付时，根据直连支付方式，过滤payToolAndPayTypeMap
	 * @param payTool 匹配直连方式的payTool
	 * @param payTypes payTool对应的payTypes
	 * @param directPayType 直连支付类型枚举
	 * @param payToolAndPayTypeMap 过滤前的payToolAndPayTypeMap
	 */
	private void filterPayToolAndTypeMapByDirectPay(String payTool, List<String> payTypes, DirectPayType directPayType, Map<String, List<String>> payToolAndPayTypeMap) {
		try{
			if(StringUtils.isEmpty(payTool) || CollectionUtils.isEmpty(payTypes) || directPayType==null){
				return;
			}
			if(PayTool.SCCANPAY.name().equals(payTool)){
				//扫码支付，单独处理，需按直连方式过滤payTypes
				switch (directPayType){
					case WECHAT: {
						payTypes.clear();
						payTypes.add(PayTypeEnum.WECHAT_ATIVE_SCAN.name());
						break;
					}
					case ALIPAY: {
						payTypes.clear();
						payTypes.add(PayTypeEnum.ALIPAY.name());
						break;
					}
					case UPOP: {
						payTypes.clear();
						payTypes.add(PayTypeEnum.UPOP_ATIVE_SCAN.name());
						break;
					}
					case JD: {
						payTypes.clear();
						payTypes.add(PayTypeEnum.JD_ATIVE_SCAN.name());
						break;
					}
					case QQ: {
						payTypes.clear();
						payTypes.add(PayTypeEnum.QQ_ATIVE_SCAN.name());
						break;
					}
					case NC_ATIVE_SCAN: {
						payTypes.clear();
						payTypes.add(PayTypeEnum.NC_ATIVE_SCAN.name());
						break;
					}
					default:
						break;
				}
			}
			//清空payToolAndPayTypeMap，单独存入当前payTool - payTypes
			payToolAndPayTypeMap.clear();
			payToolAndPayTypeMap.put(payTool,payTypes);
		}catch (Throwable t){
			logger.warn("直连支付时，根据直连支付方式，过滤payToolAndPayTypeMap出现异常:",t);
			return;
		}
	}
	

	@Override
	public OrderDetailInfoModel queryOrder(String requestMerchantNo, String token, String bizType, TransactionTypeEnum unsupportTransactionType) {
		OrderSysConfigDTO orderSysConfigDTO = CommonUtil.getBizSysCnfigParams(bizType);
		OrderDetailInfoModel orderInfo = orderInfoAccessAdapterService.getOrderDetailInfoModel(token,
				orderSysConfigDTO);
		if (!orderInfo.getSignedMerchantAccountCode().equals(requestMerchantNo)) {
			throw CommonUtil.handleException(Errors.REQUEST_MERCHANT_NO_ERROR);
		}
		orderReferCheck(orderSysConfigDTO, orderInfo);
		if(unsupportTransactionType!=null && unsupportTransactionType==orderInfo.getTransactionType()){
			throw new CashierBusinessException(TransactionTypeEnum.getErrors(unsupportTransactionType));
		}
		return orderInfo;
	}
}
