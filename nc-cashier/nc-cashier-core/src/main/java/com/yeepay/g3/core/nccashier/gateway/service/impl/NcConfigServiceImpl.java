/** 
 * Copyright: Copyright (c)2011
 * Company: 易宝支付(YeePay) 
 */
package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.BankSupportDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCConfigException;
import com.yeepay.g3.facade.ncconfig.enumtype.CardType;
import com.yeepay.g3.facade.ncconfig.param.*;
import com.yeepay.g3.facade.ncconfig.result.*;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author：peile.fan
 * @since：2016年5月19日 下午6:26:38
 * @version:
 */
@Service("ncConfigService")
public class NcConfigServiceImpl extends NcCashierBaseService implements NcConfigService {
	private static final Logger logger = NcCashierLoggerFactory.getLogger(NcConfigServiceImpl.class);

	public CardBinDTO getCardBinDTOByCardNo(String cardNo) {
		CardBinDTO cardBinDTO = null;
		try {
			cardBinDTO = configCommonFacade.queryCardInfo(cardNo);
		}catch (NCConfigException e) {
			logger.warn("queryCardInfo异常", e);
			throw CommonUtil.handleException(Errors.INPUT_PARAM_NULL);
		}
		if (cardBinDTO == null) {
			throw CommonUtil.handleException(Errors.INVALID_BANK_CARD_NO);
		}
		return cardBinDTO;
	}

	@Override
	public ConfigCashierBankRuleDTO getCashierBankRule(ConfigCashierBankRuleParam param)
			throws CashierBusinessException {
		ConfigCashierBankRuleDTO rule;
		try {
			rule = configCashierFacade.queryCardInfoAndBankRule(param);
		} catch (NCConfigException e) {
			logger.warn("getCashierBankRule异常", e);
			throw CommonUtil.handleException(Errors.SUPPORT_BANK_FAILED);
		}

		if (null == rule) {
			throw CommonUtil.handleException(Errors.SUPPORT_BANK_FAILED);
		}

		return rule;
	}

	@Override
	public ConfigCashierDTO queryConfigCashier(ConfigCashierParam param) throws CashierBusinessException {
		ConfigCashierDTO configCashierDTO = null;
		try {
			configCashierDTO = configCashierFacade.queryConfigCashier(param);
		} catch (NCConfigException e) {
			logger.warn("queryConfigCashier异常", e);
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_NULL);
		}
		if (null == configCashierDTO) {
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_NULL);
		}

		return configCashierDTO;
	}

	@Override
	public List<BankSupportDTO> getSupportBanks(PaymentRequest paymentRequest, String cusType) {
		// 封装查询参数
		ConfigCashierParam param = makeConfigCashierParam(paymentRequest);
		//获取业务方的收银台模板
		ConfigCashierDTO configCashierDTO = queryConfigCashier(param);
		Map<String, String> bankTransforInfo = CommonUtil
				.getConfig(Constant.STANDARD_BANKCODE);
		if (null == bankTransforInfo) {// 获取三代统一配置的银行编码信息异常 需要确认是否抛异常
			logger.info("获取三代统一配置的银行编码信息为空");
		}
		//收银台模板转换银行列表信息
		List<BankSupportDTO> bankList = new ArrayList<BankSupportDTO>();
		for (ConfigCashierBankRuleDTO configCashierBankRuleDTO : configCashierDTO.getConfigCashierBankRules()) {
			BankSupportDTO supportDTO = new BankSupportDTO();
			supportDTO.setBankCode(configCashierBankRuleDTO.getBankCard().name());
			supportDTO.setBankName(configCashierBankRuleDTO.getBankCard().getName());
			//银行编码转换成三代统一的银行编码信息
			if (null!=bankTransforInfo&&bankTransforInfo.containsKey(configCashierBankRuleDTO.getBankCard().name())) {
				supportDTO.setBankCode(bankTransforInfo.get(configCashierBankRuleDTO.getBankCard().name()));
			}
			if (CardType.CREDIT == configCashierBankRuleDTO.getCardType()) {
				supportDTO.setBanktype(CardTypeEnum.CREDIT);
			} else if (CardType.DEBIT == configCashierBankRuleDTO.getCardType()) {
				supportDTO.setBanktype(CardTypeEnum.DEBIT);
			} else {
				continue;
			}
			//根据cusType过滤银行
			if(StringUtils.isNotBlank(cusType) && cusType.equals(configCashierBankRuleDTO.getCusType())){
				bankList.add(supportDTO);
			}
			
		}
		return bankList;
	}

	/**
	 * 封装查询收银台模板参数
	 * 
	 * @param paymentRequest
	 * @return
	 */
	private ConfigCashierParam makeConfigCashierParam(PaymentRequest paymentRequest) {
		ConfigCashierParam param = new ConfigCashierParam();
		param.setToolCode(PayProductCode.NCCASHIER);
		param.setMerchantCode(paymentRequest.getMerchantNo());
		param.setClientCode(paymentRequest.getBizModeCode());
		param.setGoodsCode(paymentRequest.getIndustryCatalog());
		param.setSysId(Long.parseLong(paymentRequest.getOrderSysNo()));
		return param;
	}

	@Override
	public OnlineBankTemplateInfo getEbankSupportBanks(QueryOnlineBankRulesByTemplateParam param) {
		OnlineBankTemplateInfo bankTemplate = null;
		// 网银收银台模板（模板基本信息 + b2b银行规则列表 + b2c银行规则列表）
		try {
			bankTemplate = onlineBankCashierTemplateFacade.queryOnlineBankRules(param);
		} catch (NCConfigException e) {
			logger.warn("queryConfigCashier异常", e);
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_NULL);
		}
		if (bankTemplate == null) {
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_NULL);
		}
		if (bankTemplate.getBankRules() == null) {
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		if ((CollectionUtils.isEmpty(bankTemplate.getBankRules().getB2bRules())
				&& CollectionUtils.isEmpty(bankTemplate.getBankRules().getB2cRules()))) {
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_BANKS_NULL);
		}
		return bankTemplate;

	}

	@Override
	public MerchantCashierTemplateFileDTO queryMerchantCashierTemplateFileInfo(String fileId,String fileType) {
		MerchantCashierTemplateFileDTO merchantCashierTemplateFileDTO = null;
		try{
			merchantCashierTemplateFileDTO = nccashierMerchantCustomizedFileFacade.queryMerchantCashierTemplateFile(fileId,fileType);
		}catch(NCConfigException e){
			logger.warn("queryMerchantCashierTemplateFileInfo异常",e);
			throw CommonUtil.handleException(Errors.MERCHANT_CASHIER_NOT_CUSTOMIZED);
		}
		// 这里其实不需要抛异常 TODO 后期作为优化版本上吧
		if(null == merchantCashierTemplateFileDTO){
			throw CommonUtil.handleException(Errors.MERCHANT_CASHIER_NOT_CUSTOMIZED);
		}
		return merchantCashierTemplateFileDTO;
	}

	@Override
	public MerchantLayoutSelectDTO queryMerchantCashierSelectLayout(
			BasicMerchantLayoutParam merchantLayoutQueryParam) {
		MerchantLayoutSelectDTO merchantUsableCashierLayoutDTO = null;
		try{
			merchantUsableCashierLayoutDTO = nccashierMerchantLayoutConfigFacade.queryMerchantCashierLayout(merchantLayoutQueryParam);
		}catch(NCConfigException e){
			logger.warn("queryMerchantCashierTemplateFileInfo异常",e);
			throw CommonUtil.handleException(Errors.MERCHANT_CASHIER_NOT_CUSTOMIZED);
		}
		if(merchantUsableCashierLayoutDTO == null){
			throw CommonUtil.handleException(Errors.MERCHANT_CASHIER_NOT_CUSTOMIZED);
		}
		return merchantUsableCashierLayoutDTO;
	}

	@Override
	public void hasAuthPay(ConfigAuthPayParam configAuthPayParam) {
		boolean hasAuthPay = false;
		try{
			hasAuthPay = configAuthPayFacade.hasAuthPay(configAuthPayParam);
		}catch(NCConfigException e){
			logger.warn("hasAuthPay异常",e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		if (!hasAuthPay){
			throw CommonUtil.handleException(Errors.NOT_OPEN_AUTH_PAY_ERROR);
		}

	}
}
