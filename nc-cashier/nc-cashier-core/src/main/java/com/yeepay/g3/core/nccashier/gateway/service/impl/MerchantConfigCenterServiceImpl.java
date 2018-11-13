/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantConfigCenterService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CashierPayProductUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.EwalletAlipayOpenInfo;
import com.yeepay.g3.core.nccashier.vo.EwalletH5OpenInfo;
import com.yeepay.g3.core.nccashier.vo.EwalletH5WechatOpenInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantConfigRequestParam;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel.Product;
import com.yeepay.g3.facade.configcenter.constants.EnumAttributeType;
import com.yeepay.g3.facade.configcenter.constants.EnumMerchantAttributeValue;
import com.yeepay.g3.facade.configcenter.constants.EnumSubAttributeType;
import com.yeepay.g3.facade.configcenter.dto.MerchantPayBankConfigDto;
import com.yeepay.g3.facade.configcenter.dto.MerchantPayConfigDto;
import com.yeepay.g3.facade.configcenter.dto.MerchantPayConfigDto.MerchantAttributeValueDto;
import com.yeepay.g3.facade.configcenter.dto.MerchantPayConfigDto.PayProductDto;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitRequestDto;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitResponseDto;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.FeeTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhen.tan
 *
 */
@Service("merchantConfigService")
public class MerchantConfigCenterServiceImpl extends NcCashierBaseService implements MerchantConfigCenterService{
	
	private static Logger logger = LoggerFactory.getLogger(MerchantConfigCenterServiceImpl.class);

	@Override
	public MerchantPayBankConfigDto getBankInfo(String merchantNo){
		MerchantPayBankConfigDto payBankConfig = null;
		try{
			payBankConfig= merchantConfigFacade.queryMerchantPayBankConfig(merchantNo);
		}catch(Throwable t){
			logger.error("调用配置中心获取分期银行配置信息异常", t);
		}
		if(payBankConfig==null){
			throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
		}
		return payBankConfig;
	}
	
	
	/**
	 * 获取商户开通的产品信息
	 * 
	 * @param merchantConfigRequestParam
	 * @return
	 */
	@Override
	public MerchantInNetConfigModel getMerchantInNetConfig(MerchantConfigRequestParam merchantConfigRequestParam) {
		if (TransactionTypeEnum.MEMBER_RECHARGE_2C.equals(merchantConfigRequestParam.getTransactionType())) {
			return getPayConfigByAttribute(merchantConfigRequestParam);
		} else if (TransactionTypeEnum.SALE.equals(merchantConfigRequestParam.getTransactionType())
				|| TransactionTypeEnum.PREAUTH.equals(merchantConfigRequestParam.getTransactionType())) {
			return getMerchantInNetConfig(merchantConfigRequestParam.getMerchantNo());
		}
		throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
	}
	
	/**
	 * 根据属性获取支付产品信息，该版本支持个人会员充值，由于配置中心来不及给出最新模型的接口，因此该接口属于临时方案，
	 * 不支持一键支付和绑卡支付支付场景的获取，后面会下掉，换成新方案
	 * 
	 * @param merchantConfigRequestParam
	 * @return
	 */
	private MerchantInNetConfigModel getPayConfigByAttribute(MerchantConfigRequestParam merchantConfigRequestParam) {
		// 开通的所有支付产品，目前只有查询个人会员充值开通的产品才会走到这里，消费场景没法这么走，以后别的场景（如企业会员充值）能不能走这，需要确认
		MerchantPayConfigDto merchantPayConfig = queryPayConfig(merchantConfigRequestParam.getMerchantNo());
		Map<String, Map<String, List<MerchantAttributeValueDto>>> hierarchicalAttributeMap = merchantPayConfig
				.getHierarchicalAttributeMap();
		if (MapUtils.isEmpty(hierarchicalAttributeMap)) {
			throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}
		Map<String, List<MerchantAttributeValueDto>> merchantAttributeValueMap = hierarchicalAttributeMap
				.get(merchantConfigRequestParam.getTransactionType().getFirstAttribute());
		if (MapUtils.isEmpty(merchantAttributeValueMap)) {
			throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}
		List<MerchantAttributeValueDto> merchantAttributeValueDtoList = merchantAttributeValueMap
				.get(merchantConfigRequestParam.getTransactionType().getSecondAttribute());
		if (CollectionUtils.isEmpty(merchantAttributeValueDtoList)) {
			throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}

		List<String> payTypeList = new ArrayList<String>(); //所有开通的三级支付银行
		for (MerchantAttributeValueDto merchantAttributeValueDto : merchantAttributeValueDtoList) {
			if (merchantConfigRequestParam.getTransactionType().getThirdAttribute()
					.equals(merchantAttributeValueDto.getType())) {
				String curPayType = CommonUtil.payTypeMap.get(merchantAttributeValueDto.getValue());
				if(StringUtils.isNotBlank(curPayType)){
					payTypeList.add(curPayType);
				}
			}
		}
		if (CollectionUtils.isEmpty(payTypeList)) {
			throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}

		// 构造三级支付方式
		Map<CashierVersionEnum, List<Product>> productList = CashierPayProductUtil
				.constructPayProductHierarchy(payTypeList);
		MerchantInNetConfigModel configModel = new MerchantInNetConfigModel();
		configModel.setFeeTypeEnum(FeeTypeEnum.USER_FEE.getValue().equals(merchantPayConfig.getFeeType())
				? FeeTypeEnum.USER_FEE : FeeTypeEnum.MERCHANT_FEE);
		configModel.setSaleProductCode(merchantPayConfig.getMarketingCode());
		configModel.setProductMap(productList);
		configModel.setMcc(merchantPayConfig.getMccCode());
		configModel.setMerchantAccountCode(merchantConfigRequestParam.getMerchantNo());
		return configModel;
	}
	
	/**
	 * 根据商编获取商户产品开通配置
	 * 
	 * @param merchantAccountCode
	 * @return
	 */
	private MerchantPayConfigDto queryPayConfig(String merchantAccountCode) {
		MerchantPayConfigDto config = merchantConfigFacade.queryPayConfig(merchantAccountCode);
		if (config == null) {
			throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}
		return config;
	}
	
	/**
	 * 根据商编获取产品配置信息
	 * 
	 * @param merchantAccountCode
	 * @return
	 */
	private MerchantInNetConfigModel getMerchantInNetConfig(String merchantAccountCode) {
//		return getMock(merchantAccountCode);
		MerchantPayConfigDto config = queryPayConfig(merchantAccountCode);
		Map<String, List<PayProductDto>> payProductMap = config.getPayProductMap();
		if (MapUtils.isEmpty(payProductMap)) {
			throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}
		Map<CashierVersionEnum, List<Product>> productMap = new HashMap<CashierVersionEnum, List<Product>>();
		Map<String,String> merchantAttributes = new HashMap<String, String>();
		EwalletAlipayOpenInfo alipayPriority = new EwalletAlipayOpenInfo();
		EwalletH5WechatOpenInfo wechatOpenInfo = new EwalletH5WechatOpenInfo();
		for (String version : payProductMap.keySet()) {
			CashierVersionEnum versionEnum = CashierVersionEnum.transformVersionFromOPRVersion(version);
			if (versionEnum == null) {
				continue;
			}
			List<Product> products = new ArrayList<Product>();
			List<PayProductDto> productDtos = payProductMap.get(version);
			if (CollectionUtils.isNotEmpty(productDtos)) {
				for (PayProductDto productDto : productDtos) {
					PayTool payTool = null;
					List<String> payWays = productDto.getPayWays();
					try {
						payTool = PayTool.valueOf(productDto.getPayProductName());
					} catch (Exception e) {
						logger.warn("商编=" + merchantAccountCode + ", 转换支付工具payProductName="
								+ productDto.getPayProductName() + "成收银台PayTool枚举异常", e);
						continue;
					}
					if (CollectionUtils.isNotEmpty(payWays)) {
						Product product = new Product();
						product.setPayTool(payTool);
						//如果payTool是分期，则payTypes只需要存一个
						if (payTool == PayTool.CFL) {
							product.setPayTypes(payWays.subList(0, 1));
						} else {
							product.setPayTypes(payWays);
						}
						product.setPayScene(productDto.getPayScene());
						product.setBankStaging(productDto.getBankStaging());
						products.add(product);
						// 记录下钱包或钱包H5下支付宝SDK、支付宝H5和支付宝标准版的开通情况
						markAlipayOpenInfo(alipayPriority, versionEnum, payTool, productDto);
						markWechatOpenInfo(wechatOpenInfo, versionEnum, payTool, productDto);
//						handleWxH5Priority(versionEnum, payTool, productDto, config, merchantAccountCode,merchantAttributes);
					}
				}
				productMap.put(versionEnum, products);
				// 处理钱包H5下的
				if(CashierVersionEnum.WAP == versionEnum){
					EwalletH5OpenInfo ewalletH5OpenInfo = new EwalletH5OpenInfo();
					ewalletH5OpenInfo.setAlipayOpenInfo(alipayPriority);
					ewalletH5OpenInfo.setWechatOpenInfo(wechatOpenInfo);
					handleMerchantAttributes(ewalletH5OpenInfo, config, merchantAccountCode, merchantAttributes);
				}
			}
		}

		MerchantInNetConfigModel configModel = new MerchantInNetConfigModel();
		if (FeeTypeEnum.USER_FEE.getValue().equals(config.getFeeType())) {
			configModel.setFeeTypeEnum(FeeTypeEnum.USER_FEE);
		} else {
			configModel.setFeeTypeEnum(FeeTypeEnum.MERCHANT_FEE);
		}
		configModel.setSaleProductCode(config.getMarketingCode());
		configModel.setProductMap(productMap);
		configModel.setMcc(config.getMccCode());
		configModel.setMerchantAccountCode(merchantAccountCode);
		configModel.setMerchantAttributes(merchantAttributes);
		return configModel;
	}
	
	private void markWechatOpenInfo(EwalletH5WechatOpenInfo wechatOpenInfo, CashierVersionEnum versionEnum, PayTool payTool,
			PayProductDto productDto){
		if(versionEnum == CashierVersionEnum.WAP && payTool == PayTool.EWALLETH5){
			if(productDto.getPayWays().contains("WECHAT_H5")){
				wechatOpenInfo.setOpenWechatH5(true);
			}
			if(productDto.getPayWays().contains(PayTypeEnum.WECHAT_H5_LOW.name())){
				wechatOpenInfo.setOpenWechatH5Low(true);
			}
		}
	}
	
	
	/**
	 * 记录下钱包或钱包H5下支付宝SDK、支付宝H5和支付宝标准版的开通情况
	 * 
	 * @param alipayPriority
	 * @param versionEnum
	 * @param payTool
	 * @param productDto
	 */
	private void markAlipayOpenInfo(EwalletAlipayOpenInfo alipayPriority, CashierVersionEnum versionEnum, PayTool payTool,
			PayProductDto productDto) {
		if (CashierVersionEnum.WAP == versionEnum) {
			if (PayTool.EWALLET == payTool) {
				if (productDto.getPayWays().contains(PayTypeEnum.ALIPAY.name())) {
					alipayPriority.setOpenAlipay(true);
				}
			} else if (PayTool.EWALLETH5 == payTool) {
				if (productDto.getPayWays().contains(PayTypeEnum.ALIPAY_H5_STANDARD.name())) {
					alipayPriority.setOpenAlipayStandard(true);
				}
				if (productDto.getPayWays().contains(PayTypeEnum.ALIPAY_H5.name())) {
					alipayPriority.setOpenAlipayH5(true);
				}
			}
		}
	}

	
	/**
	 * 处理商户属性，目前包括：钱包支付h5高低配优先级
	 * @param transactionProperties
	 * @param wechatOpenInfo
	 * @param merchantAttributes 待返回的属性集合
	 */
	private void handleWxH5Priority(Map<String, List<MerchantPayConfigDto.MerchantAttributeValueDto>> transactionProperties, EwalletH5WechatOpenInfo wechatOpenInfo, Map<String, String> merchantAttributes) {
		List<MerchantPayConfigDto.MerchantAttributeValueDto> wechatH5PreferenceConfig = transactionProperties.get(EnumSubAttributeType.TRANSACTION_WECHAT_H5_PRIORITY.name());
		if(CollectionUtils.isEmpty(wechatH5PreferenceConfig)){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		String preferWechatH5 = "";
		for (MerchantPayConfigDto.MerchantAttributeValueDto attributeValueDto : wechatH5PreferenceConfig) {
			if (attributeValueDto!=null && "String".equalsIgnoreCase(attributeValueDto.getType())) {
				preferWechatH5 = attributeValueDto.getValue();
			}
		}
		if (EnumMerchantAttributeValue.TRANSACTION_HIGH_ALLOCATION.name().equalsIgnoreCase(preferWechatH5)) {
			merchantAttributes.put("wechatH5preference", PayTypeEnum.WECHAT_H5_WAP.name());
		} else if (EnumMerchantAttributeValue.TRANSACTION_LOW_ALLOCATION.name().equalsIgnoreCase(preferWechatH5)) {
			merchantAttributes.put("wechatH5preference", PayTypeEnum.WECHAT_H5_LOW.name());
		} else {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}
	
	/**
	 * 支付宝H5优先级处理 
	 * 
	 * @param transactionProperties
	 * @param alipayOpenInfo
	 * @param merchantAttributes
	 */
	private void handleAlipayH5Prioritity(
			Map<String, List<MerchantPayConfigDto.MerchantAttributeValueDto>> transactionProperties,
			EwalletAlipayOpenInfo alipayOpenInfo, Map<String, String> merchantAttributes) {
		// 取支付宝H5优先级 
		List<MerchantPayConfigDto.MerchantAttributeValueDto> alipayH5Priorities = transactionProperties
				.get(EnumSubAttributeType.TRANSACTION_ALIPAY_H5_PRIORITY.name());
		if(CollectionUtils.isEmpty(alipayH5Priorities)){
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		String alipayH5Priority = "";
		for (MerchantPayConfigDto.MerchantAttributeValueDto attributeValueDto : alipayH5Priorities) {
			if (attributeValueDto != null && "String".equalsIgnoreCase(attributeValueDto.getType())) {
				alipayH5Priority = attributeValueDto.getValue();
			}
		}
		if (EnumMerchantAttributeValue.TRANSACTION_LOW_ALLOCATION.name().equalsIgnoreCase(alipayH5Priority)) {
			merchantAttributes.put("alipayH5preference",
					alipayOpenInfo.isOpenAlipay() ? PayTypeEnum.ALIPAY.name() : PayTypeEnum.ALIPAY_H5.name());
		} else if (EnumMerchantAttributeValue.TRANSACTION_STANDARD_ALLOCATION.name()
				.equalsIgnoreCase(alipayH5Priority)) {
			merchantAttributes.put("alipayH5preference", PayTypeEnum.ALIPAY_H5_STANDARD.name());
		} else {
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
	}
	
	private void handleMerchantAttributes(EwalletH5OpenInfo ewalletH5OpenInfo, MerchantPayConfigDto config,
			String merchantAccountCode, Map<String, String> merchantAttributes) {
		Map<String, Map<String, List<MerchantPayConfigDto.MerchantAttributeValueDto>>> merchantAttributeMap = config
				.getMerchantAttributeMap();
		if (ewalletH5OpenInfo.alipayDoubleOpen() || ewalletH5OpenInfo.wxH5DoubleOpen()) {
			try {
				if(MapUtils.isEmpty(merchantAttributeMap)){
					throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
				}
				// 取到交易属性
				Map<String, List<MerchantPayConfigDto.MerchantAttributeValueDto>> transactionProperties = merchantAttributeMap
						.get(EnumAttributeType.TRANSACTION.name());
				if (MapUtils.isNotEmpty(transactionProperties)) {
					if (ewalletH5OpenInfo.alipayDoubleOpen()) {
						handleAlipayH5Prioritity(transactionProperties, ewalletH5OpenInfo.getAlipayOpenInfo(),
								merchantAttributes);
					}
					if (ewalletH5OpenInfo.wxH5DoubleOpen()) {
						handleWxH5Priority(transactionProperties, ewalletH5OpenInfo.getWechatOpenInfo(),
								merchantAttributes);
					}
				}else{
					throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
				}
			} catch (Throwable t) {
				logger.error("处理钱包支付h5下的优先级属性，捕获到异常，按未配置处理。商编=" + merchantAccountCode + "，微信H5双开=" + ewalletH5OpenInfo.wxH5DoubleOpen() + ", 支付宝双开=" + ewalletH5OpenInfo.alipayDoubleOpen() + "，异常=", t);
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
		}
	}

	@Override
	public List<MerchantLimitResponseDto> queryMerchantLimit(MerchantLimitRequestDto requestDto) {
		return merchantLimitFacade.queryMerchantLimitRequest(requestDto);
	}
	
	@Override
	public MerchantLimitRequestDto buildMerchantLimitRequestDto(PaymentRequest paymentRequest) {
		MerchantLimitRequestDto merchantLimitRequestDto = new MerchantLimitRequestDto();
		merchantLimitRequestDto.setMerchantNo(paymentRequest.getMerchantNo());
		merchantLimitRequestDto.setBizSystemCode(Long.parseLong(paymentRequest.getOrderSysNo()));
		merchantLimitRequestDto.setToolCode(PayProductCode.NCCASHIER);
		merchantLimitRequestDto.setClientCode(paymentRequest.getBizModeCode());
		merchantLimitRequestDto.setGoodsCode(paymentRequest.getIndustryCatalog());
		merchantLimitRequestDto.setRequestTime(new Date());
		return merchantLimitRequestDto;
	}

	@Override
	public List<MerchantLimitResponseDto> filterValidMerchantLimits(PaymentRequest paymentRequest) {
		
		// 获取商户银行卡限额列表
		MerchantLimitRequestDto request = buildMerchantLimitRequestDto(paymentRequest);
		List<MerchantLimitResponseDto> merchantLimits = queryMerchantLimit(request);
		if (CollectionUtils.isEmpty(merchantLimits)) {
			return null;
		}
		
		// 如果存在透传，则将不符合透传的限额记录去掉
		// 如果单笔限额，或者单日限额，或者，月限额为0，表示该卡暂时不支持
		if(StringUtils.isNotBlank(paymentRequest.getCardType()) || StringUtils.isNotBlank(paymentRequest.getBankCode())){
			List<MerchantLimitResponseDto> validMerchantLimits = new ArrayList<MerchantLimitResponseDto>();
			for (MerchantLimitResponseDto merchantLimit : merchantLimits) {
				if (StringUtils.isNotBlank(paymentRequest.getCardType())
						&& !paymentRequest.getCardType().equals(merchantLimit.getDebit())) {
					continue;
				}
				if (StringUtils.isNotBlank(paymentRequest.getBankCode())
						&& !paymentRequest.getBankCode().equals(merchantLimit.getBankCode())) {
					continue;
				}
				validMerchantLimits.add(merchantLimit);
			}
			return validMerchantLimits;
		}
		return merchantLimits;
	}
	
}
